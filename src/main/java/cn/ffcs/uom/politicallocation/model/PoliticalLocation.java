package cn.ffcs.uom.politicallocation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;

/**
 *行政管理区域实体.
 * 
 * @author
 * 
 **/
public class PoliticalLocation extends UomEntity implements TreeNodeEntity,
		Serializable {
	/**
	 *行政区域主键.
	 **/	
	public Long getLocationId() {
		return super.getId();
	}

	public void setLocationId(Long locationId) {
		super.setId(locationId);
	}
	/**
	 *行政区域编码.
	 **/
	@Getter
	@Setter
	private String locationCode;
	/**
	 *行政区域名称.
	 **/
	@Getter
	@Setter
	private String locationName;
	/**
	 *行政区域描述.
	 **/
	@Getter
	@Setter
	private String locationDesc;
	/**
	 *行政区域类型.
	 **/
	@Getter
	@Setter
	private String locationType;
	/**
	 *行政区域简拼.
	 **/
	@Getter
	@Setter
	private String locationAbbr;
	/**
	 *上级行政区域标识.
	 **/
	@Getter
	@Setter
	private Long upLocationId;
	/**
	 * 是否根
	 */
	@Getter
	@Setter
	private Boolean isRoot = false;

	/**
	 * 根节点
	 */
	private Long rootId;

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT * FROM POLITICAL_LOCATION A WHERE A.STATUS_CD=? AND A.UP_LOCATION_ID =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getLocationId());
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, PoliticalLocation.class);
	}
	
	public static PoliticalLocation getPoliticalLocation(Long id){
		String sql = "SELECT * FROM POLITICAL_LOCATION A WHERE A.STATUS_CD=? AND A.LOCATION_ID =?";
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(id);
		return DefaultDaoFactory.getDefaultDao().jdbcFindObject(sql, params, PoliticalLocation.class);
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		if (rootId == null) {
			rootId = 0l;
		}
		String sql = "select * from political_location a where a.status_cd=? and a.up_location_id =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rootId);
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, PoliticalLocation.class);
	}

	@Override
	public String getLabel() {
		return this.locationName;
	}

	@Override
	public boolean isGetRoot() {
		return isRoot;
	}
}
