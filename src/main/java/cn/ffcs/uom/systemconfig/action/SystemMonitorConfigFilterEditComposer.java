package cn.ffcs.uom.systemconfig.action;

import java.util.List;

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
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.action.bean.SystemMonitorConfigFilterEditBean;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigFilterManager;
import cn.ffcs.uom.webservices.manager.SystemMonitorSourcesManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;
import cn.ffcs.uom.webservices.model.SystemMonitorConfigFilter;

@Controller
@Scope("prototype")
public class SystemMonitorConfigFilterEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private SystemMonitorConfigFilterEditBean bean = new SystemMonitorConfigFilterEditBean();
	/**
	 * manager
	 */
	private SystemMonitorConfigFilterManager systemMonitorConfigFilterManager = (SystemMonitorConfigFilterManager) ApplicationContextUtil.getBean("systemMonitorConfigFilterManager");
	
	private SystemMonitorSourcesManager systemMonitorSourcesManager = (SystemMonitorSourcesManager) ApplicationContextUtil
			.getBean("systemMonitorSourcesManager");
	
	/**
	 * 业务系统
	 */
	private SystemMonitorConfig systemMonitorConfig;

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统配置信息
	 */
	private SystemMonitorConfigFilter currentSystemMonitorConfigFilter;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$systemMonitorConfigFilterEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindCombobox();
		this.bindBean();
	}
	/**
     * 绑定下拉框.
     * @throws Exception
     */
    private void bindCombobox() throws Exception    {
    	systemMonitorConfig = (SystemMonitorConfig) arg.get("systemMonitorConfig");
        ListboxUtils.rendererForEdit(bean.getFilterName(), systemMonitorSourcesManager.getValuesListByType("2",systemMonitorConfig.getEventName()));
    }

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {
			currentSystemMonitorConfigFilter = (SystemMonitorConfigFilter) arg.get("systemMonitorConfigFilter");
			if (currentSystemMonitorConfigFilter == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			this.bean.getSystemMonitorConfigFilterEditWin().setTitle("监控配置修改");
			this.bean.getSystemMonitorConfigId().setValue(currentSystemMonitorConfigFilter.getSystemMonitorConfigId().toString());
			ListboxUtils.selectByCodeValue(this.bean.getSystemMonitorFilterSwitch(),currentSystemMonitorConfigFilter.getSystemMonitorFilterSwitch());
			ListboxUtils.selectByCodeValue(this.bean.getFilterName(),currentSystemMonitorConfigFilter.getFilterName());
			this.bean.getFilterValue().setValue(currentSystemMonitorConfigFilter.getFilterValue());
			ListboxUtils.selectByCodeValue(this.bean.getRelationOperator(),currentSystemMonitorConfigFilter.getRelationOperator());

			
		} else if ("add".equals(opType)) {
			currentSystemMonitorConfigFilter = null;
			this.bean.getSystemMonitorConfigFilterEditWin().setTitle("监控配置新增");
			systemMonitorConfig = (SystemMonitorConfig) arg.get("systemMonitorConfig");
			this.bean.getSystemMonitorConfigId().setValue(systemMonitorConfig.getSystemMonitorConfigId().toString());
		}
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {
		// 信息验证
		String msg = this.infoValid();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		SystemMonitorConfigFilter systemMonitorConfigFilter = null;
		List<SystemMonitorConfigFilter> list = null;

		if ("add".equals(opType)) {
			systemMonitorConfigFilter = new SystemMonitorConfigFilter();
		} else if ("mod".endsWith(opType)) {
			systemMonitorConfigFilter = currentSystemMonitorConfigFilter;
		}
		systemMonitorConfigFilter.setSystemMonitorConfigId(Long.valueOf(this.bean.getSystemMonitorConfigId().getValue()));
		systemMonitorConfigFilter.setSystemMonitorFilterSwitch(this.bean.getSystemMonitorFilterSwitch().getSelectedItem().getValue().toString());
		systemMonitorConfigFilter.setFilterName(this.bean.getFilterName().getSelectedItem().getValue().toString());
		systemMonitorConfigFilter.setFilterValue(this.bean.getFilterValue().getValue());
		systemMonitorConfigFilter.setRelationOperator(this.bean.getRelationOperator().getSelectedItem().getValue().toString());

		if ("add".equals(opType)) {
			this.systemMonitorConfigFilterManager.addSystemMonitorConfigFilter(systemMonitorConfigFilter);
		} else {
			this.systemMonitorConfigFilterManager.updateSystemMonitorConfigFilter(systemMonitorConfigFilter);
		}
		this.bean.getSystemMonitorConfigFilterEditWin().onClose();
		Events.postEvent("onOK", this.self, systemMonitorConfigFilter);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getSystemMonitorConfigFilterEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {

		if (StrUtil.isEmpty(this.bean.getSystemMonitorConfigId().getValue())) {
			return "监控标识必填";
		}

		if (StrUtil.isEmpty(this.bean.getSystemMonitorFilterSwitch()
				.getSelectedItem().getValue().toString())) {
			return "监控条件开关必填";
		}

		if (this.bean.getFilterName().getSelectedItem()!=null&&this.bean.getFilterName().getSelectedItem().getValue()!=null) {
			if(StrUtil.isEmpty(this.bean.getFilterName().getSelectedItem().getValue().toString())){
				return "请选择条件名";
			}
		}else{
			return "请选择条件名";
		}
		
		if (StrUtil.isEmpty(this.bean.getFilterValue().getValue())) {
			return "请填写条件值";
		}

		return null;
	}
}
