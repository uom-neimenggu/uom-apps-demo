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
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpChannel extends UomEntity implements Serializable {

	/**
	 * 渠道标识
	 */
	@XmlTransient
	public Long getGrpChannelId() {
		return super.getId();
	}

	public void setGrpChannelId(Long grpChannelId) {
		super.setId(grpChannelId);
	}

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_NBR")
	private String channelNbr;

	@Setter
	@Getter
	@XmlElement(name = "PRO_ORG_ID")
	private Long proOrgId;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_NAME")
	private String channelName;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_CLASS")
	private String channelClass;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_TYPE_CD")
	private String channelTypeCd;

	@Setter
	@Getter
	@XmlElement(name = "CHN_TYPE_CD")
	private String chnTypeCd;

	@Setter
	@Getter
	@XmlElement(name = "ORG_ID")
	private Long orgId;

	@Setter
	@Getter
	@XmlElement(name = "COMMON_REGION_ID")
	private String commonRegionId;

	@Setter
	@Getter
	@XmlElement(name = "DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

	/**
	 * 电信管理区域名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getCommonRegionIdName() {
		if (!StrUtil.isEmpty(this.getCommonRegionId())) {
			String sql = "SELECT * FROM TELCOM_REGION A WHERE A.STATUS_CD = ? AND A.REGION_CODE = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getCommonRegionId());
			TelcomRegion telcomRegion = this.repository().jdbcFindObject(sql,
					params, TelcomRegion.class);
			if (telcomRegion != null) {
				return telcomRegion.getRegionName();
			} else {
				return this.getCommonRegionId();
			}
		}
		return this.getCommonRegionId();
	}

	/**
	 * 渠道类型名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getChannelClassName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpChannel", "channelClass", this.getChannelClass(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getChannelClass())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 渠道分类名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getChannelTypeCdName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpChannel", "channelTypeCd", this.getChannelTypeCd(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getChannelTypeCd())) {
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
