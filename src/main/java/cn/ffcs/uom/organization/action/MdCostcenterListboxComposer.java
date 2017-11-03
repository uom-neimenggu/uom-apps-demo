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
import cn.ffcs.uom.organization.action.bean.MdCostcenterListboxBean;
import cn.ffcs.uom.organization.manager.MdCostcenterManager;
import cn.ffcs.uom.organization.model.MdCostcenter;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class MdCostcenterListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6336139345149041445L;

	/**
	 * bean.
	 */
	private MdCostcenterListboxBean bean = new MdCostcenterListboxBean();

	private MdCostcenterManager mdCostcenterManager = (MdCostcenterManager) ApplicationContextUtil
			.getBean("mdCostcenterManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/mdm_costcenter_listbox.zul";

	/**
	 * 查询MdCostcenter.
	 */
	private MdCostcenter qryMdCostcenter;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public MdCostcenterListboxComposer() {
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
		this.queryMdCostcenter();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryMdCostcenter() throws Exception {
		qryMdCostcenter = new MdCostcenter();
		String name = this.bean.getName().getValue();
		qryMdCostcenter.setKtext(name);

		ListboxUtils.clearListbox(bean.getMdCostcenterListbox());
		PageInfo pageInfo = mdCostcenterManager.queryPageInfoByMdCostcenter(
				qryMdCostcenter, this.bean.getMdCostcenterListPaging()
						.getActivePage() + 1, this.bean
						.getMdCostcenterListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getMdCostcenterListbox().setModel(dataList);
		this.bean.getMdCostcenterListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getMdCostcenterListPaging().setActivePage(0);
		this.queryMdCostcenter();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getName().setValue(null);
		ListboxUtils.clearListbox(this.bean.getMdCostcenterListbox());
		this.bean.getMdCostcenterListPaging().setTotalSize(1);
	}
}
