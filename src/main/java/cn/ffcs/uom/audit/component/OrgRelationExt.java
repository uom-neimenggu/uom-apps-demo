package cn.ffcs.uom.audit.component;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.audit.action.bean.OrgRelationExtBean;
import cn.ffcs.uom.audit.manager.OrgRelationManager;
import cn.ffcs.uom.audit.model.OrgRelation;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 组织关系稽核查询 .
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
public class OrgRelationExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private OrgRelationExtBean bean = new OrgRelationExtBean();

	/**
	 * 组织关系稽核查询
	 */
	private OrgRelation queryOrgRelation;

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
	private final String zul = "/pages/audit/comp/org_relation_ext.zul";

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("orgRelationManager")
	private OrgRelationManager orgRelationManager = (OrgRelationManager) ApplicationContextUtil
			.getBean("orgRelationManager");

	public OrgRelationExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward("onQueryOrgRelationRequest", this,
				"onQueryOrgRelationResponse");
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
		// 去除默认查询onQueryOrgRelation();
	}

	public void onQueryOrgRelationResponse(ForwardEvent event) throws Exception {
		onQueryOrgRelation();
	}

	/**
	 * 组织关系稽核查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQueryOrgRelation() throws Exception {
		queryOrgRelation = new OrgRelation();
		this.bean.getOrgRelationListboxPaging().setActivePage(0);

		queryOrgRelation.setOrgCode(this.bean.getOrgCode().getValue());
		queryOrgRelation.setOrgName(this.bean.getOrgName().getValue());
		queryOrgRelation.setQueryTelcomRegion(this.bean.getTelcomRegion()
				.getTelcomRegion());
		queryOrgRelation.setQueryIncludeChildren(this.bean.getIncludeChildren()
				.isChecked());

		this.onOrgRelationListboxPaging();
	}

	/**
	 * 重置
	 */
	public void onOrgRelationReset() {
		this.bean.getOrgCode().setValue(null);
		this.bean.getOrgName().setValue(null);
		this.bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
		this.bean.getIncludeChildren().setChecked(true);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onOrgRelationListboxPaging() throws Exception {

		PageInfo pageInfo = this.orgRelationManager.queryPageInfoByOrgRelation(
				queryOrgRelation, this.bean.getOrgRelationListboxPaging()
						.getActivePage() + 1, this.bean
						.getOrgRelationListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgRelationListbox().setModel(dataList);
		this.bean.getOrgRelationListboxPaging().setTotalSize(
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

		} else if ("orgRelationPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING)) {

			}
		}
	}

}
