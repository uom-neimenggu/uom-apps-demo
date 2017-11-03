package cn.ffcs.uom.systemconfig.constants;

public class SystemConfigConstant {
	/**
	 * 系统配置人员选择事件
	 */
	public static final String ON_SELECT_SYSTEM_CONFIG_USER = "onSelectSystemConfigUser";

	/**
	 * 系统配置人员组件刷新事件
	 */
	public static final String ON_REFRESH_SYSTEM_CONFIG_USER = "onRefreshSystemConfigUser";

	public static final String ON_SYS_BUSI_USER_QUERY = "onQuerySysBusiUser";

	/**
	 * 关闭事件
	 */
	public static final String ON_CLOSE_SYSTEM_CONFIG_USER = "onCloseMessageConfigUser";

	/**
	 * 清空事件
	 */
	public static final String ON_CLEAN_SYSTEM_CONFIG_USER = "onCleanMessageConfigUser";

	/**
	 * 点击类事件
	 */
	public static final String ON_SYS_CLASS_SELECT = "onSelectSysClass";
	/**
	 * 点击类事件响应
	 */
	public static final String ON_SYS_CLASS_SELECT_RESPONSE = "onSelectSysClassResponse";
	/**
	 * 是实体类型
	 */
	public static Long SYS_CLASS_IS_ENTITY = 1L;
	/**
	 * 是实体类型
	 */
	public static Long SYS_CLASS_NOT_ENTITY = 0L;
	/**
	 * 类保存.
	 */
	public static final String ON_SYS_CLASS_SAVE = "onSaveSysClass";
	/**
	 * 类保存响应.
	 */
	public static final String ON_SYS_CLASS_SAVE_RESPONSE = "onSaveSysClassResponse";
	/**
	 * 类删除.
	 */
	public static final String ON_SYS_CLASS_DEL = "onDelSysClass";
	/**
	 * 类删除响应.
	 */
	public static final String ON_SYS_CLASS_DEL_RESPONSE = "onDelSysClassResponse";
	/**
	 * 点击组织树信息事件
	 */
	public static final String ON_ORG_TREE_SELECT_REQUEST = "onOrgTreeSelectRequest";
	/**
	 * 点击组织树信息响应
	 */
	public static final String ON_ORG_TREE_SELECT_RESOPONSE = "onOrgTreeSelectResponse";
	/**
	 * 点击组织树信息促发查询事件
	 */
	public static final String ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST = "onOrgTreeSelectCallQueryRequest";
	/**
	 * 点击组织树信息促发查询响应
	 */
	public static final String ON_ORG_TREE_SELECT_CALL_QUERY_RESPONSE = "onOrgTreeSelectCallQueryResponse";

	/**
	 * 删除组织树信息促发查询事件
	 */
	public static final String ON_DEL_ORG_TREE_REQUEST = "onDelOrgTreeRequest";
	/**
	 * 删除组织树信息促发查询响应
	 */
	public static final String ON_DEL_ORG_TREE_RESPONSE = "onDelOrgTreeResponse";

	/**
	 * 树发布中
	 */
	public static final Long IS_PUBLISHING = 1L;
	/**
	 * 树不再发布中
	 */
	public static final Long NOT_PUBLISHING = 0L;
	/**
	 * 点击业务系统请求事件
	 */
	public static final String ON_BUSINESS_SYSTEM_SELECT_REQUEST = "onBusinessSystemSelectRequest";
	/**
	 * 点击业务系统响应事件
	 */
	public static final String ON_BUSINESS_SYSTEM_SELECT_RESOPONSE = "onBusinessSystemSelectResponse";
	/**
	 * 点击业务系统触发TAB页查询事件
	 */
	public static final String ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST = "onBusinessSystemSelectCallQueryRequest";
	/**
	 * 点击业务系统触发TAB页查询响应事件
	 */
	public static final String ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_RESPONSE = "onBusinessSystemSelectCallQueryResponse";
}
