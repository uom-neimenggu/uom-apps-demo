package cn.ffcs.uom.organization.constants;

public class StaffOrganizationConstant {
	public static final String ON_STAFF_ORGANIZATION_QUERY = "onStaffOrganiztionQuery";
	public static final String ON_STAFF_ORGANIZATION_SAVE = "onStaffOrganiztionSave";
	
	public static final String STATISTIC_STAFF_RALA_CD = "99";
	
	/**
     * 校验字段
     */
    public static final String NULL_OR_EMPTY = "0";// 空或空字符串
    public static final String NULL_OR_EMPTY_STR = "空";
    public static final String LENGTH_LIMIT = "1";// 长度有误
    public static final String LENGTH_LIMIT_STR = "长度有误";
    public static final String FIELD_REPEAT = "2";// 重复
    public static final String FIELD_REPEAT_STR = "同一个员工和一个组织只能一条关系！员工组织关系重复";
    public static final String FIELD_ERROR = "3";
    public static final String FIELD_ERROR_STR = "格式不正确";
    public static final String FIELD_NOT_EXIST = "4";// 不存在
    public static final String FIELD_NOT_EXIST_STR = "数据不存在";
    public static final String FIELD_ERROR_VAL = "5";// 值不正确
    public static final String FIELD_ERROR_VAL_STR = "不正确的值";
    public static final String FIELD_EXIST_VAL = "6";// 值已经存在
    public static final String FIELD_EXIST_VAL_STR = "的值已经存在";
    public static final String STAFF_EXIST_MANY_ACCOUNT = "7"; //一个账号对应多个员工
    public static final String STAFF_EXIST_MANY_ACCOUNT_STR = "一个账号对应多个员工";
    public static final String ORG_REL_RULE_ERROR = "8"; //规则校验失败，字符串由校验规则返回
    public static final String ORG_REL_MANY_RELATION = "9";
    public static final String ORG_REL_MANY_RELATION_STR = "无法确定对象或单个对象与单个组织存在多个关系";
    public static final String ORG_REL_ONLY_ONE = "10"; //该员工只有一条员工组织记录强制为归属关系
    public static final String ORG_REL_ONLY_ONE_STR = "该员工只有一条员工组织记录强制为归属关系";
    public static final String ORG_REL_RALA_CD_1_CHANGE = "11";
    public static final String ORG_REL_RALA_CD_1_CHANGE_STR = "归属改为非归属，只能单个修改";
    public static final String ORG_REL_OTHER_REALTION = "12"; //剩下不止一条关系且不存在归属关系
    public static final String ORG_REL_OTHER_REALTION_STR = "数据处理后剩下不止一条关系且不存在归属关系";
    public static final String ORG_REL_EXIST = "13";
    public static final String ORG_REL_EXIST_STR = "员工组织关系不存在";
}
