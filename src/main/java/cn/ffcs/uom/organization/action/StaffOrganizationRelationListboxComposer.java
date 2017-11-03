package cn.ffcs.uom.organization.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationRelationBean;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.StaffOrganization;

/**
 * 员工组织关系选择.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class StaffOrganizationRelationListboxComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffOrganizationRelationBean bean = new StaffOrganizationRelationBean();

    /**
     * 查询qryStaffOrganization.
     */
    private StaffOrganization qryStaffOrganization;
    
    /**
     * 选择chooseStaffOrganization.
     */
    private StaffOrganization chooseStaffOrganization;
    
    private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager)ApplicationContextUtil.getBean("staffOrganizationManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffOrganizationRelationMainWin() throws Exception {
		bindBean();
		this.onStaffOrganizationRelationQuery();
		this.bean.getStaffOrganizationRelationListbox().setCheckmark(true);
	}
	
	public void bindBean() throws Exception {
		qryStaffOrganization = (StaffOrganization)arg.get("oldStaffOrganization");
	}

    /**
     * 选择列表的行.
     * 
     * @throws Exception
     *             异常
     */
	public void onStaffOrganizationRelationSelectRequest() throws Exception {
    	int selCount = bean.getStaffOrganizationRelationListbox().getSelectedCount();
        if (selCount > 0) {
        	chooseStaffOrganization = (StaffOrganization)bean.getStaffOrganizationRelationListbox().getSelectedItem().getValue();
		}
    }

	/**
	 * 查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void queryStaffOrganizationRelation() throws Exception {
		if (this.qryStaffOrganization != null) {
			PageInfo pageInfo = staffOrganizationManager
					.queryPageInfoStaffOrganization(qryStaffOrganization, bean
							.getStaffOrganizationRelationListPaging()
							.getActivePage() + 1, bean
							.getStaffOrganizationRelationListPaging()
							.getPageSize());
			List<StaffOrganization> list = pageInfo.getDataList();
			if (list != null && list.size() > 0) {
				StaffOrganization self = null;
				for (StaffOrganization temp : list) {
					if (temp != null
							&& temp.getStaffOrgId().equals(
									qryStaffOrganization.getStaffOrgId())) {
						self = temp;
					}
				}
				if (self != null) {
					list.remove(self);
				}
			}
			ListModel dataList = new BindingListModelList(list, true);
			bean.getStaffOrganizationRelationListbox().setModel(dataList);
			bean.getStaffOrganizationRelationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryStaffOrganizationRelationPaging() throws Exception {
		this.queryStaffOrganizationRelation();
	}
	
	/**
     * 查询员工组织关系.
     */
    public void onStaffOrganizationRelationQuery() {
        try {
            queryStaffOrganizationRelation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	/**
     * 点击确定.
     */
    public void onOk() {
        if(null != chooseStaffOrganization){
        	try {
				Messagebox.show("您确定选择该员工组织关系作为归属（主部门）吗？", "提示信息", Messagebox.OK | Messagebox.CANCEL,
				        Messagebox.INFORMATION, new EventListener() {
				            public void onEvent(Event event) throws Exception {
				                Integer result = (Integer) event.getData();
				                if (result == Messagebox.OK) {
				                	Events.postEvent(Events.ON_OK, bean.getStaffOrganizationRelationMainWin(), chooseStaffOrganization);
				                	bean.getStaffOrganizationRelationMainWin().onClose();
				                }
				            }
				        });
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }else {
        	ZkUtil.showInformation("请选择一条数据！", "系统提示");
        }
    }
}
