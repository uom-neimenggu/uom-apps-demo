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
import cn.ffcs.uom.organization.action.bean.OrgDeptListboxBean;
import cn.ffcs.uom.organization.manager.OrgDeptManager;
import cn.ffcs.uom.organization.model.HanaHiPsnorg;
import cn.ffcs.uom.organization.model.HanaOrgDept;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OrgDeptListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6336139345149041445L;

	/**
	 * bean.
	 */
	private OrgDeptListboxBean bean = new OrgDeptListboxBean();

	private OrgDeptManager orgDeptManager = (OrgDeptManager) ApplicationContextUtil
			.getBean("orgDeptManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/org_dept_listbox.zul";

	/**
	 * 查询HanaOrgDept.
	 */
	private HanaOrgDept qryOrgDept;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public OrgDeptListboxComposer() {
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
		this.queryOrgDept();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryOrgDept() throws Exception {
		qryOrgDept = new HanaOrgDept();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryOrgDept.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getOrgDeptListbox());
		PageInfo pageInfo = orgDeptManager
				.queryPageInfoByOrgDept(qryOrgDept, this.bean
						.getOrgDeptListPaging().getActivePage() + 1,
						this.bean.getOrgDeptListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgDeptListbox().setModel(dataList);
		this.bean.getOrgDeptListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getOrgDeptListPaging().setActivePage(0);
		this.queryOrgDept();
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
