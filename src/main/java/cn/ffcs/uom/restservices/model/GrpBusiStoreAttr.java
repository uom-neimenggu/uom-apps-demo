package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpBusiStoreAttr extends AttrItemInParam {

	/**
	 * 渠道扩展属性标识
	 */
	public Long getGrpBusiStoreAttrId() {
		return super.getId();
	}

	public void setGrpBusiStoreAttrId(Long grpBusiStoreAttrId) {
		super.setId(grpBusiStoreAttrId);
	}

	@Setter
	@Getter
	private String busiStoreNbr;

}
