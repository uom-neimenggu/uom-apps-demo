package cn.ffcs.uom.structure.component;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.dataPermission.vo.RoleAdapter;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.structure.action.bean.PoliticalStructureExtBean;
import cn.ffcs.uom.structure.manager.OrgStructureManager;
import cn.ffcs.uom.structure.model.OrgStructure;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 组织结构查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-01-10
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class PoliticalStructureExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private PoliticalStructureExtBean bean = new PoliticalStructureExtBean();

	/**
	 * 组织结构查询
	 */
	private OrgStructure queryOrgStructure;

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * zul.
	 */
	private final String zul = "/pages/structure/comp/political_structure_ext.zul";

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("orgStructureManager")
	private OrgStructureManager orgStructureManager = (OrgStructureManager) ApplicationContextUtil
			.getBean("orgStructureManager");

	public PoliticalStructureExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward("onQueryOrgStructureRequest", this,
				"onQueryOrgStructureResponse");
	}

	public void onCreate() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
				permissionTelcomRegion.setRegionName("中国");
				bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
			} else {

				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
				bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
			}
		}
		// 去除默认查询onQueryOrgStructure();
	}

	public void onQueryOrgStructureResponse(ForwardEvent event)
			throws Exception {
		onQueryOrgStructure();
	}

	/**
	 * 组织结构查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQueryOrgStructure() throws Exception {
		queryOrgStructure = new OrgStructure();
		this.bean.getOrgStructureListboxPaging().setActivePage(0);
		if (!StrUtil.isEmpty(this.bean.getOrgId().getValue())) {
			queryOrgStructure.setOrgId(Long.parseLong(this.bean.getOrgId()
					.getValue()));
		}
		queryOrgStructure.setOrgCode(this.bean.getOrgCode().getValue());
		queryOrgStructure.setOrgName(this.bean.getOrgName().getValue());
		queryOrgStructure.setOrgUuId(this.bean.getOrgUuId().getValue());
		queryOrgStructure.setQueryTelcomRegion(this.bean.getTelcomRegion()
				.getTelcomRegion());
		queryOrgStructure.setQueryIncludeChildren(this.bean
				.getIncludeChildren().isChecked());
		queryOrgStructure.setQueryOrganization(this.bean.getOrg()
				.getOrganization());

		this.onOrgStructureListboxPaging();
	}

	/**
	 * 重置
	 */
	public void onOrgStructureReset() {
		this.bean.getOrgCode().setValue(null);
		this.bean.getOrgName().setValue(null);
		this.bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
		this.bean.getIncludeChildren().setChecked(true);
		this.bean.getOrg().setOrganization(null);
	}

	public void onPageSizeSelect() throws Exception {
		if (this.bean.getPageListbox() != null
				&& this.bean.getPageListbox().getSelectedItem().getValue() != null) {
			this.bean.getOrgStructureListboxPaging().setActivePage(0);
			this.bean.getOrgStructureListboxPaging().setPageSize(
					Integer.parseInt(this.bean.getPageListbox()
							.getSelectedItem().getValue().toString()));
		}
		onOrgStructureListboxPaging();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onOrgStructureListboxPaging() throws Exception {

		PageInfo pageInfo = this.orgStructureManager
				.queryPageInfoByOrgStructure(queryOrgStructure, this.bean
						.getOrgStructureListboxPaging().getActivePage() + 1,
						this.bean.getOrgStructureListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgStructureListbox().setModel(dataList);
		this.bean.getOrgStructureListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		if (PlatformUtil.isAdmin()) {

		} else if ("orgStructurePage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING)) {

			}
		}
	}

}
