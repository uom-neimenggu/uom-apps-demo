package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.staff.component.StaffBandboxExt;

/**
 * 员工组织业务关系 Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhulintao
 * @功能说明：
 * 
 */
public class StaffOrgTranEditBean {

	@Getter
	@Setter
	private Window staffOrgTranEditComposer;

	@Getter
	@Setter
	private Listbox ralaCd;

	@Setter
	@Getter
	private Longbox staffSort;

	@Getter
	@Setter
	private StaffBandboxExt staff;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

}
