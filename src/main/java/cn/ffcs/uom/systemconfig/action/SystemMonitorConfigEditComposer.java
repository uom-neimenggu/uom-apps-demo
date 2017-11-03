package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.SystemMonitorConfigEditBean;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMonitorSourcesManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;

@Controller
@Scope("prototype")
public class SystemMonitorConfigEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private SystemMonitorConfigEditBean bean = new SystemMonitorConfigEditBean();
	/**
	 * manager
	 */
	private SystemMonitorConfigManager systemMonitorConfigManager = (SystemMonitorConfigManager) ApplicationContextUtil
			.getBean("systemMonitorConfigManager");
	
	private SystemMonitorSourcesManager systemMonitorSourcesManager = (SystemMonitorSourcesManager) ApplicationContextUtil
			.getBean("systemMonitorSourcesManager");
	
	/**
	 * 业务系统
	 */
	private BusinessSystem businessSystem;

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统配置信息
	 */
	private SystemMonitorConfig currentSystemMonitorConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$systemMonitorConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindCombobox();
		this.bindBean();
	}
	  /**
     * 绑定下拉框.
     * @throws Exception
     */
    private void bindCombobox() throws Exception    {
        ListboxUtils.rendererForEdit(bean.getEventName(), systemMonitorSourcesManager.getValuesListByType("1",null));
    }
	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {
			currentSystemMonitorConfig = (SystemMonitorConfig) arg.get("systemMonitorConfig");
			if (currentSystemMonitorConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			this.bean.getSystemMonitorConfigEditWin().setTitle("监控配置修改");
			ListboxUtils.selectByCodeValue(this.bean.getSystemMonitorSwitch(),currentSystemMonitorConfig.getSystemMonitorSwitch());
			//this.bean.getEventName().setValue(currentSystemMonitorConfig.getEventName());
			ListboxUtils.selectByCodeValue(this.bean.getEventName(), currentSystemMonitorConfig.getEventName());
			this.bean.getSystemCode().setValue(currentSystemMonitorConfig.getSystemCode());
		} else if ("add".equals(opType)) {
			currentSystemMonitorConfig = null;
			this.bean.getSystemMonitorConfigEditWin().setTitle("监控配置新增");
			businessSystem = (BusinessSystem) arg.get("businessSystem");
			this.bean.getSystemCode().setValue(businessSystem.getSystemCode());
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

		SystemMonitorConfig systemMonitorConfig = null;
		List<SystemMonitorConfig> list = null;

		if ("add".equals(opType)) {
			systemMonitorConfig = new SystemMonitorConfig();
		} else if ("mod".endsWith(opType)) {
			systemMonitorConfig = currentSystemMonitorConfig;
		}

		systemMonitorConfig.setSystemCode(this.bean.getSystemCode().getValue());

		systemMonitorConfig.setSystemMonitorSwitch(this.bean.getSystemMonitorSwitch().getSelectedItem().getValue().toString());

		systemMonitorConfig.setEventName(this.bean.getEventName().getSelectedItem().getValue().toString());
		

		if ("add".equals(opType)) {
			this.systemMonitorConfigManager.addSystemMonitorConfig(systemMonitorConfig);
		} else {
			this.systemMonitorConfigManager.updateSystemMonitorConfig(systemMonitorConfig);
		}
		this.bean.getSystemMonitorConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, systemMonitorConfig);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getSystemMonitorConfigEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {
		if (StrUtil.isEmpty(this.bean.getSystemCode().getValue())) {
			return "系统编码必填";
		}
		if (StrUtil.isEmpty(this.bean.getSystemMonitorSwitch()
				.getSelectedItem().getValue().toString())) {
			return "监控开关必填";
		}

		if (this.bean.getEventName().getSelectedItem()!=null&&this.bean.getEventName().getSelectedItem().getValue()!=null) {
			if(StrUtil.isEmpty(this.bean.getEventName().getSelectedItem().getValue().toString())){
				return "请选择事件名";
			}
		}else{
			return "请选择事件名";
		}
		
		if(systemMonitorConfigManager.checkDataIsExist(this.bean.getSystemCode().getValue(),this.bean.getEventName().getSelectedItem().getValue().toString())){
			return "事件已经存在！";
		}
		
		return null;
	}
}
