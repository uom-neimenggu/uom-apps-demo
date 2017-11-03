package cn.ffcs.uom.party.model;

import java.io.Serializable;
import java.util.List;

import org.zkoss.zul.Textbox;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 参与人联系方式 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
public class PartyContactInfo extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 参与人联系证件标识 CONTACT_ID
	 */
	public Long getContactId() {
		return super.getId();
	}

	public void setContactId(Long contactId) {
		super.setId(contactId);
	}

	/**
	 * 参与人标识
	 */
	@Getter
	@Setter
	private Long partyId;
	/**
	 * 是否首选联系人
	 */
	@Getter
	@Setter
	private String headFlag;
	/**
	 * 联系人类型
	 */
	@Getter
	@Setter
	private String contactType;
	/**
	 * 联系人名称
	 */
	@Getter
	@Setter
	private String contactName;
	/**
	 * 联系人性别
	 */
	@Getter
	@Setter
	private String contactGender;
	/**
	 * 联系地址
	 */
	@Getter
	@Setter
	private String contactAddress;
	/**
	 * 联系单位
	 */
	@Getter
	@Setter
	private String contactEmployer;
	/**
	 * 家庭电话
	 */
	@Getter
	@Setter
	private String homePhone;
	/**
	 * 办公电话
	 */
	@Getter
	@Setter
	private String officePhone;
	/**
	 * 移动电话
	 */
	@Getter
	@Setter
	private String mobilePhone;

	/**
	 * 电信内部邮箱
	 */
	@Getter
	@Setter
	private String innerEmail;
	/**
	 * 集团统一邮箱
	 */
	@Setter
	@Getter
	private String grpUnEmail;
	/**
	 * 移动电话 (备用)
	 */
	@Getter
	@Setter
	private String mobilePhoneSpare;
	/**
	 * 详细信息
	 */
	@Getter
	@Setter
	private String contactDesc;

	@Getter
	@Setter
	private String email;
	/**
	 * 邮政编码
	 */
	@Getter
	@Setter
	private String postCode;
	/**
	 * 邮件地址
	 */
	@Getter
	@Setter
	private String postAddress;
	/**
	 * 传真
	 */
	@Getter
	@Setter
	private String fax;
	/**
	 * QQ号码
	 */
	@Getter
	@Setter
	private String qqNumber;
	/**
	 * MSN号
	 */
	@Getter
	@Setter
	private String msn;
	
	/**
	 * 原因
	 */
	@Setter
	@Getter
	private String reason;
	
	/**
	 * 修改员工接口是否需要更新
	 */
	@Setter
	@Getter
	private Boolean isNeedUpdate = false;

	public PartyContactInfo() {
		super();
	}

	public static PartyContactInfo newInstance() {
		return new PartyContactInfo();
	}

	public String getHeadFlagName() {
		if (!StrUtil.isNullOrEmpty(this.getHeadFlag())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"PartyContactInfo", "headFlag", this.getHeadFlag(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getContactGenderName() {
		if (!StrUtil.isNullOrEmpty(this.getContactGender())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Individual", "gender", this.getContactGender(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getContactTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getContactType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"PartyContactInfo", "contactType", this.getContactType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}
}
