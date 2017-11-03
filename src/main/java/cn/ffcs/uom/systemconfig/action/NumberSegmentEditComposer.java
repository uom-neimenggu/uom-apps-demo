package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.NumberSegmentEditBean;
import cn.ffcs.uom.systemconfig.manager.NumberSegmentManager;
import cn.ffcs.uom.systemconfig.model.NumberSegment;

@Controller
@Scope("prototype")
public class NumberSegmentEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private NumberSegmentEditBean bean = new NumberSegmentEditBean();
	/**
	 * manager
	 */
	private NumberSegmentManager numberSegmentManager = (NumberSegmentManager) ApplicationContextUtil
			.getBean("numberSegmentManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 
	 */
	private NumberSegment currentNumberSegment;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate$numberSegmentEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {
			currentNumberSegment = (NumberSegment) arg.get("numberSegment");
			if (currentNumberSegment == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			ListboxUtils.selectByCodeValue(this.bean.getOperationBusiness(),currentNumberSegment.getOperationBusiness() + "");
			this.bean.getNumberSegment().setValue(currentNumberSegment.getNumberSegment());
			this.bean.getRemarks().setValue(currentNumberSegment.getRemarks());
			ListboxUtils.selectByCodeValue(this.bean.getStatusCd(),currentNumberSegment.getStatusCd() + "");
			this.bean.getNumberSegmentEditWin().setTitle("号码段信息修改");
		} else if ("add".equals(opType)) {
			this.bean.getNumberSegmentEditWin().setTitle("号码段信息新增");
		}
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		if (StrUtil.isEmpty(this.bean.getNumberSegment().getValue())) {
			ZkUtil.showError("号码段不能为空！", "提示信息");
			return;
		}
		if (null==this.bean.getOperationBusiness().getSelectedItem()||null==this.bean.getOperationBusiness().getSelectedItem().getValue()) {
			ZkUtil.showError("请选择网络运营商！", "提示信息");
			return;
		}
		if (null==this.bean.getStatusCd().getSelectedItem()||null==this.bean.getStatusCd().getSelectedItem().getValue()) {
			ZkUtil.showError("请选择状态！", "提示信息");
			return;
		}
		NumberSegment numberSegment = null;
		if ("add".equals(opType)) {
			numberSegment = new NumberSegment();
		} else if ("mod".endsWith(opType)) {
			numberSegment = currentNumberSegment;
		}

		numberSegment.setOperationBusiness(this.bean.getOperationBusiness().getSelectedItem().getValue().toString());
		numberSegment.setStatusCd(this.bean.getStatusCd().getSelectedItem().getValue().toString());
		numberSegment.setNumberSegment(this.bean.getNumberSegment().getValue());
		numberSegment.setRemarks(this.bean.getRemarks().getValue());
		
		if ("add".equals(opType)) {
			this.numberSegmentManager.saveNumberSegment(numberSegment);
		} else {
			this.numberSegmentManager.updateNumberSegment(numberSegment);
		}
		this.bean.getNumberSegmentEditWin().onClose();
		Events.postEvent("onOK", this.self, numberSegment);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getNumberSegmentEditWin().onClose();
	}
}
