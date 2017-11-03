package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpStaffAttr extends AttrItemInParam {

	/**
	 * 员工扩展属性标识
	 */
	public Long getGrpStaffAttrId() {
		return super.getId();
	}

	public void setGrpStaffAttrId(Long grpStaffAttrId) {
		super.setId(grpStaffAttrId);
	}

	@Setter
	@Getter
	private String salesCode;

}
