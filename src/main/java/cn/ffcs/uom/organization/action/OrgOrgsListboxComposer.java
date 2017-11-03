package cn.ffcs.uom.organization.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.action.bean.OrgOrgsListboxBean;
import cn.ffcs.uom.organization.manager.OrgOrgsManager;
import cn.ffcs.uom.organization.model.HanaOrgOrgs;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OrgOrgsListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6336139345149041445L;

	/**
	 * bean.
	 */
	private OrgOrgsListboxBean bean = new OrgOrgsListboxBean();

	private OrgOrgsManager orgOrgsManager = (OrgOrgsManager) ApplicationContextUtil
			.getBean("orgOrgsManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/org_orgs_listbox.zul";

	/**
	 * 查询HanaOrgOrgs.
	 */
	private HanaOrgOrgs qryOrgOrgs;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public OrgOrgsListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}
	
	public void onCreate() throws Exception {
		this.bean.getDate().setValue(
				new java.sql.Date(new Date().getTime()));
	}
	
	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onListboxPaging() throws Exception {
		this.queryOrgOrgs();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryOrgOrgs() throws Exception {
		qryOrgOrgs = new HanaOrgOrgs();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryOrgOrgs.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getOrgOrgsListbox());
		PageInfo pageInfo = orgOrgsManager
				.queryPageInfoByOrgOrgs(qryOrgOrgs, this.bean
						.getOrgOrgsListPaging().getActivePage() + 1,
						this.bean.getOrgOrgsListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgOrgsListbox().setModel(dataList);
		this.bean.getOrgOrgsListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getOrgOrgsListPaging().setActivePage(0);
		this.queryOrgOrgs();
	}
	
	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getDate().setValue(
				new java.sql.Date(new Date().getTime()));
	}
}
