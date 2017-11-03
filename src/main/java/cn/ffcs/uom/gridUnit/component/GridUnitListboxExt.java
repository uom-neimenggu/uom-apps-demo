package cn.ffcs.uom.gridUnit.component;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
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
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.gridUnit.component.bean.GridUnitListboxExtBean;
import cn.ffcs.uom.gridUnit.manager.GridUnitManager;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 网格单元管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-22
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class GridUnitListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private GridUnitListboxExtBean bean = new GridUnitListboxExtBean();

	private GridUnitManager gridUnitManager = (GridUnitManager) ApplicationContextUtil
			.getBean("gridUnitManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/gridUnit/comp/grid_unit_listbox_ext.zul";

	/**
	 * 选中的gridUnit.
	 */
	private GridUnit gridUnit;

	/**
	 * 查询gridUnit.
	 */
	private GridUnit queryGridUnit;

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public GridUnitListboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
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
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryGridUnit() throws Exception {
		this.bean.getGridUnitListboxPaging().setActivePage(0);
		this.onGridUnitListboxPaging();
		Events.postEvent(OrganizationConstant.ON_GIRD_UNIT_NOT_SELECT, this,
				null);// 清空员工组织业务关系列表
	}

	public void onGridUnitListboxPaging() {
		try {

			queryGridUnit = new GridUnit();
			queryGridUnit.setMmeFid(bean.getMmeFid().getValue());
			queryGridUnit.setGridName(bean.getGridName().getValue());

			/**
			 * 默认数据权最大电信管理区域
			 */
			if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			}

			queryGridUnit.setPermissionTelcomRegion(permissionTelcomRegion);

			if (this.queryGridUnit != null) {
				Paging paging = bean.getGridUnitListboxPaging();
				PageInfo pageInfo = gridUnitManager.queryPageInfoByGridUnit(
						queryGridUnit, paging.getActivePage() + 1,
						paging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getGridUnitListbox().setModel(list);
				bean.getGridUnitListboxPaging().setTotalSize(
						pageInfo.getTotalCount());
			} else {
				ListboxUtils.clearListbox(bean.getGridUnitListbox());
			}
			gridUnit = null;
		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 网格选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSelectGridUnitRequest() throws Exception {
		if (bean.getGridUnitListbox().getSelectedCount() > 0) {
			gridUnit = (GridUnit) bean.getGridUnitListbox().getSelectedItem()
					.getValue();
			Events.postEvent(OrganizationConstant.ON_GIRD_UNIT_SELECT, this,
					gridUnit);
		}
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onResetGridUnit() throws Exception {
		bean.getMmeFid().setValue(null);
		bean.getGridName().setValue(null);
	}

}
