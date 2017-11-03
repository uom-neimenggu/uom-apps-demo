package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpChannelCustomAttr extends AttrItemInParam {

	/**
	 * 渠道扩展属性标识
	 */
	public Long getGrpChannelCustomAttrId() {
		return super.getId();
	}

	public void setGrpChannelCustomAttrId(Long grpChannelCustomAttrId) {
		super.setId(grpChannelCustomAttrId);
	}

	@Setter
	@Getter
	private String channelNbr;

}
