package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
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
import cn.ffcs.uom.rolePermission.component.bean.RbacBusinessSystemResourceListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;

public class RbacBusinessSystemResourceListboxExt extends Div implements
		IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacBusinessSystemResourceListboxExtBean bean = new RbacBusinessSystemResourceListboxExtBean();

	private RbacBusinessSystemResource selectRbacBusinessSystemResource;

	/**
	 * 选中的关系
	 */
	private RbacBusinessSystemResource rbacBusinessSystemResource;

	/**
	 * 查询queryRbacBusinessSystemResource.
	 */
	private RbacBusinessSystemResource queryRbacBusinessSystemResource;

	/**
	 * 系统资源
	 */
	private RbacBusinessSystemResourceManager rbacBusinessSystemResourceManager = (RbacBusinessSystemResourceManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemResourceManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacBusinessSystemResourceListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_business_system_resource_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询系统资源列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemResourceQueryResponse(
			final ForwardEvent event) throws Exception {
		this.bean.getRbacBusinessSystemCode().setValue("");
		this.bean.getRbacBusinessSystemName().setValue("");
		this.bean.getRbacResourceCode().setValue("");
		this.bean.getRbacResourceName().setValue("");

		this.selectRbacBusinessSystemResource = (RbacBusinessSystemResource) event
				.getOrigin().getData();
		this.onQueryRbacBusinessSystemResource();
		this.setButtonValid(true, false);
	}

	/**
	 * 系统资源选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemResourceSelectRequest() throws Exception {
		if (bean.getRbacBusinessSystemResourceListbox().getSelectedCount() > 0) {
			rbacBusinessSystemResource = (RbacBusinessSystemResource) bean
					.getRbacBusinessSystemResourceListbox().getSelectedItem()
					.getValue();
			this.setButtonValid(true, true);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemResourceListPaging() throws Exception {
		this.queryRbacBusinessSystemResource();
	}

	/**
	 * 新增系统资源关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacBusinessSystemResourceAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacBusinessSystemResource", selectRbacBusinessSystemResource);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_business_system_resource_edit.zul",
				this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacBusinessSystemResource();
				}
			}
		});
	}

	/**
	 * 删除系统资源关系
	 */
	public void onRbacBusinessSystemResourceDel() {
		if (this.rbacBusinessSystemResource != null
				&& rbacBusinessSystemResource.getRbacBusinessSysResourceId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacBusinessSystemResourceManager
								.removeRbacBusinessSystemResource(rbacBusinessSystemResource);
						PubUtil.reDisplayListbox(
								bean.getRbacBusinessSystemResourceListbox(),
								rbacBusinessSystemResource, "del");
						rbacBusinessSystemResource = null;
						setButtonValid(true, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 设置系统资源按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacBusinessSystemResourceButton().setDisabled(!canAdd);
		this.bean.getDelRbacBusinessSystemResourceButton().setDisabled(
				!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacBusinessSystemResource() {
		this.bean.getRbacBusinessSystemResourceListPaging().setActivePage(0);
		this.queryRbacBusinessSystemResource();
	}

	/**
	 * 查询系统资源.
	 */
	private void queryRbacBusinessSystemResource() {
		if (this.selectRbacBusinessSystemResource != null) {
			queryRbacBusinessSystemResource = new RbacBusinessSystemResource();
			queryRbacBusinessSystemResource
					.setRbacResourceId(selectRbacBusinessSystemResource
							.getRbacResourceId());
			queryRbacBusinessSystemResource
					.setRbacBusinessSystemId(selectRbacBusinessSystemResource
							.getRbacBusinessSystemId());
			queryRbacBusinessSystemResource.setRbacBusinessSystemCode(this.bean
					.getRbacBusinessSystemCode().getValue());
			queryRbacBusinessSystemResource.setRbacBusinessSystemName(this.bean
					.getRbacBusinessSystemName().getValue());
			queryRbacBusinessSystemResource.setRbacResourceCode(this.bean
					.getRbacResourceCode().getValue());
			queryRbacBusinessSystemResource.setRbacResourceName(this.bean
					.getRbacResourceName().getValue());

			PageInfo pageInfo = rbacBusinessSystemResourceManager
					.queryPageInfoRbacBusinessSystemResource(
							queryRbacBusinessSystemResource, this.bean
									.getRbacBusinessSystemResourceListPaging()
									.getActivePage() + 1, this.bean
									.getRbacBusinessSystemResourceListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacBusinessSystemResourceListbox().setModel(dataList);
			this.bean.getRbacBusinessSystemResourceListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean
					.getRbacBusinessSystemResourceListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacBusinessSystemResource() {

		this.bean.getRbacBusinessSystemCode().setValue("");

		this.bean.getRbacBusinessSystemName().setValue("");

		this.bean.getRbacResourceCode().setValue("");

		this.bean.getRbacResourceName().setValue("");

	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDelete = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDelete = true;
		} else if ("rbacResourceTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_BUSINESS_SYSTEM_RESOURCE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_BUSINESS_SYSTEM_RESOURCE_DEL)) {
				canDelete = true;
			}
		} else if ("rbacBusinessSystemTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_BUSINESS_SYSTEM_RESOURCE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_BUSINESS_SYSTEM_RESOURCE_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacBusinessSystemResourceButton().setVisible(canAdd);
		this.bean.getDelRbacBusinessSystemResourceButton()
				.setVisible(canDelete);
	}

}
