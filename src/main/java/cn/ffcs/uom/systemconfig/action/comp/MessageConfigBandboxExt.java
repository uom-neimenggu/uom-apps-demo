package cn.ffcs.uom.systemconfig.action.comp;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.model.SystemConfigUser;

@Controller
@Scope("prototype")
public class MessageConfigBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/system_config/comp/message_config_bandbox_ext.zul";
	/**
	 * 选择的人员
	 */
	@Getter
	private SystemConfigUser systemConfigUser;
	/**
	 * 组织列表
	 */
	@Getter
	@Setter
	private MessageConfigListboxComposer messageConfigListboxExt;

	/**
	 * 构造函数
	 */
	public MessageConfigBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		messageConfigListboxExt.bean.getSystemMessageConfigListPaging()
				.setPageSize(10);
		messageConfigListboxExt.bean.getMessageConfigWindowDiv()
				.setVisible(false);
		messageConfigListboxExt.bean.getMessageConfigBandboxDiv()
				.setVisible(true);

		/**
		 * 监听事件
		 */
		this.messageConfigListboxExt.addForward(
				SystemConfigConstant.ON_SELECT_SYSTEM_CONFIG_USER, this,
				"onSelectSystemConfigUserResponse");
		this.messageConfigListboxExt.addForward(
				SystemConfigConstant.ON_CLEAN_SYSTEM_CONFIG_USER, this,
				"onCleanSystemConfigUserResponse");
		this.messageConfigListboxExt.addForward(
				SystemConfigConstant.ON_CLOSE_SYSTEM_CONFIG_USER, this,
				"onCloseSystemConfigUserResponse");
	}

	public void setSystemConfigUser(SystemConfigUser systemConfigUser ) {
		this.setValue(systemConfigUser == null ? "" : systemConfigUser.getUserName());
		this.systemConfigUser = systemConfigUser;
	}

	/**
	 * 选择人员
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectSystemConfigUserResponse(final ForwardEvent event)
			throws Exception {
		systemConfigUser = (SystemConfigUser) event.getOrigin().getData();
		if (systemConfigUser != null) {
			this.setValue(systemConfigUser.getUserName());
		}
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanSystemConfigUserResponse(final ForwardEvent event)
			throws Exception {
		this.setSystemConfigUser(null);
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 关闭窗口
	 * 
	 * @param eventt
	 * @throws Exception
	 */
	public void onCloseSystemConfigUserResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}
	
}
