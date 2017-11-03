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
import cn.ffcs.uom.organization.action.bean.MdmSupplierCreateListboxBean;
import cn.ffcs.uom.organization.manager.MdmSupplierCreateManager;
import cn.ffcs.uom.organization.model.MdmSupplierCreate;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class MdmSupplierCreateListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2954101592575904979L;

	/**
	 * bean.
	 */
	private MdmSupplierCreateListboxBean bean = new MdmSupplierCreateListboxBean();

	private MdmSupplierCreateManager mdmSupplierCreateManager = (MdmSupplierCreateManager) ApplicationContextUtil
			.getBean("mdmSupplierCreateManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/mdm_supplier_create_listbox.zul";

	/**
	 * 查询MdmSupplierCreate.
	 */
	private MdmSupplierCreate qryMdmSupplierCreate;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public MdmSupplierCreateListboxComposer() {
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
		this.queryMdmSupplierCreate();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryMdmSupplierCreate() throws Exception {
		qryMdmSupplierCreate = new MdmSupplierCreate();
		String name = this.bean.getName().getValue();
		qryMdmSupplierCreate.setName1(name);

		ListboxUtils.clearListbox(bean.getMdmSupplierCreateListbox());
		PageInfo pageInfo = mdmSupplierCreateManager.queryPageInfoByMdmSupplierCreate(
				qryMdmSupplierCreate, this.bean.getMdmSupplierCreateListPaging()
						.getActivePage() + 1, this.bean
						.getMdmSupplierCreateListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getMdmSupplierCreateListbox().setModel(dataList);
		this.bean.getMdmSupplierCreateListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getMdmSupplierCreateListPaging().setActivePage(0);
		this.queryMdmSupplierCreate();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getName().setValue(null);
		ListboxUtils.clearListbox(this.bean.getMdmSupplierCreateListbox());
		this.bean.getMdmSupplierCreateListPaging().setTotalSize(1);
	}
}
