package cn.ffcs.uom.webservices.services;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ffcs.uom.bpm.manager.BmpManager;
import cn.ffcs.uom.bpm.model.ItsmProcessListLog;
import cn.ffcs.uom.bpm.model.ProcessInformReq;
import cn.ffcs.uom.bpm.model.ProcessInformRes;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.webservices.bean.wechat.Msg;
import cn.ffcs.uom.webservices.bean.wechat.WechatRes;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.WechatUtil;

public class UomService {

	private Logger logger = Logger.getLogger(this.getClass());

	private BuildFileSqlManager buildFileSqlManager = (BuildFileSqlManager) ApplicationContextUtil
			.getBean("buildFileSqlManager");
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil.getBean("staffManager");
	
    private BmpManager bmpManager = (BmpManager) ApplicationContextUtil.getBean("bmpManager");
	/**
	 * 生成增量数据数据
	 * 
	 * @param treeId
	 * @param lastDate
	 * @param thisDate
	 * @param syncType
	 */
	public String createIncrementalData(Long treeId, Date lastDate,
			Date thisDate, String syncType, String username, String password) {
		String result = "";
		try {
			if (WsConstants.SERVICE_USERNAME.equals(username)
					&& WsConstants.SERVICE_PASSWORD.equals(password)) {
				logger.info("3.1、生成本地文件开始->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				boolean flag = buildFileSqlManager.createLocalFtpFiles(treeId,
						lastDate, thisDate, syncType);
				logger.info("3.1、生成本地文件结束->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				if (!flag) {
					result = "生成文件失败";
				}
			} else {
				result = "用户名或者密码错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "调用后台异常";
		}
		return result;
	}
    public String dbpmMethod(String inJson) {
        ProcessInformReq req = new ProcessInformReq();
        String outJson = "";
        try {
            req = (ProcessInformReq) JacksonUtil.JSon2Object(inJson,
                ProcessInformReq.class);
            ProcessInformRes res = bmpManager.saveItsmInform(req);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return outJson;
    }
    
    public String checkStaffAccount(String inJson){
        String outJson = "";
        try {
            Msg msg = (Msg) JacksonUtil.JSon2Object(inJson, Msg.class);
            WechatRes res = new WechatRes();
            if (WechatUtil.checkSignature(msg)) {
                
                Msg decryptMsg = WechatUtil.getDecryptMsg(msg);
                boolean result = staffManager.checkStaff(decryptMsg);
                if (result) {
                    res.setResult("1");
                    res.setMsg("处理成功");
                    res.setExpa("");  
                } else {
                    res.setResult("0");
                    res.setMsg("信息校验错误");
                    res.setExpa("");
                }

            } else {
                res.setResult("0");
                res.setMsg("签名错误");
                res.setExpa("");
            }
            

            outJson = JacksonUtil.Object2JSon(res);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        return outJson;
    }
    
    public static void main(String[] args) {
        Msg msg = new Msg();
    }
}