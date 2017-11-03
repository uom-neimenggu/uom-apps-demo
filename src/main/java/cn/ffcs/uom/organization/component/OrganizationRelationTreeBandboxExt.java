/**
 * 组织关系树Bandbox
 */
package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.model.OrganizationRelation;

@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class OrganizationRelationTreeBandboxExt extends Bandbox implements
		IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/organization_relation_tree_bandbox_ext.zul";
	/**
	 * 选择的组织关系
	 */
	private OrganizationRelation organizationRelation;
	/**
	 * 组织关系树
	 */
	@Getter
	private OrganizationRelationTree organizationRelationTree;

	/**
	 * 确定关闭Toolbar
	 */
	@Getter
	private Toolbar organizationRelationTreeBandboxToolbar;

	private Button okButton;

	private Button cancelButton;

	/**
	 * 是否支持多选
	 */
	@Setter
	private Boolean isMultiple = false;

	/**
	 * 保存多选List
	 */
	@Getter
	private List<OrganizationRelation> organizationRelations = new ArrayList<OrganizationRelation>();

	/**
	 * 是否代理商页面 解决 组织树组织关系新增时 代理商出现两颗树问题
	 */
	@Getter
	@Setter
	public Boolean isAgent = false;
	/**
	 * 是否内部经营实体页面 解决 组织树组织关系新增时 内部经营实体出现两颗树问题
	 */
	@Getter
	@Setter
	public Boolean isIbe = false;

	/**
	 * 构造函数
	 */
	public OrganizationRelationTreeBandboxExt() {

		// Events.postEvent(OrganizationConstant.ON_ORGANIZATION_TREE_SELECT,
		// this.organizationRelationTree, null);
	    
	}

	public void onCreate() throws Exception {
		Map arg = new HashMap();
		arg.put("isAgent", isAgent);
		arg.put("isIbe", isIbe);
		Executions.createComponents(this.zul, this, arg);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		this.init();
		//organizationRelationTree.bindTree();
	}

	/**
	 * 界面初始化.
	 */
	public void init() {
		this.setReadonly(true);
		this.organizationRelationTree.addEventListener(Events.ON_SELECT,
				new EventListener() {
					@SuppressWarnings("rawtypes")
					public void onEvent(Event event) throws Exception {
						if (OrganizationRelationTreeBandboxExt.this.organizationRelationTree
								.getSelectedCount() > 0) {
							Object data = OrganizationRelationTreeBandboxExt.this.organizationRelationTree
									.getSelectedItem().getValue();
							OrganizationRelationTreeBandboxExt.this
									.setOrganizationRelation((OrganizationRelation) ((TreeNodeImpl) data)
											.getEntity());
						}
						if (!isMultiple) {
							OrganizationRelationTreeBandboxExt.this.close();
						}
					}
				});
	}

	public Object getAssignObject() {
		return getOrganizationRelation();
	}

	public OrganizationRelation getOrganizationRelation() {
		return this.organizationRelation;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null
				|| assignObject instanceof OrganizationRelation) {
			setOrganizationRelation((OrganizationRelation) assignObject);
		}
	}

	public void setOrganizationRelation(
			OrganizationRelation organizationRelation) {
		this.setValue(organizationRelation == null ? "" : organizationRelation
				.getOrganization().getOrgName());
		this.organizationRelation = organizationRelation;
	}

	public void setOrganizationRelations(
			List<OrganizationRelation> organizationRelations) {
		String orgName = "";
		if (organizationRelations != null && organizationRelations.size() > 0) {
			for (int i = 0; i < organizationRelations.size(); i++) {
				if (i == organizationRelations.size() - 1) {
					orgName += organizationRelations.get(i).getOrganization()
							.getOrgName();
				} else {
					orgName += organizationRelations.get(i).getOrganization()
							.getOrgName()
							+ ",";
				}
			}
		}
		this.setValue(orgName);
		this.organizationRelations = organizationRelations;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> tiSet = this.organizationRelationTree.getSelectedItems();
		List<OrganizationRelation> list = new ArrayList<OrganizationRelation>();
		if (tiSet != null) {
			Iterator<Treeitem> it = tiSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				OrganizationRelation or = (OrganizationRelation) ((TreeNodeImpl) ti
						.getValue()).getEntity();
				if (or != null) {
					list.add(or);
				}
			}
		}
		this.setOrganizationRelations(list);
		OrganizationRelationTreeBandboxExt.this.close();
	}

	public void onClick$cancelButton() {
		OrganizationRelationTreeBandboxExt.this.close();
	}
}
