package cn.ffcs.uom.dataPermission.action;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.action.bean.RoleOrganizationListboxBean;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.constants.RoleOrganizationConstant;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class RoleOrganizationListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul.
	 */
	private final String zul = "/pages/dataPermission/role_organization_listbox.zul";

	/**
	 * bean.
	 */
	private RoleOrganizationListboxBean bean = new RoleOrganizationListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleOrganizationManager")
	private AroleOrganizationManager roleOrganizationManager = (AroleOrganizationManager) ApplicationContextUtil
			.getBean("aroleOrganizationManager");

	/**
	 * 当前选择的organization
	 */
	private AroleOrganization aroleOrganization;

	/**
	 * 查询organization.
	 */
	private AroleOrganization qryAroleOrganization;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RoleOrganizationListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 选择角色：查询角色组织列表
		 */
		this.addForward(RoleOrganizationConstant.ON_ROLE_ORGANIZATION_QUERY,
				this, "onRoleOrganizationQueryResponse");
		/**
		 * 角色列表查询事件:清理list
		 */
		this
				.addForward(RoleConstant.ON_ROLE_QUERY, this,
						"onRoleQueryResponse");
	}

	/**
	 * 创建初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false);
	}

	/**
	 * 角色列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onRoleQueryResponse() throws Exception {
		this.qryAroleOrganization = null;
		PubUtil.clearListbox(this.bean.getRoleOrganizationListBox());
		this.setButtonValid(false, false);
	}

	/**
	 * 选择角色列表的角色,响应查询处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRoleOrganizationQueryResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin() != null && event.getOrigin().getData() != null) {
			this.qryAroleOrganization = (AroleOrganization) event.getOrigin()
					.getData();
			this.bean.getRoleOrganizationListPaging().setActivePage(0);
			this.queryRoleOrganization();
		}
	}

	/**
	 * 选择角色组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRoleOrganization() throws Exception {
		if (bean.getRoleOrganizationListBox().getSelectedCount() > 0) {
			aroleOrganization = (AroleOrganization) bean
					.getRoleOrganizationListBox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}

	/**
	 * 新增.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleOrganizationAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (qryAroleOrganization != null
				&& qryAroleOrganization.getAroleId() != null) {
			TelcomRegion telcomRegion = PermissionUtil
					.getPermissionTelcomRegion(new long[] { qryAroleOrganization
							.getAroleId() });
			if (telcomRegion != null) {
				this.openRoleOrganizationEditWin("add");
			} else {
				ZkUtil.showError("请先分配管理区域", "提示信息");
				return;
			}
		} else {
			ZkUtil.showError("角色id错误", "提示信息");
			return;
		}
	}

	/**
	 * 删除组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleOrganizationDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)){
			return;
		}
		ZkUtil.showQuestion("你确定要删除该角色组织关系吗?", "提示信息", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					roleOrganizationManager
							.removeRoleOrganization(aroleOrganization);
//					PermissionUtil.cacheRoleOrganization();
					setButtonValid(true, false);
					cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(bean
							.getRoleOrganizationListBox(), aroleOrganization,
							"del");
					aroleOrganization = null;
				}
			}
		});
	}

	/**
	 * 打开编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openRoleOrganizationEditWin(String opType) throws Exception {
		if (qryAroleOrganization != null
				&& qryAroleOrganization.getAroleId() != null) {
			Map arg = new HashMap();
			arg.put("opType", opType);
			arg.put("aroleOrganization", qryAroleOrganization);
			Window win = (Window) Executions.createComponents(
					"/pages/dataPermission/role_organization_edit.zul", this,
					arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
//					PermissionUtil.cacheRoleOrganization();
					if (event.getData() != null) {
						if (event.getData() != null) {
							setButtonValid(true, false);
							queryRoleOrganization();
						}
					}
				}
			});
		}
	}

	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRoleOrganizationButton().setDisabled(!canAdd);
		this.bean.getDelRoleOrganizationButton().setDisabled(!canDelete);
	}

	/**
	 * 查询.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryRoleOrganization() throws Exception {
		aroleOrganization = null;
		if (this.qryAroleOrganization != null) {
			this.setButtonValid(true, false);
			ListboxUtils.clearListbox(bean.getRoleOrganizationListBox());
			PageInfo pageInfo = roleOrganizationManager
					.queryPageInfoByRoleOrganization(qryAroleOrganization,
							this.bean.getRoleOrganizationListPaging()
									.getActivePage() + 1, this.bean
									.getRoleOrganizationListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo
					.getDataList(), true);
			this.bean.getRoleOrganizationListBox().setModel(dataList);
			this.bean.getRoleOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRoleOrganizationListBox());
		}
	}

	/**
	 * 分页
	 */
	public void onRoleOrganizationListPaging() throws Exception {
		this.queryRoleOrganization();
	}
}
