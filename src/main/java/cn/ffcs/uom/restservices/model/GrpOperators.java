package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpOperators extends UomEntity implements Serializable {

	/**
	 * 经营主体标识
	 */
	@XmlTransient
	public Long getGrpOperatorsId() {
		return super.getId();
	}

	public void setGrpOperatorsId(Long grpOperatorsId) {
		super.setId(grpOperatorsId);
	}

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_NBR")
	private String operatorsNbr;

	@Setter
	@Getter
	@XmlElement(name = "PRO_ORG_ID")
	private Long proOrgId;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_NAME")
	private String operatorsName;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_TYPE_CD")
	private String operatorsTypeCd;

	@Setter
	@Getter
	@XmlElement(name = "CERT_TYPE")
	private String certType;

	@Setter
	@Getter
	@XmlElement(name = "CERT_NBR")
	private String certNbr;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_SNAME")
	private String operatorsSname;

	@Setter
	@Getter
	@XmlElement(name = "LEGAL_REPR")
	private String legalRepr;

	@Setter
	@Getter
	@XmlElement(name = "ADDRESS")
	private String adderss;

	@Setter
	@Getter
	@XmlElement(name = "TELEPHONE")
	private String telephone;

	@Setter
	@Getter
	@XmlElement(name = "CONTACT")
	private String contact;

	@Setter
	@Getter
	@XmlElement(name = "EMAIL")
	private String email;

	@Setter
	@Getter
	@XmlElement(name = "ORG_ID")
	private Long orgId;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_AREA_GRADE")
	private String operatorsAreaGrade;

	@Setter
	@Getter
	@XmlElement(name = "PARENT_OPER_NBR")
	private String parentOperNbr;

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
	 * 证件类型名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getCertTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"PartyCertification", "certType", this.getCertType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getCertType())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 级别名称
	 * 
	 * @return
	 */
	@XmlTransient
	public String getOperatorsAreaGradeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"GrpOperators", "operatorsAreaGrade",
				this.getOperatorsAreaGrade(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getOperatorsAreaGrade())) {
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
