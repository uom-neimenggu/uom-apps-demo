package cn.ffcs.uom.staff.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.position.manager.CtPositionManager;
import cn.ffcs.uom.position.model.CtPosition;
import cn.ffcs.uom.staff.action.bean.StaffCtPositionEditBean;
import cn.ffcs.uom.staff.manager.CtStaffPositionRefManager;
import cn.ffcs.uom.staff.model.CtStaffPositionRef;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffCtPositionEditComposer extends BasePortletComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8854182730238354084L;

	/**
	 * 页面bean
	 */
	private StaffCtPositionEditBean bean = new StaffCtPositionEditBean();

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 添加的员工
	 */
	private Staff staff;

	/**
	 * 员工岗位manager
	 */
	private CtStaffPositionRefManager ctStaffPositionManager = (CtStaffPositionRefManager) ApplicationContextUtil
			.getBean("ctStaffPositionRefManager");

	/**
	 * 员工组织manager
	 */
	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");

	/**
	 * 岗位manager
	 */
	private CtPositionManager ctPositionManager = (CtPositionManager) ApplicationContextUtil
			.getBean("ctPositionManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onCreate$staffCtPositionEditWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> ralaCdList = UomClassProvider.getValuesList(
				"CtStaffPositionRef", "ralaCd");
		ListboxUtils.rendererForEdit(this.bean.getRalaCd(), ralaCdList);
	}

	/**
	 * bindBean
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getStaffCtPositionEditWindow().setTitle("新增员工岗位关系");
			staff = (Staff) arg.get("staff");
			if (staff != null) {
				this.bean.getStaffName().setValue(staff.getStaffName());
				/**
				 * 初始化组织
				 */
				List<NodeVo> organizationList = staffOrganizationManager
						.queryStaffOrgNodeVoList(staff);
				ListboxUtils.rendererForEditNoSelected(
						this.bean.getOrganization(), organizationList);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {
			String staffName = this.bean.getStaffName().getValue();
			if (StrUtil.isNullOrEmpty(staffName)) {
				ZkUtil.showError("请选择员工", "提示信息");
				return;
			}

			if (bean.getCtPosition().getPositionCode() == null
					|| bean.getCtPosition().getPositionCode().length() <= 0) {
				ZkUtil.showError("请选择岗位", "提示信息");
				return;
			}

			if (bean.getRalaCd().getSelectedItem() == null
					|| bean.getRalaCd().getSelectedItem().getValue() == null) {
				ZkUtil.showError("请选择岗位类型", "提示信息");
				return;
			} else if (BaseUnitConstants.POS_RALA_CD_1.equals(bean.getRalaCd()
					.getSelectedItem().getValue().toString())) {
				CtStaffPositionRef queryCtStaffPositionRef = new CtStaffPositionRef();
				queryCtStaffPositionRef.setStaffId(staff.getStaffId());
				queryCtStaffPositionRef.setRalaCd(bean.getRalaCd()
						.getSelectedItem().getValue().toString());

				CtStaffPositionRef cp = ctStaffPositionManager
						.queryCtStaffPositionRef(queryCtStaffPositionRef);

				if (cp != null) {
					ZkUtil.showError("该员工已存在主岗", "提示信息");
					return;
				}
			}

			CtStaffPositionRef ctStaffPositionRef = new CtStaffPositionRef();
			ctStaffPositionRef.setStaffId(staff.getStaffId());

			CtPosition queryCtPosition = new CtPosition();
			queryCtPosition.setPositionName(bean.getCtPosition().getValue());
			queryCtPosition.setPositionCode(bean.getCtPosition()
					.getPositionCode());

			CtPosition ctPosition = ctPositionManager
					.queryCtPosition(queryCtPosition);

			ctStaffPositionRef.setCtPositionId(ctPosition.getCtPositionId());

			if (bean.getOrganization().getSelectedItem() != null
					&& bean.getOrganization().getSelectedItem().getValue() != null) {
				ctStaffPositionRef.setOrgId(Long.parseLong(bean
						.getOrganization().getSelectedItem().getValue()
						.toString()));
			}

			/**
			 * 是否已存在
			 */
			CtStaffPositionRef sp = ctStaffPositionManager
					.queryCtStaffPositionRef(ctStaffPositionRef);
			if (sp != null) {
				ZkUtil.showError("该岗位已存在", "提示信息");
				return;
			}
			
			ctStaffPositionRef.setRalaCd(bean.getRalaCd().getSelectedItem()
					.getValue().toString());
			
			ctStaffPositionManager.addCtStaffPositionRef(ctStaffPositionRef);
			Events.postEvent("onOK", this.self, ctStaffPositionRef);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getStaffCtPositionEditWindow().onClose();
	}
}
