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
import cn.ffcs.uom.systemconfig.action.bean.BusinessSystemEditBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;

@Controller
@Scope("prototype")
public class BusinessSystemEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private BusinessSystemEditBean bean = new BusinessSystemEditBean();
	/**
	 * manager
	 */
	private BusinessSystemManager businessSystemManager = (BusinessSystemManager) ApplicationContextUtil
			.getBean("businessSystemManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统信息
	 */
	private BusinessSystem currentBusinessSystem;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$businessSystemEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

			currentBusinessSystem = (BusinessSystem) arg.get("businessSystem");

			if (currentBusinessSystem == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getBusinessSystemEditWin().setTitle("业务系统信息修改");

			this.bean.getBusinessSystemId().setValue(
					currentBusinessSystem.getBusinessSystemId().toString());
			this.bean.getSystemCode().setValue(
					currentBusinessSystem.getSystemCode());
			this.bean.getSystemName().setValue(
					currentBusinessSystem.getSystemName());
			this.bean.getSystemDesc().setValue(
					currentBusinessSystem.getSystemDesc());

		} else if ("add".equals(opType)) {
			currentBusinessSystem = null;
			this.bean.getBusinessSystemEditWin().setTitle("业务系统信息新增");
		}
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {

		if (StrUtil.isEmpty(this.bean.getSystemCode().getValue())) {
			ZkUtil.showError("系统编码必填", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(this.bean.getSystemName().getValue())) {
			ZkUtil.showError("组织树名称必填", "提示信息");
			return;
		}

		BusinessSystem businessSystem = null;
		List<BusinessSystem> list = null;
		String systemCode = null;

		if ("add".equals(opType)) {
			businessSystem = new BusinessSystem();
		} else if ("mod".endsWith(opType)) {
			businessSystem = currentBusinessSystem;
			systemCode = currentBusinessSystem.getSystemCode();
		}

		businessSystem.setSystemCode(this.bean.getSystemCode().getValue());
		businessSystem.setSystemName(this.bean.getSystemName().getValue());
		businessSystem.setSystemDesc(this.bean.getSystemDesc().getValue());

		if ("add".equals(opType)) {
			list = this.businessSystemManager
					.queryBusinessSystemList(businessSystem);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("该系统编码：" + businessSystem.getSystemCode()
						+ ",已被使用,请重新分配系统编码!", "提示信息");
				return;
			}
			this.businessSystemManager.addBusinessSystem(businessSystem);
		} else {
			if (!businessSystem.getSystemCode().equals(systemCode)) {
				list = this.businessSystemManager
						.queryBusinessSystemList(businessSystem);
				if (list != null && list.size() > 0) {
					ZkUtil.showError("该系统编码：" + businessSystem.getSystemCode()
							+ ",已被使用,请重新分配系统编码!", "提示信息");
					return;
				}
			}
			this.businessSystemManager.updateBusinessSystem(businessSystem);
		}
		this.bean.getBusinessSystemEditWin().onClose();
		Events.postEvent("onOK", this.self, businessSystem);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getBusinessSystemEditWin().onClose();
	}
}
