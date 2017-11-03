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
import cn.ffcs.uom.bpm.bean.IndicatorBaseInfoEditBean;
import cn.ffcs.uom.bpm.bean.IndicatorConfigEditBean;
import cn.ffcs.uom.bpm.manager.IndicatorManager;
import cn.ffcs.uom.bpm.model.QaUnAssmCrt;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;

@Controller
@Scope("prototype")
public class IndicatorBaseInfoEditComposer extends BasePortletComposer {

    private static final long serialVersionUID = 1L;

    /**
	 * 页面bean
	 */
	private IndicatorBaseInfoEditBean bean = new IndicatorBaseInfoEditBean();
	
	@Autowired
	private IndicatorManager indicatorManager;
	/**
	 * 操作类型
	 */
	private String opType;

	private  QaUnAssmCrt qaUnAssmCrt;
	private  QaUnOppExecScript qaUnOppExecScript;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$indicatorBaseInfoEditWin() throws Exception {
		opType = (String) arg.get("opType");
		qaUnOppExecScript = (QaUnOppExecScript) arg.get("qaUnOppExecScript");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

		    qaUnAssmCrt = (QaUnAssmCrt) arg.get("qaUnAssmCrt");

			if (qaUnAssmCrt == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			this.bean.getPerOfPass().setValue(qaUnAssmCrt.getPerOfPass());
			this.bean.getSalt().setValue(qaUnAssmCrt.getSalt());
		} else if ("add".equals(opType)) {
		    qaUnAssmCrt = null;
		}
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {

		if (StrUtil.isEmpty(this.bean.getPerOfPass().getValue())) {
			ZkUtil.showError("标准合格率必填", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(this.bean.getSalt().getValue())) {
			ZkUtil.showError("分值位必填", "提示信息");
			return;
		}
		List<QaUnOppExecScript> list = null;

		if ("add".equals(opType)) {
		    qaUnAssmCrt = new QaUnAssmCrt();
		    if (qaUnOppExecScript != null) {
                qaUnAssmCrt.setExecSctIdenti(qaUnOppExecScript.getExecSctIdenti());
            }else {
                ZkUtil.showError("请先选择稽核项", "提示信息");
                return;
            }
		} else if ("mod".equals(opType)) {
			//businessSystem = currentBusinessSystem;
			//systemCode = currentBusinessSystem.getSystemCode();
		}

		qaUnAssmCrt.setPerOfPass(this.bean.getPerOfPass().getValue());
		qaUnAssmCrt.setSalt(this.bean.getSalt().getValue());
		if ("add".equals(opType)) {
		    qaUnAssmCrt.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
			this.indicatorManager.saveQaUnAssmCrt(qaUnAssmCrt);
		} else if("mod".equals(opType)){
		    this.indicatorManager.saveQaUnAssmCrt(qaUnAssmCrt);
		}
		this.bean.getIndicatorBaseInfoEditWin().onClose();
		Events.postEvent("onOK", this.self, qaUnAssmCrt);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getIndicatorBaseInfoEditWin().onClose();
	}
}
