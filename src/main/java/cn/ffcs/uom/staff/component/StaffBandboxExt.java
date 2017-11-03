package cn.ffcs.uom.staff.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbar;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.component.OrganizationRelationTreeBandboxExt;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 员工Bandbox .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-8
 * @功能说明：
 * 
 */

public class StaffBandboxExt extends Bandbox implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private StaffListboxExt staffListboxExt;

	private Listbox staffListbox;

	private Staff staff;

	/*
	 * 选中的员工
	 */
	@Getter
	private List<Staff> staffs;

	/**
	 * 组织下的员工
	 */
	@Getter
	private List<Staff> staffsOfOrganization;

	/**
	 * 当前选择的员工
	 */
	private List<Staff> staffsOfOrganizationTemp;
	/**
	 * 组织下单页的员工
	 */
	@Getter
	private List<Staff> staffsOfOrganizationPage;

	private Toolbar staffBandboxToolbar;

	private Button okButton;

	private Button cancelButton;

	private boolean isMultiple = false;

	@Getter
	private OrganizationRelationTreeBandboxExt organizationRelationTreeBandboxExt;

	/**
	 * 当前页的Listitem
	 */
	private List<Listitem> currentListitem;

	private boolean isLoaded = false;

	public StaffBandboxExt() {
		Executions.createComponents(SffOrPtyCtants.ZUL_STAFF_BANDBOX_EXT, this,
				null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');

		staffListboxExt = (StaffListboxExt) this.getFellow("staffBandList");
		staffBandboxToolbar = (Toolbar) this.getFellow("staffBandboxToolbar");
		okButton = (Button) this.getFellow("okButton");
		cancelButton = (Button) this.getFellow("cancelButton");
		staffBandboxToolbar.setVisible(false);
		staffListbox = (Listbox) staffListboxExt.getFellow(
				"staffListboxExtPanel").getFellow("staffListbox");

		staffListbox.getFellow("staffWindowDiv").setVisible(false);
		staffListbox.getFellow("staffBandboxDiv").setVisible(true);
		// ////////////////////////////////////////////////
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_CONFIRM, this,
				SffOrPtyCtants.ON_STAFF_CONFIRM);
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_CLEAN, this,
				SffOrPtyCtants.ON_STAFF_CLEAN);
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_CLOSE, this,
				SffOrPtyCtants.ON_STAFF_CLOSE);
		// ////////////////////////////////////
		this.addForward(SffOrPtyCtants.ON_ENTER_AGENT_PAGE, this,
				"onEnterAgentPageResponse");
		this.addForward(SffOrPtyCtants.ON_ENTER_IBE_PAGE, this,
				"onEnterIbePageResponse");
		/**
		 * 改单击选择员工
		 */
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_SELECT, this,
				"onSelectStaffResponse");
		/*
		 * 设置员工系统关系、员工角色关系，让listbox开启多选
		 */
		this.addForward(SffOrPtyCtants.ON_SET_STAFF_RELA, this,
				"onSetStaffRelaResponse");
		/*
		 * 获取有组织条件查询出来的员工
		 */
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_EXT_QUERY, this,
				"onStaffExtQueryResponse");
		/*
		 * 获取有组织条件查询出来的且单页的员工
		 */
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_EXT_PAGE_QUERY,
				this, "onStaffExtPageQueryResponse");
		staffListboxExt.addForward(SffOrPtyCtants.ON_STAFF_NOT_SELECT, this,
				"onNotSelectStaffResponse");
		/**
		 * 添加点击查询事件
		 */
		this.addEventListener("onOpen", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (!isLoaded) {
					// 去除默认查询staffListboxExt.onStaffQuery();
				}
				isLoaded = true;
			}
		});
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.setValue(null == staff ? "" : staff.getStaffName());
		this.staff = staff;
	}

	public void onConfirmStaff(final ForwardEvent event) {
		staff = (Staff) event.getOrigin().getData();
		if (null != staff) {
			if (!isMultiple) {
				setValue(staff.getStaffName());
			}
			Events.postEvent(SffOrPtyCtants.ON_STAFF_ORG_SELECT, this, staff);
		}
		if (!isMultiple) {
			this.close();
		}
	}

	public void onCleanStaff(final ForwardEvent event) {
		this.setStaff(null);
		this.staffs = new ArrayList<Staff>();
		this.staffsOfOrganization = new ArrayList<Staff>();
		this.staffsOfOrganizationTemp = new ArrayList<Staff>();
		this.staffsOfOrganizationPage = new ArrayList<Staff>();
		this.setStaffs(null);
		this.close();
	}

	public void onCloseStaff(final ForwardEvent event) {
		this.close();
	}

	public void onSelectStaffResponse(final ForwardEvent event) {
		staff = (Staff) event.getOrigin().getData();
		if (null != staff) {
			if (!isMultiple) {
				setValue(staff.getStaffName());
			}
			Events.postEvent(SffOrPtyCtants.ON_STAFF_ORG_SELECT, this, staff);
		}
		if (!isMultiple) {
			this.close();
		}
		setStaffSelected(this.staffListbox.getItems());
	}

	public void onNotSelectStaffResponse(final ForwardEvent event) {
		setStaffSelected(this.staffListbox.getItems());
	}

	@SuppressWarnings("unchecked")
	public void onStaffExtPageQueryResponse(final ForwardEvent event) {
		staffsOfOrganizationPage = (List<Staff>) event.getOrigin().getData();
		if (null != staffsOfOrganizationPage
				&& null != staffsOfOrganizationTemp) {
			// 找出staffsOfOrganizationPage中在staffsOfOrganization中也存在的staff,让他勾选;
			List<Staff> tempStaffs = new ArrayList<Staff>();
			for (Staff s : staffsOfOrganizationPage) {
				boolean flag = false;
				for (Staff staff : staffsOfOrganizationTemp) {
					if (staff.getStaffId().toString()
							.equals(s.getStaffId().toString())) {
						flag = true;
					}
				}
				if (flag) {
					tempStaffs.add(s);
				}
			}
			// 使staffListbox选中
			currentListitem = this.staffListbox.getItems();
			for (Listitem listitem : currentListitem) {
				boolean flag = false;
				for (Staff staff : tempStaffs) {
					Staff s = (Staff) listitem.getValue();
					if (staff.getStaffId().toString()
							.equals(s.getStaffId().toString())) {
						flag = true;
					}
				}
				if (flag) {
					this.staffListbox.addItemToSelection(listitem);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void onStaffExtQueryResponse(final ForwardEvent event) {
		this.staffs = new ArrayList<Staff>();
		this.staffsOfOrganization = new ArrayList<Staff>();
		this.staffsOfOrganizationTemp = new ArrayList<Staff>();
		this.staffsOfOrganizationPage = new ArrayList<Staff>();
		this.setStaffs(null);
		staffsOfOrganization = (List<Staff>) event.getOrigin().getData();
		staffsOfOrganizationTemp = staffsOfOrganization;
//		if (null != staffsOfOrganization) {
//			// 使staffListbox全部选中
//			currentListitem = this.staffListbox.getItems();
//			for (Listitem listitem : currentListitem) {
//				this.staffListbox.addItemToSelection(listitem);
//			}
//		}
		setStaffs(staffsOfOrganizationTemp);
	}

	public void setStaffSelected(List<Listitem> lists) {
		List<Staff> staffsTemp = new ArrayList<Staff>();
		if (null != lists && lists.size() > 0) {
			List<Staff> staffsTempSelected = new ArrayList<Staff>();// 被勾选的员工
			List<Staff> staffsTempUnSelected = new ArrayList<Staff>();// 未被勾选的员工
			for (Listitem listitem : lists) {
				Staff staff = (Staff) listitem.getValue();
				if (listitem.isSelected()) {
					staffsTempSelected.add(staff);
				} else {
					staffsTempUnSelected.add(staff);
				}
			}
/*			if (null != staffsOfOrganizationTemp) {
				for (Staff s : staffsOfOrganizationTemp) {// 移出未勾选的员工
					boolean flag = true;
					for (Staff unSelect : staffsTempUnSelected) {
						if (unSelect.getStaffId().toString()
								.equals(s.getStaffId().toString())) {
							flag = false;
						}
					}
					if (!flag) {
						staffsTemp.add(s);
					}
				}
				for (Staff select : staffsTempSelected) {// 增加新勾选的员工
					boolean flag = true;
					for (Staff s : staffsOfOrganizationTemp) {
						if (select.getStaffId().toString()
								.equals(s.getStaffId().toString())) {
							flag = false;
						}
					}
					if (!flag) {
						staffsTemp.add(select);
					}
				}
			} else {// 新打开staffbandext
				staffsOfOrganization = new ArrayList<Staff>();
				staffsOfOrganization.addAll(staffsTempSelected);
				staffsOfOrganizationTemp = new ArrayList<Staff>();
				staffsOfOrganizationTemp.addAll(staffsTempSelected);
				staffsTemp.addAll(staffsOfOrganizationTemp);
			}*/
			staffsTemp = staffsTempSelected;
		}
		staffsOfOrganizationTemp = staffsTemp;
		setStaffs(staffsTemp);
	}

	public void onEnterAgentPageResponse(final ForwardEvent event) {
		if (!StrUtil.isNullOrEmpty(event.getOrigin().getData())) {
			boolean isAgentTab = (Boolean) event.getOrigin().getData();
			Events.postEvent(SffOrPtyCtants.ON_AGENT_STAFF_LISTBOX_PAGE,
					this.staffListboxExt, isAgentTab);
		}
	}

	public void onEnterIbePageResponse(final ForwardEvent event) {
		if (!StrUtil.isNullOrEmpty(event.getOrigin().getData())) {
			boolean isIbeTab = (Boolean) event.getOrigin().getData();
			Events.postEvent(SffOrPtyCtants.ON_IBE_STAFF_LISTBOX_PAGE,
					this.staffListboxExt, isIbeTab);
		}
	}

	public void onSetStaffRelaResponse(final ForwardEvent event) {
		if (null != staffListbox) {
			staffListbox.setMultiple(true);
			staffListbox.setCheckmark(true);
			staffListbox.getFellow("organizationLab").setVisible(true);
			organizationRelationTreeBandboxExt = (OrganizationRelationTreeBandboxExt) staffListbox
					.getFellow("organizationRelationTreeBandboxExt");
			organizationRelationTreeBandboxExt.setVisible(true);
			organizationRelationTreeBandboxExt.getOrganizationRelationTree()
					.setMultiple(true);
			organizationRelationTreeBandboxExt.getOrganizationRelationTree()
					.setCheckmark(true);
			organizationRelationTreeBandboxExt
					.getOrganizationRelationTreeBandboxToolbar().setVisible(
							true);
			organizationRelationTreeBandboxExt.setIsMultiple(true);
			staffBandboxToolbar.setVisible(true);
			isMultiple = true;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void onClick$okButton() {
		setStaffSelected(this.staffListbox.getItems());
		this.close();
	}

	public void setStaffs(List<Staff> staffs) {
		String staffName = "";
		if (staffs != null && staffs.size() > 0) {
			for (int i = 0; i < staffs.size(); i++) {
				if (i == staffs.size() - 1) {
					staffName += staffs.get(i).getStaffName();
				} else {
					staffName += staffs.get(i).getStaffName() + ",";
				}
			}
		}
		this.setValue(staffName);
		this.staffs = staffs;
	}

	public void onClick$cancelButton() {
		this.close();
	}
}
