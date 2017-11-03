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

/**
 * 
 * 
 * @author
 * 
 **/
public class TreeOrgRela extends UomEntity implements Serializable {

	/**
	 * 组织树组织关系标识
	 */
	@Getter
	@Setter
	@Key
	private Long treeOrgRelaId;
	/**
	 * 组织树标识.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 * 关联组织标识.
	 **/
	@Getter
	@Setter
	private Long relaOrgId;
	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String relaCd;
}
