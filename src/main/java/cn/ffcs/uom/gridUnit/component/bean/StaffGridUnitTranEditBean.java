package cn.ffcs.uom.gridUnit.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.gridUnit.component.GridUnitBandboxExt;
import cn.ffcs.uom.staff.component.StaffBandboxExt;

/**
 * 员工组织业务关系 Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhulintao
 * @功能说明：
 * 
 */
public class StaffGridUnitTranEditBean {

	@Getter
	@Setter
	private Window staffGridUnitTranEditComposer;

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
	private GridUnitBandboxExt gridUnitBandbox;

}
