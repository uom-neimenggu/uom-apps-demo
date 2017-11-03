package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpOperatorsAttr extends AttrItemInParam {

	/**
	 * 经营主体扩展属性标识
	 */
	public Long getGrpOperatorsAttrId() {
		return super.getId();
	}

	public void setGrpOperatorsAttrId(Long grpOperatorsAttrId) {
		super.setId(grpOperatorsAttrId);
	}

	@Setter
	@Getter
	private String operatorsNbr;

}
