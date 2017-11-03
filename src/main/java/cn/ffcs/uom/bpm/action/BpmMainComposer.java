package cn.ffcs.uom.bpm.action;


import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.bpm.bean.BpmMainBean;
import cn.ffcs.uom.bpm.manager.BmpManager;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.bpm.model.QaUnOpsPpass;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.ExportExcelNew;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 数据质量流程管理
 * <p>
 * 主方法，按指标大项展示各数据合格率
 * </p>
 * 
 * @ClassName: BpmMainComposer
 * @Description: TODO
 * @author Comsys-WCNGS
 * @date 2016-5-12 上午9:40:58 * *
 */
@Controller
@Scope("prototype")
@SuppressWarnings({})
public class BpmMainComposer extends BasePortletComposer implements IPortletInfoProvider {
    private static final long serialVersionUID = 1L;
    
    private BpmMainBean bean = new BpmMainBean(); //BPM主页面实体Bean
    
    private QaUnOppExecScript qaUnOppExecScript;
    private QaUnOppExecScript qaHisUnOppExecScript;
    
    @Autowired	//业务逻辑层注入
    private BmpManager bmpManager;
    
    /**
	 * 数据权限：区域
	 */
    private TelcomRegion permissionTelcomRegion;
    
    @Override
    public String getPortletId() {
        // TODO Auto-generated method stub
        return super.getPortletId();
    }
    
