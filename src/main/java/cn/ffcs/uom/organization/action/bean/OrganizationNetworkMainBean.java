/**
 * 
 */
package cn.ffcs.uom.organization.action.bean;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Label;

import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

/**
 * @author yahui
 * 
 */
public class OrganizationNetworkMainBean {
	private Window organizationNetworkWin;
	private Panel organizationNetworkEditExtPanel;
	/**
	 * 单据号
	 */
	private Textbox serial;
	/**
	 * 网点编码
	 */
	private Textbox orgCode;
	/**
	 * 本机组织
	 */
	private Textbox orgName;
	/**
	 * 组织性质
	 */
	private Listbox orgType;
	/**
	 * 存在类型
	 */
	private Listbox existType;
	/**
	 * 网点类型
	 */
	private Listbox orgTypeCd;
	/**
	 * 城镇标识
	 */
	private Listbox cityTown;
	/**
	 * 组织关系类型
	 */
	private Listbox orgRelaType;
	/**
	 * 城镇标识
	 */
	private Label isCheckL;
	/**
	 * 是否校验
	 */
	private Listbox isCheck;
	/**
	 * 电信管理区域
	 */
	private TelcomRegionTreeBandbox telcomRegionTreeBandbox;
	/**
	 * 行政管理区域
	 */
	private PoliticalLocationTreeBandbox politicalLocationTreeBandbox;
	/**
	 * 上级组织
	 */
	private Textbox thehighOrgname;
	/**
	 * 组织排序
	 */
	private Longbox orgPriority;
	/**
	 * 
	 */
	private Toolbar btnToolBar;
	/**
	 * 
	 */
	private Toolbarbutton addButton;
	/**
	 * 
	 */
	private Toolbarbutton modButton;
	/**
	 * 
	 */
	private Toolbarbutton delButton;
	/**
	 * 
	 */
	private Toolbarbutton checkButton;

	
	
	public Listbox getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Listbox isCheck) {
		this.isCheck = isCheck;
	}

	public Window getOrganizationNetworkWin() {
		return organizationNetworkWin;
	}

	public void setOrganizationNetworkWin(Window organizationNetworkWin) {
		this.organizationNetworkWin = organizationNetworkWin;
	}

	public Panel getOrganizationNetworkEditExtPanel() {
		return organizationNetworkEditExtPanel;
	}

	public void setOrganizationNetworkEditExtPanel(
			Panel organizationNetworkEditExtPanel) {
		this.organizationNetworkEditExtPanel = organizationNetworkEditExtPanel;
	}


	public Textbox getSerial() {
		return serial;
	}

	public void setSerial(Textbox serial) {
		this.serial = serial;
	}

    

	public Textbox getOrgName() {
		return orgName;
	}

	public void setOrgName(Textbox orgName) {
		this.orgName = orgName;
	}

	public Listbox getOrgType() {
		return orgType;
	}

	public void setOrgType(Listbox orgType) {
		this.orgType = orgType;
	}

	public Listbox getExistType() {
		return existType;
	}

	public void setExistType(Listbox existType) {
		this.existType = existType;
	}

    

	public Textbox getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Textbox orgCode) {
		this.orgCode = orgCode;
	}

	public Listbox getOrgTypeCd() {
		return orgTypeCd;
	}

	public void setOrgTypeCd(Listbox orgTypeCd) {
		this.orgTypeCd = orgTypeCd;
	}

	public Listbox getCityTown() {
		return cityTown;
	}

	public void setCityTown(Listbox cityTown) {
		this.cityTown = cityTown;
	}

	public TelcomRegionTreeBandbox getTelcomRegionTreeBandbox() {
		return telcomRegionTreeBandbox;
	}

	public void setTelcomRegionTreeBandbox(
			TelcomRegionTreeBandbox telcomRegionTreeBandbox) {
		this.telcomRegionTreeBandbox = telcomRegionTreeBandbox;
	}

	public PoliticalLocationTreeBandbox getPoliticalLocationTreeBandbox() {
		return politicalLocationTreeBandbox;
	}

	public void setPoliticalLocationTreeBandbox(
			PoliticalLocationTreeBandbox politicalLocationTreeBandbox) {
		this.politicalLocationTreeBandbox = politicalLocationTreeBandbox;
	}

	public Textbox getThehighOrgname() {
		return thehighOrgname;
	}

	public void setThehighOrgname(Textbox thehighOrgname) {
		this.thehighOrgname = thehighOrgname;
	}

	public Longbox getOrgPriority() {
		return orgPriority;
	}

	public void setOrgPriority(Longbox orgPriority) {
		this.orgPriority = orgPriority;
	}

	public Toolbar getBtnToolBar() {
		return btnToolBar;
	}

	public void setBtnToolBar(Toolbar btnToolBar) {
		this.btnToolBar = btnToolBar;
	}

	public Toolbarbutton getAddButton() {
		return addButton;
	}

	public void setAddButton(Toolbarbutton addButton) {
		this.addButton = addButton;
	}

	public Toolbarbutton getModButton() {
		return modButton;
	}

	public void setModButton(Toolbarbutton modButton) {
		this.modButton = modButton;
	}

	public Toolbarbutton getDelButton() {
		return delButton;
	}

	public void setDelButton(Toolbarbutton delButton) {
		this.delButton = delButton;
	}

	public Label getIsCheckL() {
		return isCheckL;
	}

	public void setIsCheckL(Label isCheckL) {
		this.isCheckL = isCheckL;
	}

	public Toolbarbutton getCheckButton() {
		return checkButton;
	}

	public void setCheckButton(Toolbarbutton checkButton) {
		this.checkButton = checkButton;
	}

	public Listbox getOrgRelaType() {
		return orgRelaType;
	}

	public void setOrgRelaType(Listbox orgRelaType) {
		this.orgRelaType = orgRelaType;
	}
	
	
	

}
