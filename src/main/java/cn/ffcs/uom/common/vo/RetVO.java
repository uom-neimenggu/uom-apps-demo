package cn.ffcs.uom.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * =============================================================================<br>
 * 版权： 福富软件 版权所有 (c) 2007 - 2008.<br>
 * 文件包： com.ffcs.crm.common.vo 所含类： RetVO 编写人员：陈军 创建日期：2007-03-27 功能说明: 保存返回信息值
 * 更新记录： 日期 作者 内容<br>
 * =============================================================================<br>
 * 2007-03-27 陈军 创建新文件，并实现基本功能
 * =============================================================================<br>
 */
public class RetVO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -16272341523087738L;
    
    private String            result;                                // 返回结果
                                                                      
    private String            retCode;                               // 返回编码
                                                                      
    private String            retMsg;                                // 返回信息
                                                                      
    private List              dataList;                              // 返回数据集合
                                                                      
    private PageInfo          pageInfo;                              // 分类查询信息;
                                                                      
    private Object            object;                                // 单个对象
                                                                      
    public static RetVO newInstance() {
        RetVO retVo = new RetVO();
        return retVo;
    }
    
    public static RetVO newInstance(String result, String retMsg) {
        RetVO retVo = new RetVO();
        retVo.setResult(result);
        retVo.setRetMsg(retMsg);
        return retVo;
    }
    
    public void copyFrom(RetVO srcRetVo) {
        this.setResult(srcRetVo.getResult());
        this.setRetCode(srcRetVo.getRetCode());
        this.setRetMsg(srcRetVo.getRetMsg());
        this.setDataList(srcRetVo.getDataList());
        this.setObject(srcRetVo.getObject());
        this.setPageInfo(srcRetVo.getPageInfo());
    }
    
    /**
     * @return Object
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public Object getObject() {
        return this.object;
    }
    
    /**
     * @param object
     *            Object
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setObject(final Object object) {
        this.object = object;
    }
    
    /**
     * @return PageInfo
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public PageInfo getPageInfo() {
        return this.pageInfo;
    }
    
    /**
     * @param pageInfo
     *            PageInfo
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setPageInfo(final PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    
    /**
     * @return List
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public List getDataList() {
        return this.dataList;
    }
    
    /**
     * @param dataList
     *            List
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setDataList(final List dataList) {
        this.dataList = dataList;
    }
    
    /**
     * @return String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public String getResult() {
        return this.result;
    }
    
    /**
     * @param result
     *            String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setResult(final String result) {
        this.result = result;
    }
    
    /**
     * @return String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public String getRetCode() {
        return this.retCode;
    }
    
    /**
     * @param retCode
     *            String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setRetCode(final String retCode) {
        this.retCode = retCode;
    }
    
    /**
     * @return String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public String getRetMsg() {
        return this.retMsg;
    }
    
    /**
     * @param retMsg
     *            String
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setRetMsg(final String retMsg) {
        this.retMsg = retMsg;
    }
    
    /**
     * 对返回结果、信息、编码设值.
     * 
     * @param retMsg
     *            返回信息
     * @param retCode
     *            返回编码
     * @param result
     *            返回结果
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-21 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void setRetRsCodeMsg(final String result, final String retCode, final String retMsg) {
        this.setResult(result);
        this.setRetCode(retCode);
        this.setRetMsg(retMsg);
    }
    
}
