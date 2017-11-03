package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;

/**
 * 组织业务关系 Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-16
 * @功能说明：
 * 
 */
public class OrganizationTranEditBean {

	@Getter
	@Setter
	private Window organizationTranEditComposer;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

	@Getter
	@Setter
	private OrganizationBandboxExt tranOrg;

	/**
	 * 组织业务关系类型
	 */
	@Setter
	@Getter
	private TreeChooserBandbox tranRelaType;

}
