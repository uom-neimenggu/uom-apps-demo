package cn.ffcs.uom.common.constants;

/**
 * 接入日志平台
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年11月7日
 * @功能说明：
 *
 */
public class SysLogConstrants {
    /**
     * 表示日志操作成功
     */
    public final static String RESULT_SUCCESS = "S";
    
    /**
     * 标识日志操作失败
     */
    public final static String RESULT_FAIL = "F";
    
    /**
     * 日志失败相应的原因与编码
     */
    public final static String FAIL_CODE_UNKNOW_USER = "101";
    public final static String FAIL_CODE_UNKOW_USER_STR = "无法获取当前操作用户";
    public final static String FAIL_CODE_UNKNOW_STAFF = "102";
    public final static String FAIL_CODE_UNKNOW_STAFF_STR = "无法获取当前员工账号，操作用户ID为：";
    public final static String FAIL_CODE_UNKNOW_CLIENT_IP = "103";
    public final static String FAIL_CODE_UNKNOW_CLIENT_IP_STR = "无法获取当前操作用户IP";
    
    
    /**
     * 日志操作场景MSG_TYPE：STAFF-员工，ORG-组织，ROLE-角色，PRIM-权限
     */
    public final static String STAFF= "STAFF";
    public final static String ORG = "ORG";
    public final static String ROLE = "ROLE";
    public final static String PRIM = "PRIM";
    
    /**
     * 操作类型
     */
    public final static String ADD = "add";
    public final static String EDIT = "edit";
    public final static String DEL = "del";
    public final static String UPDATE = "update";
    public final static String IMPORT = "import";
    public final static String MOVE = "move";
    public final static String QUERY = "query";
    
    /**
     * 日志操作等级
     */
    public final static String INFO = "INFO";
    public final static String ERROR = "ERROR";
    
}
