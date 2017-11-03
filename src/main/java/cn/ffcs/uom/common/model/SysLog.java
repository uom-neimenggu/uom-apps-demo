package cn.ffcs.uom.common.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.zkoss.zk.ui.Executions;

import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年11月4日
 * @功能说明：接入统一日志库的model
 *
 */
public class SysLog implements Serializable {
    
    /**
     * .
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @Getter
    @Setter
    private long sysLogId;
    
    /**
     * 流水号
     */
    @Getter
    @Setter
    private String transId;
    
    /**
     * 消息类型，STAFF-员工，ORG-组织，ROLE-角色，PRIM-权限
     */
    @Getter
    @Setter
    private String msgType;
    
    /**
     * 接口结果S-成功，F-失败
     */
    @Getter
    @Setter
    private String result;
    
    /**
     * 记录日志开始时间
     */
    @Getter
    @Setter
    private Date beginDate;
    
    /**
     * 日志结束时间
     */
    @Getter
    @Setter
    private Date endDate;
    
    /**
     * 消耗时间
     */
    @Getter
    @Setter
    private long consumeTime;
    
    /**
     * 错误编码
     */
    @Getter
    @Setter
    private String errCode;
    
    /**
     * 错误描述
     */
    @Getter
    @Setter
    private String errMsg;
    
    /**
     * 客户ip
     */
    @Getter
    @Setter
    private String requestIp;
    
    /**
     * 当前用户账号
     */
    @Getter
    @Setter
    private String staffAccount;
    
    /**
     * 当前操作对象的表
     */
    @Getter
    @Setter
    private String operatorObject;
//    operatorType
    /**
     * 操作类型，增删改查
     */
    @Getter
    @Setter
    private String operatorType;
//    logLevel
    /**
     * 日志等级
     */
    @Getter
    @Setter
    private String logLevel;
//    logMsg
    /**
     * 日志内容
     */
    @Getter
    @Setter
    private String logMsg;
    
    @Getter
    @Setter
    private User user;
    
    /**
     * 在业务场景入口生成唯一的事务流水，事务流水生成规则：日期 +“_” + 系统标识 +“_”+ 工号 +“_”+场景编码 +“_” + 随机数
     * .
     * 时间8位，流水号10位：举个栗子
     * 20161107_sysid_H8457619_STAFF_随机数
     * 20160224_2068_AHXCAD0001_BUSS00001_450519
     * @param 
     * @return
     * @author xiaof
     * 2016年10月25日 xiaof
     */
    public static String gennerateBatchNumber(String staffAccount, String sceneCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //添加日期
        StringBuffer transString = new StringBuffer(simpleDateFormat.format(new Date()));
        //添加系统标识,工号，场景编码，随机数
        transString.append("_").append(getSystemId()).append("_").append(staffAccount).append("_").append(sceneCode).append("_");
        //最后添加一个6位随机数
        transString.append(String.valueOf(Math.round(Math.random() * 1000000000)));
        
        return transString.toString();
    }
    
    /**
     * 获取接入日志平台标识
     * .
     * 
     * @return
     * @author xiaof
     * 2016年10月25日 xiaof
     */
    private static String getSystemId() {
        List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "SystemConfig", "uomSystemLogId", null,
                BaseUnitConstants.ENTT_STATE_ACTIVE);
        if (list != null && list.size() > 0) {
            return list.get(0).getAttrValueName();
        }
        return "";
    }
    
    /**
     * 根据实体类获取对应的表名,这里限定是主数据中的表
     * .
     * 
     * @return
     * @author xiaof
     * 2016年11月14日 xiaof
     */
    public static String getTableName(Class<? extends UomEntity> clazz)
    {
        return getPersistentClass(clazz).getTable().getName();
    }
    
    private static PersistentClass getPersistentClass(Class clazz) {
        // 获取hibernate对应的实例
        Configuration hibernateConf = ((LocalSessionFactoryBean) ApplicationContextUtil.getBean("&sessionFactory")).getConfiguration();
        PersistentClass persistentClass = hibernateConf.getClassMapping(clazz.getName());
        
        if (persistentClass == null) {
            hibernateConf.addClass(clazz.getClass());
            persistentClass = hibernateConf.getClassMapping(clazz.getName());
        }
        return persistentClass;
    }
    /**
     * 开始记录日志
     * .
     * 
     * @param startTime  业务开始时间
     * @param msgType   业务场景，这个是SysLogConstrants中定义：日志操作场景
     * @author xiaof
     * 2016年11月17日 xiaof
     */
    public void startLog(Date startTime, String msgType)
    {
        this.beginDate = startTime;
        this.msgType = msgType;
        this.user = PlatformUtil.getCurrentUser();
        HttpServletRequest req = (HttpServletRequest) Executions
            .getCurrent().getNativeRequest();
        if(req != null)
        {
            this.setRequestIp(GetipUtil.getIpAddr(req));
        }
        else
        {
            //错误，无法获取客户ip
            this.setRequestIp("unKown");
            this.setErrCode(SysLogConstrants.FAIL_CODE_UNKNOW_CLIENT_IP);
            this.setErrMsg(SysLogConstrants.FAIL_CODE_UNKNOW_CLIENT_IP_STR);
            this.setResult(SysLogConstrants.RESULT_FAIL);
            this.setLogLevel(SysLogConstrants.ERROR);
        }
    }
    
    /**
     * 日志记录进入队列
     * .
     * 
     * @param logService  日志服务
     * @param objectClazz  操作对象的表model
     * @param operatorType  操作类型，常量
     * @param level 日志等级，常量
     * @param msg   日志信息
     * @author xiaof
     * 2016年11月17日 xiaof
     */
    public void endLog(LogService logService, Class objectClazz[], String operatorType, String level, String msg) {
        this.result = SysLogConstrants.RESULT_SUCCESS;
        String operatorObjectStr = "";
        for(int i = 0; i < objectClazz.length; ++i)
        {
            operatorObjectStr += this.getTableName(objectClazz[i]);
        }
        this.setOperatorObject(operatorObjectStr);
        this.setOperatorType(operatorType);
        this.setLogLevel(level);
        this.setLogMsg(msg);
        // 存放日志进入日志队列
        try {
            logService.log(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
