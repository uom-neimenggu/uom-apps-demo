package cn.ffcs.uom.organization.constants;

public class OrganizationConstant {
	public static final String ON_ORGANIZATION_QUERY = "onOrganiztionQuery";
	public static final String ON_ORGANIZATION_QUERY_RESPONSE = "onQueryOrganizationResponse";
	public static final String ON_SELECT_POLITICAL_RELATION_RESPONSE = "onSelectPoliticalRelationResponse";
	public static final String ON_ORGANIZATION_SAVE = "onOrganiztionSave";
	public static final String ON_SELECT_ORGANIZATION = "onSelectOrganization";
	public static final String ON_CLEAN_ORGANIZATION = "onCleanOrganization";
	public static final String ON_CLOSE_ORGANIZATION = "onCloseOrganization";
	public static final String ON_ORGANIZATION_TREE_QUERY = "onOrganiztionTreeQuery";
	public static final String ON_ORGANIZATION_ISSUED_QUERY = "onOrganiztionIssuedQuery";
	public static final String ON_GRID_UNIT_REF_QUERY = "onGridUnitRefQuery";
	public static final String ON_CLEAN_GRID_UNIT = "onCleanGridUnit";
	public static final String ON_CLOSE_GRID_UNIT = "onCloseGridUnit";
	/**
	 * 上下级关系
	 */
	public static final String ORGANIZATION_RELATION_PARENT_RELA_CD = "01";

	/**
	 * 输入属性Textbox
	 */
	public static final Long ATTR_SPEC_TYPE_INPUT = 1L;

	/**
	 * 选择属性Listbox
	 */
	public static final Long ATTR_SPEC_TYPE_SELECT = 2L;

	/**
	 * 选择属性Bandbox
	 */
	public static final Long ATTR_SPEC_TYPE_BANDBOX = 3L;

	/**
	 * 可以输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_NORMAL = "10";
	/**
	 * 挂树上才可输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_TREE = "11";
	/**
	 * 不可以输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_ABNORMAL = "20";
	/**
	 * 网格类别--营业部标签产品网格
	 */
	public static final String ATTR_SPEC_8_ATTR_VALUE_13 = "13";
	/**
	 * 网格类型
	 */
	public static final Long ORG_ATTR_SPEC_ID_8 = 8L;

	/**
	 * 网格小类
	 */
	public static final Long ORG_ATTR_SPEC_ID_9 = 9L;

	/**
	 * 客户群
	 */
	public static final Long ORG_ATTR_SPEC_ID_10 = 10L;

	/**
	 * 城乡属性
	 */
	public static final Long ORG_ATTR_SPEC_ID_11 = 11L;

	/**
	 * 集团4G门户编码
	 */
	public static final Long ORG_ATTR_SPEC_ID_18 = 18L;

	/**
	 * 集团统一邮箱组织编码
	 */
	public static final Long ORG_ATTR_SPEC_ID_19 = 19L;

	/**
	 * 业务编码
	 */
	public static final String TCP_CONT_RESPONSE_BUS_CODE = "000";
	/**
	 * 容许操作
	 */
	public static final String GRID_RESPONSE_RESULT_ID_1 = "1";
	/**
	 * 不容许操作
	 */
	public static final String GRID_RESPONSE_RESULT_ID_0 = "0";
	/**
	 * 操作成功
	 */
	public static final String GRID_RESPONSE_RESULT_CODE_0000 = "0000";
	/**
	 * 修改组织
	 */
	public static final String OPT_TYPE_3 = "3";
	/**
	 * 删除组织
	 */
	public static final String OPT_TYPE_5 = "5";

	/**
	 * 选择属性
	 */
	public static final Long ROOT_ORG_ID = 0l;
	/**
	 * 代理商根节点
	 */
	public static final Long ROOT_AGENT_ORG_ID = 9999999998l;
	/**
	 * 内部经营实体根节点
	 */
	public static final Long ROOT_IBE_ORG_ID = 9999999998l;

