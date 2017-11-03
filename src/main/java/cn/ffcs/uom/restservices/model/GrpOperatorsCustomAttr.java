package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpOperatorsCustomAttr extends AttrItemInParam {

	/**
	 * 经营主体自定义扩展属性标识
	 */
	public Long getGrpOperatorsCustomAttrId() {
		return super.getId();
	}

	public void setGrpOperatorsCustomAttrId(Long grpOperatorsCustomAttrId) {
		super.setId(grpOperatorsCustomAttrId);
	}

	@Setter
	@Getter
	private String operatorsNbr;

}
