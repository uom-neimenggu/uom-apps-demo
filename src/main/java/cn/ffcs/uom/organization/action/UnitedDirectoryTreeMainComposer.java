package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.action.bean.UnitedDirectoryTreeMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.UnitedDirectory;

@Controller
@Scope("prototype")
public class UnitedDirectoryTreeMainComposer extends BasePortletComposer
		implements IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8064419972929637883L;

	/**
	 * 页面bean
	 */
	private UnitedDirectoryTreeMainBean bean = new UnitedDirectoryTreeMainBean();
	/**
	 * 选中的组织
	 */
	private UnitedDirectory unitedDirectory;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		this.bean.getUnitedDirectoryTreeExt().setPortletInfoProvider(this);
		this.bean.getUnitedDirectoryInfoEditExt().setPortletInfoProvider(this);

		/**
		 * 选中组织树上的组织
		 */
		this.bean.getUnitedDirectoryTreeExt().addForward(
				OrganizationConstant.ON_SELECT_UNITED_DIRECTORY_TREE_REQUEST,
				this.self, "onSelectUnitedDirectoryTreeResponse");
		/**
		 * 删除节点成功事件
		 */
		this.bean.getUnitedDirectoryTreeExt().addForward(
				OrganizationConstant.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 组织信息保存事件
		 */
		this.bean.getUnitedDirectoryInfoEditExt().addForward(
				OrganizationConstant.ON_SAVE_UNITED_DIRECTORY_INFO, this.self,
				"onSaveUnitedDirectoryInfoResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationCostTreeMainWindow() throws Exception {
		initLeftTab();
		// 该树目前只提供查询功能，下面权限暂不开放。如需开放，还应补齐相应功能
		// initPage();
		// initLeftTabControlRightPage();
	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initLeftTab() throws Exception {

		boolean checkCostTabResult = PlatformUtil.checkHasPermission(this,
				ActionKeys.UNITED_DIRECTORY_TAB);

		if (!checkCostTabResult) {

			this.bean.getLeftTempTab().setVisible(true);

			this.bean.getLeftTempTab().setSelected(true);

			this.bean.getUnitedDirectoryTab().setVisible(false);

			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");

		} else {

			this.bean.getUnitedDirectoryTab().setSelected(true);

		}

		bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());

	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getUnitedDirectoryTreeExt().setPagePosition(
				"unitedDirectoryTreePage");

	}

	/**
	 * 20140612设置tab页用来区分不同tab页的功能权
	 */
	private void initLeftTabControlRightPage() throws Exception {
		this.bean.getUnitedDirectoryInfoEditExt().setOrgTreeTabName(
				bean.getLeftSelectTab().getId());
	}

	/**
	 * 点击tab
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setRightSelectTab(clickTab);
				callTab();
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {

		if (this.bean.getRightSelectTab() == null) {
			bean.setRightSelectTab(this.bean.getRightTabBox().getSelectedTab());
		}

		if (unitedDirectory != null) {
			if ("unitedDirectoryInfoTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_UNITED_DIRECTORY,
						this.bean.getUnitedDirectoryInfoEditExt(),
						unitedDirectory);
			}
		} else {
			/**
			 * 切换左边tab页的时候，未选择组织树上的组织，清理数据等操作
			 */
			if ("unitedDirectoryInfoTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_UNITED_DIRECTORY,
						this.bean.getUnitedDirectoryInfoEditExt(), null);
			}
		}
	}

	/**
	 * 选择组织树
	 * 
	 * @param event
	 */
	public void onSelectUnitedDirectoryTreeResponse(ForwardEvent event)
			throws Exception {

		unitedDirectory = (UnitedDirectory) event.getOrigin().getData();

		if (unitedDirectory != null) {
			callTab();
		}
	}

	/**
	 * 点击左边的tab
	 * 
	 * @param forwardEvent
	 * @throws Exception
	 */
	public void onClickLeftTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setLeftSelectTab(clickTab);
				/**
				 * 20140612设置tab页用来区分不同tab页的功能权
				 */
				// initLeftTabControlRightPage();
				callLeftTab();
			}
		}
	}

	/**
	 * 
	 */
	public void callLeftTab() throws Exception {

		if (this.bean.getLeftSelectTab() == null) {
			bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
		}

		unitedDirectory = this.bean.getUnitedDirectoryTreeExt()
				.getSelectUnitedDirectory();

		callTab();

	}

	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		this.callLeftTab();
	}

	/**
	 * 组织信息保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveUnitedDirectoryInfoResponse(ForwardEvent event)
			throws Exception {

		if (event.getOrigin().getData() != null) {

			UnitedDirectory unitedDirectory = (UnitedDirectory) event
					.getOrigin().getData();

			if (unitedDirectory != null) {

				if ("unitedDirectoryTab"
						.equals(bean.getLeftSelectTab().getId())) {
					/**
					 * 组织信息保存可能对组织名称进行了修改
					 */
					Events.postEvent(
							OrganizationConstant.ON_SAVE_UNITED_DIRECTORY_INFO,
							this.bean.getUnitedDirectoryTreeExt(),
							unitedDirectory);
				}
			}
		}
	}
}