	/**
	 * 供应商商根节点
	 */
	public static final Long ROOT_SUPPLIER_ORG_ID = 9999999997l;

	/**
	 * 营销树根节点
	 */
	public static final Long ROOT_EDW_ORG_ID = 9999999995l;
	/**
	 * 新营销树根节点
	 */
	public static final Long ROOT_MARKETING_ORG_ID = 9999999993l;
	/**
	 * 营销树2016根节点
	 */
	public static final Long ROOT_NEW_MARKETING_ORG_ID = 9999999991L;
	
	/**
	 * 营销树2017根节点
	 */
	public static final Long ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID = 9999999990L;
	/**
	 * 成本树根节点
	 */
	public static final Long ROOT_COST_ORG_ID = 9999999992l;
	/**
	 * 选择维护树上的组织
	 */
	public static final String ON_SELECT_TREE_ORGANIZATION = "onSelectTreeOrganization";
	/**
	 * 选择维护组织树根节点
	 */
	public static final String ON_SELECT_TREE_ROOT = "onSelectTreeRoot";
	/**
	 * 选择统一目录树上的组织
	 */
	public static final String ON_SELECT_TREE_UNITED_DIRECTORY = "onSelectTreeUnitedDirectory";
	/**
	 * 选择维护树上的组织
	 */
	public static final String ON_SELECT_TREE_ORGANIZATION_RESPONSE = "onSelectTreeOrganizationResponse";
	/**
	 * 维护树初始化请求
	 */
	public static final String ORGANIZATION_TREE_MAIN_INIT_REQUEST = "onOrganizationTreeManInitRequest";
	/**
	 * 维护树初始化响应
	 */
	public static final String ORGANIZATION_TREE_MAIN_INIT_RESPONSE = "onOrganizationTreeManInitResponse";
	/**
	 * 树父亲根节点
	 */
	public static final Long ROOT_TREE_PARENT_ORG_ID = 0l;

	/**
	 * 树根节点
	 */
	public static final Long ROOT_TREE_ORG_ID = 9999999999l;
	/**
	 * 选择组织岗位
	 */
	public static final String ON_SELECT_ORGANIZATION_POSITION_REQUEST = "onSelectOrganizationPositionRequest";
	/**
	 * 选择组织岗位响应
	 */
	public static final String ON_SELECT_ORGANIZATION_POSITION_RESPONSE = "onSelectOrganizationPositionResponse";
	/**
	 * 选择组织岗位
	 */
	public static final String ON_CLEAN_ORGANIZATION_POSITION_REQUEST = "onCleanOrganizationPositionRequest";
	/**
	 * 选择组织岗位响应
	 */
	public static final String ON_CLEAN_ORGANIZATION_POSITION_RESPONSE = "onCleanOrganizationPositionResponse";

	/**
	 * 选择组织岗位
	 */
	public static final String ON_CLOSE_ORGANIZATION_POSITION_REQUEST = "onCloseOrganizationPositionRequest";
	/**
	 * 选择组织岗位响应
	 */
	public static final String ON_CLOSE_ORGANIZATION_POSITION_RESPONSE = "onCloseOrganizationPositionResponse";

	/**
	 * 代理商页面
	 */
	public static final String ON_SET_AGENT_ORGANIZATION_REQUEST = "onSetAgentOrganizationType";
	/**
	 * 内部经营实体页面
	 */
	public static final String ON_SET_IBE_ORGANIZATION_REQUEST = "onSetIbeOrganizationType";
	
	/**
	 * 包区页面
	 */
	public static final String ON_PACKARE_ORGANIZATION_REQUEST = "onPackareOrganizationType";
	
	/**
	 * 代理商组织第五级数据
	 */
	public static final String ON_AGENT_CHANNNEL_ORGANIZATION_REQUEST = "onAgentChannelOrganizationType";

