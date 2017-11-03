package cn.ffcs.uom.position.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.position.dao.PositionDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 岗位实体.
 * 
 * @author
 * 
 **/
public class Position extends UomEntity implements TreeNodeEntity, Serializable {
	/**
	 * 岗位标识.
	 **/
	public Long getPositionId() {
		return super.getId();
	}

	public void setPositionId(Long positionId) {
		super.setId(positionId);
	}

	/**
	 * 父岗位标识.
	 **/
	@Getter
	@Setter
	private Long parentPositionId;
	/**
	 * 岗位编码.
	 **/
	@Getter
	@Setter
	private String positionCode;
	/**
	 * 岗位类型.
	 **/
	@Getter
	@Setter
	private String positionType;
	/**
	 * 岗位描述.
	 **/
	@Getter
	@Setter
	private String positionDesc;
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private String positionName;
	/**
	 * 全局标识码.
	 **/
	@Getter
	@Setter
	private String uuid;
	/**
	 * 是否是根节点
	 */
	@Getter
	@Setter
	private Boolean isRoot = false;
	/**
	 * 根节点
	 */
	@Getter
	@Setter
	private Long rootPositionId = 0L;
	/**
	 * 组织岗位关系
	 */
	private List<OrgPosition> orgPositionList;

	@Override
	public boolean isGetRoot() {
		// TODO Auto-generated method stub
		return isRoot;
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		// TODO Auto-generated method stub
		String sql = "SELECT T.* FROM POSITION T WHERE T.STATUS_CD = ? AND T.PARENT_POSITION_ID = ? ORDER BY POSITION_TYPE ASC";

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.rootPositionId);

		List<Position> list = Position.repository().jdbcFindList(sql, params,
				Position.class);

		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (Position position : list) {
				treeNodelist.add(position);
			}
		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		// TODO Auto-generated method stub
		String sql = "SELECT T.* FROM POSITION T WHERE T.STATUS_CD = ? AND T.PARENT_POSITION_ID = ? ORDER BY POSITION_TYPE ASC";

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getPositionId());

		List<Position> list = Position.repository().jdbcFindList(sql, params,
				Position.class);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (Position position : list) {
				treeNodelist.add(position);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		if (!StrUtil.isEmpty(this.positionName)) {
			return this.positionName;
		}
		return "";

	}

	/**
	 * 获取仓库.
	 * 
	 * @return CrmRepository
	 */
	public static PositionDao repository() {
		return (PositionDao) ApplicationContextUtil.getBean("positionDao");
	}

	public Position() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return Position
	 */
	public static Position newInstance() {
		return new Position();
	}

	/**
	 * 获取岗位名称
	 * 
	 * @return
	 */
	public String getPositionTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"Position", "positionType", this.getPositionType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";

	}

	/**
	 * 获取状态名称
	 * 
	 * @return
	 */
	public String getStaticCdName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"Position", "statusCd", this.getStatusCd(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 获取组织岗位关系列表
	 * 
	 * @return
	 */
	public List<OrgPosition> getOrgPositionList() {
		if (orgPositionList == null || orgPositionList.size() == 0) {
			StringBuffer sql = new StringBuffer();
			List params = new ArrayList();
			if (this.getPositionId() != null) {
				sql.append("SELECT * FROM ORG_POSITION A WHERE A.STATUS_CD = ? AND A.POSITION_ID = ?");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getPositionId());
			}
			return this.repository().jdbcFindList(sql.toString(), params,
					OrgPosition.class);
		}
		return null;
	}

	public String getEffDateStr() {
		return DateUtil.dateToStr(this.getEffDate());
	}

	public String getExpDateStr() {
		return DateUtil.dateToStr(this.getExpDate());
	}
}
