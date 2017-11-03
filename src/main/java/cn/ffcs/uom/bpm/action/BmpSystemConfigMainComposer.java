package cn.ffcs.uom.bpm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.bpm.bean.BmpSystemConfigMainBean;
import cn.ffcs.uom.bpm.bean.InformConfigMainBean;
import cn.ffcs.uom.bpm.manager.BmpSystemConfigManager;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.bpm.model.QaUnPrincipal;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.SysClassManager;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Controller
@Scope("prototype")
public class BmpSystemConfigMainComposer extends BasePortletComposer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3414801544961910332L;
	/**
	 * 页面bean
	 */
	private BmpSystemConfigMainBean bean = new BmpSystemConfigMainBean();
	/**
	 * 选中的系统
	 */
	private BusinessSystem businessSystem;
	private QaUnPrincipal principal;
	private QaInformMethod informMethod;
	@Autowired
	private QaInformMethodManager qaInformMethodManager;
	@Autowired
	private BmpSystemConfigManager bmpSystemConfigManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		this.bean.getBusinessSystemListExt().addForward(
			SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_REQUEST, this.self,
			"onBusSystemSelectRes");
	}
	
	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$bmpSystemConfigMainWin() throws Exception {
		// ON_BUSINESS_SYSTEM_SELECT_REQUEST
		this.bean.getBusinessSystemListExt().bean.getAddBusinessSystemButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getEditBusinessSystemButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getDelBusinessSystemButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getThresholdSwitchButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getFtpNoticeSwitchButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getFtpReplacementButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getSmsNoticeSwitchButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getMaiilSendSwitchButton().setVisible(false);
		this.bean.getBusinessSystemListExt().bean.getFtpTimerSwitchButton().setVisible(false);
	}
	
	public void onBusSystemSelectRes(ForwardEvent event) {
		businessSystem = (BusinessSystem) event.getOrigin().getData();
		qryPricipal();
		qryInformMehtod();
		setPricipalButtonValid(true, false);
		setInformButtonValid(true, false);
		
	}
	
	public void qryPricipal() {
		try {
			if (businessSystem != null && businessSystem.getBusinessSystemId() != null) {
				return;
			}
			List<QaUnPrincipal> busPrincipalList = bmpSystemConfigManager
				.qryPrincipal(businessSystem);
			ListModel list = new BindingListModelList(busPrincipalList, true);
			bean.getPricipalListBox().setModel(list);
		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}
	
	public void qryInformMehtod() {
		try {
			if (businessSystem != null && businessSystem.getBusinessSystemId() != null) {
				return;
			}
			List<QaInformMethod> informMethodList = qaInformMethodManager
				.getInformMethod(businessSystem);
			ListModel list = new BindingListModelList(informMethodList, true);
			bean.getPricipalListBox().setModel(list);
		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 选中系统干系人
	 * 
	 * @throws Exception
	 */
	public void onSelectPricipalListBox(ForwardEvent event){
		principal = (QaUnPrincipal) event.getOrigin().getData();
		setPricipalButtonValid(true, true);
	}
	/**
	 * 选中系统通知方式
	 * 
	 * @throws Exception
	 */
	public void onSelectSystemInformListBox(ForwardEvent event){
		informMethod = (QaInformMethod) event.getOrigin().getData();
		setInformButtonValid(true, true);
	}
	/*	*//**
	 * 选中类列表
	 * 
	 * @throws Exception
	 */
	/*
	 * public void onSelectSysClassListBox() throws Exception {
	 * final SysClass sysClass = (SysClass) this.bean.getSysClassListBox()
	 * .getSelectedItem().getValue();
	 * Events.postEvent(SystemConfigConstant.ON_SYS_CLASS_SELECT, this.self
	 * .getRoot(), sysClass);
	 * }
	 */
	
	/**
	 * 类保存后的事件
	 * 
	 * @throws Exception
	 */
	/*
	 * public void onSaveSysClassResponse(final ForwardEvent event)
	 * throws Exception {
	 * this.bean.getSysClassPaging().setActivePage(0);
	 * this.querySysClass = (SysClass) event.getOrigin().getData();
	 * this.onQuerySysClass();
	 * }
	 */
	
	/**
	 * 类删除后的事件
	 * 
	 * @throws Exception
	 */
	/*
	 * public void onDelSysClassResponse(final ForwardEvent event)
	 * throws Exception {
	 * SysClass sysClass = (SysClass) event.getOrigin().getData();
	 * ListModelList model = (ListModelList) this.bean.getSysClassListBox()
	 * .getModel();
	 * model.remove(sysClass);
	 * ListModel dataList = new BindingListModelList(model, true);
	 * this.bean.getSysClassListBox().setModel(dataList);
	 * }
	 */
	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	/*
	 * public void onInformMethodDel() throws Exception {
	 * if (treeBindingRule != null) {
	 * ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
	 * @Override
	 * public void onEvent(Event event) throws Exception {
	 * Integer result = (Integer) event.getData();
	 * if (result == Messagebox.OK) {
	 * treeBindingRuleManager.removeTreeBindingRule(treeBindingRule);
	 * PubUtil.reDisplayListbox(bean.getTreeBindingListBox(), treeBindingRule,
	 * "del");
	 * }
	 * }
	 * });
	 * } else {
	 * ZkUtil.showError("请选择你要删除的配置", "提示信息");
	 * return;
	 * }
	 * }
	 */
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onPricipalDel() throws Exception {
		if (principal != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						//indicatorManager.delIndicator(qaUnOppExecScript);
						qryPricipal();
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的配置", "提示信息");
			return;
		}
	}
	
	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onPricipalAdd() throws Exception {
		String opType = "add";
		Map arg = new HashMap();
		arg.put("opType", opType);
		Window win = (Window) Executions.createComponents(
			"/pages/bpm/comp/indicator_config_edit.zul", this.self, arg);
		win.setTitle("新增干系人");
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				qryPricipal();
				if (event.getData() != null) {
					
				}
			}
		});
	}
	
	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onPricipalEdit() throws Exception {
		String opType = "mod";
		Map arg = new HashMap();
		arg.put("opType", opType);
		arg.put("principal",principal);
		Window win = (Window) Executions.createComponents(
			"/pages/bpm/comp/indicator_config_edit.zul", this.self, arg);
		win.setTitle("稽核项修改");
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				qryPricipal();
				if (event.getData() != null) {
					
				}
			}
		});
	}
	
    /**
     * 设置干系人按钮状态
     * 
     * @param canAdd
     * @param canEdit
     * @param canDel
     */
    public void setPricipalButtonValid(boolean canAdd, boolean canDel) {
        this.bean.getAddPricipalButton().setDisabled(!canAdd);
        this.bean.getDelPricipalButton().setDisabled(!canDel);
    }
    
    /**
     * 设置通知方式按钮状态
     * 
     * @param canAdd
     * @param canEdit
     * @param canDel
     */
    public void setInformButtonValid(boolean canAdd, boolean canDel) {
        this.bean.getAddSystemInformButton().setDisabled(!canAdd);
        this.bean.getDelSystemInformButton().setDisabled(!canDel);
    }
}
