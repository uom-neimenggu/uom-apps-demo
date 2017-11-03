package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationRelationRelacdBandboxExt;

public class TreeStaffOrEditBean {
	@Getter
	@Setter
	private Window treeStaffOrEditWin;
	@Getter
	@Setter
	private OrganizationRelationRelacdBandboxExt relaCdBandBox;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
