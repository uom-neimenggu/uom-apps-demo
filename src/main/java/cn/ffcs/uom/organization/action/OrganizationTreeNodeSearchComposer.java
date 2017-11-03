package cn.ffcs.uom.organization.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.OrganizationTreeNodeSearchBean;

@Controller
@Scope("prototype")
public class OrganizationTreeNodeSearchComposer extends BasePortletComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private OrganizationTreeNodeSearchBean bean = new OrganizationTreeNodeSearchBean();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationTreeNodeSearchWindow() throws Exception {

	}

	/**
	 * 点击查询
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSearch() throws Exception {
		if (this.bean.getOrgName() != null
				&& !StrUtil.isEmpty(this.bean.getOrgName().getValue())) {
			Map searchMap = new HashMap();
			searchMap.put("orgName", this.bean.getOrgName().getValue());
			Events.postEvent("onSearchOK", this.self, searchMap);
		} else {
			ZkUtil.showError("请输入查询条件", "提示信息");
			return;
		}
	}
}
