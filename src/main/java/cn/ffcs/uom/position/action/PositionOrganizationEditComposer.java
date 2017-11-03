package cn.ffcs.uom.position.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.action.bean.PositionOrganizationEditBean;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;

@Controller
@Scope("prototype")
public class PositionOrganizationEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507005075323523801L;
	/**
	 * 页面bean
	 */
	private PositionOrganizationEditBean bean = new PositionOrganizationEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 添加的组织
	 */
	private Organization organization;
	/**
	 * 添加的岗位
	 */
	private Position position;

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrgPositonManager orgPositonManager = (OrgPositonManager) ApplicationContextUtil
			.getBean("orgPositonManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onCreate$positionOrganizationEditWindow() throws Exception {
		this.bean.getPositionBandboxExt().setDisabled(true);
		this.bean.getPositionBandboxExt().setReadonly(true);
		this.bean.getOrganizationBandboxExt().setReadonly(true);
		this.bindBean();
	}

	/**
	 * bindBean
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getPositionOrganizationEditWindow().setTitle("新增岗位组织关系");
			position = (Position) arg.get("position");
			if (position != null) {
				this.bean.getPositionBandboxExt().setPosition(position);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {

			Position position = this.bean.getPositionBandboxExt().getPosition();
			if (position == null || position.getPositionId() == null) {
				ZkUtil.showError("请选择岗位", "提示信息");
				return;
			}

			Organization org = this.bean.getOrganizationBandboxExt()
					.getOrganization();
			if (org == null || org.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
				return;
			}

			OrgPosition orgPosition = new OrgPosition();
			@SuppressWarnings("unused")
			Organization organization = new Organization();
			orgPosition.setOrgId(org.getOrgId());
			orgPosition.setPositionId(position.getPositionId());
			orgPositonManager.addOrganizationPosition(orgPosition);
			organization = organizationManager.getById(orgPosition.getOrgId());
			Events.postEvent("onOK", this.self, organization);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getPositionOrganizationEditWindow().onClose();
	}
}
