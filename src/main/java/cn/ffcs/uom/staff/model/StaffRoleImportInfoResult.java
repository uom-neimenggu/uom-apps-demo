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
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-01
 * @功能说明：
 * 
 */
public class StaffRoleImportInfoResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作类型
	 */
	@Setter
	@Getter
	private String optType;
	
	/**
	 * 导入员工姓名
	 */
	@Setter
	@Getter
	private String staffName;
	/**
	 * 导入员工账号
	 */
	@Setter
	@Getter
	private String staffAccount;
	/**
	 * 导入员工编码
	 */
	@Setter
	@Getter
	private String staffCode;
	/**
	 * 导入员工角色
	 */
	@Setter
	@Getter
	private String staffRoleName;
	/**
	 * 导入员工会话数
	 */
	@Setter
	@Getter
	private String staffAttrValue;

}
