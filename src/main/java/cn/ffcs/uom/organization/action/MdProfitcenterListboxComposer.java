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
import cn.ffcs.uom.organization.action.bean.MdProfitcenterListboxBean;
import cn.ffcs.uom.organization.manager.MdProfitcenterManager;
import cn.ffcs.uom.organization.model.MdProfitcenter;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class MdProfitcenterListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6889132899773514943L;

	/**
	 * bean.
	 */
	private MdProfitcenterListboxBean bean = new MdProfitcenterListboxBean();

	private MdProfitcenterManager mdProfitcenterManager = (MdProfitcenterManager) ApplicationContextUtil
			.getBean("mdProfitcenterManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/mdm_profitcenter_listbox.zul";

	/**
	 * 查询MdProfitcenter.
	 */
	private MdProfitcenter qryMdProfitcenter;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public MdProfitcenterListboxComposer() {
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
		this.queryMdProfitcenter();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryMdProfitcenter() throws Exception {
		qryMdProfitcenter = new MdProfitcenter();
		String name = this.bean.getName().getValue();
		qryMdProfitcenter.setKtext(name);

		ListboxUtils.clearListbox(bean.getMdProfitcenterListbox());
		PageInfo pageInfo = mdProfitcenterManager
				.queryPageInfoByMdProfitcenter(qryMdProfitcenter, this.bean
						.getMdProfitcenterListPaging().getActivePage() + 1,
						this.bean.getMdProfitcenterListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getMdProfitcenterListbox().setModel(dataList);
		this.bean.getMdProfitcenterListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getMdProfitcenterListPaging().setActivePage(0);
		this.queryMdProfitcenter();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getName().setValue(null);
		ListboxUtils.clearListbox(this.bean.getMdProfitcenterListbox());
		this.bean.getMdProfitcenterListPaging().setTotalSize(1);
	}
}
