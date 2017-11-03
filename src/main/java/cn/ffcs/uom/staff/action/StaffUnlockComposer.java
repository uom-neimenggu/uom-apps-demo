package cn.ffcs.uom.staff.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
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
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
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
import cn.ffcs.uom.staff.action.bean.StaffUnlockBean;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.webservices.util.WsClientUtil;

/**
 * 员工密码查询.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffUnlockComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffUnlockBean bean = new StaffUnlockBean();

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	/**
	 * 查询staff.
	 */
	private Staff qryStaffUnlock;

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
	public void onCreate$staffUnlockWin() throws Exception {
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
			qryStaffUnlock = null;
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			qryStaffUnlock = objSff;
			// if (this.permissionOrganizationList != null &&
			// this.permissionOrganizationList.size() > 0) {
			qryStaffUnlock
					.setPermissionOrganizationList(permissionOrganizationList);
			// }
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
			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
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
		if (this.qryStaffUnlock != null) {
			pageInfo = staffManager.forQuertyStaff(qryStaffUnlock, bean
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
	 * 选择用户事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void onSelectStaffRequest() throws Exception {
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
	 * 解锁事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onStaffUnlock() throws Exception {
		int selCount = bean.getStaffListbox().getSelectedCount();
		if (selCount == 1) {// 只允许单条解锁
			Messagebox.show("是否解锁员工账号？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							boolean unLockResult = false;
							// String url =
							// "http://134.64.110.194:9001/eam-apps/services/passwordServices";//function
							// unlock
							String staffUnLockServiceUrl = UomClassProvider
									.getIntfUrl("staffUnLockServiceUrl");
							if (StrUtil.isEmpty(staffUnLockServiceUrl)) {
								ZkUtil.showError(
										"解锁失败,地址staffUnLockServiceUrl未配置",
										"解锁失败信息");
								return;
							}
							if (result == Messagebox.OK) {
								for (Staff staff : staffs) {
									if (null != staff
											&& !StrUtil.isEmpty(staff
													.getStaffAccount())) {
										String resultXml = WsClientUtil
												.wsCallStaffUnLock(
														staffUnLockServiceUrl,
														"unlock",
														getReqXml(staff
																.getStaffAccount()));
										unLockResult = checkResult(resultXml);
									}
								}

								if (unLockResult)
									Messagebox.show("账号解锁成功！");
								else
									Messagebox.show("账号解锁失败！");
							}
						}
					});
		} else {
			ZkUtil.showError("请选择需要解锁的员工，只能选择一条。", "系统提示");
		}
	}

	/**
	 * 验证EAM操作结果
	 * 
	 * @param resultXml
	 * @return
	 */
	public boolean checkResult(String resultXml) {
		boolean unLockResult = false;
		try {
			Document document = DocumentHelper.parseText(resultXml);
			Element root = document.getRootElement();
			Element rspType = (Element) root.selectSingleNode("//rspType");
			if ("0".equals(rspType.getTextTrim())) {
				unLockResult = true;
			}
		} catch (Exception e) {
			unLockResult = false;
		}
		return unLockResult;
	}

	/**
	 * 获取requestXML字符串参数
	 * 
	 * @param staffAccount
	 * @return
	 */
	public String getReqXml(String staffAccount) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String currTime = sdf.format(new Date());
		StringBuffer reqXmlBuffer = new StringBuffer();
		reqXmlBuffer
				.append("<root><sessionHeader><serviceCode>EAM130013</serviceCode><!--固定值--><transactionID>");
		reqXmlBuffer.append(currTime).append("0");
		reqXmlBuffer
				.append("</transactionID><!--【8位日期编码YYYYMMDD】＋【10位随机数】--><clientId>test1</clientId><!--系统编码--></sessionHeader><sessionBody><unlockStaff><staffAccount>");
		reqXmlBuffer.append(staffAccount);
		reqXmlBuffer
				.append("</staffAccount><!--需要解锁的账号--></unlockStaff></sessionBody></root>");
		return reqXmlBuffer.toString();
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
		this.setPagePosition("StaffUnlockPage");
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canUnlock = false;

		if (PlatformUtil.isAdmin()) {
			canUnlock = true;
		} else if ("StaffUnlockPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_UNLOCK)) {
				canUnlock = true;
			}
		}
		this.bean.getUnlockButton().setVisible(canUnlock);
	}
}
