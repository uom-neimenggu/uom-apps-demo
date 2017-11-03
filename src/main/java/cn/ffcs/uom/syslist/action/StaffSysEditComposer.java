package cn.ffcs.uom.syslist.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.syslist.action.bean.StaffSysEditBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class StaffSysEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	private StaffSysEditBean bean = new StaffSysEditBean();
	private String opType;
	private Staff staff;
	private SysList sysList;
	private StaffSysRela staffSysRela;
	@Autowired
	private SysListManager sysListManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		/*
		 * 让staffListbox有多选按钮
		 */
		Events.postEvent(SffOrPtyCtants.ON_SET_STAFF_RELA, bean.getStaffBandboxExt(), null);
	}
	
	public void onCreate$staffSysEditWindow() throws Exception {
		this.bindBean();
	}
	
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getStaffSysEditWindow().setTitle("新增员工系统关系");
			staffSysRela = (StaffSysRela) arg.get("staffSysRela");
			if (staffSysRela != null) {
				staff = staffSysRela.getStaff();
				sysList = staffSysRela.getSysList();
				if(null != sysList && !SysListConstants.IS_PARENT.equals(sysList.getIsParent())){
					List<SysList> sysLists = new ArrayList<SysList>();
					sysLists.add(sysList);
					this.bean.getSysListBandboxExt().setSysList(sysList);
					this.bean.getSysListBandboxExt().setSysLists(sysLists);
				}
				if(null != staff){
					List<Staff> staffs = new ArrayList<Staff>();
					staffs.add(staff);
					this.bean.getStaffBandboxExt().setStaff(staff);
					this.bean.getStaffBandboxExt().setStaffs(staffs);
				}
			}
		}
	}
	
	public void onOk() throws Exception {
		List<SysList> sysLists = this.bean.getSysListBandboxExt().getSysLists();
		List<Staff> staffs = this.bean.getStaffBandboxExt().getStaffs();
		if(null == sysLists || sysLists.size() <= 0){
			ZkUtil.showError("请选择系统。", "提示信息");
			return;
		}
		if(null == staffs || staffs.size() <= 0){
			ZkUtil.showError("请选择员工。", "提示信息");
			return;
		}
		Events.postEvent("onOK", bean.getStaffSysEditWindow(), staff);
		sysListManager.saveStaffSysRelas(staffs, sysLists);
		ZkUtil.showInformation("员工系统关系保存成功。", "系统提示");
        bean.getStaffSysEditWindow().onClose();
	}
	
	public void onCancel() throws Exception {
        bean.getStaffSysEditWindow().onClose();
	}
	
}
