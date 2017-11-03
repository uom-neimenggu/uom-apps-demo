package cn.ffcs.uom.zkdemo.action;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.util.StringPool;
import cn.ffcs.raptornuke.plugin.common.util.Validator;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkPortletUtil;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.zkdemo.BaseComposer;
import cn.ffcs.uom.zkdemo.keys.PageKeys;
import cn.ffcs.uom.zkdemo.keys.PortletKeys;
import cn.ffcs.uom.zkdemo.keys.WebKeys;

/**
 * 表单例子
 * 
 * @author lianch@ffcs.cn
 * 
 */
@SuppressWarnings("serial")
public class FormEditCtrl extends BaseComposer {

	private String _urlStr;

	// 绝对地址(从WEBCONTENT开始)
	private static final String A_FORM_VIEW = StringPool.FORWARD_SLASH
			+ WebKeys.WEBCONTENT_M + StringPool.FORWARD_SLASH
			+ PortletKeys.ZK_DEMO + StringPool.FORWARD_SLASH
			+ PageKeys.FORM_VIEW;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 只能在doAfterCompose取得portlet共享信息
		_urlStr = ZkPortletUtil.buildUrl(this.getPortletRequest(),
				this.getPortletResponse(), A_FORM_VIEW,
				new HashMap<String, Object>(), ZkPortletUtil.PORTLETMODE_VIEW,
				ZkPortletUtil.WINDOWSTATE_NORMAL);
	}

	/**
	 * 动作$ID触发
	 */
	public void onClick$jumpBtn() {
		if (Validator.isNotNull(_urlStr)) {
			ZkUtil.showQuestion("确定跳转?", "", new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (Messagebox.ON_OK.equals(event.getName()))
						ZkUtil.sendRedirect(_urlStr);
				}
			});
		}
	}
}
