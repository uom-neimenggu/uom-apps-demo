package cn.ffcs.uom.syslist.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.syslist.action.bean.SysListboxBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class StaffSysListboxComposer extends BasePortletComposer implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private SysListboxBean bean = new SysListboxBean();
	private StaffSysRela staffSysRela;
	private StaffSysRela qryStaffSysRela;
	private SysList sysList;
	private Staff staff;
	@Autowired
	private SysListManager sysListManager;
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
		bean.getSysListbox().setPageSize(10);
		/**
		 * 点击树控件查询出员工系统关系列表
		 */
		bean.getStaffSysMainWin().addForward(SysListConstants.ON_SELECT_TREE_SYSLIST, bean.getStaffSysMainWin(),
				SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getStaffSysMainWin().addForward(SysListConstants.ON_DEL_NODE_OK, bean.getStaffSysMainWin(),
				SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
		/**
		 * 员工页面选择系统tab事件
		 */
		bean.getStaffSysMainWin().addForward(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS, bean.getStaffSysMainWin(),
				"onSelectSysResponse");
	}
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$staffSysMainWin() throws Exception {
		setPagePosition();
	}
	
	public void onSelectSysResponse(final ForwardEvent event) throws Exception{
		this.staff = (Staff)event.getOrigin().getData();
		this.querySysListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSelectTreeSysListResponse(final ForwardEvent event) throws Exception{
		this.sysList = (SysList)event.getOrigin().getData();
		this.querySysListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSysListSelectRequest() throws Exception {
		if (bean.getSysListbox().getSelectedCount() > 0) {
			staffSysRela = (StaffSysRela) bean.getSysListbox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}
	
	public void onSysListListPaging() throws Exception {
		this.querySysListRela();
	}
	
	public void onQuery() throws Exception {
		this.bean.getSysListboxPaging().setActivePage(0);
		this.querySysListRela();
	}
	
	public void onReset() throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getStaffCode().setValue("");
	}
	
	private void querySysListRela() throws Exception {
		Staff  staffTemp = Staff.newInstance();
		if(null == qryStaffSysRela){
			qryStaffSysRela = new StaffSysRela();			
		}
		if(null != sysList && null != sysList.getSysListId()){
			qryStaffSysRela.setSysListId(sysList.getSysListId());
		}else{
			sysList = new SysList();
		}
		if(null != staff && null != staff.getStaffId()){
			qryStaffSysRela.setStaffId(staff.getStaffId());
			BeanUtils.copyProperties(staffTemp, staff);
		}
		
		staffTemp.setStaffName(this.bean.getStaffName().getValue());
		staffTemp.setStaffCode(this.bean.getStaffCode().getValue());
		
		qryStaffSysRela.setQryStaff(staffTemp);
		ListboxUtils.clearListbox(bean.getSysListbox());
		PageInfo pageInfo = sysListManager.queryStaffSysRela(qryStaffSysRela, 
				this.bean.getSysListboxPaging().getActivePage() + 1,
				this.bean.getSysListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getSysListbox().setModel(dataList);
		this.bean.getSysListboxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if(null == staffSysRela){
			staffSysRela = new StaffSysRela();
		}
		if(null != sysList){
			staffSysRela.setSysListId(sysList.getSysListId());
		}
		if(null != staff){
			staffSysRela.setStaffId(staff.getStaffId());
		}
		map.put("staffSysRela", staffSysRela);
		Window window = (Window) Executions.createComponents("/pages/syslist/staff_sys_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					staff = (Staff)event.getData();
					if(null != staff){
						bean.getStaffName().setValue(staff.getStaffName());
						bean.getStaffCode().setValue(staff.getStaffCode());
					}
					querySysListRela();
				}
			}
		});
	}
	
	public void onDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.staffSysRela != null && this.staffSysRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						sysListManager.removeStaffSysRela(staffSysRela);
						querySysListRela();
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
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.STAFF_SYS_LIST_RELA_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.STAFF_SYS_LIST_RELA_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}
}
