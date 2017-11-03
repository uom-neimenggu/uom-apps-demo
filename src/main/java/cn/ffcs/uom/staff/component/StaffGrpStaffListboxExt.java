package cn.ffcs.uom.staff.component;

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
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.staff.component.bean.StaffGrpStaffListboxExtBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffGrpStaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffGrpStaff;

public class StaffGrpStaffListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private StaffGrpStaffListboxExtBean bean = new StaffGrpStaffListboxExtBean();
	/**
	 * 选中的员工
	 */
	private Staff staff;
	/**
	 * 查询集团员工
	 */
	private StaffGrpStaff queryStaffGrpStaff;

	/**
	 * 选中的关系
	 */
	private StaffGrpStaff staffGrpStaff;

	/**
	 * 员工业务
	 */
	private StaffGrpStaffManager staffGrpStaffManager = (StaffGrpStaffManager) ApplicationContextUtil
			.getBean("staffGrpStaffManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public StaffGrpStaffListboxExt() {
		Executions
				.createComponents(
						"/pages/staff/comp/staff_grp_staff_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_GRP_STAFF_QUERY, this,
				"onSelectStaffResponse");
		this.addForward(SffOrPtyCtants.ON_CLEAN_STAFF_GRP_STAFF, this,
				SffOrPtyCtants.ON_CLEAN_STAFF_GRP_STAFF_RES);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false);
	}

	/**
	 * 选择员工响应
	 */
	public void onSelectStaffResponse(ForwardEvent event) throws Exception {
		staff = (Staff) event.getOrigin().getData();
		if (staff != null) {
			this.setButtonValid(true, false);
			this.bean.getStaffName().setValue("");
			this.bean.getSalesCode().setValue("");
			this.bean.getUserName().setValue("");
			this.bean.getLoginName().setValue("");
			this.onQueryStaffGrpStaffList();
		}
	}

	/**
	 * 查询
	 */
	public void onQueryStaffGrpStaffList() throws Exception {

		queryStaffGrpStaff = new StaffGrpStaff();
		queryStaffGrpStaff.setStaffName(this.bean.getStaffName().getValue());
		queryStaffGrpStaff.setSalesCode(this.bean.getSalesCode().getValue());
		queryStaffGrpStaff.setStaffName(this.bean.getUserName().getValue());
		queryStaffGrpStaff.setStaffName(this.bean.getLoginName().getValue());

		if (staff != null && staff.getStaffId() != null) {
			queryStaffGrpStaff.setStaffId(staff.getStaffId());
		}

		PageInfo pageInfo = staffGrpStaffManager.queryPageInfoByStaffGrpStaff(
				queryStaffGrpStaff, this.bean.getStaffGrpStaffListboxPaging()
						.getActivePage() + 1, this.bean
						.getStaffGrpStaffListboxPaging().getPageSize());
		ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getStaffGrpStaffListbox().setModel(list);
		this.bean.getStaffGrpStaffListboxPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffGrpStaff() throws Exception {
		this.bean.getStaffGrpStaffListboxPaging().setActivePage(0);
		this.onQueryStaffGrpStaffList();

	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onStaffGrpStaffListboxPaging() throws Exception {
		this.onQueryStaffGrpStaffList();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffGrpStaff() throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getSalesCode().setValue("");
		this.bean.getUserName().setValue("");
		this.bean.getLoginName().setValue("");
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onAddStaffGrpStaff() throws Exception {
		if (staff != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("opType", "add");
			map.put("staff", staff);
			Window window = (Window) Executions.createComponents(
					"/pages/staff/comp/staff_grp_staff_edit.zul", this, map);
			window.doModal();
			window.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						// 新增或修改完成后，列表从数据库更新
						StaffGrpStaff staffGrpStaff = (StaffGrpStaff) event
								.getData();
						bean.getStaffName().setValue(staff.getStaffName());
						bean.getSalesCode().setValue(
								staffGrpStaff.getSalesCode());
						onQueryStaffGrpStaffList();

					}
				}
			});
		}
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onDelStaffGrpStaff() throws Exception {
		if (this.staffGrpStaff != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						staffGrpStaffManager.removeStaffGrpStaff(staffGrpStaff);
						PubUtil.reDisplayListbox(
								bean.getStaffGrpStaffListbox(), staffGrpStaff,
								"del");
						staffGrpStaff = null;
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
	 * 设置按钮
	 * 
	 * @param canAdd
	 * @param candel
	 * @throws Exception
	 */
	private void setButtonValid(boolean canAdd, boolean candel) {
		this.bean.getAddButton().setDisabled(!canAdd);
		this.bean.getDelButton().setDisabled(!candel);
	}

	/**
	 * 选择列表
	 */
	public void onSelectStaffGrpStaffList() throws Exception {
		this.setButtonValid(true, true);
		staffGrpStaff = (StaffGrpStaff) this.bean.getStaffGrpStaffListbox()
				.getSelectedItem().getValue();
	}

	/**
	 * @author 朱林涛
	 */
	public void onCleanStaffGrpStaffRespons(final ForwardEvent event) {
		this.bean.getStaffName().setValue("");
		this.bean.getSalesCode().setValue("");
		this.bean.getUserName().setValue("");
		this.bean.getLoginName().setValue("");
		staffGrpStaff = null;
		staff = null;
		ListboxUtils.clearListbox(bean.getStaffGrpStaffListbox());
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRP_STAFF_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRP_STAFF_DEL)) {
				canDel = true;
			}
		}

		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}

}
