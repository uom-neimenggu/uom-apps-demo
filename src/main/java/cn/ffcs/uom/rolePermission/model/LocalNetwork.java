package cn.ffcs.uom.rolePermission.model;

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
 * 行政管理区域实体.
 * 
 * @author
 * 
 **/
public class LocalNetwork extends UomEntity implements TreeNodeEntity,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 本地网主键.
	 **/
	public Long getLocalNetworkId() {
		return super.getId();
	}

	public void setLocalNetworkId(Long localNetworkId) {
		super.setId(localNetworkId);
	}

	/**
	 * 本地网编码.
	 **/
	@Getter
	@Setter
	private String localNetworkCode;

	/**
	 * 本地网名称.
	 **/
	@Getter
	@Setter
	private String localNetworkName;

	/**
	 * 本地网描述.
	 **/
	@Getter
	@Setter
	private String localNetworkDesc;

	/**
	 * 本地网类型.
	 **/
	@Getter
	@Setter
	private String localNetworkType;

	/**
	 * 本地网简拼.
	 **/
	@Getter
	@Setter
	private String localNetworkAbbr;
	/**
	 * 上级本地网标识.
	 **/
	@Getter
	@Setter
	private Long upLocalNetworkId;

	/**
	 * 是否根
	 */
	@Setter
	private Boolean isRoot = false;

	/**
	 * 根节点
	 */
	private Long rootId;

	@Override
	public String getLabel() {
		return this.localNetworkName;
	}

	@Override
	public boolean isGetRoot() {
		return isRoot;
	}

	public static LocalNetwork getLocalNetwork(Long localNetworkId) {
		String sql = "SELECT * FROM LOCAL_NETWORK A WHERE A.STATUS_CD=? AND A.LOCAL_NETWORK_ID = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(localNetworkId);
		return DefaultDaoFactory.getDefaultDao().jdbcFindObject(sql, params,
				LocalNetwork.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId == null) {
			rootId = 0L;
		}

		String sql = "SELECT A.* FROM LOCAL_NETWORK A WHERE A.STATUS_CD = ? AND A.UP_LOCAL_NETWORK_ID = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rootId);

		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, LocalNetwork.class);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT A.* FROM LOCAL_NETWORK A WHERE A.STATUS_CD = ? AND A.UP_LOCAL_NETWORK_ID = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getLocalNetworkId());
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, LocalNetwork.class);
	}

}
