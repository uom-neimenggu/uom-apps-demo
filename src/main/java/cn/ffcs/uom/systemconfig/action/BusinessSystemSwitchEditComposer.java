package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.BusinessSystemSwitchEditBean;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageConfigManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Controller
@Scope("prototype")
public class BusinessSystemSwitchEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private BusinessSystemSwitchEditBean bean = new BusinessSystemSwitchEditBean();

	/**
	 * manager
	 */
	private SystemIntfInfoConfigManager systemIntfInfoConfigManager = (SystemIntfInfoConfigManager) ApplicationContextUtil
			.getBean("systemIntfInfoConfigManager");

	private SystemMessageConfigManager systemMessageConfigManager = (SystemMessageConfigManager) ApplicationContextUtil
			.getBean("systemMessageConfigManager");

	private OrgTreeManager orgTreeManager = (OrgTreeManager) ApplicationContextUtil
			.getBean("orgTreeManager");

	/**
	 * 操作类型
	 */
	private String opType;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$businessSystemSwitchEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("threshold".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("阀值开关修改");
		} else if ("ftpNotice".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("FTP通知开关修改");
		} else if ("smsNotice".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("短信通知开关修改");
		} else if ("mailSend".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("邮件发送开关修改");
		} else if ("ftpIncreaseTimer".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("FTP增量定时开关修改");
		} else if ("ftpAllTimer".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("FTP全量定时开关修改");
		} else if ("ftpTimer".equals(opType)) {
			this.bean.getBusinessSystemSwitchEditWin().setTitle("FTP定时开关修改");
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

		String systemControlSwitch = this.bean.getSystemControlSwitch()
				.getSelectedItem().getValue().toString();

		if (!StrUtil.isEmpty(systemControlSwitch)) {

			if ("threshold".equals(opType)) {

				List<AttrValue> list = UomClassProvider.jdbcGetAttrValue(
						"IntfSwitch", Constants.THRESHOLD_ALARM,
						BaseUnitConstants.ENTT_STATE_ACTIVE);
				if (list != null && list.size() > 0) {
					AttrValue attrValue = list.get(0);
					attrValue.setAttrValue(systemControlSwitch);
					attrValue.update();
				}

			} else if ("ftpNotice".equals(opType)) {

				List<SystemIntfInfoConfig> systemIntfInfoConfigList = systemIntfInfoConfigManager
						.querySystemIntfInfoConfigList(null);

				if (systemIntfInfoConfigList != null
						&& systemIntfInfoConfigList.size() > 0) {
					for (SystemIntfInfoConfig systemIntfInfoConfig : systemIntfInfoConfigList) {
						systemIntfInfoConfig
								.setIntfSwitchIncrease(systemControlSwitch);
						systemIntfInfoConfig
								.setIntfSwitchAll(systemControlSwitch);
						systemIntfInfoConfig.updateOnly();
					}
				}

			} else if ("smsNotice".equals(opType)) {

				List<SystemMessageConfig> systemMessageConfigList = systemMessageConfigManager
						.querySystemMessageConfigList(null);

				if (systemMessageConfigList != null
						&& systemMessageConfigList.size() > 0) {
					for (SystemMessageConfig systemMessageConfig : systemMessageConfigList) {
						if (!WsConstants.SYSTEM_CODE_UOM_EMAIL
								.equals(systemMessageConfig.getSystemCode())) {
							systemMessageConfig
									.setSystemMessageSwitch(systemControlSwitch);
							systemMessageConfig.updateOnly();
						}
					}
				}

			} else if ("mailSend".equals(opType)) {

				SystemMessageConfig querySystemMessageConfig = new SystemMessageConfig();
				querySystemMessageConfig
						.setSystemCode(WsConstants.SYSTEM_CODE_UOM_EMAIL);

				List<SystemMessageConfig> systemMessageConfigList = systemMessageConfigManager
						.querySystemMessageConfigList(querySystemMessageConfig);

				if (systemMessageConfigList != null
						&& systemMessageConfigList.size() > 0) {
					for (SystemMessageConfig systemMessageConfig : systemMessageConfigList) {
						systemMessageConfig
								.setSystemMessageSwitch(systemControlSwitch);
						systemMessageConfig.updateOnly();
					}
				}

			} else if ("ftpIncreaseTimer".equals(opType)) {

				OrgTree orgTree = (OrgTree) arg.get("orgTree");

				if (orgTree != null && orgTree.getOrgTreeId() != null) {

					List<AttrValue> listIncrease = UomClassProvider
							.jdbcGetAttrValue("IntfSwitch", orgTree
									.getOrgTreeId().toString(),
									BaseUnitConstants.ENTT_STATE_ACTIVE);
					if (listIncrease != null && listIncrease.size() > 0) {
						AttrValue attrValue = listIncrease.get(0);
						attrValue.setAttrValue(systemControlSwitch);
						attrValue.update();
					}

				}

			} else if ("ftpAllTimer".equals(opType)) {

				OrgTree orgTree = (OrgTree) arg.get("orgTree");

				if (orgTree != null && orgTree.getOrgTreeId() != null) {

					List<AttrValue> listAll = UomClassProvider
							.jdbcGetAttrValue(
									"IntfSwitch",
									Constants.TREE_FULL_SWITCH
											+ orgTree.getOrgTreeId(),
									BaseUnitConstants.ENTT_STATE_ACTIVE);
					if (listAll != null && listAll.size() > 0) {
						AttrValue attrValue = listAll.get(0);
						attrValue.setAttrValue(systemControlSwitch);
						attrValue.update();
					}
				}

			} else if ("ftpTimer".equals(opType)) {

				List<OrgTree> orgTreeList = orgTreeManager
						.queryOrgTreeList(null);

				if (orgTreeList != null && orgTreeList.size() > 0) {
					for (OrgTree orgTree : orgTreeList) {

						List<AttrValue> listIncrease = UomClassProvider
								.jdbcGetAttrValue("IntfSwitch", orgTree
										.getOrgTreeId().toString(),
										BaseUnitConstants.ENTT_STATE_ACTIVE);
						if (listIncrease != null && listIncrease.size() > 0) {
							AttrValue attrValue = listIncrease.get(0);
							attrValue.setAttrValue(systemControlSwitch);
							attrValue.update();
						}

						List<AttrValue> listAll = UomClassProvider
								.jdbcGetAttrValue(
										"IntfSwitch",
										Constants.TREE_FULL_SWITCH
												+ orgTree.getOrgTreeId(),
										BaseUnitConstants.ENTT_STATE_ACTIVE);
						if (listAll != null && listAll.size() > 0) {
							AttrValue attrValue = listAll.get(0);
							attrValue.setAttrValue(systemControlSwitch);
							attrValue.update();
						}

					}
				}

			}
		}

		this.bean.getBusinessSystemSwitchEditWin().onClose();

		Events.postEvent("onOK", this.self, systemControlSwitch);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getBusinessSystemSwitchEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() {

		if (StrUtil.isEmpty(this.bean.getSystemControlSwitch()
				.getSelectedItem().getValue().toString())) {
			return "控制开关必填";
		}

		return null;
	}
}
