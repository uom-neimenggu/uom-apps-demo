package cn.ffcs.uom.syslist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.dao.SysListDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class SysList extends UomEntity implements TreeNodeEntity, Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;
	
	private Long sysListId;
	public Long getSysListId() {
		return sysListId;
	}
	public void setSysListId(Long sysListId) {
		super.setId(sysListId);
		this.sysListId = sysListId;
	}
	@Getter
	@Setter
	private String sysName;
	@Getter
	@Setter
	private String sysUrl;
	@Getter
	@Setter
	private Long domainId;
	@Getter
	@Setter
	private Long relaDomainId;
	@Getter
	@Setter
	private String clientCode;
	@Getter
	@Setter
	private Long telecomArea;
	
	public SysList(){
		super();
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
	
	public static SysListDao repository() {
		return (SysListDao) ApplicationContextUtil.getBean("sysListDao");
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		SysList sParam = new SysList();
		sParam.setRelaDomainId(SysListConstants.ROOT_SYS_LIST_TREE);
		List<SysList> list = SysList.repository().querySysLists(sParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (SysList sl : list) {
				treeNodelist.add(sl);
			}
		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		SysList sParam = new SysList();
		sParam.setRelaDomainId(this.getSysListId());
		List<SysList> list = SysList.repository().querySysLists(sParam);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (SysList sl : list) {
				treeNodelist.add(sl);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.sysName)) {
			return this.sysName;
		}
		return "";
	}

	public String getEffDateStr() {
		return DateUtil.dateToStr(this.getEffDate());
	}

	public String getExpDateStr() {
		return DateUtil.dateToStr(this.getExpDate());
	}

	/**
	 * 获取电信区域
	 */
	public TelcomRegion getTelcomRegion() {
		if (this.telecomArea != null) {
			return (TelcomRegion) repository().getObject(TelcomRegion.class, this.telecomArea);
		}
		return null;
	}

}