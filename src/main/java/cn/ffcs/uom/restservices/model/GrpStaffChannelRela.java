package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpStaffChannelRela extends UomEntity implements Serializable {

	/**
	 * 员工关联渠道标识
	 */
	@XmlTransient
	public Long getGrpStaffChannelRelaId() {
		return super.getId();
	}

	public void setGrpStaffChannelRelaId(Long grpStaffChannelRelaId) {
		super.setId(grpStaffChannelRelaId);
	}

	@Setter
	@Getter
	@XmlElement(name = "SALES_CODE")
	private String salesCode;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_NBR")
	private String channelNbr;

	@Setter
	@Getter
	@XmlTransient
	private String channelName;

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
	private String staffCode;

	@Setter
	@Getter
	@XmlTransient
	private String staffName;

	/**
	 * 关系类型名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getRelaTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpStaffChannelRela", "relaType", this.getRelaType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getRelaType())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

}
