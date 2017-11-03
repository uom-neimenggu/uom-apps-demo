package cn.ffcs.uom.group.component;

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
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.component.bean.GroupUomOrgListboxExtBean;
import cn.ffcs.uom.group.manager.GroupUomManager;
import cn.ffcs.uom.group.model.GroupUomOrg;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 集团组织管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class GroupUomOrgListboxExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private GroupUomOrgListboxExtBean bean = new GroupUomOrgListboxExtBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("groupUomManager")
	private GroupUomManager groupUomManager = (GroupUomManager) ApplicationContextUtil
			.getBean("groupUomManager");
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/group/comp/group_uom_org_listbox_ext.zul";

	/**
	 * 当前选择的groupUomOrg
	 */
	private GroupUomOrg groupUomOrg;

	/**
	 * 查询queryGroupUomOrg.
	 */
	private GroupUomOrg queryGroupUomOrg;
	/**
	 * 操作类型
	 */
	private String opType;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public GroupUomOrgListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setGroupUomOrgButtonValid(false, false, false);
		this.onQueryGroupUomOrg();
	}

	/**
	 * 组织关系选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupUomOrgSelectRequest() throws Exception {

		if (this.bean.getGroupUomOrgListBox().getSelectedIndex() != -1) {
			groupUomOrg = (GroupUomOrg) bean.getGroupUomOrgListBox()
					.getSelectedItem().getValue();

			if (StrUtil.isNullOrEmpty(groupUomOrg.getOrgId())) {
				this.setGroupUomOrgButtonValid(true, false, false);
			} else {
				this.setGroupUomOrgButtonValid(false, true, true);
			}
		}

	}

	/**
	 * 新增组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupUomOrgAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "add";
		this.openGroupUomOrgEditWin();
	}

	/**
	 * 修改组织关系
	 * 
	 * @throws Exception
	 */
	public void onGroupUomOrgEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "mod";
		if (!StrUtil.isNullOrEmpty(groupUomOrg)) {
			this.openGroupUomOrgEditWin();
		}
	}

	/**
	 * 打开组织关系编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openGroupUomOrgEditWin() throws Exception {
		Map arg = new HashMap();
		arg.put("opType", opType);

		arg.put("groupUomOrg", groupUomOrg);
		Window win = (Window) Executions.createComponents(
				"/pages/group/comp/group_uom_org_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				queryGroupUomOrg = (GroupUomOrg) event.getData();

				if (queryGroupUomOrg != null) {

					setGroupUomOrgButtonValid(false, false, false);

					bean.getKtext().setValue(queryGroupUomOrg.getKtext());

					onGroupUomOrgListboxPaging();
				}
			}
		});
	}

	/**
	 * 删除组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupUomOrgDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (!StrUtil.isNullOrEmpty(groupUomOrg)) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						groupUomManager.removeGroupUomOrg(groupUomOrg);
						groupUomOrg = null;
						onQueryGroupUomOrg();
						setGroupUomOrgButtonValid(false, false, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 设置组织关系按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            修改按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setGroupUomOrgButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete) {
		this.bean.getAddGroupUomOrgButton().setDisabled(!canAdd);
		this.bean.getEditGroupUomOrgButton().setDisabled(!canEdit);
		this.bean.getDelGroupUomOrgButton().setDisabled(!canDelete);
	}

	/**
	 * 查询员工组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryGroupUomOrg() throws Exception {

		this.bean.getGroupUomOrgListPaging().setActivePage(0);

		this.queryGroupUomOrg = new GroupUomOrg();

		if (!StrUtil.isEmpty(this.bean.getKtext().getValue())) {
			this.queryGroupUomOrg.setKtext(this.bean.getKtext().getValue());
		}

		this.onGroupUomOrgListboxPaging();
	}

	/**
	 * .重置查询内容 .
	 */
	public void onGroupUomOrgReset() {
		bean.getKtext().setValue(null);
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onGroupUomOrgListboxPaging() throws Exception {
		PageInfo pageInfo = this.groupUomManager.queryPageInfoByGroupUomOrg(
				queryGroupUomOrg, this.bean.getGroupUomOrgListPaging()
						.getActivePage() + 1, this.bean
						.getGroupUomOrgListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getGroupUomOrgListBox().setModel(dataList);
		this.bean.getGroupUomOrgListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {

		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
		} else if ("groupUomPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_UOM_ORG_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_UOM_ORG_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_UOM_ORG_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddGroupUomOrgButton().setVisible(canAdd);
		this.bean.getEditGroupUomOrgButton().setVisible(canEdit);
		this.bean.getDelGroupUomOrgButton().setVisible(canDel);
	}

}
