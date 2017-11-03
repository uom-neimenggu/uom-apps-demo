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
import cn.ffcs.uom.organization.action.bean.TelSyncCostcenterListboxBean;
import cn.ffcs.uom.organization.manager.TelSyncCostcenterManager;
import cn.ffcs.uom.organization.model.HanaTelSyncCostcenter;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class TelSyncCostcenterListboxComposer extends Div implements IdSpace {
	private static final long serialVersionUID = -5092464154780595074L;

	/**
	 * bean.
	 */
	private TelSyncCostcenterListboxBean bean = new TelSyncCostcenterListboxBean();

	private TelSyncCostcenterManager telSyncCostcenterManager = (TelSyncCostcenterManager) ApplicationContextUtil
			.getBean("telSyncCostcenterManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/tel_sync_costcenter_listbox.zul";

	/**
	 * 查询HanaTelSyncCostcenter.
	 */
	private HanaTelSyncCostcenter qryTelSyncCostcenter;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public TelSyncCostcenterListboxComposer() {
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
		this.queryTelSyncCostcenter();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryTelSyncCostcenter() throws Exception {
		qryTelSyncCostcenter = new HanaTelSyncCostcenter();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryTelSyncCostcenter.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getTelSyncCostcenterListbox());
		PageInfo pageInfo = telSyncCostcenterManager
				.queryPageInfoByTelSyncCostcenter(qryTelSyncCostcenter, this.bean
						.getTelSyncCostcenterListPaging().getActivePage() + 1,
						this.bean.getTelSyncCostcenterListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getTelSyncCostcenterListbox().setModel(dataList);
		this.bean.getTelSyncCostcenterListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getTelSyncCostcenterListPaging().setActivePage(0);
		this.queryTelSyncCostcenter();
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
