package cn.ffcs.uom.syslist.action;

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
import cn.ffcs.uom.syslist.action.bean.SysListMainBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysListMainComposer extends BasePortletComposer implements IPortletInfoProvider {
	private static final long serialVersionUID = 1L;
	private SysListMainBean bean = new SysListMainBean();
	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	private Component staffSysListbox;

	private Component sysRoleListbox;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getSysListTreeExt().setPortletInfoProvider(this);
		bean.getSysListEditDiv().setPortletInfoProvider(this);
		/**
		 * 展示系统相关属性
		 */
		this.bean.getSysListTreeExt().addForward(SysListConstants.ON_SELECT_TREE_SYSLIST,
				this.bean.getSysListEditDiv(), SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
		/**
		 * 显示该系统对应的员工列表
		 */
		staffSysListbox = this.self.getFellow("staffSysListbox").getFellow("staffSysMainWin");
		this.bean.getSysListTreeExt().addEventListener(
				SysListConstants.ON_SELECT_TREE_SYSLIST, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								SysList sysList = (SysList) event.getData();
								Events.postEvent(SysListConstants.ON_SELECT_TREE_SYSLIST, staffSysListbox, sysList);
							}
						}
					}
				});
		/**
		 * 显示该系统对应的角色列表
		 */
		sysRoleListbox = this.self.getFellow("sysRoleListbox").getFellow("sysRoleMainWin");
		this.bean.getSysListTreeExt().addEventListener(
				SysListConstants.ON_SELECT_TREE_SYSLIST, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								SysList sysList = (SysList) event.getData();
								Events.postEvent(SysListConstants.ON_SELECT_TREE_SYSLIST, sysRoleListbox, sysList);
							}
						}
					}
				});
		/**
		 * 删除节点成功事件
		 */
		this.bean.getSysListTreeExt().addForward(SysListConstants.ON_DEL_NODE_OK, this.self, "onDelNodeResponse");
		
		/**
		 * 系统信息保存事件
		 */
		this.bean.getSysListEditDiv().addForward(SysListConstants.ON_SAVE_SYSLIST, this.self, SysListConstants.ON_SAVE_SYSLIST_RESPONSE);
		
	}
	
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$sysListMainWin() throws Exception {
		initPage();
	}
	
	/**
	 * 设置页面不区分组织树TAB页
	 */
	private void initPage() throws Exception {
		this.bean.getSysListTreeExt().setPagePosition();
	}
	
	
	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		/**
		 * 切换左边tab页的时候，未选择系统树上的系统，清理数据等操作
		 */
		SysList sr = new SysList();
		Events.postEvent(SysListConstants.ON_SELECT_TREE_SYSLIST, this.bean.getSysListEditDiv(), sr);
	}
	
	/**
	 * 系统列表保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveSysListResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			SysList sysList = (SysList) event.getOrigin().getData();
			if (sysList != null) {
				/**
				 * 系统列表保存
				 */
				Events.postEvent(SysListConstants.ON_SAVE_SYSLISTN, this.bean.getSysListTreeExt(), sysList);
			}
		}
	}
}
