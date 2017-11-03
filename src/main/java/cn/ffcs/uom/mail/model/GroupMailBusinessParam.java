package cn.ffcs.uom.mail.model;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GroupMailBusinessParam {

	/**
	 * 业务逻辑标识
	 */
	@Setter
	@Getter
	private String bizId;

	/**
	 * 系统标识
	 */
	@Setter
	@Getter
	private String sysId;

	/**
	 * 应用程序标识
	 */
	@Setter
	@Getter
	private String appKey;

	/**
	 * 时间戳
	 */
	@Setter
	@Getter
	private String timeStamp;

	/**
	 * 邮箱账号
	 */
	@Setter
	@Getter
	private String account;

	/**
	 * 密码
	 */
	@Setter
	@Getter
	private String password;

	/**
	 * 员工姓名
	 */
	@Setter
	@Getter
	@JsonProperty("c_name")
	private String cName;

	/**
	 * 人员序号
	 */
	@Setter
	@Getter
	@JsonProperty("shownum")
	private String showNum;

	/**
	 * 性别
	 */
	@Setter
	@Getter
	private String gender;

	/**
	 * 出生年月 格式YYYY-MM-DD
	 */
	@Setter
	@Getter
	private String birthday;

	/**
	 * 所属省份
	 */
	@Setter
	@Getter
	private String states;

	/**
	 * 所属地市
	 */
	@Setter
	@Getter
	private String city;

	/**
	 * 组织部门
	 */
	@Setter
	@Getter
	private String partment;

	/**
	 * 邮箱空间大小[以M（兆）为单位]
	 */
	@Setter
	@Getter
	@JsonProperty("mailboxsize")
	private String mailboxSize;

	/**
	 * 组织序号
	 */
	@Setter
	@Getter
	private String partMentNum;

	/**
	 * 组织描述
	 */
	@Setter
	@Getter
	private String partMentDesc;
	/**
	 * 是否要修改组织名称
	 */
	@Setter
	@Getter
	private boolean modifyPartMentName;

	/**
	 * 办公电话
	 */
	@Setter
	@Getter
	@JsonProperty("company_phone")
	private String companyPhone;

	/**
	 * 移动电话
	 */
	@Setter
	@Getter
	@JsonProperty("mobile_phone")
	private String mobilePhone;

	/**
	 * 职位
	 */
	@Setter
	@Getter
	private String occupation;

	/**
	 * 用户状态 正常状态0 禁用状态1
	 */
	@Setter
	@Getter
	private String status;

	/**
	 * 是否在成员列表中显示标识位 False为不显示,True为显示
	 */
	@Setter
	@Getter
	private boolean invisible;

	/**
	 * 岗级
	 */
	@Setter
	@Getter
	private String positionNumber;

	/**
	 * 如果部门不存在，添加到省根组织下 默认为false,传true时则表示使用此规则
	 */
	@Setter
	@Getter
	private boolean addInRoot;

	/**
	 * 推荐账号
	 */
	@Setter
	@Getter
	private String recommend;

	/**
	 * HR编码
	 */
	@Setter
	@Getter
	private String hrCode;

}
