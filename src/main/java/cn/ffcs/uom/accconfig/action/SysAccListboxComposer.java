package cn.ffcs.uom.accconfig.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.accconfig.action.bean.SysAccListboxBean;
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysAccListboxComposer extends BasePortletComposer implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private SysAccListboxBean bean = new SysAccListboxBean();
	private SysAccRela sysAccRela;
	private SysAccRela qrySysAccRela;
	private SysList sysList;
	private AccConfig accConfig;
	@Autowired
	private AccConfigManager accConfigManager;
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
		this.setButtonValid(true, false);
		bean.getSysAccListbox().setPageSize(10);
		/**
		 * 点击树控件查询出配置系统关系列表
		 */
		bean.getSysAccMainWin().addForward(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, bean.getSysAccMainWin(),
				AccConfigConstants.ON_SELECT_TREE_ACCCONFIG_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getSysAccMainWin().addForward(AccConfigConstants.ON_DEL_NODE_OK, bean.getSysAccMainWin(),
				AccConfigConstants.ON_SELECT_TREE_ACCCONFIG_RESPONSE);
		/**
		 * 员工页面选择系统tab事件
		 */
		bean.getSysAccMainWin().addForward(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS, bean.getSysAccMainWin(),
				"onSelectAccResponse");
	}
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$sysAccMainWin() throws Exception {
		setPagePosition();
	}
	
	public void onSelectAccResponse(final ForwardEvent event) throws Exception{
		this.accConfig = (AccConfig)event.getOrigin().getData();
		this.querySysAccListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSelectTreeAccConfigResponse(final ForwardEvent event) throws Exception{
		this.accConfig = (AccConfig)event.getOrigin().getData();
		this.querySysAccListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSysAccSelectRequest() throws Exception {
		if (bean.getSysAccListbox().getSelectedCount() > 0) {
			sysAccRela = (SysAccRela) bean.getSysAccListbox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}
	
	public void sysAccListboxPaging() throws Exception {
		this.querySysAccListRela();
	}
	
	public void onQuery() throws Exception {
		this.bean.getSysAccListboxPaging().setActivePage(0);
		this.querySysAccListRela();
	}
	
	public void onReset() throws Exception {
		this.bean.getSysName().setValue("");
		this.bean.getClientCode().setValue("");
	}
	
	private void querySysAccListRela() throws Exception {
		if(null == qrySysAccRela){
			qrySysAccRela = new SysAccRela();
		}
		if(null != sysList && null != sysList.getSysListId()){
			qrySysAccRela.setSysListId(sysList.getSysListId());
		}else{
			sysList = new SysList();
		}
		sysList.setSysName(this.bean.getSysName().getValue());
		sysList.setClientCode(this.bean.getClientCode().getValue());
		qrySysAccRela.setQrySysList(sysList);
		if(null != accConfig && null != accConfig.getAccConfigId()){
			qrySysAccRela.setAccConfigId(accConfig.getAccConfigId());
		}
		ListboxUtils.clearListbox(bean.getSysAccListbox());
		PageInfo pageInfo = accConfigManager.querySysAccRela(qrySysAccRela, 
				this.bean.getSysAccListboxPaging().getActivePage() + 1,
				this.bean.getSysAccListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getSysAccListbox().setModel(dataList);
		this.bean.getSysAccListboxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if(null == sysAccRela){
			sysAccRela = new SysAccRela();
		}
		if(null != sysList && null != sysList.getSysListId()){
			sysAccRela.setSysListId(sysList.getSysListId());
		}
		if(null != accConfig && null != accConfig.getAccConfigId()){
			sysAccRela.setAccConfigId(accConfig.getAccConfigId());
		}
		map.put("sysAccRela", sysAccRela);
		Window window = (Window) Executions.createComponents("/pages/accconfig/sys_acc_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					accConfig = (AccConfig)event.getData();
					querySysAccListRela();
				}
			}
		});
	}
	
	public void onDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.sysAccRela != null && this.sysAccRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						accConfigManager.removeSysAccRela(sysAccRela);
						querySysAccListRela();
						setButtonValid(true, false);						
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录。", "提示信息");
			return;
		}
	}
	
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		if (canAdd != null) {
			this.bean.getAddButton().setDisabled(!canAdd);
		}
		this.bean.getDelButton().setDisabled(!canDelete);
	}
	
	
	/**
	 * 设置页面坐标
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition()  throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		
		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else {
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.SYS_ACC_CONFIG_RELA_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.SYS_ACC_CONFIG_RELA_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}
}
