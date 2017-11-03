/**
 * 
 */
package cn.ffcs.uom.information.vo;


/**
 * @author 曾臻
 * @date 2012-11-20
 *
 */
public class TargetOrgVo {
	private long orgId;
	private String orgUuid;
	private long type;
	
	public String getOrgUuid() {
		return orgUuid;
	}
	public void setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
}
