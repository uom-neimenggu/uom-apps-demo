package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ComboboxUtils;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.action.bean.SysClassBaseInfoBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.SysClassManager;
import cn.ffcs.uom.systemconfig.model.AttrSpec;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Controller
@Scope("prototype")
public class SysClassBaseInfoComposer extends BasePortletComposer implements IPortletInfoProvider{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2246197999050789977L;
	/**
	 * 页面bean
	 */
	private SysClassBaseInfoBean bean = new SysClassBaseInfoBean();
	/**
	 * 
	 */
	private SysClass currentSysClass;
	/**
	 * 判断新增还是修改
	 */
	private Boolean isNewEntity;

	private SysClassManager sysClassManager = (SysClassManager) ApplicationContextUtil
			.getBean("sysClassManager");

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}
	
	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.self.getRoot().addForward(
				SystemConfigConstant.ON_SYS_CLASS_SELECT, comp,
				SystemConfigConstant.ON_SYS_CLASS_SELECT_RESPONSE);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$sysClassBaseInfoWin() throws Exception {
		this.setComponentValid(false);
		this.setSysClassButtonValid(true, false, false, false, false);
		this.bindBox();
	}

	/**
	 * 绑定combobox和listBox.
	 * 
	 */
	private void bindBox() throws Exception {
		List<NodeVo> tableTypeList = UomClassProvider.getValuesList("SysClass",
				"tableType");
		ComboboxUtils.rendererForQuery(this.bean.getTableType(), tableTypeList);
	}

	/**
	 * 点击类时间响应
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectSysClassResponse(final ForwardEvent event)
			throws Exception {
		currentSysClass = (SysClass) event.getOrigin().getData();
		this.setComponentValid(false);
		this.setSysClassButtonValid(true, true, false, false, true);
		this.bindBean(currentSysClass);
	}

	/**
	 * 绑定combobox和listBox.
	 * 
	 */
	private void bindBean(SysClass sysClass) throws Exception {
		if (sysClass != null) {
			this.bean.getClassName().setValue(sysClass.getClassName());
			this.bean.getJavaCode().setValue(sysClass.getJavaCode());
			this.bean.getTableName().setValue(sysClass.getTableName());
			if (StrUtil.isEmpty(sysClass.getTableType())) {
				this.bean.getTableType().setSelectedIndex(-1);
			} else {
				ComboboxUtils.setSelected(this.bean.getTableType(), sysClass
						.getTableType());
			}
			this.bean.getTableType();
			this.bean.getSeqName().setValue(sysClass.getSeqName());
			this.bean.getHisTableName().setValue(sysClass.getHisTableName());
			this.bean.getHisSeqName().setValue(sysClass.getHisSeqName());
			if (sysClass.getIsEntity() != null && sysClass.getIsEntity() > 0) {
				this.bean.getIsEntity().setChecked(true);
			} else {
				this.bean.getIsEntity().setChecked(false);
			}
			this.bean.getClassDesc().setValue(sysClass.getClassDesc());
		}
	}

	/**
	 * 设置控件状态.
	 * 
	 * @param isValid
	 *            是否无效
	 */
	private void setComponentValid(final Boolean isValid) {
		// 设置为无效
		this.bean.getClassName().setReadonly(!isValid);
		this.bean.getJavaCode().setReadonly(!isValid);
		this.bean.getTableName().setReadonly(!isValid);
		this.bean.getTableType().setDisabled(!isValid);
		this.bean.getSeqName().setReadonly(!isValid);
		this.bean.getHisTableName().setReadonly(!isValid);
		this.bean.getHisSeqName().setReadonly(!isValid);
		this.bean.getIsEntity().setDisabled(!isValid);
		this.bean.getClassDesc().setReadonly(!isValid);
	}

	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canSave
	 *            保存按钮
	 * @param canRecover
	 *            恢复按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setSysClassButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canSave,
			final Boolean canRecover, final Boolean canDelete) {
		this.bean.getAddSysClassButton().setDisabled(!canAdd);
		this.bean.getEditSysClassButton().setDisabled(!canEdit);
		this.bean.getSaveSysClassButton().setDisabled(!canSave);
		this.bean.getRecoverSysClassButton().setDisabled(!canRecover);
		this.bean.getDeleteSysClassButton().setDisabled(!canDelete);
	}

	/**
	 * 点击新增按钮
	 * 
	 * @throws Exception
	 */
	public void onAddSysClassInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		this.setComponentValid(true);
		SysClass newSysClass = new SysClass();
		isNewEntity = true;
		this.bindBean(newSysClass);
		this.setSysClassButtonValid(true, false, true, false, false);
	}

	/**
	 * 点击修改按钮
	 * 
	 * @throws Exception
	 */
	public void onEditSysClassInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		this.setComponentValid(true);
		isNewEntity = false;
		this.setSysClassButtonValid(true, true, true, true, true);
	}

	/**
	 * 点击保存按钮
	 * 
	 * @throws Exception
	 */
	public void onSaveSysClassInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		SysClass sysClass = null;
		if (isNewEntity) {
			sysClass = new SysClass(true);
		} else {
			sysClass = currentSysClass;
		}

		/**
		 * 是否是实体类型的表，可以增删改查的
		 */
		if (this.bean.getIsEntity().isChecked()) {
			sysClass.setIsEntity(1l);
			if (StrUtil.isEmpty(this.bean.getSeqName().getValue())) {
				ZkUtil.showError("表序列名不能为空,请填写", "提示信息");
				return;
			}
			if (StrUtil.isEmpty(this.bean.getHisTableName().getValue())) {
				ZkUtil.showError("历史表名不能为空,请填写", "提示信息");
				return;
			}
			if (StrUtil.isEmpty(this.bean.getHisSeqName().getValue())) {
				ZkUtil.showError("历史表序列不能为空,请填写", "提示信息");
				return;
			}
		} else {
			sysClass.setIsEntity(0l);
		}
		if (StrUtil.isEmpty(this.bean.getClassName().getValue())) {
			ZkUtil.showError("实体名称不能为空,请填写", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(this.bean.getJavaCode().getValue())) {
			ZkUtil.showError("JAVA类名不能为空,请填写", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(this.bean.getTableName().getValue())) {
			ZkUtil.showError("物理表名不能为空,请填写", "提示信息");
			return;
		}
		if (this.bean.getTableType().getSelectedItem() != null) {
			sysClass.setTableType((String) this.bean.getTableType()
					.getSelectedItem().getValue());
		} else {
			ZkUtil.showError("物理表类型不能为空,请填写", "提示信息");
			return;
		}
		sysClass.setClassName(this.bean.getClassName().getValue());
		sysClass.setJavaCode(this.bean.getJavaCode().getValue());
		sysClass.setTableName(this.bean.getTableName().getValue());
		sysClass.setSeqName(this.bean.getSeqName().getValue());
		sysClass.setHisTableName(this.bean.getHisTableName().getValue());
		sysClass.setHisSeqName(this.bean.getHisSeqName().getValue());
		sysClass.setClassDesc(this.bean.getClassDesc().getValue());
		if (isNewEntity) {
			sysClassManager.saveSysClass(sysClass);
		} else {
			sysClassManager.updateSysClass(sysClass);
		}
		this.currentSysClass = sysClass;
		this.setComponentValid(false);
		this.setSysClassButtonValid(true, true, false, false, false);
		Events.postEvent(SystemConfigConstant.ON_SYS_CLASS_SAVE, this.self
				.getRoot(), sysClass);
	}

	/**
	 * 恢复
	 * 
	 * @throws Exception
	 */
	public void onRecoverSysClassInfo() throws Exception {
		this.bindBean(this.currentSysClass);
		this.setComponentValid(false);
		this.setSysClassButtonValid(true, true, false, false, true);
	}

	/**
	 * 点击删除按钮
	 * 
	 * @throws Exception
	 */
	public void onDeleteSysClassInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (currentSysClass == null
							|| currentSysClass.getClassId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {
						List<AttrSpec> list = currentSysClass.getAttrSpecList();
						if (list != null && list.size() > 0) {
							ZkUtil.showError("该记录存在属性规格配置记录,不能删除", "提示信息");
							return;
						} else {
							sysClassManager.removeSysClass(currentSysClass);
							bindBean(new SysClass(false));
							Events.postEvent(
									SystemConfigConstant.ON_SYS_CLASS_DEL, self
											.getRoot(), currentSysClass);
							currentSysClass = null;
						}
					}
				}
			}
		});
	}

}
