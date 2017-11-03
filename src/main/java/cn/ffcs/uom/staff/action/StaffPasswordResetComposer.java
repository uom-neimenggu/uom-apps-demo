package cn.ffcs.uom.staff.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.SimpleDESCry;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.action.bean.StaffPasswordResetBean;
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
public class StaffPasswordResetComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffPasswordResetBean bean = new StaffPasswordResetBean();

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
	private Staff qryStaffPasswordReset;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private List<Staff> staffs;
	/**
	 * jdbcTemplate
	 */
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

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
	public void onCreate$staffPasswordResetWin() throws Exception {
		permissionOrganizationList = null;
		loadPermission();
		initPage();
	}

	/**
	 * 查询员工. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffQuery() {
		try {
			qryStaffPasswordReset = null;
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			qryStaffPasswordReset = objSff;

			if (this.permissionOrganizationList != null
					&& this.permissionOrganizationList.size() > 0) {
				qryStaffPasswordReset
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
			qryStaffPasswordReset
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
		if (this.qryStaffPasswordReset != null) {
			pageInfo = staffManager.forQuertyStaff(qryStaffPasswordReset, bean
					.getStaffListboxPaging().getActivePage() + 1, bean
					.getStaffListboxPaging().getPageSize());
			List<Staff> list = pageInfo.getDataList();
			if (list.size() == 0) {
				ZkUtil.showError("查询无记录，请重新输入！", "系统提示");
				return null;
			}
			// 密码解密处理
			for (Staff staff : list) {
				if (!StrUtil.isEmpty(staff.getStaffPassword())) {
					staff.setStaffPassword(SimpleDESCry.decry(staff
							.getStaffPassword()));
				}
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
	@SuppressWarnings("unchecked")
	public void onSelectStaffPasswordRequest() throws Exception {
		int selCount = bean.getStaffListbox().getSelectedCount();
		if (selCount > 0) {
			Set set = bean.getStaffListbox().getSelectedItems();
			Iterator it = set.iterator();
			if (it != null) {
				staffs = new ArrayList<Staff>();
				while (it.hasNext()) {
					Listitem listitem = (Listitem) it.next();
					staffs.add((Staff) listitem.getValue());
				}
			}
		}
	}

	/**
	 * 重置用户密码事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onStaffPasswordReset() throws Exception {
		int selCount = bean.getStaffListbox().getSelectedCount();
		if (selCount == 1) {// 只允许单条重置
			Messagebox.show("您确定要重置所选择的员工账号密码吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								for (Staff staff : staffs) {
									Long staffId = staff.getId();
									StaffAccount staffAccount = staffManager
											.getStaffAccount(null,
													staff.getId());
									if (staffAccount != null) {
										// String batchNumber =
										// OperateLog.gennerateBatchNumber();
										// staffAccount.setBatchNumber(batchNumber);
										// staffAccount.setStaffPassword(SimpleDESCry.encry("111111"));
										// staffAccount.update();
										// 调用过程修改EAM密码
										List<String> paramsList = new ArrayList<String>();
										paramsList.add(staffAccount
												.getStaffAccount());// 账号
										paramsList.add(SimpleDESCry
												.encry("ahdx2014"));// 新密码
										paramsList.add("");// 填充参数
										paramsList.add("0");// 类型 1为验证旧密码

										String resultStr = executeProcdure(
												"{call eam_update_passwd(?,?,?,?,?,?,?,?)}",
												paramsList);
										if (!StrUtil.isEmpty(resultStr)) {
											String[] resultArray = resultStr
													.split("#");
											if ("0".equals(resultArray[0])) {
												Messagebox
														.show("重置密码成功！当前密码【ahdx2014】");
											} else {
												Messagebox.show(String.format(
														"%s\n错误代码:%s\n描述:%s",
														resultArray[2],
														resultArray[1],
														resultArray[3]));
											}
										} else {
											Messagebox.show("重置密码失败！");
										}

										// DbUtil.procedureCall(jdbcTemplate,
										// "{call EAM_UPDATE_PASSWD(?,?,?,?,?,?,?,?)}",
										// paramsList);
									}
								}
							}
						}
					});

		} else {
			ZkUtil.showError("请选择需要重置密码的员工，只能选择一条。", "系统提示");
		}
	}

	@SuppressWarnings("unchecked")
	public String executeProcdure(final String procedureCall,
			final List<String> params) {
		try {
			Object o = jdbcTemplate.execute(new ConnectionCallback() {
				public Object doInConnection(Connection conn)
						throws SQLException, DataAccessException {
					StringBuffer sb = new StringBuffer();
					CallableStatement cstmt = conn.prepareCall(procedureCall);
					int iLen = params.size() + 4;
					for (int i = 1; i < iLen - 3; i++) {
						cstmt.setString(i, params.get(i - 1));
					}
					cstmt.registerOutParameter(iLen - 3, Types.VARCHAR);
					cstmt.registerOutParameter(iLen - 2, Types.VARCHAR);
					cstmt.registerOutParameter(iLen - 1, Types.VARCHAR);
					cstmt.registerOutParameter(iLen, Types.VARCHAR);
					cstmt.execute();
					sb.append(cstmt.getString(iLen - 3)).append("#")
							.append(cstmt.getString(iLen - 2)).append("#")
							.append(cstmt.getString(iLen - 1)).append("#")
							.append(cstmt.getString(iLen));
					return sb.toString();
				}
			});
			return o.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.setPagePosition("StaffPasswordResetPage");
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canPasswordReset = false;

		if (PlatformUtil.isAdmin()) {
			canPasswordReset = true;
		} else if ("StaffPasswordResetPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PASSWORD_RESET)) {
				canPasswordReset = true;
			}
		}
		this.bean.getPasswordResetButton().setVisible(canPasswordReset);
	}
}
