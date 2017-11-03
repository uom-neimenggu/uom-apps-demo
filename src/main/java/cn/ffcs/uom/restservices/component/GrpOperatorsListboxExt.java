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
import cn.ffcs.uom.restservices.component.bean.GrpOperatorsListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpOperators;
import cn.ffcs.uom.restservices.model.ResponseOutParam;
import cn.ffcs.uom.restservices.model.TcpContOutParam;
import cn.ffcs.uom.restservices.model.UomModelStorageOutParam;
import cn.ffcs.uom.webservices.constants.WsConstants;

/**
 * 经营主体管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c)
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-8-25
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class GrpOperatorsListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private GrpOperatorsListboxExtBean bean = new GrpOperatorsListboxExtBean();

	/**
	 * 经营主体业务
	 */
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * grpOperators.
	 */
	private GrpOperators grpOperators;

	/**
	 * 查询queryGrpOperators.
	 */
	private GrpOperators queryGrpOperators;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public GrpOperatorsListboxExt() {
		Executions.createComponents(
				"/pages/restservices/comp/grp_operators_listbox_ext.zul", this,
				null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.setGrpOperatorsButtonValid(false);

	}

	/**
	 * 经营主体选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGrpOperatorsSelectRequest() throws Exception {
		if (bean.getGrpOperatorsListbox().getSelectedCount() > 0) {
			grpOperators = (GrpOperators) bean.getGrpOperatorsListbox()
					.getSelectedItem().getValue();
			this.setGrpOperatorsButtonValid(true);
			Events.postEvent(ChannelInfoConstant.ON_GRP_OPERATORS_SELECT, this,
					grpOperators);
		}
	}

	/**
	 * 查询经营主体. .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onGrpOperatorsQuery() {
		this.bean.getGrpOperatorsListboxPaging().setActivePage(0);
		this.setGrpOperatorsButtonValid(false);
		this.onQueryGrpOperatorsList();
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onGrpOperatorsReset() throws Exception {
		bean.getOperatorsNbr().setValue(null);
		bean.getOperatorsName().setValue(null);
	}

	/**
	 * @author 朱林涛
	 */
	public void onQueryGrpOperatorsList() {

		queryGrpOperators = new GrpOperators();

		queryGrpOperators.setOperatorsNbr(this.bean.getOperatorsNbr()
				.getValue());
		queryGrpOperators.setOperatorsName(this.bean.getOperatorsName()
				.getValue());

		PageInfo pageInfo = channelInfoManager.queryPageInfoByGrpOperators(
				queryGrpOperators, this.bean.getGrpOperatorsListboxPaging()
						.getActivePage() + 1, this.bean
						.getGrpOperatorsListboxPaging().getPageSize());
		ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getGrpOperatorsListbox().setModel(list);
		this.bean.getGrpOperatorsListboxPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 分页 .
	 * 
	 * @author 朱林涛
	 */
	public void onGrpOperatorsListboxPaging() {
		this.onQueryGrpOperatorsList();
	}

	/**
	 * 更新选择的经营主体.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGrpOperatorsUpdate() throws Exception {
		ZkUtil.showQuestion("确定要更新经营主体吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (grpOperators == null
							|| grpOperators.getGrpOperatorsId() == null) {
						ZkUtil.showError("请选择你要更新的经营主体", "提示信息");
						return;
					} else {

						OrganizationExtendAttr queryOrganizationExtendAttr = new OrganizationExtendAttr();
						queryOrganizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
						queryOrganizationExtendAttr
								.setOrgAttrValue(grpOperators.getOperatorsNbr());

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
									.contractRootInParamPacking(grpOperators,
											null);

							if (rootIn != null) {

								UomModelStorageOutParam uomModelStorageOutParam = channelInfoManager
										.saveUomModelStorage(rootIn,
												rootOutParam);

								rootOutParam = uomModelStorageOutParam
										.getRootOutParam();

							}

							onQueryGrpOperatorsList();

							if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS
									.equals(rootOutParam.getTcpCont()
											.getResponse().getRspType())) {
								Messagebox.show("更新经营主体成功！");
							} else {
								Messagebox.show("更新经营主体失败，请重新操作！");
							}

						} else {
							Messagebox.show("主数据中不存在经营主体对应关系，请重新选择更新！");
						}

					}
				}
			}
		});

	}

	public void cleanTabs() {
		queryGrpOperators = null;
		grpOperators = null;
		Events.postEvent(ChannelInfoConstant.ON_GRP_OPERATORS_CLEAR_TABS, this,
				null);
	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setGrpOperatorsWindowDivVisible(boolean visible) {
		bean.getGrpOperatorsWindowDiv().setVisible(visible);
	}

	/**
	 * 设置经营主体按钮的状态.
	 * 
	 * @param canUpdate
	 *            更新按钮
	 */
	private void setGrpOperatorsButtonValid(final Boolean canUpdate) {
		bean.getUpdateGrpOperatorsButton().setDisabled(!canUpdate);
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
		} else if ("grpOperatorsPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GRP_OPERATORS_UPDATE)) {
				canUpdate = true;
			}
		}

		this.bean.getUpdateGrpOperatorsButton().setVisible(canUpdate);
	}

}
