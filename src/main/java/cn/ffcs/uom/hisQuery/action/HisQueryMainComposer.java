package cn.ffcs.uom.hisQuery.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.hisQuery.action.bean.HisQueryMainBean;
import cn.ffcs.uom.hisQuery.manager.OrgHisManager;
import cn.ffcs.uom.hisQuery.manager.OrgRelaHisManager;
import cn.ffcs.uom.hisQuery.manager.PositionHisManager;
import cn.ffcs.uom.hisQuery.manager.StaffHisManager;

import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 历史查询.
 * 
 * @author
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class HisQueryMainComposer extends BasePortletComposer {
	/**
	 * 页面bean
	 */
	private HisQueryMainBean bean = new HisQueryMainBean();

	private OrgHisManager orgHisManager = (OrgHisManager) ApplicationContextUtil
			.getBean("orgHisManager");

	private PositionHisManager positionHisManager = (PositionHisManager) ApplicationContextUtil
			.getBean("positionHisManager");

	private StaffHisManager staffHisManager = (StaffHisManager) ApplicationContextUtil
			.getBean("staffHisManager");

	private OrgRelaHisManager orgRelaHisManager = (OrgRelaHisManager) ApplicationContextUtil
			.getBean("orgRelaHisManager");

	/**
	 * 查询时间
	 */
	private Date queryDate;

	/**
	 * 组织id
	 */
	private Long orgId;
	/**
	 * 生效时间
	 */
	private Date effDate;
	/**
	 * 失效时间
	 */
	private Date expDate;
	/**
	 * 岗位标识
	 */
	private Long positionId;
	/**
	 * 员工标识
	 */
	private Long staffId;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$hisQueryWin() throws Exception {
		this.bindCombobox();
		callTab();
	}

	/**
	 * 绑定combobox.
	 * 
	 */
	private void bindCombobox() throws Exception {
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 */
	public void onQueryHis() throws Exception {
		if (this.bean.getTab() == null) {
			bean.setTab(this.bean.getTabBox().getSelectedTab());
		}
		String tabId = this.bean.getTab().getId();
		if ("orgHisTab".equals(tabId)) {
			this.bean.getOrgListboxPaging().setActivePage(0);
		}
		if ("positionHisTab".equals(tabId)) {
			this.bean.getPositionListboxPaging().setActivePage(0);
		}
		if ("staffHisTab".equals(tabId)) {
			this.bean.getStaffListboxPaging().setActivePage(0);
		}
		if ("orgRelaBsHisTab".equals(tabId)) {
			this.bean.getOrgBsRelaListboxPaging().setActivePage(0);
		}
		if (this.bean.getQueryDate() != null
				&& this.bean.getQueryDate().getValue() != null) {
			queryDate = this.bean.getQueryDate().getValue();
		} else {
			queryDate = null;
		}
		if (this.bean.getOrgId() != null
				&& this.bean.getOrgId().getValue() != null) {
			orgId = this.bean.getOrgId().getValue();
		} else {
			orgId = null;
		}
		callTab();
	}

	/**
	 * 重置
	 * 
	 * @throws Exception
	 */
	public void onQueryHisReset() throws Exception {
		this.bean.getOrgId().setValue(null);
		this.bean.getQueryDate().setValue(null);
		this.orgId = null;
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffListboxPaging() throws Exception {
		callTab();
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrgListboxPaging() throws Exception {
		callTab();
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionListboxPaging() throws Exception {
		callTab();
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrgBsRelaListboxPaging() throws Exception {
		callTab();
	}

	/**
	 * 选择关联类型
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelect$relaCd(final ForwardEvent event) throws Exception {
	}

	/**
	 * 选择组织类型
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelect$organizationRelationHisTree(final ForwardEvent event)
			throws Exception {
	}

	/**
	 * 点击tab页面
	 * 
	 * @param forwardEvent
	 * @throws Exception
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setTab(clickTab);
				callTab();
			}
		}
	}

	/**
	 * tab页响应
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {

		if (this.bean.getTab() == null) {
			bean.setTab(this.bean.getTabBox().getSelectedTab());
		}
		if (this.bean.getQueryDate() != null
				&& this.bean.getQueryDate().getValue() != null) {
			queryDate = this.bean.getQueryDate().getValue();
		}
		if (this.bean.getOrgId() != null
				&& this.bean.getOrgId().getValue() != null) {
			orgId = this.bean.getOrgId().getValue();
		}
		String tabId = this.bean.getTab().getId();

		Map<String, Object> parmsMap = new HashMap<String, Object>();
		parmsMap.put("queryDate", queryDate);
		parmsMap.put("orgId", orgId);
		if ("orgHisTab".equals(tabId)) {
			PageInfo pageInfo = orgHisManager.queryOrgHisPageInfoByParams(
					parmsMap,
					this.bean.getOrgListboxPaging().getActivePage() + 1,
					this.bean.getOrgListboxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrgHisListbox().setModel(dataList);
			this.bean.getOrgListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		if ("positionHisTab".equals(tabId)) {
			PageInfo pageInfo = positionHisManager
					.queryPositionHisPageInfoByParams(parmsMap, this.bean
							.getPositionListboxPaging().getActivePage() + 1,
							this.bean.getPositionListboxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getPositionHisListbox().setModel(dataList);
			this.bean.getPositionListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		if ("staffHisTab".equals(tabId)) {
			PageInfo pageInfo = staffHisManager.queryStaffHisPageInfoByParams(
					parmsMap,
					this.bean.getStaffListboxPaging().getActivePage() + 1,
					this.bean.getStaffListboxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getStaffHisListbox().setModel(dataList);
			this.bean.getStaffListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		if ("orgRelaBsHisTab".equals(tabId)) {
			PageInfo pageInfo = orgRelaHisManager.queryOrgRelaPageInfoByParams(
					parmsMap, this.bean.getOrgBsRelaListboxPaging()
							.getActivePage() + 1, this.bean
							.getOrgBsRelaListboxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrgBsRelaHisListbox().setModel(dataList);
			this.bean.getOrgBsRelaListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}

	}

	/**
	 * 点击组织事件
	 * 
	 * @throws Exception
	 */
	public void onClickOrg() throws Exception {
		if (this.bean.getOrgHisListbox().getSelectedIndex() != -1) {
			this.bean.getShowOrgDetailBtn().setDisabled(false);
			orgId = ((Organization) this.bean.getOrgHisListbox()
					.getSelectedItem().getValue()).getOrgId();
			effDate = ((Organization) this.bean.getOrgHisListbox()
					.getSelectedItem().getValue()).getEffDate();
			expDate = ((Organization) this.bean.getOrgHisListbox()
					.getSelectedItem().getValue()).getExpDate();
		}

	}

	/**
	 * 查询当前组织详细信息
	 * 
	 * @throws Exception
	 */
	public void onQueryOrgDetailRequest() throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		if (orgId != null) {
			arg.put("effDate", effDate);
			arg.put("orgId", orgId);
			arg.put("expDate", expDate);
			Window win = (Window) Executions.createComponents(
					"/pages/hisQuery/organization_detail.zul", this.self, arg);
			win.doModal();

			win.addEventListener("onOk", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					queryOrg();
				}
			});
		}
	}

	/**
	 * 查询组织信息
	 * 
	 * @throws Exception
	 */
	public void queryOrg() throws Exception {

		Map<String, Object> parmsMap = new HashMap<String, Object>();
		orgId = null;
		parmsMap.put("queryDate", queryDate);
		PageInfo pageInfo = orgHisManager.queryOrgHisPageInfoByParams(parmsMap,
				this.bean.getOrgListboxPaging().getActivePage() + 1, this.bean
						.getOrgListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgHisListbox().setModel(dataList);
		this.bean.getOrgListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));

	}

	/**
	 * 点击岗位
	 * 
	 * @throws Exception
	 */
	public void onClickPosition() throws Exception {
		if (this.bean.getPositionHisListbox().getSelectedIndex() != -1) {
			this.bean.getShowPositionetailBtn().setDisabled(false);
			positionId = ((Position) this.bean.getPositionHisListbox()
					.getSelectedItem().getValue()).getPositionId();
			effDate = ((Position) this.bean.getPositionHisListbox()
					.getSelectedItem().getValue()).getEffDate();
			expDate = ((Position) this.bean.getPositionHisListbox()
					.getSelectedItem().getValue()).getExpDate();
		}
	}

	/**
	 * 查询当前岗位详细信息
	 * 
	 * @throws Exception
	 */
	public void onQueryPositionDetailRequest() throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		if (positionId != null) {
			arg.put("effDate", effDate);
			arg.put("positionId", positionId);
			arg.put("expDate", expDate);
			Window win = (Window) Executions.createComponents(
					"/pages/hisQuery/position_detail.zul", this.self, arg);
			win.doModal();

			win.addEventListener("onOk", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					queryPostion();
				}
			});
		}

	}

	/**
	 * 查询岗位信息
	 * 
	 * @throws Exception
	 */
	public void queryPostion() throws Exception {

		Map<String, Object> parmsMap = new HashMap<String, Object>();
		positionId = null;
		parmsMap.put("orgId", orgId);
		parmsMap.put("queryDate", queryDate);
		PageInfo pageInfo = positionHisManager
				.queryPositionHisPageInfoByParams(parmsMap, this.bean
						.getPositionListboxPaging().getActivePage() + 1,
						this.bean.getPositionListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getPositionHisListbox().setModel(dataList);
		this.bean.getPositionListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));

	}

	/**
	 * 点击员工
	 * 
	 * @throws Exception
	 */
	public void onClickStaff() throws Exception {
		if (this.bean.getStaffHisListbox().getSelectedIndex() != -1) {
			this.bean.getShowStaffDetailBtn().setDisabled(false);
			staffId = ((Staff) this.bean.getStaffHisListbox().getSelectedItem()
					.getValue()).getStaffId();
			effDate = ((Staff) this.bean.getStaffHisListbox().getSelectedItem()
					.getValue()).getEffDate();
			expDate = ((Staff) this.bean.getStaffHisListbox().getSelectedItem()
					.getValue()).getExpDate();
		}
	}

	/**
	 * 查询当前员工详细信息
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffDetailRequest() throws Exception {

		Map<String, Object> arg = new HashMap<String, Object>();
		if (staffId != null) {
			arg.put("effDate", effDate);
			arg.put("staffId", staffId);
			arg.put("expDate", expDate);
			Window win = (Window) Executions.createComponents(
					"/pages/hisQuery/staff_detail.zul", this.self, arg);
			win.doModal();

			win.addEventListener("onOk", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					queryStaff();
				}
			});
		}

	}

	/**
	 * 查询员工信息
	 * 
	 * @throws Exception
	 */
	public void queryStaff() throws Exception {
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		parmsMap.put("orgId", orgId);
		parmsMap.put("queryDate", queryDate);
		PageInfo pageInfo = staffHisManager.queryStaffHisPageInfoByParams(
				parmsMap,
				this.bean.getStaffListboxPaging().getActivePage() + 1,
				this.bean.getStaffListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffHisListbox().setModel(dataList);
		this.bean.getStaffListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 点击组织业务
	 * 
	 * @throws Exception
	 */
	public void onClickOrgBsRela() throws Exception {

	}

	/**
	 * 查询当前组织业务详细信息
	 * 
	 * @throws Exception
	 */
	public void onQueryOrgBsRelaDetailRequest() throws Exception {

	}

	/**
	 * 查询组织业务
	 * 
	 * @throws Exception
	 */
	public void queryOrgBsRela() throws Exception {

	}

}
