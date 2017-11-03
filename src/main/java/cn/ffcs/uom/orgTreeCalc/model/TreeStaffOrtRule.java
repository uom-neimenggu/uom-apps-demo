package cn.ffcs.uom.orgTreeCalc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.util.Key;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 
 * 
 * @author
 * 
 **/
public class TreeStaffOrtRule extends UomEntity implements Serializable {

	/**
	 * 组织树员工汇总引用关联类型配置标识
	 */
	@Getter
	@Setter
	@Key
	private Long treeStaffOrtRuleId;
	/**
	 * 组织树标识.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 引用组织关系类型.
	 **/
	@Getter
	@Setter
	private String refOrgRelaCd;
	
	/**
	 * 获取关系名称
	 * 
	 * @return
	 */
	public String getOrgRelaCdName() {
		if (!StrUtil.isEmpty(this.getRefOrgRelaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"OrganizationRelation", "relaCd", this.getRefOrgRelaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}
}
