package cn.ffcs.uom.accconfig.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.accconfig.action.bean.AccConfigEditExtBean;
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.util.StrUtil;

@Controller
@Scope("prototype")
public class AccEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	private AccConfigEditExtBean bean = new AccConfigEditExtBean();
	private String opType = null;
	@Autowired
	private AccConfigManager accConfigManager;
	/**
	 * 配置
	 */
	private AccConfig accConfig;
	/**
	 * 修改的配置
	 */
	private AccConfig oldAccConfig;
	private boolean isNewInstance = true;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$accEditWindow() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldAccConfig = (AccConfig) arg.get("accConfig");
		if ("addRootNode".equals(opType)) {
			this.bean.getAccEditWindow().setTitle("新增父配置");
		} else if ("addChildNode".equals(opType)) {
			this.bean.getAccEditWindow().setTitle("新增子配置");
		}else {
			if ("view".equals(opType)) {
				this.bean.getAccEditWindow().setTitle("查看配置");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			}else{
				this.bean.getAccEditWindow().setTitle("修改配置");
			}
			if (oldAccConfig != null) {
				this.bean.getAccName().setValue(oldAccConfig.getAccName());
				isNewInstance = false;
			}
		}
	}

	public void onOk() {
		String accName = this.bean.getAccName().getValue();
		if (StrUtil.isEmpty(accName)){
			ZkUtil.showError("配置名称不能为空。", "提示信息");
			return;
		}
		if ("addRootNode".equals(opType)) {
			accConfig = new AccConfig();
			accConfig.setAccParentId(AccConfigConstants.ROOT_ACC_CONFIG_TREE);
			accConfig.setIsParent(AccConfigConstants.IS_PARENT);
		} else if ("addChildNode".equals(opType)) {
			accConfig = new AccConfig();
			accConfig.setAccParentId(oldAccConfig.getAccConfigId());
		} else if ("mod".equals(opType)) {
			accConfig = oldAccConfig;
		}
		accConfig.setAccName(accName);
		if (isNewInstance) {
			accConfigManager.saveAccConfig(accConfig);
		} else {
			accConfigManager.updateAccConfig(accConfig);
		}
		// 抛出成功事件
		Events.postEvent("onOK", bean.getAccEditWindow(), accConfig);
		bean.getAccEditWindow().onClose();
	}
	
	public void onCancel() {
		bean.getAccEditWindow().onClose();
	}

}
