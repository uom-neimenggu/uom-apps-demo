package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.OperateLogConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.dao.OrgTreeDao;

/**
 * 组织实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings("serial")
public class OrgTree extends UomEntity implements Serializable {

	/**
	 * 组织树志标识.
	 */
	public Long getOrgTreeId() {
		return super.getId();
	}

	public void setOrgTreeId(Long orgTreeId) {
		super.setId(orgTreeId);
	}

	/**
	 * 组织树名称.
	 **/
	@Getter
	@Setter
	private String orgTreeName;
	/**
	 * 是否推导.
	 **/
	@Getter
	@Setter
	private Integer ISCalc;
	/**
	 * 最后生成时间.
	 **/
	@Getter
	@Setter
	private Date lastTime;
	/**
	 *上次生成时间.
	 **/
	@Getter
	@Setter
	private Date preTime;
	/**
	 *是否发布中.
	 **/
	private Long isPublishing;

	public Long getIsPublishing() {
		return isPublishing;
	}

	public void setIsPublishing(Long isPublishing) {
		this.isPublishing = isPublishing;
	}

	/**
	 * 构造方法
	 */
	public OrgTree() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OperateLog
	 */
	public static OrgTree newInstance() {
		return new OrgTree();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgTreeDao repository() {
		return (OrgTreeDao) ApplicationContextUtil.getBean("orgTreeDao");
	}

	/**
	 * 获取名称
	 */
	public String getPublishingName() {
		if (this.isPublishing != null) {
			if (OperateLogConstants.IS_PUBLISHING.equals(this.isPublishing)) {
				return "发布中";
			} else {
				return "未锁定";
			}
		}
		return "";
	}

	public List<TreeOrgRelaTypeRule> getTreeOrgOrgRelaList() {
		if (this.getOrgTreeId() != null) {
			String sql = "SELECT * FROM TREE_ORG_RELA_TYPE_RULE WHERE STATUS_CD=? AND ORG_TREE_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgTreeId());
			return this.repository().jdbcFindList(sql, params,
					TreeOrgRelaTypeRule.class);
		}
		return null;
	}

	public List<TreeOrgTypeRule> getTreeOrgOrgTypeList() {
		if (this.getOrgTreeId() != null) {
			String sql = "SELECT * FROM TREE_ORG_TYPE_RULE WHERE STATUS_CD=? AND ORG_TREE_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgTreeId());
			return this.repository().jdbcFindList(sql, params,
					TreeOrgTypeRule.class);
		}
		return null;
	}

	public List<TreeStaffOrtRule> getTreeStaffOrgRelaList() {
		if (this.getOrgTreeId() != null) {
			String sql = "SELECT * FROM TREE_STAFF_ORT_RULE WHERE STATUS_CD=? AND ORG_TREE_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgTreeId());
			return this.repository().jdbcFindList(sql, params,
					TreeStaffOrtRule.class);
		}
		return null;
	}

	public List<TreeStaffOtRule> getTreeStaffOrgTypeList() {
		if (this.getOrgTreeId() != null) {
			String sql = "SELECT * FROM TREE_STAFF_OT_RULE WHERE STATUS_CD=? AND ORG_TREE_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgTreeId());
			return this.repository().jdbcFindList(sql, params,
					TreeStaffOtRule.class);
		}
		return null;
	}

	public List<TreeStaffSftRule> getTreeStaffStaffTypeList() {
		if (this.getOrgTreeId() != null) {
			String sql = "SELECT * FROM TREE_STAFF_SFT_RULE WHERE STATUS_CD=? AND ORG_TREE_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgTreeId());
			return this.repository().jdbcFindList(sql, params,
					TreeStaffSftRule.class);
		}
		return null;
	}
}