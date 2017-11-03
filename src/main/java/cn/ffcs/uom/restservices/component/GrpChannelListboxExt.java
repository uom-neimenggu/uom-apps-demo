package cn.ffcs.uom.restservices.component;

import lombok.Getter;
import lombok.Setter;

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

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.restservices.component.bean.GrpChannelListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.ResponseOutParam;
import cn.ffcs.uom.restservices.model.TcpContOutParam;
import cn.ffcs.uom.restservices.model.UomModelStorageOutParam;
import cn.ffcs.uom.webservices.constants.WsConstants;

/**
 * 渠道单元管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class GrpChannelListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private GrpChannelListboxExtBean bean = new GrpChannelListboxExtBean();

	/**
	 * 渠道单元业务
	 */
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * grpChannel.
	 */
	private GrpChannel grpChannel;

	/**
	 * 查询queryGrpChannel.
	 */
	private GrpChannel queryGrpChannel;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public GrpChannelListboxExt() {
		Executions.createComponents(
				"/pages/restservices/comp/grp_channel_listbox_ext.zul", this,
				null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.setGrpChannelButtonValid(false);
	}

	/**
	 * 渠道单元选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGrpChannelSelectRequest() throws Exception {
		if (bean.getGrpChannelListbox().getSelectedCount() > 0) {
			grpChannel = (GrpChannel) bean.getGrpChannelListbox()
					.getSelectedItem().getValue();
			this.setGrpChannelButtonValid(true);
			Events.postEvent(ChannelInfoConstant.ON_GRP_CHANNEL_SELECT, this,
					grpChannel);
		}
	}

	/**
	 * 查询渠道单元. .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onGrpChannelQuery() {
		this.bean.getGrpChannelListboxPaging().setActivePage(0);
		this.setGrpChannelButtonValid(false);
		this.onQueryGrpChannelList();
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onGrpChannelReset() throws Exception {
		bean.getChannelNbr().setValue(null);
		bean.getChannelName().setValue(null);
	}

	/**
	 * @author 朱林涛
	 */
	public void onQueryGrpChannelList() {

		queryGrpChannel = new GrpChannel();

		queryGrpChannel.setChannelNbr(this.bean.getChannelNbr().getValue());
		queryGrpChannel.setChannelName(this.bean.getChannelName().getValue());

		PageInfo pageInfo = channelInfoManager.queryPageInfoByGrpChannel(
				queryGrpChannel, this.bean.getGrpChannelListboxPaging()
						.getActivePage() + 1, this.bean
						.getGrpChannelListboxPaging().getPageSize());
		ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getGrpChannelListbox().setModel(list);
		this.bean.getGrpChannelListboxPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 分页 .
	 * 
	 * @author 朱林涛
	 */
	public void onGrpChannelListboxPaging() {
		this.onQueryGrpChannelList();
	}

	/**
	 * 更新选择的渠道单元.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGrpChannelUpdate() throws Exception {
		ZkUtil.showQuestion("确定要更新渠道单元吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (grpChannel == null
							|| grpChannel.getGrpChannelId() == null) {
						ZkUtil.showError("请选择你要更新的渠道单元", "提示信息");
						return;
					} else {

						OrganizationExtendAttr queryOrganizationExtendAttr = new OrganizationExtendAttr();
						queryOrganizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
						queryOrganizationExtendAttr.setOrgAttrValue(grpChannel
								.getChannelNbr());

						// 查询该经营主体在主数据中是否存在
						OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

						if (organizationExtendAttr != null
								&& organizationExtendAttr.getOrgId() != null) {

							ContractRootInParam rootIn = null;
							ContractRootOutParam rootOutParam = new ContractRootOutParam();
							TcpContOutParam tcpCont = new TcpContOutParam();
							ResponseOutParam response = new ResponseOutParam();

							response.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);

							tcpCont.setResponse(response);
							rootOutParam.setTcpCont(tcpCont);

							rootIn = channelInfoManager
									.contractRootInParamPacking(null,
											grpChannel);

							if (rootIn != null) {

								UomModelStorageOutParam uomModelStorageOutParam = channelInfoManager
										.saveUomModelStorage(rootIn,
												rootOutParam);

								rootOutParam = uomModelStorageOutParam
										.getRootOutParam();

							}

							onQueryGrpChannelList();

							if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS
									.equals(rootOutParam.getTcpCont()
											.getResponse().getRspType())) {
								Messagebox.show("更新渠道单元成功！");
							} else {
								Messagebox.show("更新渠道单元失败，请重新操作！");
							}

						} else {
							Messagebox.show("主数据中不存在渠道单元对应关系，请重新选择更新！");
						}

					}
				}
			}
		});

	}

	public void cleanTabs() {
		queryGrpChannel = null;
		grpChannel = null;
		Events.postEvent(ChannelInfoConstant.ON_GRP_CHANNEL_CLEAR_TABS, this,
				null);
	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setGrpChannelWindowDivVisible(boolean visible) {
		bean.getGrpChannelWindowDiv().setVisible(visible);
	}

	/**
	 * 设置渠道单元按钮的状态.
	 * 
	 * @param canUpdate
	 *            更新按钮
	 */
	private void setGrpChannelButtonValid(final Boolean canUpdate) {
		bean.getUpdateGrpChannelButton().setDisabled(!canUpdate);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {

		boolean canUpdate = false;

		if (PlatformUtil.isAdmin()) {
			canUpdate = true;
		} else if ("grpChannelPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GRP_CHANNEL_UPDATE)) {
				canUpdate = true;
			}
		}

		this.bean.getUpdateGrpChannelButton().setVisible(canUpdate);
	}

}
