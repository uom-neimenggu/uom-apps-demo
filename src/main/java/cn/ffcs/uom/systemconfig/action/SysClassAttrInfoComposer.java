package cn.ffcs.uom.systemconfig.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.action.bean.SysClassAttrInfoBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.AttrSpecManager;
import cn.ffcs.uom.systemconfig.manager.AttrValueManager;
import cn.ffcs.uom.systemconfig.model.AttrSpec;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Controller
@Scope("prototype")
public class SysClassAttrInfoComposer extends BasePortletComposer implements IPortletInfoProvider{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4691468938214808866L;

	/**
	 * 页面bean
	 */
	private SysClassAttrInfoBean bean = new SysClassAttrInfoBean();
	/**
	 * manager
	 */
	private AttrSpecManager attrSpecManager = (AttrSpecManager) ApplicationContextUtil
			.getBean("attrSpecManager");
	/**
	 * manager
	 */
	private AttrValueManager attrValueManager = (AttrValueManager) ApplicationContextUtil
			.getBean("attrValueManager");
	/**
	 * 当前选中的类
	 */
	private SysClass currentSysClass;
	/**
	 * 当前选中的属性
	 */
	private AttrSpec currentAttrSpec;
	/**
	 * 当前选中的属性值
	 */
	private AttrValue currentAttrValue;

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
	public void onCreate$sysClassAttrInfoWin() throws Exception {
		this.setAttrSpecButtonValid(true, false, false);
		this.setAttrValueButtonValid(true, false, false);
	}

