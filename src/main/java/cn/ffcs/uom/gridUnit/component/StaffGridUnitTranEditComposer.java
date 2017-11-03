package cn.ffcs.uom.gridUnit.component;

import java.util.HashMap;
import java.util.List;

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
import cn.ffcs.uom.gridUnit.component.bean.StaffGridUnitTranEditBean;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;

/**
 * 员工网格单元业务关系 Edit Composer .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "rawtypes" })
public class StaffGridUnitTranEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private String opType;

	private Staff staff;

	private GridUnit gridUnit;

	private StaffOrganizationTran staffGridUnitTran;

	private StaffGridUnitTranEditBean bean = new StaffGridUnitTranEditBean();

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
	public void onCreate$staffGridUnitTranEditComposer() {
		bindEvent();
		bindBean();
		bindCombobox();
	}

	/**
	 * 监听事件 .
	 */
	private void bindEvent() {
		StaffGridUnitTranEditComposer.this.bean
				.getStaffGridUnitTranEditComposer().addEventListener(
						"onStaffGridUnitTranChange", new EventListener() {
							public void onEvent(final Event event)
									throws Exception {
								if (!StrUtil.isNullOrEmpty(event.getData())) {
									StaffGridUnitTranEditComposer.this.arg = (HashMap) event
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
		staffGridUnitTran = (StaffOrganizationTran) arg
				.get("staffGridUnitTran");

		if (staffGridUnitTran != null) {

			this.bean.getGridUnitBandbox().setDisabled(true);
			this.bean.getStaff().setDisabled(true);

			if (SffOrPtyCtants.ADD.equals(opType)) {
				bean.getStaffGridUnitTranEditComposer().setTitle("员工组织业务关系新增");
			} else if (SffOrPtyCtants.MOD.equals(opType)) {
				bean.getStaffGridUnitTranEditComposer().setTitle("员工组织业务关系修改");
				PubUtil.fillBeanFromPo(staffGridUnitTran, bean);
			}

			if (staffGridUnitTran.getStaffId() != null) {
				this.bean.getStaff().setStaff(staffGridUnitTran.getStaff());
			} else {
				this.bean.getStaff().setDisabled(false);

			}

			if (staffGridUnitTran.getOrgId() != null) {
				this.bean.getGridUnitBandbox().setGridUnit(
						staffGridUnitTran.getGridUnit());
			} else {
				this.bean.getGridUnitBandbox().setDisabled(false);
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
	public void onStaffTranOk() {

		try {

			if (bean.getStaff().getStaff() == null) {
				Messagebox.show("请选择相应的员工");
				return;
			}

			if (bean.getGridUnitBandbox().getGridUnit() == null) {
				Messagebox.show("请选择相应的网格单元");
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

				staffGridUnitTran = new StaffOrganizationTran();
				PubUtil.fillPoFromBean(bean, staffGridUnitTran);
				staffGridUnitTran.setStaffId(bean.getStaff().getStaff()
						.getStaffId());
				staffGridUnitTran.setOrgId(bean.getGridUnitBandbox()
						.getGridUnit().getGridUnitId());

				/**
				 * 同一个组织下，同一种关系类型的排序号不能重复
				 */
				StaffOrganizationTran vo = new StaffOrganizationTran();
				vo.setOrgId(staffGridUnitTran.getOrgId());
				vo.setRalaCd(staffGridUnitTran.getRalaCd());
				vo.setStaffSort(staffGridUnitTran.getStaffSort());
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
				vo.setStaffId(staffGridUnitTran.getStaffId());
				vo.setOrgId(staffGridUnitTran.getOrgId());
				existlist = null;
				existlist = staffManager.queryStaffOrgTranList(vo);
				//前三种1、负责人2、非负责人，3.其他互斥
                //后面 各种维护人员可以兼职多个
                //获取关联类型，判断是否是1011，1021，1001分别是：线路维护人，设备维护人，装维负责人
                if(staffGridUnitTran.getRalaCd().equals("1011") || staffGridUnitTran.getRalaCd().equals("1021") 
                    || staffGridUnitTran.getRalaCd().equals("1001"))
                {
                    existlist = null;
                }
				
				if (existlist != null && existlist.size() > 0) {
					ZkUtil.showInformation("该员工和该组织已存在业务关系,不能重复添加", "提示信息");
					return;
				}

				/**
				 * 不同关联类型对人数的控制
				 */
				vo = new StaffOrganizationTran();
				existlist = null;

				vo.setOrgId(staffGridUnitTran.getOrgId());
				vo.setRalaCd(staffGridUnitTran.getRalaCd());
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

				staffManager.saveStaffOrgTran(staffGridUnitTran);

			} else if (SffOrPtyCtants.MOD.equals(opType)) {// 下面的逻辑需要改动，具体参考SffOrPtyCtants.ADD.equals(opType)里面的

				// PubUtil.fillPoFromBean(bean, staffGridUnitTran);
				// staffGridUnitTran.setGridUnitId(bean.getGridUnit().getGridUnit()
				// .getGridUnitId());
				//
				// /**
				// * 一个员工只能和组织有一种业务关系
				// */
				// StaffOrganizationTran vo = new StaffOrganizationTran();
				// vo.setStaffId(staffGridUnitTran.getStaffId());
				// vo.setGridUnitId(staffGridUnitTran.getGridUnitId());
				// List<StaffOrganizationTran> existlist = staffManager
				// .queryStaffGridUnitTranList(vo);
				// if (existlist != null && existlist.size() > 0) {
				// for (StaffOrganizationTran staffGridUnitTran : existlist)
				// {
				// if (staffGridUnitTran.getStaffGridUnitTranId().longValue() !=
				// staffGridUnitTran
				// .getStaffGridUnitTranId().longValue()) {
				// ZkUtil.showInformation("该员工和该组织已存在业务关系,不能重复添加",
				// "提示信息");
				// return;
				// }
				// }
				// }
				//
				// if (staffGridUnitTran.getRalaCd().equals(
				// GridUnitConstant.STAFF_ORG_TRAN_RALA_CD)) {
				//
				// /**
				// * 一个组织下只能有一个负责人
				// */
				// vo = new StaffOrganizationTran();
				// existlist = null;
				//
				// vo.setGridUnitId(staffGridUnitTran.getGridUnitId());
				// vo.setRalaCd(staffGridUnitTran.getRalaCd());
				// existlist = staffManager.queryStaffGridUnitTranList(vo);
				// if (existlist != null && existlist.size() > 0) {
				// for (StaffOrganizationTran staffGridUnitTran : existlist)
				// {
				// if (staffGridUnitTran.getStaffGridUnitTranId().longValue() !=
				// staffGridUnitTran
				// .getStaffGridUnitTranId().longValue()) {
				// ZkUtil.showInformation(
				// "一个组织下只能有一个负责人,请重新选择关系类型.", "提示信息");
				// return;
				// }
				// }
				// }
				//
				// }
				//
				// staffManager.updateStaffGridUnitTran(staffGridUnitTran);
			}

			Events.postEvent("onOK", bean.getStaffGridUnitTranEditComposer(),
					staffGridUnitTran);
			bean.getStaffGridUnitTranEditComposer().onClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getStaffGridUnitTranEditComposer().onClose();
	}

}
