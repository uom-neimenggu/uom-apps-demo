package cn.ffcs.uom.party.constants;

import cn.ffcs.uom.common.model.UomClassProvider;

public class PartyCertificationConstant {
	/**
     * 校验字段
     */
    public static final String NULL_OR_EMPTY = "0";// 空或空字符串
    public static final String NULL_OR_EMPTY_STR = "空";
    public static final String LENGTH_LIMIT = "1";// 长度有误
    public static final String LENGTH_LIMIT_STR = "长度有误";
    public static final String FIELD_ERROR = "2";
    public static final String FIELD_ERROR_STR = "不正确的值";
    public static final String FIELD_NOT_EXIST = "3";// 不存在
    public static final String FIELD_NOT_EXIST_STR = "员工账号或者对应的参与人不存在";
    public static final String FIELD_ERROR_VAL = "4";// 值不正确
    public static final String FIELD_ERROR_VAL_STR = "实名认证未通过";
    public static final String DEFAULT_CERTIFICATION_NOTEXIST = "5";// 值不正确
    public static final String DEFAULT_CERTIFICATION_NOTEXIST_STR = "该员工对应的参与人不存在默认证件";
    public static final String CERTIFICATION_NOTEXIST = "6";// 值不正确
    public static final String CERTIFICATION_NOTEXIST_STR = "该员工对应的参与人的该证件号码证件不存在";
    public static final String ONE_DEFAULT_CERTIFICATION = "7";// 值不正确
    public static final String ONE_DEFAULT_CERTIFICATION_STR = "该员工对应的参与人只有一个证件";
    public static final String FIELD_ERROR_CERT_ALREADY_USE = "8";
	public static final String FIELD_ERROR_CERT_ALREADY_USE_STR = "身份证已经被使用次数超过%次上限";
	
}
