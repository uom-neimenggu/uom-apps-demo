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
import cn.ffcs.uom.organization.action.bean.WbPsnMsgListboxBean;
import cn.ffcs.uom.organization.manager.WbPsnMsgManager;
import cn.ffcs.uom.organization.model.HanaWbPsnMsg;

/**
 * 人力外包公司查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class WbPsnMsgListboxComposer extends Div implements IdSpace {
	private static final long serialVersionUID = -8060515943090100846L;

	/**
	 * bean.
	 */
	private WbPsnMsgListboxBean bean = new WbPsnMsgListboxBean();

	private WbPsnMsgManager wbPsnMsgManager = (WbPsnMsgManager) ApplicationContextUtil
			.getBean("wbPsnMsgManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/wb_psn_msg_listbox.zul";

	/**
	 * 查询WbPsnMsg.
	 */
	private HanaWbPsnMsg qryWbPsnMsg;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public WbPsnMsgListboxComposer() {
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
		this.queryWbPsnMsg();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryWbPsnMsg() throws Exception {
		qryWbPsnMsg = new HanaWbPsnMsg();
		String monthId = new SimpleDateFormat("yyyyMM").format(this.bean.getDate().getValue());
		qryWbPsnMsg.setMonthId(Integer.parseInt(monthId));

		ListboxUtils.clearListbox(bean.getWbPsnMsgListbox());
		PageInfo pageInfo = wbPsnMsgManager
				.queryPageInfoByWbPsnMsg(qryWbPsnMsg, this.bean
						.getWbPsnMsgListPaging().getActivePage() + 1,
						this.bean.getWbPsnMsgListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getWbPsnMsgListbox().setModel(dataList);
		this.bean.getWbPsnMsgListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getWbPsnMsgListPaging().setActivePage(0);
		this.queryWbPsnMsg();
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
