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
import cn.ffcs.uom.organization.action.bean.OmPostseriesListboxBean;
import cn.ffcs.uom.organization.manager.OmPostseriesManager;
import cn.ffcs.uom.organization.model.HanaOmPostseries;

/**
 * 人力外包公司查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OmPostseriesListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6019253836729803684L;

	/**
	 * bean.
	 */
	private OmPostseriesListboxBean bean = new OmPostseriesListboxBean();

	private OmPostseriesManager omPostseriesManager = (OmPostseriesManager) ApplicationContextUtil
			.getBean("omPostseriesManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/om_postseries_listbox.zul";

	/**
	 * 查询OmPostseries.
	 */
	private HanaOmPostseries qryOmPostseries;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public OmPostseriesListboxComposer() {
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
		this.queryOmPostseries();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryOmPostseries() throws Exception {
		qryOmPostseries = new HanaOmPostseries();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryOmPostseries.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getOmPostseriesListbox());
		PageInfo pageInfo = omPostseriesManager
				.queryPageInfoByOmPostseries(qryOmPostseries, this.bean
						.getOmPostseriesListPaging().getActivePage() + 1,
						this.bean.getOmPostseriesListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOmPostseriesListbox().setModel(dataList);
		this.bean.getOmPostseriesListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getOmPostseriesListPaging().setActivePage(0);
		this.queryOmPostseries();
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