    @Override
    public ThemeDisplay getThemeDisplay() {
        // TODO Auto-generated method stub
        return super.getThemeDisplay();
    }
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        // TODO Auto-generated method stub
        UomZkUtil.autoFitHeight(comp);
        super.doAfterCompose(comp); 
        Components.wireVariables(comp, bean);
    }
    

    //当前稽核查询
    public void onQueryBpmItems() throws Exception {
    	onQuerybpmInfoListPaging();
    }
    
    public String getQaUnRegionNameByCurUser() {
    	String qaUnRegionName = null;
    	permissionTelcomRegion = new TelcomRegion();
    	
    	if(PlatformUtil.getCurrentUser() != null && !PlatformUtil.isAdmin()){
    		permissionTelcomRegion = PermissionUtil
					.getPermissionTelcomRegion(PlatformUtil
							.getCurrentUser().getRoleIds());
    		if(permissionTelcomRegion != null) {
    			String regionName = permissionTelcomRegion.getRegionName();
        		
        		if((regionName.indexOf("中国") != -1) || (regionName.indexOf("安徽省") != -1)) {
        			qaUnRegionName = null;
        		} else {
        			qaUnRegionName = permissionTelcomRegion.getRegionName();
        		}
    		} else {
    			qaUnRegionName = "";
    		}
    	}else{
    		qaUnRegionName = null;
    	}
    	
    	return qaUnRegionName;
    }
    
    public void onQuerybpmInfoListPaging() throws Exception{
    	//实例化实体类--对应数据库表
    	qaUnOppExecScript = new QaUnOppExecScript();
    	
    	//User user = PlatformUtil.getCurrentUser();
    	
        if (this.bean.getQaUnOppExecScriptTreeBandbox() != null
            && this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript() != null) {
        	
        	String qaUnRegionName = getQaUnRegionNameByCurUser();
        	
        	if("".equals(qaUnRegionName)) {
				Messagebox.show("数据权限不存在，请联系管理员处理");
				return;
			}
        	
        	qaUnOppExecScript.setExecKidIdenti(this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti());
        	qaUnOppExecScript.setExecKidName(this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidName());  
        	
        	PageInfo pageInfo = this.bmpManager.getBmpInfos(qaUnRegionName, Long.parseLong(this.bean
            		.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti()), this.bean
                .getBpmInfoListPaging().getActivePage() + 1, this.bean.getBpmInfoListPaging()
                .getPageSize());      	
            ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
            this.bean.getBpmInfoListbox().setModel(dataList);														
            this.bean.getBpmInfoListPaging().setTotalSize( NumericUtil.nullToZero(pageInfo.getTotalCount()));     
        }
    	//实例化实体类--对应数据库表
//    	qaUnOppExecScript = new QaUnOppExecScript();
//        if (this.bean.getQaUnOppExecScriptTreeBandbox() != null
//            && this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript() != null) {
//        	qaUnOppExecScript.setExecKidIdenti(this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti());
//        	qaUnOppExecScript.setExecKidName(this.bean.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidName());       	
//        	PageInfo pageInfo = this.bmpManager.getBmpInfos(Long.parseLong(this.bean
//            		.getQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti()), this.bean
//                .getBpmInfoListPaging().getActivePage() + 1, this.bean.getBpmInfoListPaging()
//                .getPageSize());      	
//            ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
//            this.bean.getBpmInfoListbox().setModel(dataList);														
//            this.bean.getBpmInfoListPaging().setTotalSize( NumericUtil.nullToZero(pageInfo.getTotalCount()));     
//        }
    }
    
    //历史稽核查询
    public void onQueryHisBpmItems() throws Exception {
    	onQueryHisbpmInfoListPaging();
    }
    public void onQueryHisbpmInfoListPaging() throws Exception {
    	qaHisUnOppExecScript = new QaUnOppExecScript();
    	
        if (this.bean.getHisQaUnOppExecScriptTreeBandbox() != null
            && this.bean.getHisQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript() != null) {
        	
        	String qaUnRegionName = getQaUnRegionNameByCurUser();
        	
        	if("".equals(qaUnRegionName)) {
				Messagebox.show("数据权限不存在，请联系管理员处理");
				return;
			}
        	
        	Date beginDate = this.bean.getBeginDate().getValue();
        	Date endDate = this.bean.getEndDate().getValue();
        	qaHisUnOppExecScript.setExecKidIdenti(this.bean.getHisQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti());
        	qaHisUnOppExecScript.setExecKidName(this.bean.getHisQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidName());
        	
        	PageInfo pageInfo = this.bmpManager.getHisBmpInfos(qaUnRegionName,Long.valueOf(this.bean
                .getHisQaUnOppExecScriptTreeBandbox().getQaUnOppExecScript().getExecKidIdenti()), this.bean
                .getHisBpmInfoListPaging().getActivePage() + 1, this.bean.getHisBpmInfoListPaging()
                .getPageSize(),beginDate,endDate);
            ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
            this.bean.getHisBpmInfoListbox().setModel(dataList);
            this.bean.getHisBpmInfoListPaging().setTotalSize(
                NumericUtil.nullToZero(pageInfo.getTotalCount()));
        }
    }
    
    //导出清单
    public void onDownLoadDetailsRequest() throws Exception {
        if (this.bean.getBpmInfoListbox() != null
            && this.bean.getBpmInfoListbox().getSelectedItem() != null) {
            QaUnOpsPpass audit = (QaUnOpsPpass) this.bean.getBpmInfoListbox().getSelectedItem().getValue();
            Long execKidIdenti = audit.getExecKidIdenti();
            String checkType = audit.getCheckType();
            String unit = audit.getUnit();
            List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
            if ("STAFF".equals(checkType)) {
                data = bmpManager.getBmpStaffMap(Long.valueOf(execKidIdenti),unit);
            } else if("ORG".equals(checkType)){
                data = bmpManager.getBmpOrgMap(Long.valueOf(execKidIdenti),unit);
            }
            // 设置字符集
            String charset = "UTF-8";
            String fileName = unit+"-"+qaUnOppExecScript.getExecKidName()+DateUtil.getYYYYMMDD(new Date());
            HttpServletRequest httpRequest = (HttpServletRequest) Executions
                .getCurrent().getNativeRequest();
            //List<Map<String, Object>> list = organizationService.getExportOrgList(exportOrgVo);
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //ExcelUtil.createWorkBook(list, header, header,os);
            ExportExcelNew.exportExcel("", data, os);
            byte[] content = os.toByteArray();
            // 设置response参数，可以打开下载页面
/*            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;*/
            
            String encodedName = null;
            encodedName = URLEncoder.encode(fileName+".xlsx", charset);
            // 将空格替换为+号
            encodedName = encodedName.replace("%20", "+");

            // 解决ie6 bug 或者是火狐浏览器
            if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
                    || Servlets.isGecko3(httpRequest)) {
                encodedName = new String(encodedName.getBytes(charset),
                        "ISO8859-1");
            }
            
            Filedownload.save(content, "application/octet-stream", encodedName);
/*            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }*/
        } else {
            Messagebox.show("请选择具体单位后再导出清单!");//Message.show("XXXX!!");zk页面弹出提示XXXX！！
            return;
        }
    }
}
