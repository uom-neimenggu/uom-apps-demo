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
public class TreeOrgStaffRela extends UomEntity implements Serializable {

	/**
	 * 组织树组织员工关系标识
	 */
	@Getter
	@Setter
	@Key
	private Long treeOrgStaffRelaId;
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
	 * 员工标识.
	 **/
	@Getter
	@Setter
	private Long staffId;
	/**
	 * 原组织标识.
	 **/
	@Getter
	@Setter
	private Long oriOrgId;
	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String relaCd;
	/**
	 * 员工排序号
	 */
	@Getter
	@Setter
	private Long staffSeq;
	/**
	 * 账号
	 */
	@Getter
	@Setter
	private String userCode;
	/**
	 * 备注
	 */
	@Getter
	@Setter
	private String note;
}
