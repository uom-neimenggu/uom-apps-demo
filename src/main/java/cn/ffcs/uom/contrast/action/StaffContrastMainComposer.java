package cn.ffcs.uom.contrast.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.contrast.action.bean.StaffContrastMainBean;
import cn.ffcs.uom.contrast.manager.ContrastManager;
import cn.ffcs.uom.contrast.model.StaffContrast;

/**
 * 日志操作查询.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings( { "unused" })
public class StaffContrastMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffContrastMainBean bean = new StaffContrastMainBean();

	/**
	 * 查询的员工对照记录
	 */
	private StaffContrast queryStaffContrast;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("contrastManager")
	private ContrastManager contrastManager;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$staffContrastMainWin() throws Exception {
		onStaffContrastQueryRequest();
	}

	/**
	 * 组织树列表查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onStaffContrastQueryRequest() throws Exception {
		queryStaffContrast = new StaffContrast();
		this.bean.getStaffContrastListboxPaging().setActivePage(0);

		queryStaffContrast.setStaffName(this.bean.getStaffName().getValue());
		queryStaffContrast.setStaffOaAccount(this.bean.getOldStaffAccount()
				.getValue());
		queryStaffContrast.setStaffOssAccount(this.bean.getOssAccount()
				.getValue());

		this.onStaffContrastListboxPaging();
	}

	/**
	 * 重置
	 */
	public void onStaffContrastReset() {
		this.bean.getStaffName().setValue(null);
		this.bean.getOldStaffAccount().setValue(null);
		this.bean.getOssAccount().setValue(null);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onStaffContrastListboxPaging() throws Exception {
		PageInfo pageInfo = this.contrastManager.queryPageInfoByStaffContrast(
				queryStaffContrast, this.bean.getStaffContrastListboxPaging()
						.getActivePage() + 1, this.bean
						.getStaffContrastListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffContrastListbox().setModel(dataList);
		this.bean.getStaffContrastListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

}
