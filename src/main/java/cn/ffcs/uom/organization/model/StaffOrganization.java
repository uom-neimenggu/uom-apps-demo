package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.dao.StaffOrganizationDao;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.systemconfig.model.AttrValue;

import com.opensymphony.xwork2.util.Key;

/**
 * 员工组织关系实体.
 * 
 * @author
 * 
 **/
public class StaffOrganization extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工组织关系标识
	 */
	public Long getStaffOrgId() {
		return super.getId();
	}

	public void setStaffOrgId(Long staffOrgId) {
		super.setId(staffOrgId);
	}

	/**
	 * 员工标识
	 */
	@Getter
	@Setter
	@Key
	private Long staffId;
	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	@Key
	private Long orgId;
	/**
	 * 组织树标识
	 */
	@Getter
	@Setter
	@Key
	private Long orgTreeId;
	/**
	 * 关联类型
	 */
	@Getter
	@Setter
	@Key
	private String ralaCd;
	/**
	 * 员工排序号
	 */
	@Getter
	@Setter
	@Key
	private Long staffSeq;
	/**
	 * 账号
	 */
	@Getter
	@Setter
	@Key
	private String userCode;
	/**
	 * 备注
	 */
	@Getter
	@Setter
	@Key
	private String note;
	/**
	 * 全局标识码.
	 **/
	@Getter
	@Setter
	private String uuid;
	
	/**
	 * 变更原因
	 */
	@Getter
	@Setter
	private String reason;

	/**
	 * 推导树节点数据，未避免影响其他调用，时间太赶直接在员工组织中增加变量
	 */
	@Getter
	@Setter
	private treeCalcAction treeCalcVo;

	@Getter
	@Setter
	private Party partyObj;

	@Getter
	@Setter
	private Staff staffObj;

	@Getter
	@Setter
	private String staffAccount;

	/**
	 * 新增或修改时需要修改的组织管理列表
	 */
	@Getter
	@Setter
	private List<StaffOrganization> needUpdateStaffOrganizationlist;
	/**
	 * 删除时需要修改的员工组织关系（兼职改为归属）
	 */
	@Getter
	@Setter
	private StaffOrganization removeNeedUpdateStaffOrganization;
	/**
	 * 需要改变为归属关系的关系列表
	 */
	@Getter
	@Setter
	private List<StaffOrganization> needUpdateToRela1List;
	/**
	 * 需要改变为兼职的关系列表
	 */
	@Getter
	@Setter
	private List<StaffOrganization> needUpdateToRela3List;
	/**
	 * 需要删除的关系列表
	 */
	@Getter
	@Setter
	private List<StaffOrganization> needRemoveList;

	/**
	 * 构造方法
	 */
	public StaffOrganization() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return TreeOrgRelaTypeRule
	 */
	public static StaffOrganization newInstance() {
		return new StaffOrganization();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static StaffOrganizationDao repository() {
		return (StaffOrganizationDao) ApplicationContextUtil
				.getBean("staffOrganizationDao");
	}

	/**
	 * 关系类型
	 * 
	 * @return
	 */
	public String getRalaCdName() {
		if (!StrUtil.isEmpty(this.getRalaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"StaffOrganization", "ralaCd", this.getRalaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取员工
	 * 
	 * @return
	 */
	public Staff getStaff() {
		if (this.getStaffId() != null) {
			Object object = this.repository().getObject(Staff.class,
					this.getStaffId());
			if (object != null) {
				return (Staff) object;
			}
		}
		return null;
	}

	/**
	 * 获取员工
	 * 
	 * @return
	 */
	public String getStaffName() {
		Staff staff = this.getStaff();
		if (staff != null) {
			return staff.getStaffName();
		}
		return "";
	}

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.getOrgId() != null) {
			Object object = this.repository().getObject(Organization.class,
					this.getOrgId());
			if (object != null) {
				return (Organization) object;
			}
		}
		return null;
	}

	/**
	 * 获取组织名称
	 * 
	 * @return
	 */
	public String getOrganizationName() {
		Organization org = this.getOrganization();
		if (org != null) {
			if (!StrUtil.isEmpty(org.getOrgFullName())) {
				return org.getOrgFullName(); // wangy，组织显示全称
			} else {
				return org.getOrgName();
			}
		}
		return "";
	}
	
	/**
	 * 获取组织编码
	 * 
	 * @return
	 */
	public String getOrganizationCode() {
		Organization org = this.getOrganization();
		if (org != null && !StrUtil.isEmpty(org.getOrgCode())) {
			return org.getOrgCode();
		}
		return "";
	}

	/**
	 * 获取员工组织关系
	 */
	public List<StaffOrganization> getStaffOrganizationList() {
		if (this.getStaffId() != null) {
			String sql = "SELECT * FROM STAFF_ORGANIZATION WHERE STATUS_CD = ? AND STAFF_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getStaffId());
			return this.repository().jdbcFindList(sql, params,
					StaffOrganization.class);
		}
		return null;
	}

	/**
	 * 获取员工归属组织关系
	 */
	public StaffOrganization getStaffOrganization() {
		List<StaffOrganization> list = this.getStaffOrganizationList();
		if (list != null && list.size() > 0) {
			for (StaffOrganization so : list) {
				if (so != null
						&& BaseUnitConstants.RALA_CD_1.equals(so.getRalaCd())) {
					return so;
				}
			}
		}
		return null;
	}
    @Getter
    @Setter	
	private List<StaffRole> addStaffRoles;
    @Getter
    @Setter	
	private List<StaffRole> delStaffRoles;
}
