package cn.ffcs.uom.organization.constants;

public class OrganizationTranConstant {
	/**
	 * 业务关系编码规则 各个层级分别以两位编码进行区分，事例如下： 域内业务关系--10 域外业务关系--20 跨域内外业务关系--30
	 */

	/**
	 * 营销组织与财务组织关系[多对一]前缘
	 */
	public static final String MARKETING_FINACIAL_PRE = "1001";

	/**
	 * 营销组织与成本中心对应关系[多对一]
	 */
	public static final String MARKETING_FINACIAL_MANY_TO_ONE = "100101";

	/**
	 * 网运组织关系前缀
	 */
	public static final String NETWORK_OPERATION_PRE = "1002";

	/**
	 * 营业部与网运接入包区关系[多对一]
	 */
	public static final String DEPARTMENT_NETWORK_MANY_TO_ONE = "100201";

	/**
	 * 统一用户与集团统一目录关系前缀
	 */
	public static final String UOM_GROUP_DIRECTORY_PRE = "3001";

	/**
	 * 统一用户与集团统一目录组织对应关系[一对一]
	 */
	public static final String UOM_GROUP_DIRECTORY_ONE_TO_ONE = "300101";

	/**
	 * 统一用户与集团主数据关系前缀
	 */
	public static final String UOM_GROUP_MAIN_DATA_PRE = "3002";

	/**
	 * 统一用户财务公司组织与集团公司代码对应关系[一对一]
	 */
	public static final String UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE = "300201";

	/**
	 * 统一用户财务利润组织与集团利润中心对应关系[一对一]
	 */
	public static final String UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE = "300202";

	/**
	 * 统一用户财务成本组织与集团成本中心对应关系[一对一]
	 */
	public static final String UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE = "300203";

	/**
	 * 统一用户网点组织与集团成本中心对应关系[多对一]
	 */
	public static final String UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE = "300204";

	/**
	 * 统一用户网点组织与集团供应商对应关系[多对一]
	 */
	public static final String UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE = "300205";
	/**
	 * 统一用户营销组织与集团成本中心对应关系[多对一]
	 */
	public static final String UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE = "300206";
	/**
	 * 统一用户网点组织与集团供应商付款方银行账号对应关系[多对一]
	 */
	public static final String UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE = "300207";
	/**
	 * 渠道划小业务关系 channelPackareaRelationVo
	 */
	public static final String CHANNEL_PACKAREA_RELATION = "100300";

	/**
	 * 组织树选择组织响应事件
	 */
	public static final String ON_ORGANIZATION_TRAN_QUERY = "onOrganiztionTranQuery";
	/**
	 * 组织树选择组织响应事件
	 */
	public static final String ON_UOM_GROUP_ORG_TRAN_QUERY = "onUomGroupOrgTranQuery";

	/**
	 * 集团成本中心
	 */
	public static final String GROUP_COST_CENTER_ONE_TO_ONE = "1";
	/**
	 * 集团利润中心
	 */
	public static final String GROUP_PROFIT_CENTER_ONE_TO_ONE = "2";
	/**
	 * 集团公司代码
	 */
	public static final String GROUP_COMPANY_CODE_ONE_TO_ONE = "3";
	/**
	 * 集团供应商
	 */
	public static final String GROUP_SUPPLIER_ONE_TO_ONE = "4";
	/**
	 * 集团统一目录
	 */
	public static final String GROUP_DIRECTORY_ONE_TO_ONE = "5";
	/**
	 * 供应商付款方银行账号
	 */
	public static final String GROUP_SUPPLIER_BANK_ONE_TO_ONE = "6";

	/**
	 * 组织业务关系大类前缀长度
	 */
	public static final int ORGANIZATION_TRAN_PRE_LENGTH = 4;

	/**
	 * 组织查询事件
	 */
	public static final String ON_GROUP_ORGANIZATION_QUERY = "onGroupOrganiztionQuery";
	/**
	 * 组织选择事件
	 */
	public static final String ON_SELECT_GROUP_ORGANIZATION = "onSelectGroupOrganization";

	/**
	 * 组织清空事件
	 */
	public static final String ON_CLEAN_GROUP_ORGANIZATION = "onCleanGroupOrganization";

	/**
	 * 组织关闭事件
	 */
	public static final String ON_CLOSE_GROUP_ORGANIZATION = "onCloseGroupOrganization";

	/**
	 * 组织bandbox设置组织类型
	 */
	public static final String ON_SET_ORGTYPE_REQUEST = "onSetOrgTypeList";
	
	/**
	 * 查询供应商相关信息
	 */
	public static final String ON_SUPPLIER_INFO_REQUEST = "onSupplierInfoRequest";

}
