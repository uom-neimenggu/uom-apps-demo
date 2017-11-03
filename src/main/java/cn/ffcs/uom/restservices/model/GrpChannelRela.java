package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpChannelRela extends UomEntity implements Serializable {

	/**
	 * 店中商标识
	 */
	@XmlTransient
	public Long getGrpChannelRelaId() {
		return super.getId();
	}

	public void setGrpChannelRelaId(Long grpChannelRelaId) {
		super.setId(grpChannelRelaId);
	}

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_NBR")
	private String channelNbr;

	@Setter
	@Getter
	@XmlElement(name = "RELA_CHANNEL_NBR")
	private String relaChannelNbr;

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

	/**
	 * 获取上级渠道组织名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getRelaChannelName() {
		if (!StrUtil.isEmpty(this.getRelaChannelNbr())) {
			String sql = "SELECT * FROM GRP_CHANNEL WHERE STATUS_CD = ? AND CHANNEL_NBR = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getRelaChannelNbr());
			List<GrpChannel> grpChannelRelaList = GrpChannelRela.repository()
					.jdbcFindList(sql, params, GrpChannel.class);

			if (grpChannelRelaList != null && grpChannelRelaList.size() > 0) {
				return grpChannelRelaList.get(0).getChannelName();
			}
		}
		return "";
	}

	/**
	 * 关系类型名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getRelaTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpChannelRela", "relaType", this.getRelaType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getRelaType())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static ChannelInfoDao repository() {
		return (ChannelInfoDao) ApplicationContextUtil
				.getBean("channelInfoDao");
	}

}
