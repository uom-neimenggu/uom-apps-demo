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
import cn.ffcs.uom.contrast.action.bean.StaffContrastCrmMainBean;
import cn.ffcs.uom.contrast.manager.ContrastManager;
import cn.ffcs.uom.contrast.model.StaffContrastCrm;

/**
 * 日志操作查询.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller	//控制层组件扫描
@Scope("prototype") //zk controller 注解配置，注意scope必须是prototype，因为composer是一个状态对象
//@SuppressWarnings({ "unused" })
public class StaffContrastCrmMainComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffContrastCrmMainBean bean = new StaffContrastCrmMainBean();
	
	/**
	 * 查询CRM员工的对照记录
	 */
	private StaffContrastCrm queryStaffContrastCrm;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("contrastManager")  //业务层注入注解
	private ContrastManager contrastManager;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$staffContrastCrmMainWin() throws Exception {
		onStaffContrastCrmQueryRequest();
	}

	/**
	 * 查询--
	 * 分页查询员工对照表信息
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onStaffContrastCrmQueryRequest() throws Exception {
		queryStaffContrastCrm = new StaffContrastCrm();
		this.bean.getStaffContrastCrmListboxPaging().setActivePage(0);
	
		queryStaffContrastCrm.setEmpeeAcct(this.bean.getEmpeeAcct().getValue());//获取前端输入的-CRM原帐号
		queryStaffContrastCrm.setCertName(this.bean.getCertName().getValue());//获取前端输入的-员工姓名

		this.onStaffContrastCrmListboxPaging();//分页查询员工对照表信息
	}

	/**
	 * 重置
	 */
	public void onStaffContrastCrmReset() {
		this.bean.getEmpeeAcct().setValue(null);
		this.bean.getCertName().setValue(null);
	}

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @throws Exception
	 */
	public void onStaffContrastCrmListboxPaging() throws Exception {
		PageInfo pageInfo = this.contrastManager
				.queryPageInfoByStaffContrastCrm(queryStaffContrastCrm,
						this.bean.getStaffContrastCrmListboxPaging()
								.getActivePage() + 1, this.bean
								.getStaffContrastCrmListboxPaging()
								.getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffContrastCrmListbox().setModel(dataList);
		this.bean.getStaffContrastCrmListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

}








