package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 组织关系实体.
 * 
 * @author
 * 
 **/
public class UnitedDirectory implements TreeNodeEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String ctou;

	@Getter
	@Setter
	private String deptname;

	@Getter
	@Setter
	private String parentcode;

	@Getter
	@Setter
	private String parentcorpcode;

	@Getter
	@Setter
	private Double shownum;

	@Getter
	@Setter
	private String ctorgmanager;

	@Getter
	@Setter
	private String deptstatus;

	@Getter
	@Setter
	private String ctorgtype;

	@Getter
	@Setter
	private String ctcorptype;

	@Getter
	@Setter
	private String ctdepttype;

	@Getter
	@Setter
	private String ctdeptlevel;

	@Getter
	@Setter
	private String ctvicemanager;

	@Getter
	@Setter
	private Long delStatus;

	@Getter
	@Setter
	private String reservCol2;

	@Getter
	@Setter
	private String reservCol3;

	@Getter
	@Setter
	private Date createDate;

	@Getter
	@Setter
	private Date updateDate;

	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;

	@Override
	public boolean isGetRoot() {
		return this.isRoot;
	}

	/**
	 * 根节点
	 */
	@Getter
	@Setter
	private String rootId = "8900000001";

	/**
	 * 构造方法
	 */
	public UnitedDirectory() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationRelation
	 */
	public static UnitedDirectory newInstance() {
		return new UnitedDirectory();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	// public static UnitedDirectoryDao repository() {
	// return (UnitedDirectoryDao) ApplicationContextUtil
	// .getBean("unitedDirectoryDao");
	// }

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static BaseDao repository() {
		return (BaseDao) ApplicationContextUtil.getBean("baseDao");
	}

	@Override
	public String getLabel() {

		if (!StrUtil.isEmpty(this.deptname)) {
			return this.deptname;
		}

		return "";
	}
	
	/**
	 * 获取状态值
	 * 
	 * @return
	 */
	public String getDelStatusName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"UomEntity", "statusCd", this.getDelStatus().toString(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getDelStatus().toString())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}
	
	/**
	 * 
	 * 上级组织名
	 * @return
	 */
	public String getParentDeptname() {
		String sql = "select t.deptname parentdeptname from ct_organization t where t.ctou = ? and t.del_status = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(this.parentcode);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		
		List<Map<String, Object>> list = UnitedDirectory.repository().getJdbcTemplate().queryForList(sql,
				params.toArray());
		if(list != null && !list.isEmpty()) {
			return (String) list.get(0).get("parentdeptname");
		} else {
			return "";
		}
	}
	
	/**
	 * 组织路径
	 * @return
	 */
	public String getUniDirPath() {
		String sql = "select substr(sys_connect_by_path(t.deptname, '->'),3) path  from ct_organization t"
				+ " where t.ctou = ? and t.del_status = ?"
				+ " start with t.parentcode = ? "
				+ " connect by prior t.ctou = t.parentcode";

		List<Object> params = new ArrayList<Object>();
		params.add(this.ctou);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.rootId);
		List<Map<String, Object>> list = UnitedDirectory.repository().getJdbcTemplate().queryForList(sql,
				params.toArray());
		if(list != null && !list.isEmpty()) {
			return (String) list.get(0).get("path");
		} else {
			return "";
		}
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		String sql = "SELECT T.* FROM CT_ORGANIZATION T WHERE T.DEL_STATUS = ? AND T.PARENTCODE = ? ORDER BY T.SHOWNUM ASC";

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.rootId);

		List<UnitedDirectory> list = UnitedDirectory.repository().jdbcFindList(
				sql, params, UnitedDirectory.class);

		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (UnitedDirectory unitedDirectory : list) {
				treeNodelist.add(unitedDirectory);
			}
		}
		return treeNodelist;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT T.* FROM CT_ORGANIZATION T WHERE T.DEL_STATUS = ? AND T.PARENTCODE = ? ORDER BY T.SHOWNUM ASC";

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getCtou());

		List<UnitedDirectory> list = UnitedDirectory.repository().jdbcFindList(
				sql, params, UnitedDirectory.class);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (UnitedDirectory unitedDirectory : list) {
				treeNodelist.add(unitedDirectory);
			}
		}
		return treeNodelist;
	}

}
