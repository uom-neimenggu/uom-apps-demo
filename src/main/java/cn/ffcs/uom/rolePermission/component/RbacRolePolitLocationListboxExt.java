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
import cn.ffcs.uom.rolePermission.component.bean.RbacRolePolitLocationListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRolePolitLocationManager;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public class RbacRolePolitLocationListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacRolePolitLocationListboxExtBean bean = new RbacRolePolitLocationListboxExtBean();

	/**
	 * 角色树上选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的关系
	 */
	private RbacRolePolitLocation rbacRolePolitLocation;

	/**
	 * 查询queryRbacRolePolitLocation.
	 */
	private RbacRolePolitLocation queryRbacRolePolitLocation;

	@SuppressWarnings("unused")
	private String opType;

	/**
	 * 角色行政管理区域
	 */
	private RbacRolePolitLocationManager rbacRolePolitLocationManager = (RbacRolePolitLocationManager) ApplicationContextUtil
			.getBean("rbacRolePolitLocationManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRolePolitLocationListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_role_polit_location_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_ROLE_POLIT_LOCATION_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_ROLE_POLIT_LOCATION_QUERY_RESPONSE);
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
	 * 查询行政管理区域列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRolePolitLocationQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getLocationCode().setValue("");
		this.bean.getLocationName().setValue("");

		this.rbacRoleRelation = (RbacRoleRelation) event.getOrigin().getData();
		this.onQueryRbacRolePolitLocation();
		this.setButtonValid(true, false, false);
	}

	/**
	 * 行政管理区域选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRolePolitLocationSelectRequest() throws Exception {
		if (bean.getRbacRolePolitLocationListbox().getSelectedCount() > 0) {
			rbacRolePolitLocation = (RbacRolePolitLocation) bean
					.getRbacRolePolitLocationListbox().getSelectedItem()
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
	public void onRbacRolePolitLocationListPaging() throws Exception {
		this.queryRbacRolePolitLocation();
	}

	/**
	 * 新增角色行政管理区域层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRolePolitLocationAdd() throws Exception {
		this.openRbacRolePolitLocationEditWin("add");
	}

	/**
	 * 修改角色行政管理区域层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRolePolitLocationEdit() throws Exception {
		this.openRbacRolePolitLocationEditWin("mod");
	}

	/**
	 * 打开行政管理区域编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacRolePolitLocationEditWin(String opType)
			throws Exception {

		Map arg = new HashMap();
		this.opType = opType;
		arg.put("opType", opType);
		arg.put("rbacRoleRelation", rbacRoleRelation);
		if ("mod".equals(opType)) {
			arg.put("rbacRolePolitLocation", rbacRolePolitLocation);
		}
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_polit_location_edit.zul",
				this, arg);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacRolePolitLocation();
				}
			}
		});
	}

	/**
	 * 删除角色行政管理区域关系
	 */
	public void onRbacRolePolitLocationDel() {
		if (this.rbacRolePolitLocation != null
				&& rbacRolePolitLocation.getRbacRolePolitLocationId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacRolePolitLocationManager
								.removeRbacRolePolitLocation(rbacRolePolitLocation);
						PubUtil.reDisplayListbox(
								bean.getRbacRolePolitLocationListbox(),
								rbacRolePolitLocation, "del");
						rbacRolePolitLocation = null;
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
	 * 设置角色行政管理区域按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canEdit,
			final Boolean canDel) {
		this.bean.getAddRbacRolePolitLocationButton().setDisabled(!canAdd);
		this.bean.getEditRbacRolePolitLocationButton().setDisabled(!canEdit);
		this.bean.getDelRbacRolePolitLocationButton().setDisabled(!canDel);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacRolePolitLocation() {
		this.bean.getRbacRolePolitLocationListPaging().setActivePage(0);
		this.queryRbacRolePolitLocation();
	}

	/**
	 * 查询角色行政管理区域.
	 */
	private void queryRbacRolePolitLocation() {
		if (this.rbacRoleRelation != null) {
			queryRbacRolePolitLocation = new RbacRolePolitLocation();
			queryRbacRolePolitLocation.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());
			queryRbacRolePolitLocation.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacRolePolitLocation.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());
			queryRbacRolePolitLocation.setLocationCode(this.bean
					.getLocationCode().getValue());
			queryRbacRolePolitLocation.setLocationName(this.bean
					.getLocationName().getValue());

			PageInfo pageInfo = rbacRolePolitLocationManager
					.queryPageInfoRbacRolePolitLocation(
							queryRbacRolePolitLocation, this.bean
									.getRbacRolePolitLocationListPaging()
									.getActivePage() + 1, this.bean
									.getRbacRolePolitLocationListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacRolePolitLocationListbox().setModel(dataList);
			this.bean.getRbacRolePolitLocationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacRolePolitLocationListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacRolePolitLocation() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

		this.bean.getLocationCode().setValue("");

		this.bean.getLocationName().setValue("");

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

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDelete = true;
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_POLIT_LOCATION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_POLIT_LOCATION_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_POLIT_LOCATION_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacRolePolitLocationButton().setVisible(canAdd);
		this.bean.getEditRbacRolePolitLocationButton().setVisible(canEdit);
		this.bean.getDelRbacRolePolitLocationButton().setVisible(canDelete);
	}

}
