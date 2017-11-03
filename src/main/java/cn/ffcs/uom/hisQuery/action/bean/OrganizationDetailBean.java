package cn.ffcs.uom.hisQuery.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *组织编辑Bean
 * 
 * @author
 **/
public class OrganizationDetailBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window organizationDetailWindow;
	/**
	 *组织名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 *组织编码.
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 *组织类型.
	 **/
	@Getter
	@Setter
	private Listbox orgType;
	/**
	 *存在类型.
	 **/
	@Getter
	@Setter
	private Listbox existType;
	/**
	 *组织级别
	 */
	@Getter
	@Setter
	private Combobox orgLevel;
	/**
	 *组织英文名称.
	 **/
	@Getter
	@Setter
	private Textbox orgNameEn;
	/**
	 *组织规模.
	 **/
	@Getter
	@Setter
	private Longbox orgScale;
	/**
	 *负责人信息.
	 **/
	@Getter
	@Setter
	private Textbox principal;
	/**
	 *集团编码.
	 **/
	@Getter
	@Setter
	private Textbox orgGroupCode;
	/**
	 *业务编码.
	 **/
	@Getter
	@Setter
	private Textbox orgBusinessCode;
	/**
	 *公用管理区域标识.
	 **/
//	@Getter
//	@Setter
//	private RegionSelectExt commonRegionName;
	/**
	 *组织简介.
	 **/
	@Getter
	@Setter
	private Textbox orgContent;
	
	
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Longbox orgId;
	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveOrg;
	/**
	 * .
	 */
	@Getter
	@Setter
	private Longbox orgSeq;
	/**
	 * 组织树类型
	 */
	@Getter
	@Setter
	private Combobox relaCd;
	/**
	 *地址.
	 **/
	@Getter
	@Setter
	private Textbox address;
	/**
	 *邮箱.
	 **/
	@Getter
	@Setter
	private Textbox email;
	/**
	 *备用邮箱.
	 **/
	@Getter
	@Setter
	private Textbox secondaryEmail;
	/**
	 *联系电话.
	 **/
	@Getter
	@Setter
	private Textbox phone;
	/**
	 *备用联系电话.
	 **/
	@Getter
	@Setter
	private Textbox secondaryPhone;
	/**
	 *全称.
	 **/
	@Getter
	@Setter
	private Textbox orgFullName;
	/**
	 *简称.
	 **/
	@Getter
	@Setter
	private Textbox orgShortName;

}
