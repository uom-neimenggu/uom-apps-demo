package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class GrpChannelAttr extends AttrItemInParam {

	/**
	 * 渠道扩展属性标识
	 */
	public Long getGrpChannelAttrId() {
		return super.getId();
	}

	public void setGrpChannelAttrId(Long grpChannelAttrId) {
		super.setId(grpChannelAttrId);
	}

	@Setter
	@Getter
	private String channelNbr;

}
