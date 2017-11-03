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
import cn.ffcs.uom.organization.action.bean.MdmContractListboxBean;
import cn.ffcs.uom.organization.manager.MdmContractManager;
import cn.ffcs.uom.organization.model.MdmContract;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class MdmContractListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2716662450697180923L;

	/**
	 * bean.
	 */
	private MdmContractListboxBean bean = new MdmContractListboxBean();

	private MdmContractManager mdmContractManager = (MdmContractManager) ApplicationContextUtil
			.getBean("mdmContractManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/mdm_contract_listbox.zul";

	/**
	 * 查询MdmContract.
	 */
	private MdmContract qryMdmContract;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public MdmContractListboxComposer() {
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
		this.queryMdmContract();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryMdmContract() throws Exception {
		qryMdmContract = new MdmContract();
		String name = this.bean.getName().getValue();
		qryMdmContract.setContractname(name);

		ListboxUtils.clearListbox(bean.getMdmContractListbox());
		PageInfo pageInfo = mdmContractManager.queryPageInfoByMdmContract(
				qryMdmContract, this.bean.getMdmContractListPaging()
						.getActivePage() + 1, this.bean
						.getMdmContractListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getMdmContractListbox().setModel(dataList);
		this.bean.getMdmContractListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getMdmContractListPaging().setActivePage(0);
		this.queryMdmContract();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		this.bean.getName().setValue(null);
		ListboxUtils.clearListbox(this.bean.getMdmContractListbox());
		this.bean.getMdmContractListPaging().setTotalSize(1);
	}
}
