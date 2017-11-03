package cn.ffcs.uom.organization.action;

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
import cn.ffcs.uom.organization.action.bean.MdmProfitcenterListboxBean;
import cn.ffcs.uom.organization.manager.MdmProfitcenterManager;
import cn.ffcs.uom.organization.model.MdmProfitcenter;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class MdmProfitcenterListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6889132899773514943L;

	/**
	 * bean.
	 */
	private MdmProfitcenterListboxBean bean = new MdmProfitcenterListboxBean();

	private MdmProfitcenterManager mdmProfitcenterManager = (MdmProfitcenterManager) ApplicationContextUtil
			.getBean("mdmProfitcenterManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/mdm_profitcenter_listbox.zul";

	/**
	 * 查询MdmProfitcenter.
	 */
	private MdmProfitcenter qryMdmProfitcenter;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public MdmProfitcenterListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onListboxPaging() throws Exception {
		this.queryMdmProfitcenter();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryMdmProfitcenter() throws Exception {
		qryMdmProfitcenter = new MdmProfitcenter();
		String name = this.bean.getName().getValue();
		qryMdmProfitcenter.setKtext(name);

		ListboxUtils.clearListbox(bean.getMdmProfitcenterListbox());
		PageInfo pageInfo = mdmProfitcenterManager.queryPageInfoByMdmProfitcenter(
				qryMdmProfitcenter, this.bean.getMdmProfitcenterListPaging()
						.getActivePage() + 1, this.bean
						.getMdmProfitcenterListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getMdmProfitcenterListbox().setModel(dataList);
		this.bean.getMdmProfitcenterListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getMdmProfitcenterListPaging().setActivePage(0);
		this.queryMdmProfitcenter();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getName().setValue(null);
		ListboxUtils.clearListbox(this.bean.getMdmProfitcenterListbox());
		this.bean.getMdmProfitcenterListPaging().setTotalSize(1);
	}
}
