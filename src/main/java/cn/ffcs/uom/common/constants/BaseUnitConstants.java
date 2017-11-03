package cn.ffcs.uom.common.constants;

public class BaseUnitConstants {
	/**
	 * 生效状态
	 */
	public static final String ENTT_STATE_ACTIVE = "1000";
	/**
	 * 失效状态
	 */
	public static final String ENTT_STATE_INACTIVE = "1100";
	/**
	 * 未生效状态
	 */
	public static final String ENTT_STATE_UNACTIVE = "1200";
	/**
	 * 审批中状态
	 */
	public static final String ENTT_STATE_PROCESS = "1320";
	/**
	 * 已归档状态
	 */
	public static final String ENTT_STATE_FILE = "1300";
	/**
	 * 新增状态
	 */
	public static final String ENTT_STATE_ADD = "ADD";
	/**
	 * 修改状态
	 */
	public static final String ENTT_STATE_MOD = "MOD";
	/**
	 * 删除状态
	 */
	public static final String ENTT_STATE_DEL = "DEL";
	/**
	 * 状态
	 */
	public static final String TABLE_CLOUMN_STATUS_CD = "STATUS_CD";
	/**
	 * 失效时间
	 */
	public static final String TABLE_CLOUMN_EXP_DATE = "EXP_DATE";
	/**
	 * 生效时间
	 */
	public static final String TABLE_CLOUMN_EFF_DATE = "EFF_DATE";
	/**
	 * 状态时间
	 */
	public static final String TABLE_CLOUMN_STATUS_DATE = "STATUS_DATE";
	/**
	 * 修改时间
	 */
	public static final String TABLE_CLOUMN_UPDATE_DATE = "UPDATE_DATE";

	/**
	 * 是推导
	 */
	public static final String IS_CALC_ACTIVE = "1";
	/**
	 * 不是推导
	 */
	public static final String IS_CALC_INACTIVE = "2";
	/**
	 * 新增
	 */
	public static final String OPE_TYPE_ADD = "1";
	/**
	 * 修改
	 */
	public static final String OPE_TYPE_UPDATE = "2";
	/**
	 * 删除
	 */
	public static final String OPE_TYPE_DEL = "3";

	/**
	 * 归属主部门
	 */
	public static final String RALA_CD_1 = "1";

	/**
	 * 兼职
	 */
	public static final String RALA_CD_3 = "3";

	/**
	 * 开关开启
	 */
	public static final String SWITCH_OPEN = "1";

	/**
	 * 开关关闭
	 */
	public static final String SWITCH_CLOSE = "0";

	/**
	 * 新增
	 */
	public final static String OPERATION_TYPE_ADD = "add";

	/**
	 * 修改
	 */
	public final static String OPERATION_TYPE_MOD = "mod";

	/**
	 * 删除
	 */
	public final static String OPERATION_TYPE_DEL = "del";

	/**
	 * 数据源-主数据平台
	 */
	public final static String DATA_SOURCE_UOM = "1";

	/**
	 * 数据源-全息网格单元
	 */
	public final static String DATA_SOURCE_GRID_UNIT = "2";
	
	/**
	 * itsm流程单日志表类型：1、调用itsm发起流程日志
	 */
	public final static String ITSM_PROCESS_LOG_TYPE_1 = "1";
    /**
     * itsm流程单日志表类型：2、itsm通知uom流程状态日志
     */
	public final static String ITSM_PROCESS_LOG_TYPE_2 = "2";
	/**
	 * itsm流程单状态 0 归档
	 */
	public final static String ITSM_PROCESS_STATUS_0 = "0";
	/**
     * itsm流程单状态 1 流转中
     */
	public final static String ITSM_PROCESS_STATUS_1 = "1";
	/**
	 * itsm单据对象类型1、分公司 2、业务系统
	 */
	public final static String ITSM_PROCESS_TYPE_1 = "1";
    /**
     * itsm单据对象类型1、分公司 2、业务系统
     */
	public final static String ITSM_PROCESS_TYPE_2 = "2";
	
    /**
     * itsm与UOM字段映射类型1、分公司 2、业务系统
     */
    public final static String ITSM_TYPE_1 = "1";
    /**
     * itsm与UOM字段映射类型1、分公司 2、业务系统
     */
    public final static String ITSM_TYPE_2 = "2";
    
    /**
	 * 主岗
	 */
	public static final String POS_RALA_CD_1 = "1";
}
