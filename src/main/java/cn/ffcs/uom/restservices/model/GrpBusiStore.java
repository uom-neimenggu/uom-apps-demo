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
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpBusiStore extends UomEntity implements Serializable {

	/**
	 * 经营场所标识
	 */
	@XmlTransient
	public Long getGrpBusiStoreId() {
		return super.getId();
	}

	public void setGrpBusiStoreId(Long grpBusiStoreId) {
		super.setId(grpBusiStoreId);
	}

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_NBR")
	private String busiStoreNbr;

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_NAME")
	private String busiStoreName;

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_HOUSE_TYPE")
	private String busiStoreHouseType;

	@Setter
	@Getter
	@XmlElement(name = "COMMON_REGION_ID")
	private String commonRegionId;

	@Setter
	@Getter
	@XmlElement(name = "ADDRESS")
	private String address;

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
	 * 获取dao
	 * 
	 * @return
	 */
	public static ChannelInfoDao repository() {
		return (ChannelInfoDao) ApplicationContextUtil
				.getBean("channelInfoDao");
	}

}
