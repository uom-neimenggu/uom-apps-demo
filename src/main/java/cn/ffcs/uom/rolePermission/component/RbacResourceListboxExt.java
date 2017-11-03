package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.rolePermission.component.bean.RbacResourceListboxBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacResourceManager;
import cn.ffcs.uom.rolePermission.manager.RbacResourceRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacResourceListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacResourceListboxBean bean = new RbacResourceListboxBean();

	@Autowired
	@Qualifier("rbacResourceManager")
	private RbacResourceManager rbacResourceManager = (RbacResourceManager) ApplicationContextUtil
			.getBean("rbacResourceManager");

	@Autowired
	@Qualifier("rbacResourceRelationManager")
	private RbacResourceRelationManager rbacResourceRelationManager = (RbacResourceRelationManager) ApplicationContextUtil
			.getBean("rbacResourceRelationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_resource_listbox_ext.zul";

	/**
	 * rbacResource.
	 */
	private RbacResource rbacResource;

	/**
	 * 查询rbacResource.
	 */
	private RbacResource queryRbacResource;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 */
	private String opType;

	public RbacResourceListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setRbacResourceButtonValid(true, false, false, false);
	}

	public void onQueryRbacResourceResponse(final ForwardEvent event)
			throws Exception {
		queryRbacResource = (RbacResource) event.getOrigin().getData();
		this.onQueryRbacResource();
		if (this.bean.getRbacResourceListbox().getItemCount() == 0) {
			this.setRbacResourceButtonValid(true, false, false, false);
		}
	}

	/**
	 * 资源选择.
	 */
	public void onRbacResourceSelectRequest() throws Exception {
		if (this.bean.getRbacResourceListbox().getSelectedCount() > 0) {
			rbacResource = (RbacResource) bean.getRbacResourceListbox()
					.getSelectedItem().getValue();
			this.setRbacResourceButtonValid(true, true, true, true);
			Events.postEvent(
					RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_REQUEST,
					this, rbacResource);
		}

	}

	/**
	 * 新增资源.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacResourceAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacResourceEditWin("add");
	}

	/**
	 * 查看资源.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacResourceView() throws Exception {
		this.openRbacResourceEditWin("view");
	}

	/**
	 * 修改资源 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacResourceEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacResourceEditWin("mod");
	}

	/**
	 * 删除资源.
	 */
	public void onRbacResourceDel() throws Exception {

		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (rbacResource == null
							|| rbacResource.getRbacResourceId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {

						RbacResourceRelation rbacResourceRelation = new RbacResourceRelation();
						rbacResourceRelation
								.setRbacParentResourceId(rbacResource
										.getRbacResourceId());

						List<RbacResourceRelation> rbacResourceRelationList = rbacResourceRelationManager
								.queryRbacResourceRelationList(rbacResourceRelation);

						if (rbacResourceRelationList != null
								&& rbacResourceRelationList.size() > 0) {
							ZkUtil.showError("你选择的资源存在子资源，请先删除该子资源", "提示信息");
							return;
						}
						rbacResourceManager.removeRbacResource(rbacResource);
						PubUtil.reDisplayListbox(bean.getRbacResourceListbox(),
								rbacResource, "del");
					}
				}
			}
		});

	}

	/**
	 * 打开资源编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacResourceEditWin(String operationType) throws Exception {
		Map arg = new HashMap();
		this.opType = operationType;
		arg.put("opType", opType);
		if (opType.equals("mod") || opType.equals("view")) {
			arg.put("rbacResource", rbacResource);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_resource_edit.zul", this, arg);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			public void onEvent(Event event) throws Exception {
				setRbacResourceButtonValid(true, false, false, false);
				if ("add".equals(opType)) {
					PubUtil.reDisplayListbox(bean.getRbacResourceListbox(),
							(RbacResource) event.getData(), "add");
				} else if ("mod".equals(opType)) {
					queryRbacResource(queryRbacResource);
				}
			}
		});
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canView
	 *            查看按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setRbacResourceButtonValid(final Boolean canAdd,
			final Boolean canView, final Boolean canEdit,
			final Boolean canDelete) {
		this.bean.getAddRbacResourceButton().setDisabled(!canAdd);
		this.bean.getViewRbacResourceButton().setDisabled(!canView);
		this.bean.getEditRbacResourceButton().setDisabled(!canEdit);
		this.bean.getDelRbacResourceButton().setDisabled(!canDelete);
	}

	/**
	 * 按条件查询
	 */
	public void onQueryRbacResource() throws Exception {

		queryRbacResource = new RbacResource();

		if (this.bean.getRbacResourceCode() != null
				&& !StrUtil.isEmpty(this.bean.getRbacResourceCode().getValue())) {
			queryRbacResource.setRbacResourceCode(this.bean
					.getRbacResourceCode().getValue());
		}

		if (this.bean.getRbacResourceName() != null
				&& !StrUtil.isEmpty(this.bean.getRbacResourceName().getValue())) {
			queryRbacResource.setRbacResourceName(this.bean
					.getRbacResourceName().getValue());
		}

		this.bean.getRbacResourceListboxPaging().setActivePage(0);
		this.queryRbacResource(queryRbacResource);
	}

	/**
	 * 分页.
	 */
	public void onRbacResourceListboxPaging() throws Exception {
		this.queryRbacResource(queryRbacResource);
		this.setRbacResourceButtonValid(true, false, false, false);
	}

	/**
	 * 查询资源.
	 */
	private void queryRbacResource(RbacResource rbacResource) throws Exception {
		PageInfo pageInfo = this.rbacResourceManager.queryPageInfoRbacResource(
				queryRbacResource, this.bean.getRbacResourceListboxPaging()
						.getActivePage() + 1, this.bean
						.getRbacResourceListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getRbacResourceListbox().setModel(dataList);
		this.bean.getRbacResourceListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 重置
	 */
	public void onResetRbacResource() {
		this.bean.getRbacResourceCode().setValue(null);
		this.bean.getRbacResourceName().setValue(null);
	}

	/**
	 * 关闭窗口
	 */
	public void onCloseRbacResource() {
		Events.postEvent(RolePermissionConstants.ON_CLOSE_RBAC_RESOURCE, this,
				null);

	}

	/**
	 * 清除资源
	 */
	public void onCleanRbacResource() {
		this.bean.getRbacResourceListbox().clearSelection();
		Events.postEvent(RolePermissionConstants.ON_CLEAN_RBAC_RESOURCE, this,
				null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setRbacResourceOptDivVisible(boolean visible) {
		bean.getRbacResourceOptDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setRbacResourceBandboxDivVisible(boolean visible) {
		bean.getRbacResourceBandboxDiv().setVisible(visible);
	}
}
