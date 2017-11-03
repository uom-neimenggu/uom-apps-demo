package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;
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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.action.bean.GroupOrganizationListboxExtBean;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.model.GroupOrganization;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

/**
 * 组织管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class GroupOrganizationListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	@Getter
	private GroupOrganizationListboxExtBean bean = new GroupOrganizationListboxExtBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("groupOrganizationManager")
	private GroupOrganizationManager groupOrganizationManager = (GroupOrganizationManager) ApplicationContextUtil
			.getBean("groupOrganizationManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/comp/group_organization_listbox_ext.zul";

	/**
	 * 当前选择的organization
	 */
	private GroupOrganization groupOrganization;

	/**
	 * 查询queryGroupOrganization.
	 */
	private GroupOrganization queryGroupOrganization;

	private UomGroupOrgTran uomGroupOrgTran;

	/**
	 * 组织管理区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;
	/**
	 * listbox是否只有成本树组织
	 */
	@Getter
	@Setter
	private Boolean isCostListbox = false;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 组织类型列表
	 */
	private List<String> queryOrgTypeList;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public GroupOrganizationListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		/**
		 * 设定组织类型列表
		 */
		this.addForward(OrganizationTranConstant.ON_SET_ORGTYPE_REQUEST, this,
				"onSetOrgTypeListResponse");
		/**
		 * 设定组织类型列表
		 */
		this.addForward(OrganizationTranConstant.ON_SUPPLIER_INFO_REQUEST,
				this, "onSupplierInfoResponse");

	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		this.bindCombobox();
	}

	/**
	 * 绑定combobox.
	 * 
	 */
	private void bindCombobox() throws Exception {

		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"GroupOrganization", "orgType");

		ListboxUtils.rendererForEdit(this.bean.getOrgType(), orgTypeList);

	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onQueryGroupOrganization() throws Exception {

		queryGroupOrganization = new GroupOrganization();
		queryOrgTypeList = new ArrayList<String>();

		PubUtil.fillPoFromBean(bean, queryGroupOrganization);

		if (!StrUtil.isEmpty(queryGroupOrganization.getOrgType())) {
			queryOrgTypeList.add(queryGroupOrganization.getOrgType());
		}

		if (bean.getTDesc2() != null
				&& !StrUtil.isEmpty(bean.getTDesc2().getValue())) {
			queryGroupOrganization.setTDesc(bean.getTDesc2().getValue());
		}

		if (uomGroupOrgTran != null
				&& !StrUtil.isEmpty(uomGroupOrgTran.getTranOrgCode())) {
			queryOrgTypeList
					.add(OrganizationTranConstant.GROUP_SUPPLIER_BANK_ONE_TO_ONE);
			queryGroupOrganization.setSupplierCode(uomGroupOrgTran
					.getTranOrgCode());

		}
		queryGroupOrganization.setQueryOrgTypeList(queryOrgTypeList);

		this.bean.getGroupOrganizationListPaging().setActivePage(0);
		this.queryGroupOrganization();
		/**
		 * 抛出组织查询事件
		 */
		Events.postEvent(OrganizationTranConstant.ON_GROUP_ORGANIZATION_QUERY,
				this, null);
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupOrganizationSelect() throws Exception {
		if (bean.getGroupOrganizationListBox().getSelectedCount() > 0) {
			groupOrganization = (GroupOrganization) bean
					.getGroupOrganizationListBox().getSelectedItem().getValue();
			/**
			 * 抛出组织选择事件
			 */
			Events.postEvent(
					OrganizationTranConstant.ON_SELECT_GROUP_ORGANIZATION,
					this, groupOrganization);
		}
	}

	/**
	 * 查询组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryGroupOrganization() throws Exception {
		if (this.queryGroupOrganization != null) {
			ListboxUtils.clearListbox(bean.getGroupOrganizationListBox());
			PageInfo pageInfo = groupOrganizationManager
					.queryPageInfoByGroupOrganization(queryGroupOrganization,
							this.bean.getGroupOrganizationListPaging()
									.getActivePage() + 1, this.bean
									.getGroupOrganizationListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getGroupOrganizationListBox().setModel(dataList);
			this.bean.getGroupOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
			groupOrganization = null;
			uomGroupOrgTran = null;
		}
	}

	/**
	 * 接受bandbox传过来组织类型列表范围
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetOrgTypeListResponse(ForwardEvent event) throws Exception {
		Map map = (Map) event.getOrigin().getData();

		queryOrgTypeList = (List<String>) map.get("orgTypeList");

		if (queryOrgTypeList.size() > 0) {

			onQueryGroupOrganization();

		}
	}

	public void onSupplierInfoResponse(ForwardEvent event) throws Exception {

		uomGroupOrgTran = (UomGroupOrgTran) event.getOrigin().getData();

		if (uomGroupOrgTran != null
				&& !StrUtil.isEmpty(uomGroupOrgTran.getTranOrgCode())
				&& OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE
						.equals(uomGroupOrgTran.getTranRelaType())) {

			onQueryGroupOrganization();

		}
	}

	/**
	 * 分页
	 * 
	 * @throws Exception
	 */
	public void onGroupOrganizationListboxExtPaging() throws Exception {
		this.queryGroupOrganization();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGroupOrganization() throws Exception {
		PubUtil.fillBeanFromPo(new GroupOrganization(), this.bean);
		bean.getTDesc2().setValue(null);
	}
}
