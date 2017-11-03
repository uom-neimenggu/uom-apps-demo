package cn.ffcs.uom.position.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.position.action.bean.PositionOrganizationListboxBean;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;

/**
 * 组织管理.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class PositionOrganizationListboxComposer extends BasePortletComposer
		implements IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private PositionOrganizationListboxBean bean = new PositionOrganizationListboxBean();
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("positionManager")
	private PositionManager positionManager;

	private OrgPositonManager orgPositonManager = (OrgPositonManager) ApplicationContextUtil
			.getBean("orgPositonManager");

	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	/**
	 * 当前选择的organization
	 */
	private Organization organization;

	/**
	 * 查询queryOrganization.
	 */
	private Organization queryOrganization;

	/**
	 * 查询queryOrgPosition.
	 */
	private OrgPosition queryOrgPosition;

	/**
	 * 当前选择的position
	 */
	private Position position;

	/**
	 * 查询queryPosition
	 */
	private Position queryPosition;

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
		// 接受抛出的查询事件
		bean.getPositionOrganizationListboxComp().addForward(
				PositionConstant.ON_SELECT_POSITION,
				bean.getPositionOrganizationListboxComp(),
				PositionConstant.ON_POSITION_ORGANIZATION_QUERY_RESPONSE);
		bean.getPositionOrganizationListboxComp().addForward(
				PositionConstant.ON_DEL_NODE_OK,
				bean.getPositionOrganizationListboxComp(),
				PositionConstant.ON_POSITION_ORGANIZATION_QUERY_RESPONSE);
		this.setPositionOrganizationButtonValid(true, false, false);
		bean.getPositionOrganizationListBox().setPageSize(10);
		/**
		 * 页面嵌入所以需要用事件接受页面信息
		 */
		this.bean.getPositionOrganizationListboxComp().addForward(
				"onSetPagePosition", comp, "onSetPagePositionResponse");
	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onPositionOrganiztionQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getOrgCode().setValue("");
		this.bean.getOrgName().setValue("");

		this.position = (Position) event.getOrigin().getData();
		this.queryPositionOrganization();
		this.setPositionOrganizationButtonValid(true, false, false);
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionOrganizationSelectRequest() throws Exception {
		if (bean.getPositionOrganizationListBox().getSelectedCount() > 0) {
			organization = (Organization) bean.getPositionOrganizationListBox()
					.getSelectedItem().getValue();
			this.setPositionOrganizationButtonValid(true, true, true);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionOrganizationListPaging() throws Exception {
		this.queryPositionOrganization();
	}

	/**
	 * 新增岗位组织关系
	 * 
	 * @throws Exception
	 */
	public void onOrgPositionAdd() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("position", position);
		Window window = (Window) Executions.createComponents(
				"/pages/position/position_organization_edit.zul", this.self,
				map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					Organization organization = (Organization) event.getData();
					bean.getOrgCode().setValue(organization.getOrgCode());
					bean.getOrgName().setValue(organization.getOrgName());
					queryPositionOrganization();

				}
			}
		});
	}

	/**
	 * 删除组织岗位关系
	 * 
	 * @throws Exception
	 */
	public void onOrgPositionDel() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.position != null && this.organization != null) {
			final OrgPosition orgPosition = new OrgPosition();

			orgPosition.setPositionId(position.getPositionId());
			orgPosition.setOrgId(organization.getOrgId());

			this.queryOrgPosition = orgPositonManager
					.queryOrganizationPosition(orgPosition);

			orgPosition.setOrgPositionId(this.queryOrgPosition
					.getOrgPositionId());

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						orgPositonManager
								.removeOrganizationPosition(orgPosition);
						PubUtil.reDisplayListbox(
								bean.getPositionOrganizationListBox(),
								organization, "del");
						organization = null;
						setPositionOrganizationButtonValid(true, false, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 组织路径查看
	 * 
	 * @throws Exception
	 */
	public void onOrganizationPathView() throws Exception {
		if (organization != null && organization.getOrgId() != null) {

			OrganizationRelation organizationRelation = new OrganizationRelation();
			organizationRelation.setOrgId(organization.getOrgId());
			List<OrganizationRelation> organizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (organizationRelationList != null
					&& organizationRelationList.size() > 0) {
				Map arg = new HashMap();
				arg.put("organizationRelationList", organizationRelationList);
				Window win = (Window) Executions.createComponents(
						"/pages/organization/organization_path.zul", this.self,
						arg);
				win.doModal();
			}
		}
	}

	/**
	 * 设置组织按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setPositionOrganizationButtonValid(final Boolean canAdd,
			final Boolean canDelete, final Boolean canView) {
		if (canAdd != null) {
			this.bean.getAddPositionOrganizationButton().setDisabled(!canAdd);
		}
		this.bean.getDelPositionOrganizationButton().setDisabled(!canDelete);
		this.bean.getViewOrganizationPathButton().setDisabled(!canView);
	}

	/**
	 * 查询组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryPositionOrganization() throws Exception {
		queryOrganization = new Organization();
		queryOrganization.setOrgCode(this.bean.getOrgCode().getValue());
		queryOrganization.setOrgName(this.bean.getOrgName().getValue());

		if (this.position != null) {
			ListboxUtils.clearListbox(bean.getPositionOrganizationListBox());
			PageInfo pageInfo = positionManager.queryPageInfoByPositionId(
					this.queryOrganization, this.position, this.bean
							.getPositionOrganizationListPaging()
							.getActivePage() + 1, this.bean
							.getPositionOrganizationListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getPositionOrganizationListBox().setModel(dataList);
			this.bean.getPositionOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getPositionOrganizationListBox());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganizationPosition() throws Exception {
		this.bean.getPositionOrganizationListPaging().setActivePage(0);
		this.queryPositionOrganization();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetOrganizationPosition() throws Exception {

		this.bean.getOrgCode().setValue("");

		this.bean.getOrgName().setValue("");

	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void onSetPagePositionResponse(ForwardEvent event) throws Exception {
		boolean canAdd = false;
		boolean canDelete = false;
		boolean canView = false;
		String pagePoition = "";
		if (event.getOrigin().getData() != null) {
			pagePoition = (String) event.getOrigin().getData();
		}

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDelete = true;
			canView = true;
		} else if ("positionPage".equals(pagePoition)) {
			if (PlatformUtil.checkHasPermission(this,
					ActionKeys.POSITION_TREE_ORG_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(this,
					ActionKeys.POSITION_TREE_ORG_POSITION_DEL)) {
				canDelete = true;
			}
			if (PlatformUtil.checkHasPermission(this,
					ActionKeys.POSITION_TREE_ORG_POSITION_VIEW)) {
				canView = true;
			}
		}
		this.bean.getAddPositionOrganizationButton().setVisible(canAdd);
		this.bean.getDelPositionOrganizationButton().setVisible(canDelete);
		this.bean.getViewOrganizationPathButton().setVisible(canView);
	}

}
