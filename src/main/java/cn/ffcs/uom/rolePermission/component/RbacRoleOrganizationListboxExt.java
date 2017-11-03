package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleOrganizationListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public class RbacRoleOrganizationListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacRoleOrganizationListboxExtBean bean = new RbacRoleOrganizationListboxExtBean();

	/**
	 * 角色树上选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的关系
	 */
	private RbacRoleOrganization rbacRoleOrganization;

	/**
	 * 查询queryRbacRoleOrganization.
	 */
	private RbacRoleOrganization queryRbacRoleOrganization;

	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");
	/**
	 * 角色组织
	 */
	private RbacRoleOrganizationManager rbacRoleOrganizationManager = (RbacRoleOrganizationManager) ApplicationContextUtil
			.getBean("rbacRoleOrganizationManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRoleOrganizationListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_role_organization_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false, false);
	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getOrgCode().setValue("");
		this.bean.getOrgName().setValue("");

		this.rbacRoleRelation = (RbacRoleRelation) event.getOrigin().getData();
		this.onQueryRbacRoleOrganization();
		this.setButtonValid(true, false, false);
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationSelectRequest() throws Exception {
		if (bean.getRbacRoleOrganizationListbox().getSelectedCount() > 0) {
			rbacRoleOrganization = (RbacRoleOrganization) bean
					.getRbacRoleOrganizationListbox().getSelectedItem()
					.getValue();
			this.setButtonValid(true, true, true);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationListPaging() throws Exception {
		this.queryRbacRoleOrganization();
	}

	/**
	 * 新增角色组织关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacRoleOrganizationAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacRoleRelation", rbacRoleRelation);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_organization_edit.zul", this,
				map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacRoleOrganization();
				}
			}
		});
	}

	/**
	 * 删除角色组织关系
	 */
	public void onRbacRoleOrganizationDel() {
		if (this.rbacRoleOrganization != null
				&& rbacRoleOrganization.getRbacRoleOrgId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacRoleOrganizationManager
								.removeRbacRoleOrganization(rbacRoleOrganization);
						PubUtil.reDisplayListbox(
								bean.getRbacRoleOrganizationListbox(),
								rbacRoleOrganization, "del");
						rbacRoleOrganizationManager
								.removeRbacRoleOrganizationToRaptornuke(rbacRoleOrganization);
						rbacRoleOrganization = null;
						setButtonValid(true, false, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 组织路径查看
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onOrganizationPathView() throws Exception {
		if (rbacRoleOrganization != null
				&& rbacRoleOrganization.getRbacOrgId() != null) {

			OrganizationRelation organizationRelation = new OrganizationRelation();
			organizationRelation.setOrgId(rbacRoleOrganization.getRbacOrgId());
			List<OrganizationRelation> organizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (organizationRelationList != null
					&& organizationRelationList.size() > 0) {
				Map arg = new HashMap();
				arg.put("organizationRelationList", organizationRelationList);
				Window win = (Window) Executions.createComponents(
						"/pages/organization/organization_path.zul", this, arg);
				win.doModal();
			}
		}
	}

	/**
	 * 设置角色组织按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete,
			final Boolean canView) {
		this.bean.getAddRbacRoleOrganizationButton().setDisabled(!canAdd);
		this.bean.getDelRbacRoleOrganizationButton().setDisabled(!canDelete);
		this.bean.getViewOrganizationPathButton().setDisabled(!canView);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacRoleOrganization() {
		this.bean.getRbacRoleOrganizationListPaging().setActivePage(0);
		this.queryRbacRoleOrganization();
	}

	/**
	 * 查询角色组织.
	 */
	private void queryRbacRoleOrganization() {
		if (this.rbacRoleRelation != null) {
			queryRbacRoleOrganization = new RbacRoleOrganization();
			queryRbacRoleOrganization.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());
			queryRbacRoleOrganization.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacRoleOrganization.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());
			queryRbacRoleOrganization.setOrgCode(this.bean.getOrgCode()
					.getValue());
			queryRbacRoleOrganization.setOrgName(this.bean.getOrgName()
					.getValue());

			PageInfo pageInfo = rbacRoleOrganizationManager
					.queryPageInfoRbacRoleOrganization(
							queryRbacRoleOrganization, this.bean
									.getRbacRoleOrganizationListPaging()
									.getActivePage() + 1, this.bean
									.getRbacRoleOrganizationListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacRoleOrganizationListbox().setModel(dataList);
			this.bean.getRbacRoleOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacRoleOrganizationListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacRoleOrganization() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

		this.bean.getOrgCode().setValue("");

		this.bean.getOrgName().setValue("");

	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDelete = false;
		boolean canPathView = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDelete = true;
			canPathView = true;
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_DEL)) {
				canDelete = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_PATH_VIEW)) {
				canPathView = true;
			}
		}
		this.bean.getAddRbacRoleOrganizationButton().setVisible(canAdd);
		this.bean.getDelRbacRoleOrganizationButton().setVisible(canDelete);
		this.bean.getViewOrganizationPathButton().setVisible(canPathView);
	}

}