	/**
	 * 组织树页面排除代理商组织
	 */
	public static final String ON_EXCLUDE_AGENT_ORGANIZATION_REQUEST = "onExcludeAgentOrganization";
	/**
	 * 组织树页面排除内部经营实体组织
	 */
	public static final String ON_EXCLUDE_IBE_ORGANIZATION_REQUEST = "onExcludeIbeOrganization";
	/**
	 * 组织树页面排除内部经营实体组织
	 */
	public static final String ON_MULTIDIMENSIONAL_TREE_ORGANIZATION_QUERY = "onMultidimensionalTreeOrganizationQuery";

	/**
	 * 代理商页面添加下级节点包含营业网点
	 */
	public static final String ON_SET_AGENT_CONTAIN_SALESNETWORK_REQUEST = "onSetAgentContainSalesNetwork";
	/**
	 * 代理商页面添加下级节点包含营业网点
	 */
	public static final String ON_SET_IBE_CONTAIN_SALESNETWORK_REQUEST = "onSetIbeContainSalesNetwork";

	/**
	 * 代理商页面选择根节点
	 */
	public static final String ON_CHOOSE_AGENT_ORGANIZATION_ROOT_REQUEST = "onChooseAgentOrganizationRoot";
	/**
	 * 内部经营实体页面选择根节点
	 */
	public static final String ON_CHOOSE_IBE_ORGANIZATION_ROOT_REQUEST = "onChooseIbeOrganizationRoot";

	/**
	 * 选择代理商组织树
	 */
	public static final String ON_SELECT_AGENT_ORGANIZATION_TREE_REQUEST = "onSelectAgentOrganizationTree";
	/**
	 * 选择专业树
	 */
	public static final String ON_SELECT_PROFESSION_ORGANIZATION_TREE_REQUEST = "onSelectProfessionOrganizationTree";
	/**
	 * 选择内部经营实体组织树
	 */
	public static final String ON_SELECT_IBE_ORGANIZATION_TREE_REQUEST = "onSelectIbeOrganizationTree";
	/**
	 * 选择代理商组织树
	 */
	public static final String ON_SELECT_POLITICAL_ORGANIZATION_TREE_REQUEST = "onSelectPoliticalOrganizationTree";
	/**
	 * 选择供应商商组织树
	 */
	public static final String ON_SELECT_SUPPLIER_ORGANIZATION_TREE_REQUEST = "onSelectSupplierOrganizationTree";
	/**
	 * 选择中通服组织树
	 */
	public static final String ON_SELECT_ORGANIZATION_TREE_REQUEST = "onSelectOrganizationTree";
	/**
	 * 选择EDW组织树
	 */
	public static final String ON_SELECT_EDW_ORGANIZATION_TREE_REQUEST = "onSelectEdwOrganizationTree";
	/**
	 * 选择Marketing组织树
	 */
	public static final String ON_SELECT_MARKETING_ORGANIZATION_TREE_REQUEST = "onSelectMarketingOrganizationTree";
	/**
	 * 选择Cost组织树
	 */
	public static final String ON_SELECT_COST_ORGANIZATION_TREE_REQUEST = "onSelectCostOrganizationTree";
	/**
	 * 选择统一目录树
	 */
	public static final String ON_SELECT_UNITED_DIRECTORY_TREE_REQUEST = "onSelectUnitedDirectoryTreeRequest";

	/**
	 * 组织bandbox设置组织类型
	 */
	public static final String ON_SET_ORGTYPE_REQUEST = "onSetOrgTypeList";

	/**
	 * 组织bandbox设置页面类型，公开页面忽略数据区 电信管理区域
	 */
	public static final String ON_SET_CONFIG_PAGE_REQUEST = "onSetConfigPage";

	/**
	 * 查看员工组织业务关系
	 */
	public static final String ON_STAFF_ORGANIZATION_TRAN_QUERY = "onStaffOrganiztionTranQuery";
	/**
	 * 查询组织岗位
	 */
	public static final String ON_ORGANIZATION_POSITION_QUERY = "onOrganiztionPositionQuery";

	public static final String ON_ORGANIZATION_TREE_SELECT = "onOrganiztionTreeSelect";

