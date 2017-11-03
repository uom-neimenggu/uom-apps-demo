package cn.ffcs.uom.rolePermission.component;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleEditExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleExtAttrManager;
import cn.ffcs.uom.rolePermission.manager.RbacRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleEditExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacRoleEditExtBean bean = new RbacRoleEditExtBean();

	@Autowired
	@Qualifier("rbacRoleManager")
	private RbacRoleManager rbacRoleManager = (RbacRoleManager) ApplicationContextUtil
			.getBean("rbacRoleManager");

	@Autowired
	@Qualifier("rbacRoleExtAttrManager")
	private RbacRoleExtAttrManager rbacRoleExtAttrManager = (RbacRoleExtAttrManager) ApplicationContextUtil
			.getBean("rbacRoleExtAttrManager");
	
	/**
     * 日志服务队列
     */
    @Qualifier("logService")
    @Autowired
    private LogService logService = (LogService) ApplicationContextUtil.getBean("logService");
	
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_edit_ext.zul";

	/**
	 * 选中的 selectRbacRole.
	 */
	private RbacRole selectRbacRole;

	/**
	 * 编辑中的editRbacRole.
	 */
	private RbacRole editRbacRole;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRoleEditExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		bindCombobox();
		RbacRoleEditExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_ROLE_INFO,
				RbacRoleEditExt.this, "onSaveRbacRole");
		this.addForward(RolePermissionConstants.ON_SELECT_TREE_RBAC_ROLE, this,
				"onSelectRbacRoleResponse");
		this.setRbacRoleButtonValid(true, false, false);
	}

	/**
	 * 角色选择.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRbacRoleResponse(final ForwardEvent event)
			throws Exception {

		this.selectRbacRole = (RbacRole) event.getOrigin().getData();

		if (this.selectRbacRole == null
				|| this.selectRbacRole.getRbacRoleId() == null) {
			this.selectRbacRole = new RbacRole();
		}

		this.bean.getRbacRoleId().setValue(selectRbacRole.getRbacRoleId());
		this.bean.getRbacRoleCode().setValue(selectRbacRole.getRbacRoleCode());
		this.bean.getRbacRoleName().setValue(selectRbacRole.getRbacRoleName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacRoleType(),
				selectRbacRole.getRbacRoleType());
		this.bean.getRbacRoleDesc().setValue(selectRbacRole.getRbacRoleDesc());
		/**
		 * 扩展属性
		 */
		List<RbacRoleExtAttr> extendAttrList = selectRbacRole
				.getRbacRoleExtAttrList();
		this.bean.getRbacRoleExtAttrExt().setExtendValueList(extendAttrList);
		// this.bean.getRbacRoleExtAttrExt().setAllDisable(true);
		this.setRbacRoleButtonValid(true, false, false);
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> rbacRoleType = UomClassProvider.getValuesList("RbacRole",
				"rbacRoleType");
		ListboxUtils.rendererForEdit(bean.getRbacRoleType(), rbacRoleType);

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		this.setRbacRoleButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 * 
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onSave() throws PortalException, SystemException {

		if (bean.getRbacRoleCode() == null
				|| StrUtil.isEmpty(bean.getRbacRoleCode().getValue())) {
			ZkUtil.showError("角色编码不能为空", "提示信息");
			return;
		}

		if (bean.getRbacRoleName() == null
				|| StrUtil.isEmpty(bean.getRbacRoleName().getValue())) {
			ZkUtil.showError("角色名称不能为空", "提示信息");
			return;
		}
		
		/**
         * 开始日志添加操作
         * 添加日志到队列需要：
         * 业务开始时间，日志消息类型，错误编码和描述
         */
        SysLog log = new SysLog();
        log.startLog(new Date(), SysLogConstrants.ROLE);

		this.editRbacRole = this.selectRbacRole;

		this.editRbacRole.setRbacRoleCode(bean.getRbacRoleCode().getValue()
				.trim());
		this.editRbacRole.setRbacRoleName(bean.getRbacRoleName().getValue()
				.trim());
		if (bean.getRbacRoleType().getSelectedItem().getValue() != null) {
			this.editRbacRole.setRbacRoleType(bean.getRbacRoleType()
					.getSelectedItem().getValue().toString());
		}
		this.editRbacRole.setRbacRoleDesc(bean.getRbacRoleDesc().getValue());

		/**
		 * 扩展属性
		 */
		List<RbacRoleExtAttr> extendAttrList = editRbacRole
				.getRbacRoleExtAttrList();

		List<RbacRoleExtAttr> beanList = this.bean.getRbacRoleExtAttrExt()
				.getExtendValueList();

		if (extendAttrList == null || extendAttrList.size() <= 0) {
			editRbacRole.setRbacRoleExtAttrList(beanList);
		} else {
			for (RbacRoleExtAttr rbacRoleExtAttr : beanList) {
				for (RbacRoleExtAttr dbRbacRoleExtAttr : extendAttrList) {
					if (rbacRoleExtAttr.getRbacRoleAttrSpecId().equals(
							dbRbacRoleExtAttr.getRbacRoleAttrSpecId())) {
						String rbacRoleAttrVlue = rbacRoleExtAttr
								.getRbacRoleAttrValue();
						BeanUtils.copyProperties(rbacRoleExtAttr,
								dbRbacRoleExtAttr);
						rbacRoleExtAttr.setRbacRoleAttrValue(rbacRoleAttrVlue);
					}
				}
			}
		}

		editRbacRole.setRbacRoleExtAttrList(beanList);

		rbacRoleManager.updateRbacRole(editRbacRole);
		Class clazz[] ={RbacRoleRelation.class};
	    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "修改角色记录日志");

		this.setRbacRoleButtonValid(true, false, false);

		/**
		 * 抛出保存事件，用来在角色树中保存更改后的角色名称
		 */
		Events.postEvent(RolePermissionConstants.ON_SAVE_RBAC_ROLE_INFO, this,
				editRbacRole);

		rbacRoleManager.updateRbacRoleToRaptornuke(editRbacRole);

		RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();
		rbacRoleExtAttr.setRbacRoleId(editRbacRole.getRbacRoleId());
		this.bean.getRbacRoleExtAttrExt().setExtendValueList(
				rbacRoleExtAttrManager
						.queryRbacRoleExtAttrList(rbacRoleExtAttr));
	}

	/**
	 * 点击恢复
	 */
	public void onRecover() {
		this.setRbacRoleButtonValid(true, false, false);
		this.bean.getRbacRoleId().setValue(selectRbacRole.getRbacRoleId());
		this.bean.getRbacRoleCode().setValue(selectRbacRole.getRbacRoleCode());
		this.bean.getRbacRoleName().setValue(selectRbacRole.getRbacRoleName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacRoleType(),
				selectRbacRole.getRbacRoleType());
		this.bean.getRbacRoleDesc().setValue(selectRbacRole.getRbacRoleDesc());
		/**
		 * 扩展属性
		 */
		List<RbacRoleExtAttr> extendAttrList = selectRbacRole
				.getRbacRoleExtAttrList();
		this.bean.getRbacRoleExtAttrExt().setExtendValueList(extendAttrList);
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canView
	 *            查看按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setRbacRoleButtonValid(final Boolean canEdit,
			final Boolean canSave, final Boolean canRecover) {
		if (canEdit != null) {
			this.bean.getEditButton().setDisabled(!canEdit);
		}
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

}
