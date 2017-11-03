package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.dao.StaffDao;
import lombok.Getter;
import lombok.Setter;

/**
 * 信息实体类封装 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-7-29
 * @功能说明：
 * 
 */
public class ImportInfoResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作类型
	 */
	@Setter
	@Getter
	private String optType;
	
	/**
	 * 导入员工Id
	 */
	@Setter
	@Getter
	private Long staffId;
	/**
	 * 导入员工姓名
	 */
	@Setter
	@Getter
	private String staffName;
	/**
	 * 导入员工编码
	 */
	@Setter
	@Getter
	private String staffCode;
	/**
	 * 导入员工工号
	 */
	@Setter
	@Getter
	private String staffNbr;
	/**
	 * 导入员工FIX_ID
	 */
	@Setter
	@Getter
	private Long staffFixId;
	/**
	 * 导入员工UUID
	 */
	@Setter
	@Getter
	private String uuid;
	/**
	 * 组织ID
	 */
	@Setter
	@Getter
	private Long orgId;
	/**
	 * 导入员工组织
	 */
	@Setter
	@Getter
	private String orgName;

	/**
	 * 获取员工组织
	 * 
	 * @return
	 */
	/*
	 * public String getOrgName(){ if(!StrUtil.isNullOrEmpty(this.getOrgId())){
	 * Organization organization = repositoryOrg().getById(this.getOrgId());
	 * if(null != organization) { return organization.getOrgName(); } } return
	 * null; }
	 * 
	 * public static OrganizationDao repositoryOrg() { return (OrganizationDao)
	 * ApplicationContextUtil.getBean("organizationDao"); }
	 */
}
