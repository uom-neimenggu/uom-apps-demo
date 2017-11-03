package cn.ffcs.uom.mail.model;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GroupMailRootOutParam {

	/**
	 * 结果
	 */
	@Setter
	@Getter
	private boolean ret;

	/**
	 * 错误信息
	 */
	@Setter
	@Getter
	private String errorMsg;

	/**
	 * 是否成功
	 */
	@Setter
	@Getter
	private boolean isSuccess;

	/**
	 * 信息
	 */
	@Setter
	@Getter
	private String msg;

	/**
	 * 时间戳
	 */
	@Setter
	@Getter
	private String timeStamp;

	/**
	 * 推荐账号
	 */
	@Setter
	@Getter
	private String[] recommend;

	/**
	 * 如果accountList不为空，则表示此组织下还有用户，不能删除
	 */
	@Setter
	@Getter
	private String[] accountList;

	/**
	 * 用户信息
	 */
	@Setter
	@Getter
	private GroupMailUser user;

	/**
	 * 出生年月 格式YYYY-MM-DD
	 */
	@Setter
	@Getter
	private String birthday;

	/**
	 * 职位
	 */
	@Setter
	@Getter
	private String occupation;

	/**
	 * 人员序号
	 */
	@Setter
	@Getter
	@JsonProperty("shownum")
	private String showNum;

	/**
	 * 省份
	 */
	@Setter
	@Getter
	private String states;

	/**
	 * 岗级
	 */
	@Setter
	@Getter
	private String positionNumber;

	/**
	 * 员工姓名
	 */
	@Setter
	@Getter
	@JsonProperty("c_name")
	private String cName;

	/**
	 * 城市
	 */
	@Setter
	@Getter
	private String city;

	/**
	 * 移动电话
	 */
	@Setter
	@Getter
	@JsonProperty("mobile_phone")
	private String mobilePhone;

	/**
	 * 移动电话
	 */
	@Setter
	@Getter
	@JsonProperty("company_phone")
	private String companyPhone;

	/**
	 * 邮箱账号
	 */
	@Setter
	@Getter
	private String account;

	/**
	 * 性别
	 */
	@Setter
	@Getter
	private String gender;

}
