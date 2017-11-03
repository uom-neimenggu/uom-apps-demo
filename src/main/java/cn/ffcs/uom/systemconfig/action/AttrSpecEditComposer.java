package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.action.bean.AttrSpecEditBean;
import cn.ffcs.uom.systemconfig.manager.AttrSpecManager;
import cn.ffcs.uom.systemconfig.model.AttrSpec;

@Controller
@Scope("prototype")
public class AttrSpecEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -628913125852992640L;
	/**
	 * 页面bean
	 */
	private AttrSpecEditBean bean = new AttrSpecEditBean();
	/**
	 * manager
	 */
	private AttrSpecManager attrSpecManager = (AttrSpecManager) ApplicationContextUtil
			.getBean("attrSpecManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 当前classId
	 */
	private Long classId;
	/**
	 * 当前属性
	 */
	private AttrSpec currentAttrSpec;

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
	public void onCreate$attrSpecEditWin() throws Exception {
		this.bindBox();
		opType = (String) arg.get("opType");
		if ("update".equals(opType)) {
			currentAttrSpec = (AttrSpec) arg.get("attrSpec");
			this.bindBean();
		} else {
			classId = (Long) arg.get("classId");
		}
	}

	/**
	 * 绑定combobox和listBox.
	 * 
	 */
	private void bindBox() throws Exception {
		List<NodeVo> attrTypeList = UomClassProvider.getValuesList("AttrSpec",
				"attrType");
		ListboxUtils.rendererForEdit(this.bean.getAttrType(), attrTypeList);
		List<NodeVo> attrValueTypeList = UomClassProvider.getValuesList(
				"AttrSpec", "attrValueType");
		ListboxUtils.rendererForEdit(this.bean.getAttrValueType(),
				attrValueTypeList);
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		if (this.currentAttrSpec != null) {
			this.bean.getJavaCode()
					.setValue(this.currentAttrSpec.getJavaCode());
			this.bean.getAttrCd().setValue(this.currentAttrSpec.getAttrCd());
			this.bean.getAttrName()
					.setValue(this.currentAttrSpec.getAttrName());
			ListboxUtils.selectByCodeValue(this.bean.getAttrType(),
					this.currentAttrSpec.getAttrType());
			ListboxUtils.selectByCodeValue(this.bean.getAttrValueType(),
					this.currentAttrSpec.getAttrValueType());
			this.bean.getAttrDesc()
					.setValue(this.currentAttrSpec.getAttrDesc());
		}
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		if (StrUtil.isEmpty(this.bean.getJavaCode().getValue())) {
			ZkUtil.showError("程序编码必填", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(this.bean.getAttrCd().getValue())) {
			ZkUtil.showError("属性编码必填", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(this.bean.getAttrName().getValue())) {
			ZkUtil.showError("属性名称必填", "提示信息");
			return;
		}
		if (this.bean.getAttrType().getSelectedItem() == null) {
			ZkUtil.showError("属性类型必填", "提示信息");
			return;
		}
		if (this.bean.getAttrValueType().getSelectedItem() == null) {
			ZkUtil.showError("属性值类型必填", "提示信息");
			return;
		}
		AttrSpec attrSpec = null;
		AttrSpec oldAttrSpec = new AttrSpec();
		oldAttrSpec = currentAttrSpec;
		if ("add".equals(opType)) {
			attrSpec = new AttrSpec(true);
			attrSpec.setClassId(classId);
		} else {
			attrSpec = currentAttrSpec;
		}
		/**
		 * 修改时保存旧的值
		 */
		String javaCodeTemp = attrSpec.getJavaCode();
		String attrCdTemp = attrSpec.getAttrCd();

		attrSpec.setJavaCode(this.bean.getJavaCode().getValue());
		attrSpec.setAttrCd(this.bean.getAttrCd().getValue());
		attrSpec.setAttrName(this.bean.getAttrName().getValue());
		attrSpec.setAttrType((String) this.bean.getAttrType().getSelectedItem()
				.getValue());
		attrSpec.setAttrValueType((String) this.bean.getAttrValueType()
				.getSelectedItem().getValue());
		attrSpec.setAttrDesc(this.bean.getAttrDesc().getValue());
		AttrSpec queryAttrSpec = new AttrSpec();
		List list = null;
		/**
		 * 新增或者有做修改时
		 */
		if ("add".equals(opType)
				|| ("update".equals(opType) && !attrSpec.getJavaCode().equals(
						javaCodeTemp))) {
			queryAttrSpec = new AttrSpec();
			queryAttrSpec.setClassId(attrSpec.getClassId());
			queryAttrSpec.setJavaCode(attrSpec.getJavaCode());
			list = this.attrSpecManager
					.queryAttrSpecListByQueryAttrSpec(queryAttrSpec);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("程序编码已存在,请重新输入", "提示信息");
				return;
			}
		}
		if ("add".equals(opType)
				|| ("update".equals(opType) && !attrSpec.getAttrCd().equals(
						attrCdTemp))) {
			queryAttrSpec.setClassId(attrSpec.getClassId());
			queryAttrSpec.setAttrCd(attrSpec.getAttrCd());
			list = this.attrSpecManager
					.queryAttrSpecListByQueryAttrSpec(queryAttrSpec);
			if (list != null && list.size() > 0) {
				ZkUtil.showError("属性编码已存在,请重新输入", "提示信息");
				return;
			}
		}
		if ("add".equals(opType)) {
			this.attrSpecManager.saveAttrSpec(attrSpec);
		} else {
			this.attrSpecManager.updateAttrSpec(attrSpec);
		}
		this.bean.getAttrSpecEditWin().onClose();
		Events.postEvent("onOK", this.self, attrSpec);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getAttrSpecEditWin().onClose();
	}

}
