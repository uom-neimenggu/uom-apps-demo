package cn.ffcs.uom.staff.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.WebKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.SimpleDESCry;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.action.bean.StaffPasswordQueryBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 员工密码查询.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffPasswordQueryComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffPasswordQueryBean bean = new StaffPasswordQueryBean();

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	/**
	 * 查询staff.
	 */
	private Staff qryStaffPasswordQuery;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

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
		this.setPortletInfoProvider(this);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffPasswordQueryWin() throws Exception {
		permissionOrganizationList = null;
		loadPermission();
		// this.onStaffQuery();
	}

	/**
	 * 查询员工. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffQuery() {
		try {
			qryStaffPasswordQuery = null;
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			qryStaffPasswordQuery = objSff;

			if (this.permissionOrganizationList != null
					&& this.permissionOrganizationList.size() > 0) {
				qryStaffPasswordQuery
						.setPermissionOrganizationList(permissionOrganizationList);
			}
			/**
			 * 默认数据权最大电信管理区域
			 */
			if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			}
			qryStaffPasswordQuery
					.setPermissionTelcomRegion(permissionTelcomRegion);

			this.bean.getStaffListboxPaging().setActivePage(0);
			if (!StrUtil.isNullOrEmpty(sffAcc.getStaffAccount())) {
				loadStaffPassword();
			} else {
				ZkUtil.showError("请输入要查询的账号！", "系统提示");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载数据权
	 * 
	 * @throws Exception
	 */
	public void loadPermission() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg
						.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());
				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
	}

	/**
	 * 加载用户密码列表
	 * 
	 * @throws Exception
	 */
	public PageInfo loadStaffPassword() throws Exception {
		PageInfo pageInfo = null;
		if (this.qryStaffPasswordQuery != null) {
			pageInfo = staffManager.forQuertyStaff(qryStaffPasswordQuery, bean
					.getStaffListboxPaging().getActivePage() + 1, bean
					.getStaffListboxPaging().getPageSize());
			List<Staff> list = pageInfo.getDataList();
			if (list.size() == 0) {
				ZkUtil.showError("查询无记录，请重新输入！", "系统提示");
				return null;
			}
			// 密码解密处理
			for (Staff staff : list) {
				if (!StrUtil.isEmpty(staff.getStaffPassword()))
					staff.setStaffPassword(SimpleDESCry.decry(staff
							.getStaffPassword()));
			}
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getStaffListbox().setModel(dataList);
			bean.getStaffListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		return pageInfo;
	}

	/**
	 * 选择用户密码事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectStaffPasswordRequest() throws Exception {

	}

	/**
	 * .重置查询内容
	 */
	public void onStaffReset() throws Exception {
		bean.getStaffAccount().setValue(null);
	}

	/**
	 * 分页事件
	 */
	public void onStaffListboxPaging() throws Exception {
		PageInfo pageInfo = this.loadStaffPassword();
	}

}
