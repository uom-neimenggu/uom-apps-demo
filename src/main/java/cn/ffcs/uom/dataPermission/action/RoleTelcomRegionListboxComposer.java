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
import cn.ffcs.uom.dataPermission.action.bean.RoleTelcomRegionListboxBean;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.constants.RoleTelcomRegionConstant;
import cn.ffcs.uom.dataPermission.manager.AroleTelcomRegionManager;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class RoleTelcomRegionListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul.
	 */
	private final String zul = "/pages/dataPermission/role_telcom_region_listbox.zul";

	/**
	 * bean.
	 */
	private RoleTelcomRegionListboxBean bean = new RoleTelcomRegionListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleTelcomRegionManager")
	private AroleTelcomRegionManager roleTelcomRegionManager = (AroleTelcomRegionManager) ApplicationContextUtil
			.getBean("aroleTelcomRegionManager");

	/**
	 * 当前选择的organization
	 */
	private AroleTelcomRegion aroleTelcomRegion;

	/**
	 * 查询organization.
	 */
	private AroleTelcomRegion qryAroleTelcomRegion;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RoleTelcomRegionListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 选择角色后响应查询角色电信管理区域查询事件
		 */
		this.addForward(RoleTelcomRegionConstant.ON_ROLE_TELCOM_REGION_QUERY,
				this, "onRoleTelcomRegionQueryResponse");
		/**
		 * 角色列表查询事件：清空列表
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
		this.qryAroleTelcomRegion = null;
		PubUtil.clearListbox(this.bean.getRoleTelcomRegionListBox());
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
	public void onRoleTelcomRegionQueryResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin() != null && event.getOrigin().getData() != null) {
			this.qryAroleTelcomRegion = (AroleTelcomRegion) event.getOrigin()
					.getData();
			this.bean.getRoleTelcomRegionListPaging().setActivePage(0);
			this.queryRoleTelcomRegion();
		}

	}

	/**
	 * 选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRoleTelcomRegion() throws Exception {
		if (bean.getRoleTelcomRegionListBox().getSelectedCount() > 0) {
			aroleTelcomRegion = (AroleTelcomRegion) bean
					.getRoleTelcomRegionListBox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}

	/**
	 * 新增.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleTelcomRegionAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (qryAroleTelcomRegion != null
				&& qryAroleTelcomRegion.getAroleId() != null) {
			TelcomRegion telcomRegion = PermissionUtil
					.getPermissionTelcomRegion(new long[] { qryAroleTelcomRegion
							.getAroleId() });
			if (telcomRegion != null) {
				ZkUtil.showError("只能分配一个管理区域", "提示信息");
				return;
			}
			this.openRoleTelcomRegionEditWin("add");

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
	public void onRoleTelcomRegionDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("你确定要删除该角色电信管理区域关系吗?", "提示信息", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					roleTelcomRegionManager
							.removeRoleTelcomRegion(aroleTelcomRegion);
//					PermissionUtil.cacheRoleTelcomRegion();

					setButtonValid(true, false);
					cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(bean
							.getRoleTelcomRegionListBox(), aroleTelcomRegion,
							"del");
					aroleTelcomRegion = null;
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
	private void openRoleTelcomRegionEditWin(String opType) throws Exception {
		if (qryAroleTelcomRegion != null
				&& qryAroleTelcomRegion.getAroleId() != null) {
			Map arg = new HashMap();
			arg.put("opType", opType);
			arg.put("aroleTelcomRegion", qryAroleTelcomRegion);
			Window win = (Window) Executions.createComponents(
					"/pages/dataPermission/role_telcom_region_edit.zul", this,
					arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
//					PermissionUtil.cacheRoleTelcomRegion();
					if (event.getData() != null) {
						if (event.getData() != null) {
							setButtonValid(true, false);
							queryRoleTelcomRegion();
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
		this.bean.getAddRoleTelcomRegionButton().setDisabled(!canAdd);
		this.bean.getDelRoleTelcomRegionButton().setDisabled(!canDelete);
	}

	/**
	 * 查询.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryRoleTelcomRegion() throws Exception {
		aroleTelcomRegion = null;
		if (this.qryAroleTelcomRegion != null) {
			this.setButtonValid(true, false);
			ListboxUtils.clearListbox(bean.getRoleTelcomRegionListBox());
			PageInfo pageInfo = roleTelcomRegionManager
					.queryPageInfoByRoleTelcomRegion(qryAroleTelcomRegion,
							this.bean.getRoleTelcomRegionListPaging()
									.getActivePage() + 1, this.bean
									.getRoleTelcomRegionListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo
					.getDataList(), true);
			this.bean.getRoleTelcomRegionListBox().setModel(dataList);
			this.bean.getRoleTelcomRegionListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRoleTelcomRegionListBox());
		}
	}
}
