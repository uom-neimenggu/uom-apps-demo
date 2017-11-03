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
import cn.ffcs.uom.organization.action.bean.WbCorpListboxBean;
import cn.ffcs.uom.organization.manager.WbCorpManager;
import cn.ffcs.uom.organization.model.HanaWbCorp;

/**
 * 人力外包公司查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class WbCorpListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5365164817935897910L;

	/**
	 * bean.
	 */
	private WbCorpListboxBean bean = new WbCorpListboxBean();

	private WbCorpManager wbCorpManager = (WbCorpManager) ApplicationContextUtil
			.getBean("wbCorpManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/wb_corp_listbox.zul";

	/**
	 * 查询WbCorp.
	 */
	private HanaWbCorp qryWbCorp;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public WbCorpListboxComposer() {
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
		this.queryWbCorp();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryWbCorp() throws Exception {
		qryWbCorp = new HanaWbCorp();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryWbCorp.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getWbCorpListbox());
		PageInfo pageInfo = wbCorpManager
				.queryPageInfoByWbCorp(qryWbCorp, this.bean
						.getWbCorpListPaging().getActivePage() + 1,
						this.bean.getWbCorpListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getWbCorpListbox().setModel(dataList);
		this.bean.getWbCorpListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getWbCorpListPaging().setActivePage(0);
		this.queryWbCorp();
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
