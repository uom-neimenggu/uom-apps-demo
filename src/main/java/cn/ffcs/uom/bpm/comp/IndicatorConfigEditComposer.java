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
import cn.ffcs.uom.bpm.bean.IndicatorConfigEditBean;
import cn.ffcs.uom.bpm.manager.IndicatorManager;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;

@Controller
@Scope("prototype")
public class IndicatorConfigEditComposer extends BasePortletComposer {

    private static final long serialVersionUID = 1L;

    /**
	 * 页面bean
	 */
	private IndicatorConfigEditBean bean = new IndicatorConfigEditBean();
	
	@Autowired
	private IndicatorManager indicatorManager;
	/**
	 * 操作类型
	 */
	private String opType;

	private  QaUnOppExecScript qaUnOppExecScript;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$indicatorConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

		    qaUnOppExecScript = (QaUnOppExecScript) arg.get("qaUnOppExecScript");

			if (qaUnOppExecScript == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getIndicatorConfigEditWin().setTitle("稽核项修改");

			this.bean.getIndicatorName().setValue(qaUnOppExecScript.getExecName());
			this.bean.getIndicatorCode().setValue(
			    qaUnOppExecScript.getExecCode());
			this.bean.getIndicatorType().setValue(qaUnOppExecScript.getCheckType());
			this.bean.getSystem().setValue(qaUnOppExecScript.getSystemCode());

		} else if ("add".equals(opType)) {
		    qaUnOppExecScript = null;
			this.bean.getIndicatorConfigEditWin().setTitle("稽核项新增");
		}
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {

		if (StrUtil.isEmpty(this.bean.getIndicatorName().getValue())) {
			ZkUtil.showError("稽核项名称必填", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(this.bean.getIndicatorCode().getValue())) {
			ZkUtil.showError("稽核项编码必填", "提示信息");
			return;
		}
        if (StrUtil.isEmpty(this.bean.getIndicatorType().getValue())) {
            ZkUtil.showError("稽核项类型必填", "提示信息");
            return;
        }
        if (StrUtil.isEmpty(this.bean.getSystem().getValue())) {
            ZkUtil.showError("稽核系统必填", "提示信息");
            return;
        }
		List<QaUnOppExecScript> list = null;

		if ("add".equals(opType)) {
		    qaUnOppExecScript = new QaUnOppExecScript();
		} else if ("mod".equals(opType)) {
			//businessSystem = currentBusinessSystem;
			//systemCode = currentBusinessSystem.getSystemCode();
		}

		qaUnOppExecScript.setExecName(this.bean.getIndicatorName().getValue());
		qaUnOppExecScript.setExecCode(this.bean.getIndicatorCode().getValue());
		qaUnOppExecScript.setCheckType(this.bean.getIndicatorType().getValue());
		qaUnOppExecScript.setSystemCode(this.bean.getSystem().getValue());
		if ("add".equals(opType)) {
			list = this.indicatorManager.qryIndicator(qaUnOppExecScript);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("该稽核项编码：" + qaUnOppExecScript.getExecCode()
						+ ",已被使用,请重新分配稽核项编码!", "提示信息");
				return;
			}
			qaUnOppExecScript.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
			this.indicatorManager.saveIndicator(qaUnOppExecScript);
		} else if("mod".equals(opType)){
		    this.indicatorManager.saveIndicator(qaUnOppExecScript);
		}
		this.bean.getIndicatorConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, qaUnOppExecScript);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getIndicatorConfigEditWin().onClose();
	}
}
