package cn.ffcs.uom.common.vo;


/**
 * @author 曾臻
 * @date 2012-11-8
 *
 */
public class UserVo {
	private long userId;
	private String firstName;
	private String lastName;
	private String screenName;
	private String groupName;
	private long groupId;
	private String loginIp;
	private String email;
	private String tel;
	private String uuid;
	private String userUuId;
	
	
	public String getUserUuId() {
		return userUuId;
	}
	public void setUserUuId(String userUuId) {
		this.userUuId = userUuId;
	}
	//辅助属性
	private boolean selfFlag;
	
	
	
	/**
	 * @return the selfFlag
	 */
	public boolean isSelfFlag() {
		return selfFlag;
	}
	/**
	 * @param selfFlag the selfFlag to set
	 */
	public void setSelfFlag(boolean selfFlag) {
		this.selfFlag = selfFlag;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setScreenName(String screenName) {

		this.screenName = screenName;
	}
	public String getScreenName() {

		return screenName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {

		return groupName;
	}
	public void setGroupId(long groupid) {

		this.groupId = groupid;
	}
	public long getGroupId() {

		return groupId;
	}
	public void setLoginIp(String loginIp) {

		this.loginIp = loginIp;
	}
	public String getLoginIp() {

		return loginIp;
	}
	public void setEmail(String email) {

		this.email = email;
	}
	public String getEmail() {

		return email;
	}
	public void setTel(String tel) {

		this.tel = tel;
	}
	public String getTel() {

		return tel;
	}
	public void setUuid(String uuid) {

		this.uuid = uuid;
	}
	public String getUuid() {

		return uuid;
	}

	
}
