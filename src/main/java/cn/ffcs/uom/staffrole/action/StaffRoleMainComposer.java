package cn.ffcs.uom.staffrole.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staffrole.action.bean.StaffRoleMainBean;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Controller
@Scope("prototype")
public class StaffRoleMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private StaffRoleMainBean bean = new StaffRoleMainBean();

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	private Component staffRoleListbox;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffRoleTreeExt().setPortletInfoProvider(this);
		bean.getRoleEditDiv().setPortletInfoProvider(this);
		/**
		 * 展示角色相关属性
		 */
		this.bean.getStaffRoleTreeExt().addForward(
				StaffRoleConstants.ON_SELECT_TREE_ROLE,
				this.bean.getRoleEditDiv(),
				StaffRoleConstants.ON_SELECT_TREE_ROLE_RESPONSE);
		/**
		 * 显示该角色对应的员工列表
		 */
		staffRoleListbox = this.self.getFellow("staffRoleListbox").getFellow(
				"staffRoleMainWin");
		this.bean.getStaffRoleTreeExt().addEventListener(
				StaffRoleConstants.ON_SELECT_TREE_ROLE, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								StaffRole staffRole = (StaffRole) event
										.getData();
								// if(null != staffRole &&
								// !StaffRoleConstants.IS_PARENT.equals(staffRole.getIsParent())){
								// Events.postEvent(StaffRoleConstants.ON_SELECT_TREE_ROLE,
								// staffRoleListbox, staffRole);
								// }
								Events.postEvent(
										StaffRoleConstants.ON_SELECT_TREE_ROLE,
										staffRoleListbox, staffRole);
							}
						}
					}
				});
		/**
		 * 删除节点成功事件
		 */
		this.bean.getStaffRoleTreeExt().addForward(
				StaffRoleConstants.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 角色信息保存事件
		 */
		this.bean.getRoleEditDiv().addForward(StaffRoleConstants.ON_SAVE_ROLE,
				this.self, StaffRoleConstants.ON_SAVE_ROLE_RESPONSE);

	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffRoleMainWin() throws Exception {
		initPage();
	}

	/**
	 * 设置页面不区分组织树TAB页
	 */
	private void initPage() throws Exception {
		this.bean.getStaffRoleTreeExt().setPagePosition();
		Events.postEvent(SffOrPtyCtants.ON_STAFF_ROLE_PAGE_POSITION,
				staffRoleListbox, "staffRolePage");
	}

	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		/**
		 * 切换左边tab页的时候，未选择角色树上的角色，清理数据等操作
		 */
		StaffRole sr = new StaffRole();
		Events.postEvent(StaffRoleConstants.ON_SELECT_TREE_ROLE,
				this.bean.getRoleEditDiv(), sr);
	}

	/**
	 * 角色保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveRoleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			StaffRole staffRole = (StaffRole) event.getOrigin().getData();
			if (staffRole != null) {
				/**
				 * 角色保存
				 */
				Events.postEvent(StaffRoleConstants.ON_SAVE_ROLEN,
						this.bean.getStaffRoleTreeExt(), staffRole);
			}
		}
	}

}
