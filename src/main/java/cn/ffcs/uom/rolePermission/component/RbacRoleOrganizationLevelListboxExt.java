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
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleOrganizationLevelListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationLevelManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public class RbacRoleOrganizationLevelListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacRoleOrganizationLevelListboxExtBean bean = new RbacRoleOrganizationLevelListboxExtBean();

	/**
	 * 角色树上选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的关系
	 */
	private RbacRoleOrganizationLevel rbacRoleOrganizationLevel;

	/**
	 * 查询queryRbacRoleOrganizationLevel.
	 */
	private RbacRoleOrganizationLevel queryRbacRoleOrganizationLevel;

	@SuppressWarnings("unused")
	private String opType;

	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");
	/**
	 * 角色组织
	 */
	private RbacRoleOrganizationLevelManager rbacRoleOrganizationLevelManager = (RbacRoleOrganizationLevelManager) ApplicationContextUtil
			.getBean("rbacRoleOrganizationLevelManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRoleOrganizationLevelListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_role_organization_level_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_LEVEL_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_LEVEL_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false, false, false);
	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationLevelQueryResponse(
			final ForwardEvent event) throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getOrgCode().setValue("");
		this.bean.getOrgName().setValue("");

		this.rbacRoleRelation = (RbacRoleRelation) event.getOrigin().getData();
		this.onQueryRbacRoleOrganizationLevel();
		this.setButtonValid(true, false, false, false);
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationLevelSelectRequest() throws Exception {
		if (bean.getRbacRoleOrganizationLevelListbox().getSelectedCount() > 0) {
			rbacRoleOrganizationLevel = (RbacRoleOrganizationLevel) bean
					.getRbacRoleOrganizationLevelListbox().getSelectedItem()
					.getValue();
			this.setButtonValid(true, true, true, true);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleOrganizationLevelListPaging() throws Exception {
		this.queryRbacRoleOrganizationLevel();
	}

	/**
	 * 新增角色组织层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRoleOrganizationLevelAdd() throws Exception {
		this.openRbacRoleOrganizationLevelEditWin("add");
	}

	/**
	 * 修改角色组织层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRoleOrganizationLevelEdit() throws Exception {
		this.openRbacRoleOrganizationLevelEditWin("mod");
	}

	/**
	 * 打开组织编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacRoleOrganizationLevelEditWin(String opType)
			throws Exception {

		Map arg = new HashMap();
		this.opType = opType;
		arg.put("opType", opType);
		arg.put("rbacRoleRelation", rbacRoleRelation);
		if ("mod".equals(opType)) {
			arg.put("rbacRoleOrganizationLevel", rbacRoleOrganizationLevel);
		}
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_organization_level_edit.zul",
				this, arg);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacRoleOrganizationLevel();
				}
			}
		});
	}

	/**
	 * 删除角色组织关系
	 */
	public void onRbacRoleOrganizationLevelDel() {
		if (this.rbacRoleOrganizationLevel != null
				&& rbacRoleOrganizationLevel.getRbacRoleOrgLevelId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacRoleOrganizationLevelManager
								.removeRbacRoleOrganizationLevel(rbacRoleOrganizationLevel);
						PubUtil.reDisplayListbox(
								bean.getRbacRoleOrganizationLevelListbox(),
								rbacRoleOrganizationLevel, "del");
						rbacRoleOrganizationLevelManager
								.removeRbacRoleOrganizationLevelToRaptornuke(rbacRoleOrganizationLevel);
						rbacRoleOrganizationLevel = null;
						setButtonValid(true, false, false, false);
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
		if (rbacRoleOrganizationLevel != null
				&& rbacRoleOrganizationLevel.getRbacOrgId() != null) {

			OrganizationRelation organizationRelation = new OrganizationRelation();
			organizationRelation.setOrgId(rbacRoleOrganizationLevel
					.getRbacOrgId());
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
	private void setButtonValid(final Boolean canAdd, final Boolean canEdit,
			final Boolean canDel, final Boolean canView) {
		this.bean.getAddRbacRoleOrganizationLevelButton().setDisabled(!canAdd);
		this.bean.getEditRbacRoleOrganizationLevelButton()
				.setDisabled(!canEdit);
		this.bean.getDelRbacRoleOrganizationLevelButton().setDisabled(!canDel);
		this.bean.getViewOrganizationPathButton().setDisabled(!canView);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacRoleOrganizationLevel() {
		this.bean.getRbacRoleOrganizationLevelListPaging().setActivePage(0);
		this.queryRbacRoleOrganizationLevel();
	}

	/**
	 * 查询角色组织.
	 */
	private void queryRbacRoleOrganizationLevel() {
		if (this.rbacRoleRelation != null) {
			queryRbacRoleOrganizationLevel = new RbacRoleOrganizationLevel();
			queryRbacRoleOrganizationLevel.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());
			queryRbacRoleOrganizationLevel.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacRoleOrganizationLevel.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());
			queryRbacRoleOrganizationLevel.setOrgCode(this.bean.getOrgCode()
					.getValue());
			queryRbacRoleOrganizationLevel.setOrgName(this.bean.getOrgName()
					.getValue());

			PageInfo pageInfo = rbacRoleOrganizationLevelManager
					.queryPageInfoRbacRoleOrganizationLevel(
							queryRbacRoleOrganizationLevel, this.bean
									.getRbacRoleOrganizationLevelListPaging()
									.getActivePage() + 1, this.bean
									.getRbacRoleOrganizationLevelListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacRoleOrganizationLevelListbox().setModel(dataList);
			this.bean.getRbacRoleOrganizationLevelListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean
					.getRbacRoleOrganizationLevelListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacRoleOrganizationLevel() {

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
		boolean canEdit = false;
		boolean canDelete = false;
		boolean canPathView = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDelete = true;
			canPathView = true;
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_LEVEL_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_LEVEL_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_LEVEL_DEL)) {
				canDelete = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_ORGANIZATION_LEVEL_PATH_VIEW)) {
				canPathView = true;
			}
		}
		this.bean.getAddRbacRoleOrganizationLevelButton().setVisible(canAdd);
		this.bean.getEditRbacRoleOrganizationLevelButton().setVisible(canEdit);
		this.bean.getDelRbacRoleOrganizationLevelButton().setVisible(canDelete);
		this.bean.getViewOrganizationPathButton().setVisible(canPathView);
	}

}
