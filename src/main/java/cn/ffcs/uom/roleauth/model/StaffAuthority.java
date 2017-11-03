package cn.ffcs.uom.roleauth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.dao.AuthorityDao;

public class StaffAuthority extends UomEntity implements TreeNodeEntity, Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 是否是根节点
	 */
	
	private boolean isRoot = false;
	public boolean isRoot() {
		return isRoot;
	}
	public void setIsRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public StaffAuthority(){
		super();
	}
	private Long authorityId;
	public Long getAuthorityId() {
		return authorityId;
	}
	public void setAuthorityId(Long authorityId) {
		super.setId(authorityId);
		this.authorityId = authorityId;
	}

	private String authorityName;
	public String getAuthorityName() {
		return authorityName;
	}
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	private Long authorityParentId;
	public Long getAuthorityParentId() {
		return authorityParentId;
	}
	public void setAuthorityParentId(Long authorityParentId) {
		this.authorityParentId = authorityParentId;
	}

	private String isParent;
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
	
	public static AuthorityDao repository() {
		return (AuthorityDao) ApplicationContextUtil.getBean("authorityDao");
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		StaffAuthority authParam = new StaffAuthority();
		authParam.setAuthorityParentId(RoleAuthConstants.ROOT_ROLE_AUTH_TREE);
		List<StaffAuthority> auths = repository().queryAuthority(authParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (auths != null) {
			for (StaffAuthority auth : auths) {
				treeNodelist.add(auth);
			}
		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		StaffAuthority authParam = new StaffAuthority();
		authParam.setAuthorityParentId(this.getAuthorityId());
		List<StaffAuthority> auths = repository().queryAuthority(authParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (auths != null) {
			for (StaffAuthority auth : auths) {
				treeNodelist.add(auth);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.authorityName)) {
			return this.authorityName;
		}
		return "";
	}	

	public String getEffDateStr() {
		return DateUtil.dateToStr(this.getEffDate());
	}

	public String getExpDateStr() {
		return DateUtil.dateToStr(this.getExpDate());
	}

}
