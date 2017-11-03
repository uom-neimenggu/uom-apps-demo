package cn.ffcs.uom.staff.action;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.action.bean.StaffOrgTranEditBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;

/**
 * 员工组织业务关系 Edit Composer .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "rawtypes" })
public class StaffOrgTranEditComposer extends BasePortletComposer {

	private String opType;

	private Staff staff;

	private StaffOrganizationTran staffOrganizationTran;

	private static final long serialVersionUID = 1L;

	private StaffOrganizationTran staffOrgTran;
	/**
	 * 是否是组织树页面
	 */
	private Boolean isOrgTreePage = false;
	/**
	 * 树TAB区分
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;

	private StaffOrgTranEditBean bean = new StaffOrgTranEditBean();

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffOrgTranEditComposer() {
		bindEvent();
		bindBean();
		bindCombobox();
	}

	/**
	 * 监听事件 .
	 */
	private void bindEvent() {
		StaffOrgTranEditComposer.this.bean.getStaffOrgTranEditComposer()
				.addEventListener("onStaffOrgTranChange", new EventListener() {
					public void onEvent(final Event event) throws Exception {
						if (!StrUtil.isNullOrEmpty(event.getData())) {
							StaffOrgTranEditComposer.this.arg = (HashMap) event
									.getData();
							bindBean();
						}
					}
				});
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() {
		opType = StrUtil.strnull(arg.get("opType"));
		staffOrganizationTran = (StaffOrganizationTran) arg
				.get("staffOrganizationTran");
		/**
		 * 是否是组织树页面，是的话组织不可选
		 */
		isOrgTreePage = (Boolean) arg.get("isOrgTreePage");
		// 全息网格单元树判断
		variableOrgTreeTabName = (String) arg.get("variableOrgTreeTabName");

		if (staffOrganizationTran != null) {

			if (isOrgTreePage) {
				this.bean.getOrg().setDisabled(true);
			} else {
				this.bean.getStaff().setDisabled(true);
			}

			if (SffOrPtyCtants.ADD.equals(opType)) {
				bean.getStaffOrgTranEditComposer().setTitle("员工组织业务关系新增");
			} else if (SffOrPtyCtants.MOD.equals(opType)) {
				bean.getStaffOrgTranEditComposer().setTitle("员工组织业务关系修改");
				this.bean.getOrg().setDisabled(true);
				this.bean.getStaff().setDisabled(true);
				PubUtil.fillBeanFromPo(staffOrganizationTran, bean);
			}

			if (staffOrganizationTran.getStaffId() != null) {
				this.bean.getStaff().setStaff(staffOrganizationTran.getStaff());
			}

			if (staffOrganizationTran.getOrgId() != null) {
				if (!StrUtil.isEmpty(variableOrgTreeTabName)
						&& variableOrgTreeTabName.equals("gridUnitTreeTab")) {
					GridUnit gridUnit = staffOrganizationTran.getGridUnit();
					Organization org = new Organization();
					if (gridUnit != null) {
						org.setOrgId(gridUnit.getGridUnitId());
						org.setOrgName(gridUnit.getGridName());
					}
					this.bean.getOrg().setOrganization(org);
				} else {
					this.bean.getOrg().setOrganization(
							staffOrganizationTran.getOrganization());
				}
			}

		}

	}

	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() {
		List<NodeVo> liTp = UomClassProvider.getValuesList(
				"StaffOrganizationTran", "ralaCd");
		ListboxUtils.rendererForEdit(bean.getRalaCd(), liTp);
	}

	/**
	 * 保存.
	 * 
	 * @throws Exception
	 */
	public void onOk() {

		try {

			if (bean.getStaff().getStaff() == null) {
				Messagebox.show("请选择相应的员工");
				return;
			}

			if (bean.getOrg().getOrganization() == null) {
				Messagebox.show("请选择相应的组织");
				return;
			}

			if (StrUtil.isNullOrEmpty(bean.getRalaCd())
					|| StrUtil.isNullOrEmpty(bean.getRalaCd().getSelectedItem()
							.getValue())) {
				Messagebox.show("请选择关联类型");
				return;
			}

			if (StrUtil.isNullOrEmpty(bean.getStaffSort())
					|| StrUtil.isNullOrEmpty(bean.getStaffSort().getValue())) {
				Messagebox.show("请输入员工排序号");
				return;
			}

			if (SffOrPtyCtants.ADD.equals(opType)) {

				staffOrgTran = new StaffOrganizationTran();
				PubUtil.fillPoFromBean(bean, staffOrgTran);
				staffOrgTran
						.setStaffId(bean.getStaff().getStaff().getStaffId());
				staffOrgTran.setOrgId(bean.getOrg().getOrganization()
						.getOrgId());

				/**
				 * 同一个组织下，同一种关系类型的排序号不能重复
				 */
				StaffOrganizationTran vo = new StaffOrganizationTran();
				vo.setOrgId(staffOrgTran.getOrgId());
				vo.setRalaCd(staffOrgTran.getRalaCd());
				vo.setStaffSort(staffOrgTran.getStaffSort());
				List<StaffOrganizationTran> existlist = staffManager
						.queryStaffOrgTranList(vo);
				if (existlist != null && existlist.size() > 0) {
					ZkUtil.showInformation("相同关联类型下,员工排序号不能重复!", "提示信息");
					return;
				}

				/**
				 * 一个员工只能和组织有一种业务关系
				 */
				vo = new StaffOrganizationTran();
				vo.setStaffId(staffOrgTran.getStaffId());
				vo.setOrgId(staffOrgTran.getOrgId());
				existlist = null;
				existlist = staffManager.queryStaffOrgTranList(vo);
				if (existlist != null && existlist.size() > 0) {
					ZkUtil.showInformation("该员工和该组织已存在业务关系,不能重复添加", "提示信息");
					return;
				}

				/**
				 * 不同关联类型对人数的控制
				 */
				vo = new StaffOrganizationTran();
				existlist = null;

				vo.setOrgId(staffOrgTran.getOrgId());
				vo.setRalaCd(staffOrgTran.getRalaCd());
				existlist = staffManager.queryStaffOrgTranList(vo);

				if (existlist != null && existlist.size() > 0
						&& !StrUtil.isNullOrEmpty(vo.getRalaCdNumber())) {
					if (existlist.size() >= vo.getRalaCdNumber()) {
						ZkUtil.showInformation("关联类型是" + vo.getRalaCdName()
								+ "的，人数不能超过" + vo.getRalaCdNumber() + "个!",
								"提示信息");
						return;
					}
				}

				staffManager.saveStaffOrgTran(staffOrgTran);

			} else if (SffOrPtyCtants.MOD.equals(opType)) {// 下面的逻辑需要改动，具体参考SffOrPtyCtants.ADD.equals(opType)里面的

				// PubUtil.fillPoFromBean(bean, staffOrgTran);
				// staffOrgTran.setOrgId(bean.getOrg().getOrganization()
				// .getOrgId());
				//
				// /**
				// * 一个员工只能和组织有一种业务关系
				// */
				// StaffOrganizationTran vo = new StaffOrganizationTran();
				// vo.setStaffId(staffOrgTran.getStaffId());
				// vo.setOrgId(staffOrgTran.getOrgId());
				// List<StaffOrganizationTran> existlist = staffManager
				// .queryStaffOrgTranList(vo);
				// if (existlist != null && existlist.size() > 0) {
				// for (StaffOrganizationTran staffOrganizationTran : existlist)
				// {
				// if (staffOrgTran.getStaffOrgTranId().longValue() !=
				// staffOrganizationTran
				// .getStaffOrgTranId().longValue()) {
				// ZkUtil.showInformation("该员工和该组织已存在业务关系,不能重复添加",
				// "提示信息");
				// return;
				// }
				// }
				// }
				//
				// if (staffOrgTran.getRalaCd().equals(
				// OrganizationConstant.STAFF_ORG_TRAN_RALA_CD)) {
				//
				// /**
				// * 一个组织下只能有一个负责人
				// */
				// vo = new StaffOrganizationTran();
				// existlist = null;
				//
				// vo.setOrgId(staffOrgTran.getOrgId());
				// vo.setRalaCd(staffOrgTran.getRalaCd());
				// existlist = staffManager.queryStaffOrgTranList(vo);
				// if (existlist != null && existlist.size() > 0) {
				// for (StaffOrganizationTran staffOrganizationTran : existlist)
				// {
				// if (staffOrgTran.getStaffOrgTranId().longValue() !=
				// staffOrganizationTran
				// .getStaffOrgTranId().longValue()) {
				// ZkUtil.showInformation(
				// "一个组织下只能有一个负责人,请重新选择关系类型.", "提示信息");
				// return;
				// }
				// }
				// }
				//
				// }
				//
				// staffManager.updateStaffOrgTran(staffOrgTran);
			}

			Events.postEvent("onOK", bean.getStaffOrgTranEditComposer(),
					staffOrgTran);
			bean.getStaffOrgTranEditComposer().onClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getStaffOrgTranEditComposer().onClose();
	}

}