	/**
	 * 管理类前缀
	 */
	public static final String MANAGER_PRE = "N01";
	/**
	 * 行政前缀
	 */
	public static final String ADMINISTRATIVE_PRE = "N0101";
	/**
	 * 单位前缀
	 */
	public static final String COMPANY_PRE = "N010101";
	/**
	 * 部门前缀
	 */
	public static final String DEPARTMENT_PRE = "N010102";
	/**
	 * 团队前缀
	 */
	public static final String DEPARTMENT_TEAM = "N010103";
	/**
	 * 营业网点前缀
	 */
	public static final String SALES_NETWORK_PRE = "N02";
	/**
	 * 财务组织类型前缀
	 */
	public static final String FINANCE_ORG_PRE = "N12";
	/**
	 * 营销组织类型前缀
	 */
	public static final String MARKETING_ORG_PRE = "N11";
	/**
	 * 代理商
	 */
	public static final String ROLE_TYPE_AGENT = "1301";
	/**
	 * 组织默认排序
	 */
	public static final Long DEFAULT_ORG_PRIORITY = 200L;
	/**
	 * 删除节点成功事件
	 */
	public static final String ON_DEL_NODE_OK = "onDelNodeOK";

	public static final String SEQ_ORG_CODE = "SEQ_ORG_CODE";

	public static final String PREFIX_OF_ORG_CODE = "81";

	public static final int LENGTH_OF_ORG_CODE = 8;

	/**
	 * 选择推导树
	 */
	public static final String ON_SELECT_TREE_TYPE = "onSelectTreeType";

	/**
	 * 选择推导树抛出时间
	 */
	public static final String ON_SELECT_DEDUCE_TREE_SECOND_REQUEST = "onSelectDuceTreeSecondRequest";

	/**
	 * 组织列表删除组织
	 */
	public static final String ON_DEL_ORGANIZAITON = "onDelOrganization";
	/**
	 * 是否显示新增删除按钮
	 */
	public static final String ON_IS_VISIBLE = "onIsVisible";

	/**
	 * 组织树组织信息保存事件
	 */
	public static final String ON_SAVE_ORGANIZATION_INFO = "onSaveOrganiztionInfo";
	/**
	 * 组织树组织信息保存事件
	 */
	public static final String ON_SAVE_UNITED_DIRECTORY_INFO = "onSaveUnitedDirectoryInfo";
	/**
	 * 营销树2015层级控制事件
	 */
	public static final String ON_ORGANIZATION_LEVEL_REQUEST = "onOrganizationLevelRequest";
	/**
	 * 营销树2015层级控制事件
	 */
	public static final String ON_ORGANIZATION_LEVEL_RESPONSE = "onOrganizationLevelResponse";

	/**
	 * 组织树组织信息保存事件
	 */
	public static final String ON_CHANGE_TREE_STYLE = "onChangeTreeStyle";

	/**
	 * 组织员工选择.
	 */
	public static final String ON_STAFFORGANIZATION_SELECT = "onSelectStaffOrganization";
	/**
	 * 网格组织关系选择.
	 */
	public static final String ON_GIRD_UNIT_REF_SELECT = "onSelectGridUnitRef";
	/**
	 * 网格单元选择.
	 */
	public static final String ON_GIRD_UNIT_SELECT = "onSelectGridUnit";
	public static final String ON_GIRD_UNIT_NOT_SELECT = "onSelectNotGridUnit";
	public static final String ON_GIRD_UNIT_CLEAR_TABS = "onGridUnitClearTabs";
	public static final String ON_GIRD_UNIT_CLEAR_TABS_RESPONS = "onGridUnitClearTabsRespons";
	/**
	 * 代理商组织类型
	 */
	public static final String ORG_TYPE_AGENT = "N0901000000";
	/**
	 * 内部组织
	 */
	public static final String ORG_TYPE_N = "1000";

	/**
	 * 内部组织组织编码第一位
	 */
	public static final String ORG_CODE_N = "1";

