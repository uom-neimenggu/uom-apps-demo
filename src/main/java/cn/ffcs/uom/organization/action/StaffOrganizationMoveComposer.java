package cn.ffcs.uom.organization.action;
import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;

@Controller
@Scope("prototype")
public class StaffOrganizationMoveComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7582425110520881101L;

	@Getter
	@Setter
	private Window staffOrganizationMoveWindow;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
	@Getter
	@Setter
	private OrganizationBandboxExt organizationBandboxExt;

	private String opType;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, this);
	}

	/**
	 * 初始化
	 */
	public void onCreate$staffOrganizationMoveWindow() throws Exception {
		opType = (String) arg.get("opType");
		if ("move".equals(opType)) {
			this.staffOrganizationMoveWindow.setTitle("员工移动");
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if (this.organizationBandboxExt != null
				&& organizationBandboxExt.getOrganization() != null) {
			Events.postEvent("onOK", this.self, organizationBandboxExt
					.getOrganization());
			this.staffOrganizationMoveWindow.onClose();
		} else {
			ZkUtil.showError("请选择组织", "提示信息");
			return;
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.staffOrganizationMoveWindow.onClose();
	}
}
