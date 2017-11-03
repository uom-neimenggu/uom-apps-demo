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
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleTelcomRegionListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleTelcomRegionManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;

public class RbacRoleTelcomRegionListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacRoleTelcomRegionListboxExtBean bean = new RbacRoleTelcomRegionListboxExtBean();

	/**
	 * 角色树上选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的关系
	 */
	private RbacRoleTelcomRegion rbacRoleTelcomRegion;

	/**
	 * 查询queryRbacRoleTelcomRegion.
	 */
	private RbacRoleTelcomRegion queryRbacRoleTelcomRegion;

	@SuppressWarnings("unused")
	private String opType;

	/**
	 * 角色电信管理区域
	 */
	private RbacRoleTelcomRegionManager rbacRoleTelcomRegionManager = (RbacRoleTelcomRegionManager) ApplicationContextUtil
			.getBean("rbacRoleTelcomRegionManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRoleTelcomRegionListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_role_telcom_region_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_ROLE_TELCOM_REGION_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_ROLE_TELCOM_REGION_QUERY_RESPONSE);
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
	 * 查询电信管理区域列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleTelcomRegionQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getRegionCode().setValue("");
		this.bean.getRegionName().setValue("");

		this.rbacRoleRelation = (RbacRoleRelation) event.getOrigin().getData();
		this.onQueryRbacRoleTelcomRegion();
		this.setButtonValid(true, false, false);
	}

	/**
	 * 电信管理区域选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleTelcomRegionSelectRequest() throws Exception {
		if (bean.getRbacRoleTelcomRegionListbox().getSelectedCount() > 0) {
			rbacRoleTelcomRegion = (RbacRoleTelcomRegion) bean
					.getRbacRoleTelcomRegionListbox().getSelectedItem()
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
	public void onRbacRoleTelcomRegionListPaging() throws Exception {
		this.queryRbacRoleTelcomRegion();
	}

	/**
	 * 新增角色电信管理区域层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRoleTelcomRegionAdd() throws Exception {
		this.openRbacRoleTelcomRegionEditWin("add");
	}

	/**
	 * 修改角色电信管理区域层级关系
	 * 
	 * @throws Exception
	 */
	public void onRbacRoleTelcomRegionEdit() throws Exception {
		this.openRbacRoleTelcomRegionEditWin("mod");
	}

	/**
	 * 打开电信管理区域编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacRoleTelcomRegionEditWin(String opType)
			throws Exception {

		Map arg = new HashMap();
		this.opType = opType;
		arg.put("opType", opType);
		arg.put("rbacRoleRelation", rbacRoleRelation);
		if ("mod".equals(opType)) {
			arg.put("rbacRoleTelcomRegion", rbacRoleTelcomRegion);
		}
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_telcom_region_edit.zul", this,
				arg);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacRoleTelcomRegion();
				}
			}
		});
	}

	/**
	 * 删除角色电信管理区域关系
	 */
	public void onRbacRoleTelcomRegionDel() {
		if (this.rbacRoleTelcomRegion != null
				&& rbacRoleTelcomRegion.getRbacRoleTelcomRegionId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacRoleTelcomRegionManager
								.removeRbacRoleTelcomRegion(rbacRoleTelcomRegion);
						PubUtil.reDisplayListbox(
								bean.getRbacRoleTelcomRegionListbox(),
								rbacRoleTelcomRegion, "del");
						rbacRoleTelcomRegionManager
								.removeRbacRoleTelcomRegionToRaptornuke(rbacRoleTelcomRegion);
						rbacRoleTelcomRegion = null;
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
	 * 设置角色电信管理区域按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canEdit,
			final Boolean canDel) {
		this.bean.getAddRbacRoleTelcomRegionButton().setDisabled(!canAdd);
		// this.bean.getEditRbacRoleTelcomRegionButton().setDisabled(!canEdit);
		this.bean.getDelRbacRoleTelcomRegionButton().setDisabled(!canDel);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacRoleTelcomRegion() {
		this.bean.getRbacRoleTelcomRegionListPaging().setActivePage(0);
		this.queryRbacRoleTelcomRegion();
	}

	/**
	 * 查询角色电信管理区域.
	 */
	private void queryRbacRoleTelcomRegion() {
		if (this.rbacRoleRelation != null) {
			queryRbacRoleTelcomRegion = new RbacRoleTelcomRegion();
			queryRbacRoleTelcomRegion.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());
			queryRbacRoleTelcomRegion.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacRoleTelcomRegion.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());
			queryRbacRoleTelcomRegion.setRegionCode(this.bean.getRegionCode()
					.getValue());
			queryRbacRoleTelcomRegion.setRegionName(this.bean.getRegionName()
					.getValue());

			PageInfo pageInfo = rbacRoleTelcomRegionManager
					.queryPageInfoRbacRoleTelcomRegion(
							queryRbacRoleTelcomRegion, this.bean
									.getRbacRoleTelcomRegionListPaging()
									.getActivePage() + 1, this.bean
									.getRbacRoleTelcomRegionListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacRoleTelcomRegionListbox().setModel(dataList);
			this.bean.getRbacRoleTelcomRegionListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacRoleTelcomRegionListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacRoleTelcomRegion() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

		this.bean.getRegionCode().setValue("");

		this.bean.getRegionName().setValue("");

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
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TELCOM_REGION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TELCOM_REGION_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacRoleTelcomRegionButton().setVisible(canAdd);
		this.bean.getDelRbacRoleTelcomRegionButton().setVisible(canDelete);
	}

}
