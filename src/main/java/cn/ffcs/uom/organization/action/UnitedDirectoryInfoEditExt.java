package cn.ffcs.uom.organization.action;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.UnitedDirectoryInfoEditExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.UnitedDirectory;

public class UnitedDirectoryInfoEditExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3695647753612258247L;
	/**
	 * 页面
	 */
	private final String zul = "/pages/organization/united_directory_info_edit_ext.zul";
	/**
	 * 页面bean
	 */
	@Getter
	@Setter
	private UnitedDirectoryInfoEditExtBean bean = new UnitedDirectoryInfoEditExtBean();

	/**
	 * 选中的组织
	 */
	private UnitedDirectory oldUnitedDirectory;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Getter
	@Setter
	private String variableOrgTreeTabName;

	/**
	 * 构造方法
	 */
	public UnitedDirectoryInfoEditExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(OrganizationConstant.ON_SELECT_TREE_UNITED_DIRECTORY,
				this, "onSelectTreeUnitedDirectoryResponse");
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false, false);
	}

	/**
	 * 组织树选择组织
	 * 
	 * @throws Exception
	 */
	public void onSelectTreeUnitedDirectoryResponse(ForwardEvent event)
			throws Exception {

		oldUnitedDirectory = (UnitedDirectory) event.getOrigin().getData();

		if (oldUnitedDirectory != null) {

			this.bean.getUnitedDirectoryInfoExt().setOpType("mod");
			this.bean.getUnitedDirectoryInfoExt().setUnitedDirectory(
					oldUnitedDirectory);
			this.setButtonValid(true, false, false);

		} else {
			/**
			 * 未选择组织树清理已存在的数据
			 */
			this.bean.getUnitedDirectoryInfoExt().setOpType("clear");
			this.bean.getUnitedDirectoryInfoExt().setUnitedDirectory(
					new UnitedDirectory());
		}

		// if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
		// this.setOrgTreeTabName(variableOrgTreeTabName);
		// }

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.setButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 * 
	 * @throws Exception
	 */
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		String msg = this.bean.getUnitedDirectoryInfoExt()
				.getDoValidUnitedDirectoryInfo();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		UnitedDirectory unitedDirectory = this.bean.getUnitedDirectoryInfoExt()
				.getUnitedDirectory();

		this.oldUnitedDirectory = unitedDirectory;

		this.setButtonValid(true, false, false);
		/**
		 * 抛出保存事件，用来判断组织类型是否改变（管理类）
		 */
		Events.postEvent(OrganizationConstant.ON_SAVE_UNITED_DIRECTORY_INFO,
				this, unitedDirectory);
	}

	/**
	 * 点击恢复
	 * 
	 * @throws Exception
	 */
	public void onRecover() throws Exception {
		this.setButtonValid(true, false, false);
		this.bean.getUnitedDirectoryInfoExt().setUnitedDirectory(
				oldUnitedDirectory);
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canEdit
	 * @param canSave
	 * @param canRecover
	 */
	private void setButtonValid(boolean canEdit, boolean canSave,
			boolean canRecover) {
		this.bean.getEditButton().setDisabled(!canEdit);
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("unitedDirectoryTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_UNITED_DIRECTORY_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_UNITED_DIRECTORY_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_UNITED_DIRECTORY_INFO_RECOVER)) {
					canRecover = true;
				}
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}
}
