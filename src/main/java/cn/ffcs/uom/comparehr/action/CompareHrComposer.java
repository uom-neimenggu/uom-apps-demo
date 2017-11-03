package cn.ffcs.uom.comparehr.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.comparehr.action.bean.CompareHrMainBean;
import cn.ffcs.uom.comparehr.constants.OperateHrConstant;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 人力比较.
 * 
 * @author faq
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class CompareHrComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private CompareHrMainBean bean = new CompareHrMainBean();

	/**
	 * 查询staff.
	 */
	private Staff qryStaff;

	private List<Staff> staffs;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		// TODO Auto-generated method stub
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.setPortletInfoProvider(this);
	}

	/**
	 * 初始化bean
	 */
	private void bindBean() {
		NodeVo nAdd = new NodeVo();
		NodeVo nUpdate = new NodeVo();
		NodeVo nNoChange = new NodeVo();
		NodeVo nUpdated = new NodeVo();
		NodeVo nRemove = new NodeVo();
		nAdd.setId(OperateHrConstant.STATUS_ADD);
		nAdd.setName(OperateHrConstant.STATUS_ADD_STR);
		nUpdate.setId(OperateHrConstant.STATUS_UPDATE);
		nUpdate.setName(OperateHrConstant.STATUS_UPDATE_STR);
		nNoChange.setId(OperateHrConstant.STATUS_NOCHANGE);
		nNoChange.setName(OperateHrConstant.STATUS_NOCHANGE_STR);
		nUpdated.setId(OperateHrConstant.STATUS_UPDATED);
		nUpdated.setName(OperateHrConstant.STATUS_UPDATED_STR);
		nRemove.setId(OperateHrConstant.STATUS_DEL);
		nRemove.setName(OperateHrConstant.STATUS_DEL_STR);
		List<NodeVo> liTp = new ArrayList<NodeVo>();
		liTp.add(nAdd);
		liTp.add(nUpdate);
		liTp.add(nNoChange);
		liTp.add(nUpdated);
		liTp.add(nRemove);
		ListboxUtils.rendererForEdit(bean.getCurrentStatusListbox(), liTp);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$compareMainWin() throws Exception {
		bindBean();
		// 去除默认查询this.onStaffQuery();
		/**
		 * 多选
		 */
		this.bean.getCompareListbox().setCheckmark(true);
		this.bean.getCompareListbox().setMultiple(true);
		initPage();
	}

	/**
	 * 选择比较列表的行.
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	public void onCompareSelectRequest() throws Exception {
		int selCount = bean.getCompareListbox().getSelectedCount();
		if (selCount > 0) {
			Set set = bean.getCompareListbox().getSelectedItems();
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
	 * 校正员工账号、工号
	 * 
	 * @throws InterruptedException
	 */
	public void onReviseStaff() throws InterruptedException {
		int selCount = bean.getCompareListbox().getSelectedCount();
		if (selCount > 0) {
			if (null != staffs && staffs.size() > 0) {
				Messagebox.show("您确定要校正所选择的员工工号和账号？", "提示信息", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.INFORMATION,
						new EventListener() {
							public void onEvent(Event event) throws Exception {
								Integer result = (Integer) event.getData();
								if (result == Messagebox.OK) {
									for (Staff staff : staffs) {
										String hrStaffNbr = staff
												.getHrStaffNbr();
										String hrStaffAccount = staff
												.getHrStaffAccount();
										if (!StrUtil.isNullOrEmpty(hrStaffNbr)) {
											if (hrStaffNbr.startsWith("34")) {
												hrStaffAccount = hrStaffNbr
														.substring(2);
											} else if (hrStaffNbr
													.startsWith("W34")) {
												hrStaffAccount = hrStaffAccount
														.replace("W34", "W9");
											}
										}

										StaffAccount staffAccount = new StaffAccount();
										staffAccount
												.setStaffAccount(hrStaffAccount);

										List<StaffAccount> staffAccountList = staffManager
												.getNoStatusStaffAccountList(staffAccount);

										if (staffAccountList != null
												&& staffAccountList.size() > 0) {
											// 排除自身问题
											for (StaffAccount newStaffAccount : staffAccountList) {
												if (!(staff.getStaffId()
														.equals(newStaffAccount
																.getStaffId()))) {
													ZkUtil.showError(
															"员工："
																	+ staff.getStaffName()
																	+ "校正后工号会重复,不能校正,请确认",
															"提示信息");
													return;
												}
											}
										}
									}
									staffManager.updateStaffByOperateHr(staffs);
									Messagebox.show("校正改员工工号和账号成功！");
									onStaffQuery();
								}
							}
						});
			}
		} else {
			ZkUtil.showError("请选择需要校正的员工，可选多条。", "系统提示");
		}
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void queryStaff() throws Exception {
		if (this.qryStaff != null) {
			PageInfo pageInfo = staffManager.forQuertyCompareStaff(qryStaff,
					bean.getCompareListPaging().getActivePage() + 1, bean
							.getCompareListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getCompareListbox().setModel(dataList);
			bean.getCompareListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryComparePaging() throws Exception {
		this.queryStaff();
	}

	/**
	 * 查询员工.
	 */
	public void onStaffQuery() {
		try {
			cleanTabs();
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			OrganizationRelation organizationRelation = bean
					.getOrganizationRelationTreeBandboxExt()
					.getOrganizationRelation();
			objSff.setOrganizationRelation(organizationRelation);
			qryStaff = objSff;
			if (!StrUtil.isNullOrEmpty(bean.getCurrentStatusListbox()
					.getSelectedItem().getValue())) {
				String currentStatus = bean.getCurrentStatusListbox()
						.getSelectedItem().getValue().toString();
				qryStaff.setCurrentStatus(currentStatus);
			}
			queryStaff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanTabs() {
		qryStaff = null;
		staffs = null;
	}

	/**
	 * .重置查询内容
	 */
	public void onStaffReset() throws Exception {
		bean.getStaffAccount().setValue(null);
		bean.getStaffName().setValue(null);
		bean.getStaffCode().setValue(null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.setPagePosition("compareHrPage");
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canRevise = false;

		if (PlatformUtil.isAdmin()) {
			canRevise = true;
		} else if ("compareHrPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COMPAREHR_REVISE)) {
				canRevise = true;
			}
		}
		this.bean.getReviseButton().setVisible(canRevise);
	}
}
