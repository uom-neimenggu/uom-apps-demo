package cn.ffcs.uac.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacStaffDao;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * @Title:UacStaff
 * @Description:UacStaff员工实体类
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年8月31日上午11:02:03
 */
public class UacStaff extends UomEntity implements Serializable {

	private static final long serialVersionUID = -4029750619408761101L;

	public Long getStaffId() {
		return super.getId();
	}

	public void setStaffId(Long staffId) {
		super.setId(staffId);
	}

	@Getter
	@Setter
	private Long contactId;
	@Getter
	@Setter
	private Long certId;
	@Getter
	@Setter
	private String property;
	@Getter
	@Setter
	private String account;
	@Getter
	@Setter
	private String ecode;
	@Getter
	@Setter
	private String staffName;
	@Getter
	@Setter
	private String hrCode;
	@Getter
	@Setter
	private String guid;
	@Getter
	@Setter
	private String irrePasswd;
	@Getter
	@Setter
	private String revePasswd;
	@Getter
	@Setter
	private String uuid;
	@Getter
	@Setter
	private Long ossId;
	@Getter
	@Setter
	private String transactionRecord;
	@Getter
	@Setter
	private String empDesc;
	@Getter
	@Setter
	private String duty;
	@Getter
	@Setter
	private Long type;

	@Setter
	private UacCert uacCert;

	public UacCert getUacCert() {
		if (this.uacCert != null) {
			return this.uacCert;
		} else if (!StrUtil.isNullOrEmpty(this.getStaffId())) {
			UacCert dbUacCert = (UacCert) UacCert.repository().getObject(
					UacCert.class, this.getCertId());
			if (dbUacCert != null) {
				return dbUacCert;
			}
		}
		
		return null;
	}

	@Setter
	private UacContact uacContact;

	public UacContact getUacContact() {
		if (this.uacContact != null) {
			return this.uacContact;
		} else if (!StrUtil.isNullOrEmpty(this.getContactId())) {
			UacContact uacContact = (UacContact) UacContact.repository()
					.getObject(UacContact.class, this.getContactId());
			if (uacContact != null) {
				return uacContact;
			}
		}

		return null;
	}

	@Setter
	private UacAttachedInfo uacAttachedInfo;

	public UacAttachedInfo getUacAttachedInfo() {
		if (this.uacAttachedInfo != null) {
			return this.uacAttachedInfo;
		} else if (this.getStaffId() != null) {
			String sql = "SELECT * FROM uac_attached_info WHERE STATUS_CD = ? AND STAFF_ID = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getStaffId());
			List<UacAttachedInfo> list = UacAttachedInfo.repository()
					.jdbcFindList(sql, params, UacAttachedInfo.class);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}

		return null;
	}

	@Setter
	private List<UacExpandInfo> uacExpandInfoList;

	public List<UacExpandInfo> getUacExpandInfoList() {
		if (this.uacExpandInfoList != null) {
			return this.uacExpandInfoList;
		} else if (this.getStaffId() != null) {
			String sql = "SELECT * FROM UAC_EXPAND_INFO WHERE STATUS_CD = ? AND STAFF_ID = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getStaffId());
			List<UacExpandInfo> list = UacExpandInfo.repository().jdbcFindList(
					sql, params, UacExpandInfo.class);

			return list;
		}

		return null;
	}

	public String getPropertyName() {
		if (!StrUtil.isNullOrEmpty(this.getProperty())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Staff", "staffProperty", this.getProperty(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public UacStaff() {
		super();
	}

	public static UacStaff newInstance() {
		return new UacStaff();
	}

	public static UacStaffDao repository() {
		return (UacStaffDao) ApplicationContextUtil.getBean("uacStaffDao");
	}
}
