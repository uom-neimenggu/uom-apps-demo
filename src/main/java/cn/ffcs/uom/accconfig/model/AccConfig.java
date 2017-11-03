package cn.ffcs.uom.accconfig.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.dao.AccConfigDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;

public class AccConfig extends UomEntity implements TreeNodeEntity, Serializable{

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
	public AccConfig(){
		super();
	}
	//访问配置标识
	private Long accConfigId;
	public Long getAccConfigId() {
		return accConfigId;
	}
	public void setAccConfigId(Long accConfigId) {
		super.setId(accConfigId);
		this.accConfigId = accConfigId;
	}
	
	//配置名称
	@Getter
	@Setter
	private String accName;
	
	//父配置标识
	private Long accParentId;
	public Long getAccParentId() {
		return accParentId;
	}
	public void setAccParentId(Long accParentId) {
		this.accParentId = accParentId;
	}

	//是否是父节点
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
	
	public static AccConfigDao repository() {
		return (AccConfigDao) ApplicationContextUtil.getBean("accConfigDao");
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		AccConfig accParam = new AccConfig();
		accParam.setAccParentId(AccConfigConstants.ROOT_ACC_CONFIG_TREE);
		List<AccConfig> auths = repository().queryAccConfig(accParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (auths != null) {
			for (AccConfig auth : auths) {
				treeNodelist.add(auth);
			}
		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		AccConfig accParam = new AccConfig();
		accParam.setAccParentId(this.getAccConfigId());
		List<AccConfig> auths = repository().queryAccConfig(accParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (auths != null) {
			for (AccConfig auth : auths) {
				treeNodelist.add(auth);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.accName)) {
			return this.accName;
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