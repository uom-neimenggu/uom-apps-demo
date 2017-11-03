package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.OrganizationInfoExt;

/**
 *组织编辑Bean
 * 
 * @author
 **/
public class OrganizationEditBean {
	/**
	 * window
	 */
	@Getter
	@Setter
	private Window organizationEditWindow;
	/**
	 * 组织信息.
	 */
	@Getter
	@Setter
	private OrganizationInfoExt organizationInfoExt;
	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveOrg;

	/*
	 * 取消按钮.
	 */
	@Getter
	@Setter
	private Button cancelOrg;

}
