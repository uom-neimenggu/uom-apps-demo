package cn.ffcs.uom.dataPermission.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.model.RoleConstants;
import cn.ffcs.raptornuke.portal.util.PortalUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.action.bean.RoleListboxBean;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.manager.RoleManager;
import cn.ffcs.uom.dataPermission.vo.RoleAdapter;

@Controller
@Scope("prototype")
public class RoleListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul.
	 */
	private final String zul = "/pages/dataPermission/comp/role_listbox.zul";

	/**
	 * bean.
	 */
	@Getter
	private RoleListboxBean bean = new RoleListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("roleManager")
	private RoleManager roleManager = (RoleManager) ApplicationContextUtil
			.getBean("roleManager");

	/**
	 * 当前选择角色
	 */
	private RoleAdapter role;

	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 停用角色，排除系统角色
	 */
	private HashSet<String> stopRoles = new HashSet<String>();

	public RoleListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 排除角色初始化，社区，组织，系统
		 */
		for (int i = 0; i < RoleConstants.SYSTEM_COMMUNITY_ROLES.length; i++) {
			stopRoles.add(RoleConstants.SYSTEM_COMMUNITY_ROLES[i]);
		}
		for (int i = 0; i < RoleConstants.SYSTEM_ORGANIZATION_ROLES.length; i++) {
			stopRoles.add(RoleConstants.SYSTEM_ORGANIZATION_ROLES[i]);
		}
		for (int i = 0; i < RoleConstants.SYSTEM_ROLES.length; i++) {
			stopRoles.add(RoleConstants.SYSTEM_ROLES[i]);
		}
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		bean.getRoleListPaging().setPageSize(25);
		onQueryRole();
	}

	/**
	 * 查询角色
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onQueryRole() throws Exception {
		/**
		 * 查询初始化：第一页
		 */
		this.bean.getRoleListPaging().setActivePage(0);
		this.queryRole();
		/**
		 * 抛出角色查询事件：用于tab页数据清空
		 */
		Events.postEvent(RoleConstant.ON_ROLE_QUERY, this, null);
	}

	/**
	 * 角色选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleSelect() throws Exception {
		if (bean.getRoleListBox().getSelectedCount() > 0) {
			role = (RoleAdapter) bean.getRoleListBox().getSelectedItem()
					.getValue();
			/**
			 * 抛出选择角色事件
			 */
			Events.postEvent(RoleConstant.ON_SELECT_ROLE, this, role);
		}
	}

	/**
	 * 查询角色
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryRole() throws Exception {
		ListboxUtils.clearListbox(bean.getRoleListBox());
		PageInfo pageInfo = roleManager.queryPageInfoByRole(null, this.bean
				.getRoleListPaging().getActivePage() + 1, this.bean
				.getRoleListPaging().getPageSize());
		List<RoleAdapter> roles = new ArrayList<RoleAdapter>();
		for (int i = 0; i < pageInfo.getDataList().size(); i++) {
			Role item = (Role) pageInfo.getDataList().get(i);
			RoleAdapter role = new RoleAdapter();

			HttpServletRequest request = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();

			role.setTitle(item.getTitle(PortalUtil.getLocale(request)));
			role.setDescription(item.getDescription(PortalUtil
					.getLocale(request)));
			role.setRole(item);
			if(stopRoles.contains(item.getName())){
				role.setIsSystemRole(true);
			} else {
				role.setIsSystemRole(false);
			}
			roles.add(role);
		}

		ListModel dataList = new BindingListModelList(roles, true);
		this.bean.getRoleListBox().setModel(dataList);
		this.bean.getRoleListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
		role = null;
	}

	/**
	 * 分页
	 * 
	 * @throws Exception
	 */
	public void onRoleListboxPaging() throws Exception {
		this.queryRole();
	}
}
