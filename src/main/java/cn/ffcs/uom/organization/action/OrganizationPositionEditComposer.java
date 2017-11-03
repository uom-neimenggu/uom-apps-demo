package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.action.bean.OrganizationPositionEditBean;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;

@Controller
@Scope("prototype")
public class OrganizationPositionEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507005075323523801L;
	/**
	 * 页面bean
	 */
	private OrganizationPositionEditBean bean = new OrganizationPositionEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 添加的组织
	 */
	private Organization organization;
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
	public void onCreate$organizationPositionEditWindow() throws Exception {
		this.bean.getOrganizationBandboxExt().setDisabled(true);
		this.bean.getOrganizationBandboxExt().setReadonly(true);
		this.bean.getPositionTreeBandboxExt().setReadonly(true);
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
			this.bean.getOrganizationPositionEditWindow().setTitle("新增组织岗位关系");
			organization = (Organization) arg.get("organization");
			if (organization != null) {
				this.bean.getOrganizationBandboxExt().setOrganization(
						organization);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {
			Organization org = this.bean.getOrganizationBandboxExt()
					.getOrganization();
			if (org == null || org.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
				return;
			}
			/**
			 * 组织下的岗位批量新增
			 */
			List<Position> positionList = this.bean.getPositionTreeBandboxExt()
					.getPositionList();
			if (positionList == null || positionList.size() <= 0) {
				ZkUtil.showError("请选择岗位", "提示信息");
				return;
			}
			List<OrgPosition> orgPositionList = new ArrayList<OrgPosition>();
			for (Position position : positionList) {
				OrgPosition orgPosition = new OrgPosition();
				orgPosition.setOrgId(org.getOrgId());
				orgPosition.setPositionId(position.getPositionId());
				OrgPosition op = orgPositonManager
						.queryOrganizationPosition(orgPosition);
				if (op != null) {
					ZkUtil.showError("岗位:" + position.getPositionName()
							+ ",已存在", "提示信息");
					return;
				}
				orgPositionList.add(orgPosition);
			}
			orgPositonManager.addOrganizationPositionList(orgPositionList);
			Events.postEvent("onOK", this.self, orgPositionList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getOrganizationPositionEditWindow().onClose();
	}
}
