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
import cn.ffcs.uom.bpm.bean.PrincipalConfigEditBean;
import cn.ffcs.uom.bpm.manager.BmpSystemConfigManager;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.bpm.model.QaUnPrincipal;
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
public class PrincipalConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private PrincipalConfigEditBean bean = new PrincipalConfigEditBean();
	
	@Autowired
	private BmpSystemConfigManager bmpSystemConfigManager;
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统信息
	 */
	private QaUnPrincipal qaUnPrincipal;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$principalConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		qaUnPrincipal = (QaUnPrincipal) arg.get("principal");
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

		if (StrUtil.isEmpty(this.bean.getPrincipalName().getValue())) {
			ZkUtil.showError("干系人名称必填", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(this.bean.getCellNum().getValue())) {
			ZkUtil.showError("手机号码必填", "提示信息");
			return;
		}

		List<QaUnPrincipal> list = null;
		String systemCode = null;

		if ("add".equals(opType)) {
			qaUnPrincipal = new QaUnPrincipal();
		} else if ("mod".endsWith(opType)) {
			//businessSystem = currentBusinessSystem;
			//systemCode = currentBusinessSystem.getSystemCode();
			if (qaUnPrincipal == null) {
				ZkUtil.showError("请先选择干系人", "提示信息");
				return;
			}
		}

		qaUnPrincipal.setPrincipalName(this.bean.getPrincipalName().getValue());
		qaUnPrincipal.setCellNum(this.bean.getCellNum().getValue());
		qaUnPrincipal.setEmail(this.bean.getEmail().getValue());
		qaUnPrincipal.setSort(this.bean.getSort().getValue());
		qaUnPrincipal.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if ("add".equals(opType)) {
			list = this.bmpSystemConfigManager.qryPrincipal(qaUnPrincipal);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("该干系人已存在", "提示信息");
				return;
			}
			this.bmpSystemConfigManager.savePrincipal(qaUnPrincipal);
		} else {

		}
		this.bean.getPrincipalConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, qaUnPrincipal);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getPrincipalConfigEditWin().onClose();
	}
}
