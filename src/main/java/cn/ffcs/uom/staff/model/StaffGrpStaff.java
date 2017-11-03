package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.staff.dao.StaffGrpStaffDao;

/**
 * 员工岗位关系实体.
 * 
 * @author
 * 
 **/
public class StaffGrpStaff extends UomEntity implements Serializable {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工业务关系标识.
	 **/
	public Long getStaffGrpStaffId() {
		return super.getId();
	}

	public void setStaffGrpStaffId(Long staffGrpStaffId) {
		super.setId(staffGrpStaffId);
	}

	/**
	 * 员工标识.
	 **/
	@Getter
	@Setter
	private Long staffId;

	/**
	 * 集团渠道视图销售员姓名.
	 **/
	@Getter
	@Setter
	private String staffName;

	/**
	 * 集团渠道视图销售员编码.
	 **/
	@Getter
	@Setter
	private String salesCode;

	/**
	 * 省主数据人员姓名.
	 **/
	@Getter
	@Setter
	private String uomStaffName;

	/**
	 * 省内账号.
	 **/
	@Getter
	@Setter
	private String uomStaffAccount;

	/**
	 * 集团MSS姓名.
	 **/
	@Getter
	@Setter
	private String userName;

	/**
	 * 集团MSS账号.
	 **/
	@Getter
	@Setter
	private String loginName;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffGrpStaffDao repository() {
		return (StaffGrpStaffDao) ApplicationContextUtil
				.getBean("staffGrpStaffDao");
	}

	/**
	 * 获取员工业务关系
	 * 
	 * @return
	 */
	public StaffGrpStaff getStaffGrpStaff() {
		if (this.getStaffGrpStaffId() != null) {
			return (StaffGrpStaff) OrgPosition.repository().getObject(
					StaffGrpStaff.class, this.getStaffGrpStaffId());
		}
		return null;
	}
}
