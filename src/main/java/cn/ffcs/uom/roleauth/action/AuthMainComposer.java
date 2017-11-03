package cn.ffcs.uom.roleauth.action;

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
import cn.ffcs.uom.roleauth.action.bean.AuthMainBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

@Controller
@Scope("prototype")
public class AuthMainComposer extends BasePortletComposer implements IPortletInfoProvider {
	private static final long serialVersionUID = 1L;
	private AuthMainBean bean = new AuthMainBean();
	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	/**
	 * 角色权限
	 */
	private Component roleAuthListbox;
	
	/**
	 * 系统权限
	 */
	private Component sysAuthListbox;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getAuthorityTreeExt().setPortletInfoProvider(this);
		bean.getAuthEditDiv().setPortletInfoProvider(this);
		/**
		 * 展示权限相关属性
		 */
		this.bean.getAuthorityTreeExt().addForward(RoleAuthConstants.ON_SELECT_TREE_AUTH,
				this.bean.getAuthEditDiv(), RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
		/**
		 * 显示该权限对应的角色列表
		 */
		roleAuthListbox = this.self.getFellow("roleAuthListbox").getFellow("roleAuthMainWin");
		this.bean.getAuthorityTreeExt().addEventListener(
				RoleAuthConstants.ON_SELECT_TREE_AUTH, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								StaffAuthority authority = (StaffAuthority) event.getData();
								Events.postEvent(RoleAuthConstants.ON_SELECT_TREE_AUTH, roleAuthListbox, authority);
							}
						}
					}
				});
		/**
		 * 显示该权限对应的系统列表
		 */
		sysAuthListbox = this.self.getFellow("sysAuthListbox").getFellow("sysAuthMainWin");
		this.bean.getAuthorityTreeExt().addEventListener(
				RoleAuthConstants.ON_SELECT_TREE_AUTH, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								StaffAuthority authority = (StaffAuthority) event.getData();
								Events.postEvent(RoleAuthConstants.ON_SELECT_TREE_AUTH, sysAuthListbox, authority);
							}
						}
					}
				});
		/**
		 * 删除节点成功事件
		 */
		this.bean.getAuthorityTreeExt().addForward(RoleAuthConstants.ON_DEL_NODE_OK, this.self, "onDelNodeResponse");
		
		/**
		 * 权限信息保存事件
		 */
		this.bean.getAuthEditDiv().addForward(RoleAuthConstants.ON_SAVE_AUTH, this.self, RoleAuthConstants.ON_SAVE_AUTH_RESPONSE);
		
	}
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$authMainWin() throws Exception {
		initPage();
	}
	
	/**
	 * 设置页面不区分组织树TAB页
	 */
	private void initPage() throws Exception {
		this.bean.getAuthorityTreeExt().setPagePosition();
	}
	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		/**
		 * 切换左边tab页的时候，未选择权限树上的系统，清理数据等操作
		 */
		StaffAuthority sr = new StaffAuthority();
		Events.postEvent(RoleAuthConstants.ON_SELECT_TREE_AUTH, this.bean.getAuthEditDiv(), sr);
	}
	
	/**
	 * 系统列表保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveAuthResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			StaffAuthority authority = (StaffAuthority) event.getOrigin().getData();
			if (authority != null) {
				/**
				 * 权限保存
				 */
				Events.postEvent(RoleAuthConstants.ON_SAVE_AUTHN, this.bean.getAuthorityTreeExt(), authority);
			}
		}
	}
}
