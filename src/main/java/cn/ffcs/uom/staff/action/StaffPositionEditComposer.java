package cn.ffcs.uom.staff.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.staff.action.bean.StaffPositionEditBean;
import cn.ffcs.uom.staff.manager.StaffPositionManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffPosition;

@Controller
@Scope("prototype")
@SuppressWarnings({"unused"})
public class StaffPositionEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507005075323523801L;
	/**
	 * 页面bean
	 */
	private StaffPositionEditBean bean = new StaffPositionEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 添加的员工
	 */
	private Staff staff;
	/**
	 * 组织岗位manager
	 */
    private OrgPositonManager orgPositonManager = (OrgPositonManager) ApplicationContextUtil
			.getBean("orgPositonManager");

	/**
	 * 员工岗位manager
	 */
	private StaffPositionManager staffPositionManager = (StaffPositionManager) ApplicationContextUtil
			.getBean("staffPositionManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onCreate$staffPositionEditWindow() throws Exception {
		this.bean.getOrganizationPositionBandboxExt().setReadonly(true);
		this.bindBean();
	}

	/**
	 * bindBean
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getStaffPositionEditWindow().setTitle("新增员工职位关系");
			staff = (Staff) arg.get("staff");
			if (staff != null) {
				this.bean.getStaffName().setValue(staff.getStaffName());
				/**
				 * 初始化岗位bandbox
				 */
				this.bean.getOrganizationPositionBandboxExt().setOpType(
						"staffPosition");
				this.bean.getOrganizationPositionBandboxExt().setStaff(staff);
				this.bean.getOrganizationPositionBandboxExt().init();
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {
			String staffName = this.bean.getStaffName().getValue();
			if(StrUtil.isNullOrEmpty(staffName)){
				ZkUtil.showError("请选择员工", "提示信息");
				return;
			}
			OrgPosition orgPosition = this.bean
					.getOrganizationPositionBandboxExt().getOrgPosition();
			if (orgPosition == null || orgPosition.getOrgPositionId() == null) {
				ZkUtil.showError("请选择职位", "提示信息");
				return;
			}
			StaffPosition staffPosition = new StaffPosition();
			staffPosition.setStaffId(staff.getStaffId());
			staffPosition.setOrgPositionRelaId(orgPosition.getOrgPositionId());
			/**
			 * 是否已存在
			 */
			StaffPosition sp = staffPositionManager
					.queryStaffPosition(staffPosition);
			if (sp != null) {
				ZkUtil.showError("该岗位已存在", "提示信息");
				return;
			}
			staffPositionManager.addStaffPosition(staffPosition);
			Events.postEvent("onOK", this.self, staffPosition);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getStaffPositionEditWindow().onClose();
	}
}
