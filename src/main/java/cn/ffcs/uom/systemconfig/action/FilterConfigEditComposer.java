package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.FilterConfigEditBean;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.model.FilterConfig;

@Controller
@Scope("prototype")
public class FilterConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private FilterConfigEditBean bean = new FilterConfigEditBean();
	/**
	 * manager
	 */
	private FilterConfigManager filterConfigManager = (FilterConfigManager) ApplicationContextUtil
			.getBean("filterConfigManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 
	 */
	private FilterConfig currentFilterConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate$filterConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {
			currentFilterConfig = (FilterConfig) arg.get("filterConfig");
			if (currentFilterConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			this.bean.getFilterChar().setValue(currentFilterConfig.getFilterChar());
			ListboxUtils.selectByCodeValue(this.bean.getStatusCd(),currentFilterConfig.getStatusCd() + "");
			this.bean.getFilterConfigEditWin().setTitle("过滤字符信息修改");
		} else if ("add".equals(opType)) {
			this.bean.getFilterConfigEditWin().setTitle("过滤字符信息新增");
		}
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		if (StrUtil.isEmpty(this.bean.getFilterChar().getValue())) {
			ZkUtil.showError("过滤字符不能为空！", "提示信息");
			return;
		}
		if (null==this.bean.getStatusCd().getSelectedItem()||null==this.bean.getStatusCd().getSelectedItem().getValue()) {
			ZkUtil.showError("请选择状态！", "提示信息");
			return;
		}
		FilterConfig filterConfig = null;
		if ("add".equals(opType)) {
			filterConfig = new FilterConfig();
		} else if ("mod".endsWith(opType)) {
			filterConfig = currentFilterConfig;
		}

		filterConfig.setStatusCd(this.bean.getStatusCd().getSelectedItem().getValue().toString());
		filterConfig.setFilterChar(this.bean.getFilterChar().getValue());
		
		if ("add".equals(opType)) {
			this.filterConfigManager.saveFilterConfig(filterConfig);
		} else {
			this.filterConfigManager.updateFilterConfig(filterConfig);
		}
		this.bean.getFilterConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, filterConfig);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getFilterConfigEditWin().onClose();
	}
}
