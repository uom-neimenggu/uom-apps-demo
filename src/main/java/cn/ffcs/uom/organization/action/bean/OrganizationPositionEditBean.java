package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.position.component.PositionTreeBandboxExt;

public class OrganizationPositionEditBean {
	private Window organizationPositionEditWindow;
	private OrganizationBandboxExt organizationBandboxExt;
	@Getter
	@Setter
	private PositionTreeBandboxExt positionTreeBandboxExt;
	private Button saveBtn;
	private Button cancelBtn;

	public Window getOrganizationPositionEditWindow() {
		return organizationPositionEditWindow;
	}

	public void setOrganizationPositionEditWindow(
			Window organizationPositionEditWindow) {
		this.organizationPositionEditWindow = organizationPositionEditWindow;
	}

	public OrganizationBandboxExt getOrganizationBandboxExt() {
		return organizationBandboxExt;
	}

	public void setOrganizationBandboxExt(
			OrganizationBandboxExt organizationBandboxExt) {
		this.organizationBandboxExt = organizationBandboxExt;
	}

	public Button getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(Button saveBtn) {
		this.saveBtn = saveBtn;
	}

	public Button getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(Button cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

}
