package cn.ffcs.uom.gridUnit.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.gridUnit.action.bean.GridUnitMainBean;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 网格管理实体Bean .
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
public class GridUnitMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private GridUnitMainBean bean = new GridUnitMainBean();

	/**
	 * gridUnit.
	 */
	private GridUnit gridUnit;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getGridUnitListboxExt().setPortletInfoProvider(this);
		bean.getStaffGridUnitTranListboxExt().setPortletInfoProvider(this);
		bean.getGridUnitListboxExt().addForward(
				OrganizationConstant.ON_GIRD_UNIT_SELECT, comp,
				"onSelectGridUnitResponse");
		bean.getGridUnitListboxExt().addForward(
				OrganizationConstant.ON_GIRD_UNIT_NOT_SELECT, comp,
				"onSelectGridUnitResponse");
		bean.getGridUnitListboxExt().addForward(
				OrganizationConstant.ON_GIRD_UNIT_CLEAR_TABS, comp,
				OrganizationConstant.ON_GIRD_UNIT_CLEAR_TABS_RESPONS);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$gridUnitMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择员工列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onSelectGridUnitResponse(final ForwardEvent event)
			throws Exception {
		gridUnit = (GridUnit) event.getOrigin().getData();
		callTab();
	}

	/**
	 * tab响应
	 * 
	 * @throws Exception
	 * 
	 */
	public void callTab() {
		try {
			if (bean.getTab() == null) {
				bean.setTab(bean.getTabBox().getSelectedTab());
			}

			String tab = this.bean.getTab().getId();

			if (gridUnit != null) {

				if ("gridUnitRefTab".equals(tab)) {
					this.bean.getGridUnitRefListboxExt()
							.setVariablePagePosition("gridUnitTab");
					GridUnitRef gridUnitRef = new GridUnitRef();
					gridUnitRef.setOrgId(gridUnit.getGridUnitId());
					Events.postEvent(
							OrganizationConstant.ON_GRID_UNIT_REF_QUERY,
							this.bean.getGridUnitRefListboxExt(), gridUnitRef);
				} else if ("staffGridUnitTranTab".equals(tab)) {
					Events.postEvent(
							SffOrPtyCtants.ON_STAFF_GRID_UNIT_TRAN_SELECT,
							bean.getStaffGridUnitTranListboxExt(), gridUnit);
				}

			} else {

				if ("gridUnitRefTab".equals(tab)) {
					this.bean.getGridUnitRefListboxExt()
							.setVariablePagePosition("gridUnitTab");
					Events.postEvent(
							OrganizationConstant.ON_GRID_UNIT_REF_QUERY,
							this.bean.getGridUnitRefListboxExt(), null);

				} else if ("staffGridUnitTranTab".equals(tab)) {
					bean.getStaffGridUnitTranListboxExt()
							.onCleaningStaffGridUnitTran();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickTab(ForwardEvent event) {
		try {
			Event origin = event.getOrigin();
			if (origin != null) {
				Component comp = origin.getTarget();
				if (comp != null && comp instanceof Tab) {
					bean.setTab((Tab) comp);
					callTab();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理点击后Tabs .
	 * 
	 * @param event
	 */
	public void onGridUnitClearTabsRespons(final ForwardEvent event) {
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFF_GRID_UNIT_TRAN,
				bean.getStaffGridUnitTranListboxExt(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getStaffGridUnitTranListboxExt().setPagePosition(
				"gridUnitPage");
	}
}
