package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpStaffCustomAttr extends AttrItemInParam {

	/**
	 * 员工扩展属性标识
	 */
	public Long getGrpStaffCustomAttrId() {
		return super.getId();
	}

	public void setGrpStaffCustomAttrId(Long grpStaffCustomAttrId) {
		super.setId(grpStaffCustomAttrId);
	}

	@Setter
	@Getter
	private String salesCode;

}
