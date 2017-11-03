package cn.ffcs.uom.organization.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.MultidimensionalTreeNodeEditBean;
import cn.ffcs.uom.organization.model.Organization;

@Controller
@Scope("prototype")
public class MultidimensionalTreeNodeEditComposer extends BasePortletComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1014502602479906821L;
	/**
	 * 页面bean
	 */
	private MultidimensionalTreeNodeEditBean bean = new MultidimensionalTreeNodeEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 选择的组织
	 */
	private Organization organization;
	
	private String orgTreeRootId;
	/**
	 * manager
	 */
	/*private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");*/

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$multidimensionalTreeNodeEditWindow() throws Exception {
		opType = (String) arg.get("opType");
		orgTreeRootId = (String) arg.get("orgTreeRootId");

		//多维树初始化
		if ("addMultidimensionalTreeChildNode".equals(opType)&&!StrUtil.isEmpty(orgTreeRootId)) {
			this.bean.getMultidimensionalTreeNodeEditWindow().setTitle("增加子节点");
			this.bean.getOrganizationBandboxExt().setMultidimensionalTreeInit(orgTreeRootId);
		}else {
			ZkUtil.showError("未定义操作类型", "提示信息");
			this.bean.getMultidimensionalTreeNodeEditWindow().onClose();
			return;
		}
		
	}

	/**
	 * 确定
	 * 
	 * @throws Exception
	 */
	public void onAdd() throws Exception {
		this.bean.getMultidimensionalTreeNodeEditWindow().onClose();
		Map map = new HashMap();
		Organization newOrganization = this.bean.getOrganizationBandboxExt().getOrganization();
		if (newOrganization == null) {
			ZkUtil.showError("请选择组织", "提示信息");
			return;
		}
		map.put("organization", newOrganization);
		Events.postEvent("onOK", this.self, map);
	}

	/**
	 * 取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getMultidimensionalTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增组织
	 * 
	 * @throws Exception
	 */
	public void onAddOrg() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("orgTreeRootId", orgTreeRootId);
		map.put("organization", organization);
		Window win = (Window) Executions.createComponents("/pages/organization/agent_organization_edit.zul", this.self,map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Organization org = (Organization) event.getData();
				if (org != null) {
					bean.getOrganizationBandboxExt().setOrganization(org);
				}
			}
		});
	}
}
