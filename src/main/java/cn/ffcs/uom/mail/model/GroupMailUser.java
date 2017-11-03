package cn.ffcs.uom.mail.model;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GroupMailUser {

	/**
	 * 邮箱账号
	 */
	@Setter
	@Getter
	private String account;
	/**
	 * 邮箱账号信息
	 */
	@Setter
	@Getter
	private String accountInfo;

	/**
	 * 如果部门不存在，添加到省根组织下 默认为false,传true时则表示使用此规则
	 */
	@Setter
	@Getter
	private boolean addInRootWhenNotDepartMent;

	/**
	 * 地址
	 */
	@Setter
	@Getter
	private String address;

	/**
	 * 别名
	 */
	@Setter
	@Getter
	private String alias;

	/**
	 * 出生年月 格式YYYY-MM-DD
	 */
	@Setter
	@Getter
	private String birthday;

	/**
	 * 员工姓名
	 */
	@Setter
	@Getter
	@JsonProperty("c_name")
	private String cName;

	/**
	 * 省份
	 */
	@Setter
	@Getter
	private String proviceId;

	/**
	 * 城市
	 */
	@Setter
	@Getter
	private String cityId;

	/**
	 * 公司电话
	 */
	@Setter
	@Getter
	@JsonProperty("compay_phone")
	private String compayPhone;

	/**
	 * 密码
	 */
	@Setter
	@Getter
	private String password;

	/**
	 * 确认密码
	 */
	@Setter
	@Getter
	private String confirmPassword;

	/**
	 * 性别
	 */
	@Setter
	@Getter
	private String gender;

	@Setter
	@Getter
	private String groupids;

	/**
	 * HR编码
	 */
	@Setter
	@Getter
	private String hrCode;

	/**
	 * 是否在成员列表中显示标识位 False为不显示,True为显示
	 */
	@Setter
	@Getter
	private boolean invisible;

	/**
	 * IP
	 */
	@Setter
	@Getter
	private String ip;

	/**
	 * 邮箱空间大小[以M（兆）为单位]
	 */
	@Setter
	@Getter
	@JsonProperty("mailBoxSize")
	private String mailboxSize;

	/**
	 * 移动电话
	 */
	@Setter
	@Getter
	@JsonProperty("mobile_phone")
	private String mobilePhone;

	/**
	 * 是否要修改组织名称
	 */
	@Setter
	@Getter
	private boolean modifyPartMentName;

	/**
	 * 消息
	 */
	@Setter
	@Getter
	private String msg;

	/**
	 * 职位
	 */
	@Setter
	@Getter
	private String occupation;

	/**
	 * 操作标识
	 */
	@Setter
	@Getter
	private String oprationFlag;

	/**
	 * 组织部门
	 */
	@Setter
	@Getter
	private String partMent;

	/**
	 * 组织描述
	 */
	@Setter
	@Getter
	private String partMentDesc;

	/**
	 * 组织序号
	 */
	@Setter
	@Getter
	private String partMentNum;

	/**
	 * 岗级
	 */
	@Setter
	@Getter
	private String positionNumber;

	/**
	 * 推荐账号
	 */
	@Setter
	@Getter
	private String[] recommend;

	/**
	 * 办公电话
	 */
	@Setter
	@Getter
	private String retCode;

	/**
	 * 人员序号
	 */
	@Setter
	@Getter
	@JsonProperty("shownum")
	private String showNum;

	/**
	 * 用户状态 正常状态0 禁用状态1
	 */
	@Setter
	@Getter
	private String status;

}
