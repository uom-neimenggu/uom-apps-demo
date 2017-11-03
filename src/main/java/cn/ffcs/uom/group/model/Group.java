package cn.ffcs.uom.group.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.group.dao.GroupDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

public class Group {

	/**
	 * 员工标识.
	 **/
	@Getter
	@Setter
	private Long id;
	/**
	 * 员工编号.
	 **/
	@Getter
	@Setter
	private String uniCode;
	/**
	 * 用户帐号.
	 **/
	@Getter
	@Setter
	private String loginName;
	/**
	 * 用户姓名.
	 **/
	@Getter
	@Setter
	private String userName;
	/**
	 * 性别.
	 **/
	@Getter
	@Setter
	private String ctGender;
	/**
	 * 证件号.
	 **/
	@Getter
	@Setter
	private String ctIdentityNumber;
	/**
	 * 员工编码.
	 **/
	@Getter
	@Setter
	private String ctHrUserCode;
	/**
	 * 出生日期.
	 **/
	@Getter
	@Setter
	private Date ctBirthday;
	/**
	 * 员工分类.
	 **/
	@Getter
	@Setter
	private String ctUserType;
	/**
	 * 员工状态.
	 **/
	@Getter
	@Setter
	private String ctStatus;
	/**
	 * 备用账号.
	 **/
	@Getter
	@Setter
	private String reserveAccount;
	/**
	 * 创建时间.
	 **/
	@Getter
	@Setter
	private Date createDate;

	// public String getCreateDate() {
	// return DateUtil.dateToStr(createDate, "yyyy-MM-dd HH:mm:ss");
	// }

	/**
	 * 删除标识.
	 **/
	@Getter
	@Setter
	private Long delStatus;
	/**
	 * 预留字段2.
	 **/
	@Getter
	@Setter
	private String reserv_Col2;
	/**
	 * 预留字段3.
	 **/
	@Getter
	@Setter
	private String reserv_Col3;
	/**
	 * 预留字段3.
	 **/
	@Getter
	@Setter
	private Date updateDate;
	
	public String getStaffId() {
		if (!StrUtil.isEmpty(this.getUniCode())) {
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM STAFF_ACCOUNT WHERE STATUS_CD = ? AND GUID = ? ");
			List<Object> params = new ArrayList<Object>();

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getUniCode());
			List<StaffAccount> list = Staff.repository().jdbcFindList(
					sb.toString(), params, StaffAccount.class);
			if (list != null && list.size() > 0) {
				return list.get(0).getStaffId().toString();
			}
		}
		return "";
	}

	/**
	 * 性别.
	 **/
	private String getCtGenderName() {
		if (!StrUtil.isNullOrEmpty(this.getCtGender())) {
			if ("Z01".equals(this.getCtGender())) {
				return "男";
			} else if ("Z02".equals(this.getCtGender())) {
				return "女";
			}
		}
		return "";
	}

	/**
	 * 员工状态.
	 **/
	private String getCtStatusName() {
		if (!StrUtil.isNullOrEmpty(this.getCtStatus())) {
			if ("1".equals(this.getCtStatus().substring(0, 1))) {
				return "在岗";
			} else if ("2".equals(this.getCtStatus().substring(0, 1))) {
				return "不在岗";
			} else if ("3".equals(this.getCtStatus().substring(0, 1))) {
				return "无岗位";
			}
		} else {
			return "无岗位";
		}
		return "无岗位";
	}

	public static GroupDao repository() {
		return (GroupDao) ApplicationContextUtil.getBean("groupDao");
	}

}
