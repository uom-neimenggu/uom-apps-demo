package cn.ffcs.uom.staffrole.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.rolePermission.util.RolePermissionUtil;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;

public class StaffRole extends UomEntity implements TreeNodeEntity,
		Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;
	private boolean isGetRoot = false;// 设置为true则表示用来获取根节点
	private int level;// 该节点所在层级
	private boolean end;// 是否末端节点
	private StaffRoleTreeBandboxExt component;// 所在组件
	@Getter
	@Setter
	private Long accConfigId;
	@Getter
	@Setter
	private Long roleParentId;
	@Getter
	@Setter
	private String roleName;
	@Getter
	@Setter
	private String roleCode;

	private String isParent;

	public StaffRoleTreeBandboxExt getComponent() {
		return component;
	}

	public void setComponent(StaffRoleTreeBandboxExt component) {
		this.component = component;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public StaffRole() {
		super();
	}

	public StaffRole(String roleName) {
		this.roleName = roleName;
	}

	private Long roleId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		super.setId(roleId);
		this.roleId = roleId;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	@Override
	public boolean isGetRoot() {
		return isRoot;
	}

	public void setGetRoot(boolean isGetRoot) {
		this.isGetRoot = isGetRoot;
	}

	public static StaffRoleDao repository() {
		return (StaffRoleDao) ApplicationContextUtil.getBean("staffRoleDao");
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		StaffRole srParam = new StaffRole();
		srParam.setRoleParentId(StaffRoleConstants.ROOT_STAFF_ROLE_TREE);
		List<StaffRole> list = StaffRole.repository().queryStaffRoles(srParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			if (PlatformUtil.isAdmin() ||RolePermissionUtil.isAllPermission(PlatformUtil
					.getCurrentUserId())) {
				for (StaffRole staffRole : list) {
					treeNodelist.add(staffRole);
				}
			} else if (RolePermissionUtil.isLocalAgencyAdmin(PlatformUtil
					.getCurrentUserId())) {
				for(StaffRole staffRole : list) {
					if(staffRole.getRoleName().equals("VPN角色")) {
						treeNodelist.add(staffRole);
					}
				}
			}

		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		StaffRole srParam = new StaffRole();
		srParam.setRoleParentId(this.getRoleId());
		List<StaffRole> list = StaffRole.repository().queryStaffRoles(srParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (StaffRole staffRole : list) {
				treeNodelist.add(staffRole);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.roleName)) {
			return this.roleName;
		}
		return "";
	}

	public String getEffDateStr() {
		return DateUtil.dateToStr(this.getEffDate());
	}

	public String getExpDateStr() {
		return DateUtil.dateToStr(this.getExpDate());
	}

	public boolean equals(Object obj) {
		if (obj instanceof StaffRole) {
			StaffRole u = (StaffRole) obj;
			return this.roleId.equals(u.roleId);
		}
		return super.equals(obj);
	}
}
