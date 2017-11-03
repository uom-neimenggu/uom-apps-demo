package cn.ffcs.uom.gridUnit.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class GridUnit extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID.
	 **/
	public Long getGridUnitId() {
		return super.getId();
	}

	public void setGridUnitId(Long gridUnitId) {
		super.setId(gridUnitId);
	}

	/**
	 * 区域ID.
	 **/
	@Getter
	@Setter
	private String areaEid;

	/**
	 * 区域名称.
	 **/
	@Getter
	@Setter
	private String areaName;

	/**
	 * 子区域ID.
	 **/
	@Getter
	@Setter
	private String district;

	/**
	 * 子区域名称.
	 **/
	@Getter
	@Setter
	private String subareaName;

	/**
	 * 网格单元ID.
	 **/
	@Getter
	@Setter
	private Long mmeFid;

	/**
	 * 网格单元名称.
	 **/
	@Getter
	@Setter
	private String gridName;

	/**
	 * 网格单元大类.
	 **/
	@Getter
	@Setter
	private String gridType;

	/**
	 * 网格单元小类.
	 **/
	@Getter
	@Setter
	private String gridSubtype;

	/**
	 * 数据权限：区域
	 */
	@Getter
	@Setter
	private TelcomRegion permissionTelcomRegion;
}
