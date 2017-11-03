package cn.ffcs.uom.restservices.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelInfoInParam {

	@Setter
	@Getter
	@XmlElement(name = "OPERATOR_NBR")
	private String operatorNbr;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS")
	private GrpOperators operators;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_ATTR")
	private OperatorsAttrInParam operatorsAttr;

	@Setter
	@Getter
	@XmlElement(name = "OPERATORS_CUSTOM_ATTR")
	private OperatorsCustomAttrInParam operatorsCustomAttr;

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE")
	private GrpBusiStore busiStore;

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_ATTR")
	private BusiStoreAttrInParam busiStoreAttr;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL")
	private GrpChannel channel;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_ATTR")
	private ChannelAttrInParam channelAttr;

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_CUSTOM_ATTR")
	private ChannelCustomAttrInParam channelCustomAttr;

	@Setter
	@Getter
	@XmlElement(name = "STAFF")
	private GrpStaff staff;

	@Setter
	@Getter
	@XmlElement(name = "STAFF_ATTR")
	private StaffAttrInParam staffAttr;

	@Setter
	@Getter
	@XmlElement(name = "STAFF_CUSTOM_ATTR")
	private StaffCustomAttrInParam staffCustomAttr;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE")
	private BizZoneInParam bizZone;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_ATTR")
	private BizZoneAttrInParam bizZoneAttr;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_CUSTOM_ATTR")
	private BizZoneCustomAttrInParam bizZoneCustomAttr;

	@Setter
	@Getter
	@XmlElementWrapper(name = "CHANNEL_RELAS")
	@XmlElement(name = "CHANNEL_RELA")
	private List<GrpChannelRela> channelRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "CHANNEL_OPERATORS_RELAS")
	@XmlElement(name = "CHANNEL_OPERATORS_RELA")
	private List<GrpChannelOperatorsRela> channelOperatorsRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "STAFF_OPERATORS_RELAS")
	@XmlElement(name = "STAFF_OPERATORS_RELA")
	private List<StaffOperatorsRelaInParam> staffOperatorsRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "STAFF_CHANNEL_RELAS")
	@XmlElement(name = "STAFF_CHANNEL_RELA")
	private List<GrpStaffChannelRela> staffChannelRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "CHANNEL_BIZ_ZONE_RELAS")
	@XmlElement(name = "CHANNEL_BIZ_ZONE_RELA")
	private List<ChannelBizZoneRelaInParam> channelBizZoneRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "STAFF_BIZ_ZONE_RELAS")
	@XmlElement(name = "STAFF_BIZ_ZONE_RELA")
	private List<StaffBizZoneRelaInParam> staffBizZoneRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "BUSI_STORE_BIZ_ZONE_RELAS")
	@XmlElement(name = "BUSI_STORE_BIZ_ZONE_RELA")
	private List<BusiStoreBizZoneRelaInParam> busiStoreBizZoneRelas;

	@Setter
	@Getter
	@XmlElementWrapper(name = "CHANNEL_BUSI_STORE_RELAS")
	@XmlElement(name = "CHANNEL_BUSI_STORE_RELA")
	private List<GrpChannelBusiStoreRela> channelBusiStoreRelas;

}
