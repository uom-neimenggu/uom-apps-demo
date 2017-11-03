package cn.ffcs.uom.gridUnit.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Paging;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.gridUnit.component.bean.GridUnitRefListboxExtBean;
import cn.ffcs.uom.gridUnit.manager.GridUnitRefManager;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 省内组织业务关系
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-14
 * @功能说明：
 * 
 */
public class GridUnitRefListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private GridUnitRefManager gridUnitRefManager = (GridUnitRefManager) ApplicationContextUtil
			.getBean("gridUnitRefManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Getter
	@Setter
	private GridUnitRefListboxExtBean bean = new GridUnitRefListboxExtBean();

	/**
	 * 是否是推导树
	 */
	@Getter
	private boolean isDuceTree = false;
	/**
	 * 是否是组织树页面
	 */
	@Getter
	@Setter
	private Boolean isOrgTreePage = false;

	/**
	 * 是否是全息网格单元tab
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;

	/**
	 * 全息网格单元区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;

	// 选中的组织业务关系
	private GridUnitRef gridUnitRef;

	/**
	 * 组织树中当前选择的organization
	 */
	private Organization organization;

	// 查询
	private GridUnitRef queryGridUnitRef;

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		this.isDuceTree = isDuceTree;
	}

	public GridUnitRefListboxExt() {
		Executions.createComponents(
				"/pages/gridUnit/comp/grid_unit_ref_listbox_ext.zul", this,
				null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 查询组织关系
		 */
		this.addForward(OrganizationConstant.ON_GRID_UNIT_REF_QUERY, this,
				"onSelectOrganizationResponse");
	}

	public void onCreate() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			} else {
				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
	}

	/**
	 * 选择组织响应事件
	 * 
	 * @param event
	 */
	public void onSelectOrganizationResponse(ForwardEvent event)
			throws Exception {

		gridUnitRef = (GridUnitRef) event.getOrigin().getData();

		if (!StrUtil.isNullOrEmpty(gridUnitRef)) {

			organization = new Organization();

			if ("gridUnitTreeTab".equals(variableOrgTreeTabName)) {

				organization.setOrgId(gridUnitRef.getRelaOrgId());

			} else if ("gridUnitTab".equals(variablePagePosition)) {

				organization.setOrgId(gridUnitRef.getOrgId());

			}

		}

		if (organization != null && organization.getOrgId() != null) {

			this.onQueryGridUnitRef();

		} else {

			/**
			 * 组织树未选择组织清理数据
			 */
			ListboxUtils.clearListbox(this.bean.getGridUnitRefListbox());
		}

	}

	public void onSelectGridUnitRefRequest() {
		if (bean.getGridUnitRefListbox().getSelectedCount() > 0) {

			gridUnitRef = (GridUnitRef) bean.getGridUnitRefListbox()
					.getSelectedItem().getValue();
			Events.postEvent(OrganizationConstant.ON_GIRD_UNIT_REF_SELECT,
					this, gridUnitRef);
		}
	}

	/**
	 * 清空选中的组织业务关系 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onCleaningGridUnitRef() throws Exception {
		ListboxUtils.clearListbox(bean.getGridUnitRefListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author 朱林涛
	 */
	public void onGridUnitRefListboxPaging() {
		queryGridUnitRefListboxForPaging();
	}

	public void queryGridUnitRefListboxForPaging() {
		try {

			if (queryGridUnitRef != null) {

				/**
				 * 默认数据权最大电信管理区域
				 */
				if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
					permissionTelcomRegion = new TelcomRegion();
					permissionTelcomRegion
							.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
				}

				queryGridUnitRef
						.setPermissionTelcomRegion(permissionTelcomRegion);

				Paging paging = bean.getGridUnitRefListboxPaging();
				PageInfo pageInfo = gridUnitRefManager
						.queryPageInfoByGridUnitRef(queryGridUnitRef,
								paging.getActivePage() + 1,
								paging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getGridUnitRefListbox().setModel(list);
				bean.getGridUnitRefListboxPaging().setTotalSize(
						pageInfo.getTotalCount());
			}

		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryGridUnitRef() {

		queryGridUnitRef = new GridUnitRef();

		PubUtil.fillPoFromBean(bean, queryGridUnitRef);

		if (!StrUtil.isNullOrEmpty(organization)) {

			if ("gridUnitTreeTab".equals(variableOrgTreeTabName)) {

				queryGridUnitRef.setRelaOrgId(organization.getOrgId());

			} else if ("gridUnitTab".equals(variablePagePosition)) {

				if (gridUnitRef != null && gridUnitRef.getOrgId() != null) {
					queryGridUnitRef.setOrgId(gridUnitRef.getOrgId());
				}

			}

		} else {// 组织树上未选择组织时，不允许查询
			return;
		}

		this.bean.getGridUnitRefListboxPaging().setActivePage(0);
		this.queryGridUnitRefListboxForPaging();
		Events.postEvent(OrganizationConstant.ON_GIRD_UNIT_REF_SELECT, this,
				null);// 清空员工组织业务关系列表

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGridUnitRef() throws Exception {
		this.bean.getOrgCode().setValue(null);
		this.bean.getOrgName().setValue(null);
		this.bean.getMmeFid().setValue(null);
		this.bean.getGridName().setValue(null);
	}

}