	/**
	 * 外部组织
	 */
	public static final String ORG_TYPE_W = "1100";
	/**
	 * 外部组织组织编码第一位
	 */
	public static final String ORG_CODE_W = "2";
	/**
	 * 实业公司
	 */
	public static final String ORG_TYPE_N0904000000 = "N0904000000";
	/**
	 * 供应商
	 */
	public static final String ORG_TYPE_N0902000000 = "N0902000000";
	/**
	 * 内部经营实体
	 */
	public static final String ORG_TYPE_N0903000000 = "N0903000000";
	/**
	 * 集团(外部)
	 */
	public static final String ORG_TYPE_N1001010100 = "N1001010100";
	/**
	 * 公司(外部)
	 */
	public static final String ORG_TYPE_N1001010200 = "N1001010200";
	/**
	 * 公司(外部)
	 */
	public static final String ORG_TYPE_N1001020100 = "N1001020100";
	/**
	 * 班组(外部)
	 */
	public static final String ORG_TYPE_N1001040100 = "N1001040100";
	/**
	 * 收入单元(2015)组织类型
	 */
	public static final String ORG_TYPE_N1101040000 = "N1101040000";
	/**
	 * 网格(2015)组织类型
	 */
	public static final String ORG_TYPE_N1101050000 = "N1101050000";
	/**
	 * 划小单元（2015）组织类型划小单元（2015）-N1103000000
	 */
	public static final String ORG_TYPE_N1103000000 = "N1103000000";

	/**
	 * 校验字段
	 */
	public static final String NULL_OR_EMPTY = "0";// 空或空字符串
	public static final String NULL_OR_EMPTY_STR = "空";
	public static final String LENGTH_LIMIT = "1";// 长度有误
	public static final String LENGTH_LIMIT_STR = "长度有误";
	public static final String FIELD_REPEAT = "2";// 重复
	public static final String FIELD_REPEAT_STR = "重复";
	public static final String FIELD_ERROR = "3";
	public static final String FIELD_ERROR_STR = "格式不正确";
	public static final String FIELD_NOT_EXIST = "4";// 不存在
	public static final String FIELD_NOT_EXIST_STR = "上级组织ID不存在";
	public static final String FIELD_ERROR_VAL = "5";// 值不正确
	public static final String FIELD_ERROR_VAL_STR = "不正确的值";
	public static final String FIELD_EXIST_VAL = "6";// 值已经存在
	public static final String FIELD_EXIST_VAL_STR = "的值已经存在";
	public static final String FIELD_NOT_EXIST_AGENT_W = "7";
	public static final String FIELD_NOT_EXIST_AGENT_W_STR = "外部上级组织编码不存在或不是代理商";
	public static final String FIELD_NOT_EXIST_AGENT_N = "8";
	public static final String FIELD_NOT_EXIST_AGENT_N_STR = "内部上级组织编码不存在";
	public static final String FIELD_NOT_IN_REIGHT_SITE = "9";
	public static final String FIELD_NOT_IN_REIGHT_SITE_STR = "组织上级编码不是第4,5,6级";
	public static final String FIELD_NOT_IN_SEVENT_SITE = "10";
	public static final String FIELD_NOT_IN_SEVENT_SITE_STR = "网格类型只能是第7级";
	public static final String FILE_NOT_GRID = "11";
	public static final String FILE_NOT_GRID_STR = "必须是网格类型才可以有网格扩展属性";
	public static final String FILE_TYPE_NOT_RIGHT = "12";
	public static final String FILE_TYPE_NOT_RIGHT_STR = "网格类型和网格小类不匹配";
    
	

	/**
	 * 营销树ID
	 */
	public static final Long CUSTMS_TREE_ID = 5L;
	/**
	 * 新营销树ID
	 */
	public static final Long MARKETING_TREE_ID = 10L;
	/**
	 * 财务成本树ID
	 */
	public static final Long COST_TREE_ID = 8L;

	public static final String ORG_CONTRACTINFO_ADDRESS_ERROR = "组织联系地址不能为空";

	/**
	 * 关系类型-营销管理
	 */
	public static final String RELA_CD_YXGL = "0400";

