package cn.ffcs.uom.systemconfig.action.comp;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.SysBusiUserListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.SysBusiUserManager;
import cn.ffcs.uom.webservices.model.SystemBusiUser;

@Controller
@Scope("prototype")
public class SysBusiUserListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysBusiUserListExtBean bean = new SysBusiUserListExtBean();

	private String zul = "/pages/system_config/comp/sys_busi_user_listbox.zul";

	private SysBusiUserManager sysBusiUserManager = (SysBusiUserManager) ApplicationContextUtil
			.getBean("sysBusiUserManager");
	
	/**
	 * 查询人员系统关系
	 */
	private SystemBusiUser querySysBusiUser;

	/**
	 * 选中的业务系统
	 */
	private BusinessSystem businessSystem;

	/**
	 * 操作类型
	 */
	private String opType;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 构造方法
	 */
	public SysBusiUserListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		
		// 接受配置人员列表抛出的人员系统关系查询事件
		this.addEventListener(
				SystemConfigConstant.ON_SYS_BUSI_USER_QUERY,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						SystemBusiUser sysBusiUser = (SystemBusiUser) event
								.getData();
						onQueryBusinessSystem(sysBusiUser);
					}
				});
		
		/**
		 * 系统配置人员组件刷新事件
		 */
		this.addForward(SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, this,
				"onBusinessSystemClearResponse");
	}
	
	/**
	 * 查询业务系统
	 */
	public void onQueryBusinessSystem(SystemBusiUser sysBusiUser) {
		try {
			this.bean.getBusinessSystemListPaging().setActivePage(0);
			this.querySysBusiUser = sysBusiUser;
			this.onBusinessSystemListboxPaging();
			this.setButtonValid(true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询业务系统
	 */
	public void onBusinessSystemListboxPaging() throws Exception {
		PageInfo pageInfo = this.sysBusiUserManager
				.queryBusinessSystemPageInfo(querySysBusiUser, this.bean
						.getBusinessSystemListPaging().getActivePage() + 1,
						this.bean.getBusinessSystemListPaging()
								.getPageSize());
		ListModel dataList = new BindingListModelList(
				pageInfo.getDataList(), true);
		this.bean.getBusinessSystemListBox().setModel(dataList);
		this.bean.getBusinessSystemListPaging().setTotalSize(
				pageInfo.getTotalCount());
	}
	
	/**
	 * 人员列表刷新清空系统列表响应
	 * 
	 * @throws Exception
	 */
	public void onBusinessSystemClearResponse() throws Exception {
		this.querySysBusiUser = null;
		ListboxUtils.clearListbox(this.bean.getBusinessSystemListBox());
		this.setButtonValid(false, false);
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(true, false);
	}


	/**
	 * 选中业务系统
	 */
	public void onBusinessSystemSelect() throws Exception {
		if (this.bean.getBusinessSystemListBox().getSelectedCount() > 0) {
			businessSystem = (BusinessSystem) this.bean.
					getBusinessSystemListBox().getSelectedItem().getValue();
			if (businessSystem != null) {
				this.setButtonValid(true, true);
			}
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canDel) {
		this.bean.getAddBusinessSystemButton().setDisabled(!canAdd);
		this.bean.getDelBusinessSystemButton().setDisabled(!canDel);
	}

	/**
	 * 删除业务系统
	 */
	public void onBusinessSystemDel() throws Exception {

		if (businessSystem != null) {
			ZkUtil.showQuestion("你确定要删除该人员配置系统关系吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						sysBusiUserManager
								.removeBusinessSystem(businessSystem,querySysBusiUser);
						PubUtil.reDisplayListbox(
								bean.getBusinessSystemListBox(),
								businessSystem, "del");
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的业务系统", "提示信息");
			return;
		}
	}

	/**
	 * 新增业务系统
	 * 
	 * @throws Exception
	 */
	public void onBusinessSystemAdd() throws Exception {
		this.openBusinessSystemEditWin("add",querySysBusiUser);
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openBusinessSystemEditWin(String type,SystemBusiUser sysBusiUser) throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.opType = type;
		arg.put("opType", opType);
		arg.put("sysBusiUser", sysBusiUser);
		
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/sys_busi_user_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onQueryBusinessSystem(querySysBusiUser);
			}
		});
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if ("messageConfigPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MESSAGE_CONFIG_SYSTEM_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MESSAGE_CONFIG_SYSTEM_DEL)) {
				canDel = true;
			}
			
		}

		this.bean.getAddBusinessSystemButton().setVisible(canAdd);
		this.bean.getDelBusinessSystemButton().setVisible(canDel);
	}

}