	/**
	 * 属性按钮
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDelete
	 */
	private void setAttrSpecButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete) {
		this.bean.getAddAttrSpecButton().setDisabled(!canAdd);
		this.bean.getEditAttrSpecButton().setDisabled(!canEdit);
		this.bean.getDeleteAttrSpecButton().setDisabled(!canDelete);
	}

	/**
	 * 属性值按钮
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDelete
	 */
	private void setAttrValueButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete) {
		this.bean.getAddAttrValueButton().setDisabled(!canAdd);
		this.bean.getEditAttrValueButton().setDisabled(!canEdit);
		this.bean.getDeleteAttrValueButton().setDisabled(!canDelete);
	}

	/**
	 * 选择类响应事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectSysClassResponse(final ForwardEvent event)
			throws Exception {
		currentSysClass = (SysClass) event.getOrigin().getData();
		if (currentSysClass != null && currentSysClass.getClassId() != null) {
			this.onQryAttrSpec();
		}
		ListboxUtils.clearListbox(this.bean.getAttrValueListbox());
		this.currentAttrSpec = null;
	}

	/**
	 * 查询属性及分页
	 * 
	 * @throws Exception
	 */
	public void onQryAttrSpec() throws Exception {
		setAttrSpecButtonValid(true, false, false);
		if (currentSysClass != null && currentSysClass.getClassId() != null) {
			PageInfo pageInfo = this.attrSpecManager.queryPageInfoByClassId(
					currentSysClass.getClassId(), this.bean.getAttrSpecPaging()
							.getActivePage() + 1, this.bean.getAttrSpecPaging()
							.getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo
					.getDataList(), true);
			this.bean.getAttrSpecListbox().setModel(dataList);
			this.bean.getAttrSpecPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 选择属性事件
	 * 
	 * @throws Exception
	 */
	public void onAttrSpecSelectRequest() throws Exception {
		currentAttrSpec = (AttrSpec) this.bean.getAttrSpecListbox()
				.getSelectedItem().getValue();
		this.setAttrSpecButtonValid(true, true, true);
		if (currentAttrSpec != null && currentAttrSpec.getAttrId() != null) {
			this.onQryAttrValue();
		}
	}

	/**
	 * 新增属性
	 * 
	 * @throws Exception
	 */
	public void onAddAttrSpecInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		if (currentSysClass != null && currentSysClass.getClassId() != null) {
			final Map arg = new HashMap();
			arg.put("opType", "add");
			arg.put("classId", currentSysClass.getClassId());
			final Window win = (Window) Executions.createComponents(
					"/pages/system_config/attr_spec_edit.zul", this.self, arg);
			win.setClosable(true);
			win.setTitle("新增属性");
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQryAttrSpec();
				}
			});
		} else {
			ZkUtil.showError("请选择你只要增加属性的类", "提示信息");
			return;
		}
	}

	/**
	 * 修改属性
	 * 
	 * @throws Exception
	 */
	public void onEditAttrSpecInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		if (currentAttrSpec != null && currentAttrSpec.getAttrId() != null) {
			final Map arg = new HashMap();
			arg.put("opType", "update");
			arg.put("attrSpec", currentAttrSpec);
			final Window win = (Window) Executions.createComponents(
					"/pages/system_config/attr_spec_edit.zul", this.self, arg);
			win.setClosable(true);
			win.setTitle("修改属性");
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQryAttrSpec();
				}
			});
		} else {
			ZkUtil.showError("请选择你只要修改的属性", "提示信息");
			return;
		}
	}

	/**
	 * 删除属性
	 * 
	 * @throws Exception
	 */
	public void onDeleteAttrSpecInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {

			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (currentAttrSpec != null) {
						List<AttrValue> list = currentAttrSpec
								.getAttrValueList();
						if (list != null && list.size() > 0) {
							ZkUtil.showError("该记录存在属性值配置记录,不能删除", "提示信息");
							return;
						} else {
							attrSpecManager.removeAttrSpec(currentAttrSpec);
							/**
							 * 删除列表处理
							 */
							ListModelList model = (ListModelList) bean
									.getAttrSpecListbox().getModel();
							model.remove(currentAttrSpec);
							ListModel dataList = new BindingListModelList(
									model, true);
							bean.getAttrSpecListbox().setModel(dataList);
							currentAttrSpec = null;
							setAttrSpecButtonValid(true, false, false);
						}
					} else {
						ZkUtil.showError("请选择你要删除的属性记录", "提示信息");
						return;
					}
				}
			}
		});
	}

	/**
	 * 属性取值分页处理
	 * 
	 * @throws Exception
	 */
	public void onQryAttrValue() throws Exception {
		setAttrValueButtonValid(true, false, false);
		if (this.currentAttrSpec != null
				&& this.currentAttrSpec.getAttrId() != null) {
			PageInfo pageInfo = this.attrValueManager.queryPageInfoByAttrId(
					currentAttrSpec.getAttrId(), this.bean.getAttrValuePaging()
							.getActivePage() + 1, this.bean
							.getAttrValuePaging().getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo
					.getDataList(), true);
			this.bean.getAttrValueListbox().setModel(dataList);
			this.bean.getAttrValuePaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 选择属性
	 * 
	 * @throws Exception
	 */
	public void onAttrValueListSelectRequest() throws Exception {
		this.setAttrValueButtonValid(true, true, true);
		currentAttrValue = (AttrValue) this.bean.getAttrValueListbox()
				.getSelectedItem().getValue();
	}

	/**
	 * 属性取值新增
	 * 
	 * @throws Exception
	 */
	public void onAddAttrValueInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		if (currentAttrSpec != null && currentAttrSpec.getAttrId() != null) {
			final Map arg = new HashMap();
			arg.put("opType", "add");
			arg.put("attrId", currentAttrSpec.getAttrId());
			final Window win = (Window) Executions.createComponents(
					"/pages/system_config/attr_value_edit.zul", this.self, arg);
			win.setClosable(true);
			win.setTitle("新增属性值");
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onQryAttrValue();
				}
			});
		} else {
			ZkUtil.showError("请选择你只要增加属性值的属性", "提示信息");
			return;
		}
	}

	/**
	 * 属性取值修改
	 * 
	 * @throws Exception
	 */
	public void onEditAttrValueInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		if (currentAttrValue != null
				&& currentAttrValue.getAttrValueId() != null) {
			final Map arg = new HashMap();
			arg.put("opType", "update");
			arg.put("attrValue", currentAttrValue);
			final Window win = (Window) Executions.createComponents(
					"/pages/system_config/attr_value_edit.zul", this.self, arg);
			win.setClosable(true);
			win.setTitle("修改属性值");
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onQryAttrValue();
				}
			});
		} else {
			ZkUtil.showError("请选择你只要修改的属性值", "提示信息");
			return;
		}
	}

	/**
	 * 属性取值删除
	 * 
	 * @throws Exception
	 */
	public void onDeleteAttrValueInfo() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this,
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {

			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (currentAttrValue != null
							&& currentAttrValue.getAttrValueId() != null) {
						int count = currentAttrValue.getUseRecordCount();
						if (count == -1) {
							ZkUtil.showError("非实例表,属性不能删除", "提示信息");
							return;
						}
						if (count > 0) {
							ZkUtil.showError("存在使用该配置的的记录不能删除", "提示信息");
							return;
						}
						attrValueManager.removeAttrValue(currentAttrValue);
						/**
						 * 删除列表处理
						 */
						ListModelList model = (ListModelList) bean
								.getAttrValueListbox().getModel();
						model.remove(currentAttrValue);
						ListModel dataList = new BindingListModelList(model,
								true);
						bean.getAttrValueListbox().setModel(dataList);
						currentAttrValue = null;
						setAttrValueButtonValid(true, false, false);
					} else {
						ZkUtil.showError("请选择你要删除的属性值记录", "提示信息");
						return;
					}
				}
			}
		});
	}
}
