package cn.ffcs.uom.organization.constants;

public class ProcessStatusConstant {
    /**
     * 相应查看工单的时候的信号量
     */
    public static final String ON_WORK_FLOW_REQUEST = "onWorkFlowRequest";
    /**
     * 设置流程状态量
     */
    /**
     * 有效
     */
    public static final String ENTT_STATE_ACTIVE = "1000";
    /**
     * 暂停suspend
     */
    public static final String ENTT_STATE_SUSPEND = "1001";
    /**
     * 锁定
     */
    public static final String ENTT_STATE_LOCKING = "1010";
    /**
     * 密码错误锁定
     */
    public static final String ENTT_STATE_ERROR_PASSWORD_LOCKING = "1011";
    /**
     * 停用Disable
     */
    public static final String ENTT_STATE_DISABLE = "1020";
    /**
     * 无效
     */
    public static final String ENTT_STATE_INACTIVE = "1100";
    /**
     * 冻结 Frozen
     */
    public static final String ENTT_STATE_FROZEN = "1200";
    /**
     * 未生效
     */
    public static final String ENTT_STATE_NOT_ACTIVE = "1300";
    /**
     * 审批通过  Approval 
     */
    public static final String ENTT_STATE_APPROVAL = "1310";
    /**
     * 审批中
     */
    public static final String ENTT_STATE_IN_APPROVAL = "1320";
    /**
     * 审批未通过
     */
    public static final String ENTT_STATE_NOT_APPROVAL = "1330";
    
    /**
     * 活动实例的状态量
     */
    /**
     * 运行状态
     */
    public static final String ACTIVITY_RUN = "2";
    /**
     * 完成
     */
    public static final String ACTIVITY_COMPLETE = "7";
    /**
     * 终止
     */
    public static final String ACTIVITY_TERMINATION = "8";
    /**
     * 挂起
     */
    public static final String ACTIVITY_HANG = "3";
    /**
     * 待领取
     */
    public static final String ACTIVITY_UNCLAIMED = "4";
    /**
     * 待激活
     */
    public static final String ACTIVITY_TO_BE_ACTIVATED = "10";
    /**
     * 应用异常
     */
    public static final String ACTIVITY_EXCEPTION = "-1";
    
    
    /**
     * 代办工作项的状态量
     */
    /**
     * 运行
     */
    public static final String WORK_ITEM_RUN = "10";
    /**
     * 完成
     */
    public static final String WORK_ITEM_FINISH = "12";
    /**
     * 终止
     */
    public static final String WORK_ITEM_OVER = "13";
    /**
     * 挂起
     */
    public static final String WORK_ITEM_HANG_UP = "8";
    /**
     * 待领取
     */
    public static final String WORK_ITEM_UNCLAIMED = "4";
    
    
    
}
