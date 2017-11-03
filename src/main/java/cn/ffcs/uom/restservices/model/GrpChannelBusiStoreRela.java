package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpChannelBusiStoreRela extends UomEntity implements Serializable {

	/**
	 * 渠道经营主体关系标识
	 */
	@XmlTransient
	public Long getGrpChannelBusiStoreRelaId() {
		return super.getId();
	}

	public void setGrpChannelBusiStoreRelaId(Long grpChannelBusiStoreRelaId) {
		super.setId(grpChannelBusiStoreRelaId);
	}

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_NBR")
	private String channelNbr;

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_NBR")
	private String busiStoreNbr;
	@Setter
	@Getter
	@XmlElement(name = "RELA_TYPE")
	private String relaType;

	@Setter
	@Getter
	@XmlElement(name = "DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

	@Setter
	@Getter
	@XmlTransient
	private String channelName;

	@Setter
	@Getter
	@XmlTransient
	private String busiStoreName;

	/**
	 * 关系类型名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getRelaTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpChannelBusiStoreRela", "relaType", this.getRelaType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getRelaType())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

}
