package cn.ffcs.uom.bpm.comp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.bpm.bean.InformConfigEditBean;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.BusinessSystemEditBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;

@Controller
@Scope("prototype")
public class InformConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private InformConfigEditBean bean = new InformConfigEditBean();
	
	@Autowired
	private QaInformMethodManager qaInformMethodManager;
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统信息
	 */
	private QaInformMethod informMethod;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$informConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
/*		if ("mod".equals(opType)) {

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
		}*/
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {

		if (StrUtil.isEmpty(this.bean.getInformMethodName().getValue())) {
			ZkUtil.showError("通知方式名称必填", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(this.bean.getInformMethodCode().getValue())) {
			ZkUtil.showError("通知方式编码必填", "提示信息");
			return;
		}

		QaInformMethod informMethod = null;
		List<QaInformMethod> list = null;
		String systemCode = null;

		if ("add".equals(opType)) {
		    informMethod = new QaInformMethod();
		} else if ("mod".endsWith(opType)) {
			//businessSystem = currentBusinessSystem;
			//systemCode = currentBusinessSystem.getSystemCode();
		}

		informMethod.setInformName(this.bean.getInformMethodName().getValue());
		informMethod.setInformType(this.bean.getInformMethodCode().getValue());
		informMethod.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if ("add".equals(opType)) {
			list = this.qaInformMethodManager.getInformMethod(informMethod);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("该通知方式编码：" + informMethod.getInformType()
						+ ",已被使用,请重新分配通知方式编码!", "提示信息");
				return;
			}
			this.qaInformMethodManager.saveInformMethod(informMethod);
		} else {

		}
		this.bean.getInformConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, informMethod);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getInformConfigEditWin().onClose();
	}
}
