package cn.ffcs.uom.accconfig.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.accconfig.action.bean.SysAccEditBean;
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysAccEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	private SysAccEditBean bean = new SysAccEditBean();
	private String opType;
	private AccConfig accConfig;
	private SysList sysList;
	private SysAccRela sysAccRela;
	@Autowired
	private AccConfigManager accConfigManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$sysAccEditWindow() throws Exception {
		this.bindBean();
	}
	
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getSysAccEditWindow().setTitle("新增配置系统关系");
			sysAccRela = (SysAccRela) arg.get("sysAccRela");
			if (sysAccRela != null) {
				accConfig = sysAccRela.getAccConfig();
				sysList = sysAccRela.getSysList();
				if(null != accConfig && !AccConfigConstants.IS_PARENT.equals(accConfig.getIsParent())){
					this.bean.getAccConfigBandboxExt().setAccConfig(accConfig);
					List<AccConfig> accConfigs = new ArrayList<AccConfig>();
					accConfigs.add(accConfig);
					this.bean.getAccConfigBandboxExt().setAccConfigs(accConfigs);
				}
				if(null != sysList && !SysListConstants.IS_PARENT.equals(sysList.getIsParent())){
					List<SysList> sysLists = new ArrayList<SysList>();
					sysLists.add(sysList);
					this.bean.getSysListBandboxExt().setSysList(sysList);
					this.bean.getSysListBandboxExt().setSysLists(sysLists);
				}
			}
		}
	}
	
	public void onOk() throws Exception {
		List<SysList> sysLists = this.bean.getSysListBandboxExt().getSysLists();
		List<AccConfig> accConfigs = this.bean.getAccConfigBandboxExt().getAccConfigs();
		if(null == sysLists || sysLists.size() <= 0){
			ZkUtil.showError("请选择系统。", "提示信息");
			return;
		}
		if(null == accConfigs || accConfigs.size() <= 0){
			ZkUtil.showError("请选择配置。", "提示信息");
			return;
		}
		Events.postEvent("onOK", bean.getSysAccEditWindow(), accConfig);
		accConfigManager.saveSysAccRelas(accConfigs, sysLists);
		ZkUtil.showInformation("配置系统关系保存成功。", "系统提示");
        bean.getSysAccEditWindow().onClose();
	}
	
	public void onCancel() throws Exception {
        bean.getSysAccEditWindow().onClose();
	}
	
}
