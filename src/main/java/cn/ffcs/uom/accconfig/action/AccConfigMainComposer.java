package cn.ffcs.uom.accconfig.action;

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
import cn.ffcs.uom.accconfig.action.bean.AccConfigMainBean;
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;

@Controller
@Scope("prototype")
public class AccConfigMainComposer extends BasePortletComposer implements IPortletInfoProvider {
	private static final long serialVersionUID = 1L;
	private AccConfigMainBean bean = new AccConfigMainBean();
	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	private Component sysAccListbox;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getAccConfigTreeExt().setPortletInfoProvider(this);
		bean.getAccConfigEditDiv().setPortletInfoProvider(this);
		
		sysAccListbox = this.self.getFellow("sysAccListbox").getFellow("sysAccMainWin");
		this.bean.getAccConfigTreeExt().addEventListener(
				AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								AccConfig accConfig = (AccConfig) event.getData();
								Events.postEvent(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, bean.getAccConfigEditDiv(), accConfig);
								Events.postEvent(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, sysAccListbox, accConfig);
							}
						}
					}
				});
		/**
		 * 删除节点成功事件
		 */
		this.bean.getAccConfigTreeExt().addForward(AccConfigConstants.ON_DEL_NODE_OK, this.self, "onDelNodeResponse");
		
		/**
		 * 配置信息保存事件
		 */
		this.bean.getAccConfigEditDiv().addForward(AccConfigConstants.ON_SAVE_ACCCONFIG, this.self, AccConfigConstants.ON_SAVE_ACCCONFIG_RESPONSE);
		
	}
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$accConfigMainWin() throws Exception {
		initPage();
	}
	
	/**
	 * 设置页面不区分组织树TAB页
	 */
	private void initPage() throws Exception {
		this.bean.getAccConfigTreeExt().setPagePosition();
	}
	
	
	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		/**
		 * 切换左边tab页的时候，未选择配置树上的配置，清理数据等操作
		 */
		AccConfig sr = new AccConfig();
		Events.postEvent(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, this.bean.getAccConfigEditDiv(), sr);
	}
	
	/**
	 * 配置列表保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveAccConfigResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			AccConfig accConfig = (AccConfig) event.getOrigin().getData();
			if (accConfig != null) {
				/**
				 * 配置列表保存
				 */
				Events.postEvent(AccConfigConstants.ON_SAVE_ACCCONFIGN, this.bean.getAccConfigTreeExt(), accConfig);
			}
		}
	}
}
