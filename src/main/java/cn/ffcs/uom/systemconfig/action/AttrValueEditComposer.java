package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.AttrValueEditBean;
import cn.ffcs.uom.systemconfig.manager.AttrValueManager;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Controller
@Scope("prototype")
public class AttrValueEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3966795788531940929L;
	/**
	 * 页面bean
	 */
	private AttrValueEditBean bean = new AttrValueEditBean();
	/**
	 * manager
	 */
	private AttrValueManager attrValueManager = (AttrValueManager) ApplicationContextUtil
			.getBean("attrValueManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 当前attrId
	 */
	private Long attrId;
	/**
	 * 当前属性值
	 */
	private AttrValue currentAttrValue;

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
	public void onCreate$attrValueEditWin() throws Exception {
		opType = (String) arg.get("opType");
		if ("update".equals(opType)) {
			currentAttrValue = (AttrValue) arg.get("attrValue");
			this.bindBean();
		} else {
			attrId = (Long) arg.get("attrId");
		}
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		if (this.currentAttrValue != null) {
			this.bean.getAttrValue().setValue(currentAttrValue.getAttrValue());
			if("update".equals(opType)){
				this.bean.getAttrValue().setDisabled(true);
			}
			this.bean.getAttrValueName().setValue(
					currentAttrValue.getAttrValueName());
			this.bean.getAttrDesc().setValue(currentAttrValue.getAttrDesc());
		}
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		if (StrUtil.isEmpty(this.bean.getAttrValue().getValue())) {
			ZkUtil.showError("属性值必填", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(this.bean.getAttrValueName().getValue())) {
			ZkUtil.showError("取值名称必填", "提示信息");
			return;
		}
		AttrValue attrValue = null;
		if ("add".equals(opType)) {
			attrValue = new AttrValue(true);
			attrValue.setAttrId(attrId);
		} else {
			attrValue = currentAttrValue;
		}
		String attrValueTemp = attrValue.getAttrValue();

		attrValue.setAttrValue(this.bean.getAttrValue().getValue());
		attrValue.setAttrDesc(this.bean.getAttrDesc().getValue());
		attrValue.setAttrValueName(this.bean.getAttrValueName().getValue());
		/**
		 * 新增或者有做修改时
		 */
		if ("add".equals(opType)
				|| ("update".equals(opType) && !attrValue.getAttrValue()
						.equals(attrValueTemp))) {
			AttrValue queryAttrValue = new AttrValue();
			queryAttrValue.setAttrId(attrValue.getAttrId());
			queryAttrValue.setAttrValue(attrValue.getAttrValue());
			List<AttrValue> list = this.attrValueManager
					.queryAttrValueListByQueryAttrValue(queryAttrValue);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("属性值重复", "提示信息");
				return;
			}
		}
		if ("add".equals(opType)) {
			this.attrValueManager.saveAttrValue(attrValue);
		} else {
			this.attrValueManager.updateAttrValue(attrValue);
		}
		this.bean.getAttrValueEditWin().onClose();
		Events.postEvent("onOK", this.self, attrValue);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getAttrValueEditWin().onClose();
	}
}