	/**
	 * 关系类型-营销聚合
	 */
	public static final String RELA_CD_YXJH = "0401";
	/**
	 * 关系类型-营销聚合2015
	 */
	public static final String RELA_CD_YXJH_2015 = "0402";
	/**
	 * 关系类型-营销聚合2016
	 */
	public static final String RELA_CD_YXJH_2016 = "0403";
	/**
	 * 关系类型-营销聚合2017
	 */
	public static final String RELA_CD_YXJH_2017 = "0404";

	/**
	 * 关系类型-财务利润成本管理
	 */
	public static final String RELA_CD_COST_0500 = "0500";
	/**
	 * 关系类型-财务利润成本关系
	 */
	public static final String RELA_CD_COST_0501 = "0501";

	/**
	 * 营业部
	 */
	public static final String ORG_TYPE_N0102010900 = "N0102010900";
	/**
	 * 直营厅
	 */
	public static final String ORG_TYPE_N0101020300 = "N0101020300";
	/**
	 * 直营厅
	 */
	public static final String ORG_TYPE_N0102010800 = "N0102010800";

	public static final String ORG_TYPE_N0202 = "N0202";// 渠道

	/**
	 * 内部网点
	 */
	public static final String ORG_TYPE_N0202010000 = "N0202010000";// 自营实体渠道
	public static final String ORG_TYPE_N0202030000 = "N0202030000";// 自营直销渠道
	public static final String ORG_TYPE_N0202040000 = "N0202040000";// 自营电子渠道

	/**
	 * 外部网点
	 */
	public static final String ORG_TYPE_N0202020000 = "N0202020000";// 社会实体渠道
	public static final String ORG_TYPE_N0202050000 = "N0202050000";// 社会直销渠道
	public static final String ORG_TYPE_N0202060000 = "N0202060000";// 社会电子渠道

	/**
	 * 上级管理机构
	 */
	public static final String RELA_CD_INNER = "0101";
	/**
	 * 上级管理机构(外部)
	 */
	public static final String RELA_CD_EXTER = "0102";
	/**
	 * 外部关系
	 */
	public static final String RELA_CD_JZ = "0201";

	/**
	 * 营销聚合2015
	 */
	public static final String RELA_CD_MARKETING_CONVERGENCE = "0402";
	/**
	 * 营销聚合2016
	 */
	public static final String RELA_CD_MARKETING_CONVERGENCE_0403 = "0403";
	/**
	 * 营销聚合2017
	 */
	public static final String RELA_CD_MARKETING_CONVERGENCE_0404 = "0404";

	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL = "SEQ_EDA_CODE_LEVEL";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL1 = "SEQ_EDA_CODE_LEVEL1";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL2 = "SEQ_EDA_CODE_LEVEL2";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL3 = "SEQ_EDA_CODE_LEVEL3";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL4 = "SEQ_EDA_CODE_LEVEL4";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL5 = "SEQ_EDA_CODE_LEVEL5";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL6 = "SEQ_EDA_CODE_LEVEL6";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL7 = "SEQ_EDA_CODE_LEVEL7";
	/**
	 * EDA组织编码
	 */
	public static final String SEQ_EDA_CODE_LEVEL7_GRID = "SEQ_EDA_CODE_LEVEL7_GRID";
	/**
	 * 员工组织业务关系--负责人
	 */
	public static final String STAFF_ORG_TRAN_RALA_CD = "1";
	/**
	 * 组织存在类型 1实体组织
	 */
	public static final String ORG_EXIST_TYPE = "1";
	/**
	 * 集团渠道组织地址
	 */
	public static final Long GRP_ORG_ADDRESS = 50000019L;
	/**
	 * 树3
	 */
	public static final Long ORG_TREE_ID_3 = 3L;
	
	/**
	 * 组织树选中，对应查看相应的员工组织业务关系
	 */
	public static final String ON_SELECT_ORG_STAFF_ORGANIZATION_TRAN_QUERY = "onSelectOrgStaffOrganizationTranQuery";

}
