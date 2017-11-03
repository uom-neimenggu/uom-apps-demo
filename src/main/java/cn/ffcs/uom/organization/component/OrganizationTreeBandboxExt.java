/**
 * 
 */
package cn.ffcs.uom.organization.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.model.Organization;

@Controller
@Scope("prototype")
public class OrganizationTreeBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/organization_tree_bandbox_ext.zul";
	/**
	 * 选择的组织
	 */
	private Organization organization;
	/**
	 * 组织树
	 */
	private OrganizationTree organizationTree;

	/**
	 * 构造函数
	 */
	public OrganizationTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		this.init();
	}

	/**
	 * 界面初始化.
	 */
	public void init() {
		this.setReadonly(true);
		this.organizationTree.addEventListener(Events.ON_SELECT,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (OrganizationTreeBandboxExt.this.organizationTree
								.getSelectedCount() > 0) {
							Object data = OrganizationTreeBandboxExt.this.organizationTree
									.getSelectedItem().getValue();
							OrganizationTreeBandboxExt.this
									.setOrganization((Organization) ((TreeNodeImpl) data)
											.getEntity());
						}
						OrganizationTreeBandboxExt.this.close();
					}
				});
	}

	public Object getAssignObject() {
		return getOrganization();
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof Organization) {
			setOrganization((Organization) assignObject);
		}
	}

	public void setOrganization(Organization organization) {
		this.setValue(organization == null ? "" : organization.getOrgName());
		this.organization = organization;
	}
}
