package cn.ffcs.uom.common.vo;


/**
 * @author 曾臻
 * @date 2012-11-8
 *
 */
public class OrgVo {
	private String name;
	private long parentOrgId;
	private Long orgId;
	private String uuid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getParentOrgId() {
		return parentOrgId;
	}
	public void setParentOrgId(long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public void setUuid(String uuid) {

		this.uuid = uuid;
	}
	public String getUuid() {

		return uuid;
	}
	
}
