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
import cn.ffcs.uom.organization.action.bean.TelDeptCostConListboxBean;
import cn.ffcs.uom.organization.manager.TelDeptCostConManager;
import cn.ffcs.uom.organization.model.HanaTelDeptCostCon;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class TelDeptCostConListboxComposer extends Div implements IdSpace {
	private static final long serialVersionUID = 768200555535937538L;

	/**
	 * bean.
	 */
	private TelDeptCostConListboxBean bean = new TelDeptCostConListboxBean();

	private TelDeptCostConManager telDeptCostConManager = (TelDeptCostConManager) ApplicationContextUtil
			.getBean("telDeptCostConManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/tel_dept_cost_con_listbox.zul";

	/**
	 * 查询HanaTelDeptCostCon.
	 */
	private HanaTelDeptCostCon qryTelDeptCostCon;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public TelDeptCostConListboxComposer() {
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
		this.queryTelDeptCostCon();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryTelDeptCostCon() throws Exception {
		qryTelDeptCostCon = new HanaTelDeptCostCon();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryTelDeptCostCon.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getTelDeptCostConListbox());
		PageInfo pageInfo = telDeptCostConManager
				.queryPageInfoByTelDeptCostCon(qryTelDeptCostCon, this.bean
						.getTelDeptCostConListPaging().getActivePage() + 1,
						this.bean.getTelDeptCostConListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getTelDeptCostConListbox().setModel(dataList);
		this.bean.getTelDeptCostConListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getTelDeptCostConListPaging().setActivePage(0);
		this.queryTelDeptCostCon();
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
