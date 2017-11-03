package cn.ffcs.uom.organization.action;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ExportExcelNew;
import cn.ffcs.uom.common.util.FtpUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.action.bean.ChannelPackareaRelationListboxBean;
import cn.ffcs.uom.organization.manager.ChannelPackareaRelationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：20160919
 * @功能说明：包区网点展示
 */
@Controller
@Scope("prototype")
public class ChannelPackareaRelationListboxComposer extends Div implements
		IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul
	 */
	private final String zul = "/pages/organization/comp/channel_packarea_relation_listbox.zul";

	/**
	 * 包区和网点的页面bean
	 */
	@Setter
	@Getter
	private ChannelPackareaRelationListboxBean bean = new ChannelPackareaRelationListboxBean();

	/**
	 * 查询页面的参数
	 */
	private ChannelPackareaRelationVo queryChannelPackareaRelationVo;

	/**
	 * 页面选中一条数据
	 */
	private ChannelPackareaRelationVo channelPackareaRelationVo;

	private Boolean isBandbox = false;

	/**
	 * 是否是审批Listbox
	 */
	@Getter
	@Setter
	private Boolean isApprove = false;

	/**
	 * 权限
	 */
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	/**
	 * 包区和渠道关系业务
	 */
	private ChannelPackareaRelationManager channelPackareaRelationManager = (ChannelPackareaRelationManager) ApplicationContextUtil
			.getBean("channelPackareaRelationManager");

	/**
	 * 构造函数，初始化页面 .
	 * 
	 * @author xiaof 2016年9月19日 xiaof
	 * @throws SystemException
	 * @throws PortalException
	 */
	public ChannelPackareaRelationListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
				permissionTelcomRegion.setRegionName("中国");
			} else {
				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
				//permissionTelcomRegion.getRegionName();
			}

			bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
		}

	}

	public void onChannelPackareaRelationAdd() throws PortalException,
			SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openEditOrganizationTranWin("add");
	}

	public void onChannelPackareaRelationView() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.openEditOrganizationTranWin("view");
	}

	public void onChannelPackareaRelationApprove() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (BaseUnitConstants.ENTT_STATE_PROCESS
				.equals(channelPackareaRelationVo.getStatusCd())) {
			this.openEditOrganizationTranWin("approve");
		} else {
			ZkUtil.showInformation("此记录不是审批中，无法审批", "提示信息");
		}
	}

	public void onChannelPackareaRelationEdit() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (BaseUnitConstants.ENTT_STATE_ACTIVE
				.equals(channelPackareaRelationVo.getStatusCd())) {
			this.openEditOrganizationTranWin("mod");
		} else {
			ZkUtil.showInformation("此记录不是生效状态，无法修改", "提示信息");
		}
	}

	private void openEditOrganizationTranWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			if (!"add".equals(opType)) {
				arg.put("channelPackareaRelationVo", channelPackareaRelationVo);
			}

			Window win = null;
			win = (Window) Executions.createComponents(
					"/pages/organization/channel_packarea_tran_edit.zul", this,
					arg);
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				public void onEvent(Event event) {

					if (event.getData() != null) {

						UomGroupOrgTran uomGroupOrgTran = (UomGroupOrgTran) event
								.getData();

						if (uomGroupOrgTran != null
								&& uomGroupOrgTran.getOrgId() != null
								&& uomGroupOrgTran.getTranOrgId() != null) {

							// 渠道网点id查询
							Organization organization = (Organization) Organization
									.repository().getObject(Organization.class,
											uomGroupOrgTran.getOrgId());

							// 营销包区
							Organization tranOrganization = (Organization) Organization
									.repository().getObject(
											Organization.class,
											Long.valueOf(uomGroupOrgTran
													.getTranOrgId()));

							if (organization != null) {
								bean.getAgentChannelName().setValue(
										organization.getOrgName());
								bean.getAgentChannelNbr().setValue(
										organization.getOrgCode());
							}

							if (tranOrganization != null) {
								bean.getCustPackareaName().setValue(
										tranOrganization.getOrgName());
								bean.getCustPackareaCode().setValue(
										tranOrganization.getOrgCode());
							}

							bean.getTelcomRegion().setTelcomRegion(
									permissionTelcomRegion);
						}
					}
					onQueryChannelPackareaRelation();
				}
			});
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception ec) {
			ec.printStackTrace();
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		// 根据权限设置按钮是否显示
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;
		boolean canView = false;
		boolean canExport = false;
		boolean canApprove = false;

		if ("channelPackareaRelationPage".equals(page)) {
			// 管理员全部显示
			if (PlatformUtil.isAdmin()) {
				canAdd = true;
				canEdit = true;
				canDel = true;
				canView = true;
				canExport = true;
			} else {
				// 不是管理员，挨个判断
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_RELATION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_RELATION_EDIT)) {
					canEdit = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_RELATION_VIEW)) {
					canView = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_RELATION_EXPORT)) {
					canExport = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_RELATION_DEL)) {
					canDel = true;
				}
			}
		} else if ("channelPackareaApprovePage".equals(page)) {
			// 管理员全部显示
			if (PlatformUtil.isAdmin()) {
				canView = true;
				canApprove = true;
			} else {
				// 不是管理员，挨个判断
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_APPROVE_VIEW)) {
					canView = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.CHANNEL_PACKAREA_APPROVE_APPROVE)) {
					canApprove = true;
				}
			}
		}

		// 设置页面按钮状态
		this.bean.getAddChannelPackareaRelationButton().setVisible(canAdd);
		this.bean.getEditChannelPackareaRelationButton().setVisible(canEdit);
		this.bean.getDelChannelPackareaRelationButton().setVisible(canDel);
		this.bean.getViewChannelPackareaRelationButton().setVisible(canView);
		this.bean.getExportChannelPackareaRelationButton()
				.setVisible(canExport);
		this.bean.getApproveChannelPackareaRelationButton().setVisible(
				canApprove);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {

		// this.setOrganizationButtonValid(true, false, false);
		this.setChannelPackareaRelationButtonValid(true, false, false, false,
				true, false);
		bean.getChannelPackareaRelationListBox().setPageSize(25);
	}

	/**
	 * 设置组织按钮的状态.初始化设置为可以选择
	 */
	private void setChannelPackareaRelationButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDel, final Boolean canView,
			final Boolean canExport, final Boolean canApprove) {
		// true标识不可编辑，false标识可编辑
		this.bean.getAddChannelPackareaRelationButton().setDisabled(!canAdd);
		this.bean.getEditChannelPackareaRelationButton().setDisabled(!canEdit);
		this.bean.getDelChannelPackareaRelationButton().setDisabled(!canDel);
		this.bean.getViewChannelPackareaRelationButton().setDisabled(!canView);
		this.bean.getExportChannelPackareaRelationButton().setDisabled(
				!canExport);
		this.bean.getApproveChannelPackareaRelationButton().setDisabled(
				!canApprove);
	}

	/**
	 * 页面查询事件 .
	 * 
	 * @author xiaof 2016年9月19日 xiaof
	 */
	public void onQueryChannelPackareaRelation() {
		// 查询的时候，未选中数据，应该设置按钮不可选中,并清空选中对象
		channelPackareaRelationVo = null;
		// 获取到关系对应的标识号
		// this.setOrganizationButtonValid(true, false, false);
		this.setChannelPackareaRelationButtonValid(true, false, false, false,
				true, false);
		this.bean.getChannelPackareaRelationListPaging().setActivePage(0);
		this.queryChannelPackareaRelationList();
	}

	/**
	 * 重置数据框 .
	 *
	 */
	public void onResetChannelPackareaRelation() {
		this.bean.getAgentChannelName().setValue(null);
		this.bean.getAgentChannelNbr().setValue(null);
		this.bean.getCustPackareaCode().setValue(null);
		this.bean.getCustPackareaName().setValue(null);
		this.bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
		ListboxUtils
				.clearListbox(this.bean.getChannelPackareaRelationListBox());
		this.bean.getChannelPackareaRelationListPaging().setTotalSize(1);
	}

	private ChannelPackareaRelationVo initChannelPackareaRelationVo() {
		ChannelPackareaRelationVo channelPackareaRelationVo = new ChannelPackareaRelationVo();
		//获取渠道编码
		channelPackareaRelationVo.setAgentChannelNbr(this.bean
				.getAgentChannelNbr().getValue());
		//渠道名称
		channelPackareaRelationVo.setAgentChannelName(this.bean
				.getAgentChannelName().getValue());
		//包区编码
		channelPackareaRelationVo.setCustPackareaCode(this.bean
				.getCustPackareaCode().getValue());
		//包区名称
		channelPackareaRelationVo.setCustPackareaName(this.bean
				.getCustPackareaName().getValue());
		
		if (this.bean.getTelcomRegion().getTelcomRegion() != null
				&& this.bean.getTelcomRegion().getTelcomRegion()
						.getTelcomRegionId() != null) {
			//电信管理区域下拉选
			channelPackareaRelationVo.setTelcomRegionId(this.bean
					.getTelcomRegion().getTelcomRegion().getTelcomRegionId());
		} else if (permissionTelcomRegion != null) {
			channelPackareaRelationVo.setTelcomRegionId(permissionTelcomRegion
					.getTelcomRegionId());
		}

		if (isApprove) {
			channelPackareaRelationVo
					.setStatusCd(BaseUnitConstants.ENTT_STATE_PROCESS);
		}

		return channelPackareaRelationVo;
	}

	private void queryChannelPackareaRelationList() {
		queryChannelPackareaRelationVo = initChannelPackareaRelationVo();

		PageInfo pageInfo = channelPackareaRelationManager
				.queryPageInfoByChannelPackareaRelationVo(
						queryChannelPackareaRelationVo, this.bean
								.getChannelPackareaRelationListPaging()
								.getActivePage() + 1, this.bean
								.getChannelPackareaRelationListPaging()
								.getPageSize());

		ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getChannelPackareaRelationListBox().setModel(list);
		this.bean.getChannelPackareaRelationListPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 列表选择 .
	 * 
	 */
	public void onChannelPackareaRelationSelect() {
		// 判断是否有选择的关系
		if (this.bean.getChannelPackareaRelationListBox().getSelectedCount() > 0) {
			channelPackareaRelationVo = (ChannelPackareaRelationVo) this.bean
					.getChannelPackareaRelationListBox().getSelectedItem()
					.getValue();

			if (channelPackareaRelationVo.getId() != null) {
				// 获取到关系对应的标识号
				// this.setOrganizationButtonValid(true, true, true);
				this.setChannelPackareaRelationButtonValid(true, true, true,
						true, true, true);
			}
		}
	}

	/**
	 * 导出
	 */
	public void onChannelPackareaRelationExport() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		// 设置字符集
		String charset = "UTF-8";
		// 项目根目录
		HttpServletRequest httpRequest = (HttpServletRequest) Executions
				.getCurrent().getNativeRequest();

		String createDate = new SimpleDateFormat("yyyy-MM").format(new Date());

		queryChannelPackareaRelationVo = initChannelPackareaRelationVo();

		List<Map<String, Object>> channelPackareaRelationList = channelPackareaRelationManager
				.queryListByChannelPackareaRelationVo(queryChannelPackareaRelationVo);

		String excelName = "包区网点关系数据";

		if (channelPackareaRelationList == null
				|| channelPackareaRelationList.size() <= 0) {
			Messagebox.show(excelName + "数据为空,未下载！");
			return;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExportExcelNew.exportExcel(excelName + createDate,
				channelPackareaRelationList, out);
		byte[] content = out.toByteArray();

		// 清空缓冲区
		out.flush();
		// 关闭文件输出流
		out.close();

		// 编码后文件名
		String encodedName = null;
		encodedName = URLEncoder.encode(excelName + createDate + ".xlsx",
				charset);
		// 将空格替换为+号
		encodedName = encodedName.replace("%20", "+");

		// 解决ie6 bug 或者是火狐浏览器
		if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
				|| Servlets.isGecko3(httpRequest)) {
			encodedName = new String(
					(excelName + createDate + ".xlsx").getBytes(charset),
					"ISO8859-1");
		}

		Filedownload.save(content, "application/octet-stream", encodedName);
	}

	/**
	 * 删除一条数据关系 .
	 * 
	 * @author xiaof 2016年9月21日 xiaof
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onChannelPackareaRelationDel() throws PortalException,
			SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		try {
			Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						@Override
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								channelPackareaRelationManager
										.removeUomGroupOrgTran(channelPackareaRelationVo);
								delPic(channelPackareaRelationVo);
								Messagebox.show("包区网点关系删除成功");
								// 更新页面，重新显示
								onQueryChannelPackareaRelation();
							}
						}
					});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void delPic(ChannelPackareaRelationVo channelPackareaRelationVo) {
		String[] flagArray = { "cert", "five", "store" };

		for (int i = 0; i < flagArray.length; i++) {
			String hostname = UomClassProvider.getSystemConfig("ftpHost");
			String username = UomClassProvider.getSystemConfig("ftpUsername");
			String password = UomClassProvider.getSystemConfig("ftpPassword");
			String pathname = UomClassProvider
					.getSystemConfig("uploadPicFtpPath");
			String filename = channelPackareaRelationVo.getAgentChannelId()
					+ "_" + channelPackareaRelationVo.getCustPackareaId() + "_"
					+ flagArray[i] + ".jpg";
			FtpUtil.deleteFile(hostname, 21, username, password, pathname,
					filename);
		}
	}

	/**
	 * 分页 .
	 * 
	 * @author xiaof 2016年12月5日 xiaof
	 */
	public void onChannelPackareaRelationListboxPaging() {
		this.queryChannelPackareaRelationList();
	}

	public Boolean getIsBandbox() {
		return isBandbox;
	}

	public void setIsBandbox(Boolean isBandbox) {
		this.isBandbox = isBandbox;
	}
}
