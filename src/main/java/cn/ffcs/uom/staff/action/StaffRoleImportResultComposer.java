package cn.ffcs.uom.staff.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.action.bean.StaffRoleImportResultBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.ImportInfoResult;
import cn.ffcs.uom.staff.model.StaffRoleImportInfoResult;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-01
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffRoleImportResultComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private StaffRoleImportResultBean bean = new StaffRoleImportResultBean();

	private List<StaffRoleImportInfoResult> staffRoleimportInfoResultList;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffRoleImportResultWindow() throws Exception {
		bindBean();
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void bindBean() throws Exception {
		String opType = StrUtil.strnull(arg.get("opType"));
		if (SffOrPtyCtants.VIEW.equals(opType)) {
			bean.getStaffRoleImportResultWindow().setTitle("导入结果信息");
			List<StaffRoleRela> staffRoleRelaList = (List<StaffRoleRela>) arg
					.get("staffRoleRelaList");
			List<StaffRoleRela> showDelStaffRoleRelaList = (List<StaffRoleRela>) arg
					.get("showDelStaffRoleRelaList");
			staffRoleimportInfoResultList = new ArrayList<StaffRoleImportInfoResult>();
			for (StaffRoleRela staffRoleRela : staffRoleRelaList) {
				StaffRoleImportInfoResult staffRoleImportInfoResult = new StaffRoleImportInfoResult();
				
				staffRoleImportInfoResult.setOptType("新增");
				
				staffRoleImportInfoResult.setStaffName(staffRoleRela
						.getStaffName());
				staffRoleImportInfoResult.setStaffAccount(staffRoleRela
						.getStaffRoleStaffAccount());
				staffRoleImportInfoResult.setStaffCode(staffRoleRela
						.getStaffCode());
				staffRoleImportInfoResult.setStaffRoleName(staffRoleRela
						.getStaffRoleName());
				staffRoleImportInfoResult.setStaffAttrValue(staffRoleRela
						.getStaffAttrValue());

				staffRoleimportInfoResultList.add(staffRoleImportInfoResult);
			}
			
			for (StaffRoleRela staffRoleRela : showDelStaffRoleRelaList) {
				StaffRoleImportInfoResult staffRoleImportInfoResult = new StaffRoleImportInfoResult();
				
				staffRoleImportInfoResult.setOptType("删除");

				staffRoleImportInfoResult.setStaffName(staffRoleRela
						.getStaffName());
				staffRoleImportInfoResult.setStaffAccount(staffRoleRela
						.getStaffRoleStaffAccount());
				staffRoleImportInfoResult.setStaffCode(staffRoleRela
						.getStaffCode());
				staffRoleImportInfoResult.setStaffRoleName(staffRoleRela
						.getStaffRoleName());
				staffRoleImportInfoResult.setStaffAttrValue(staffRoleRela
						.getStaffAttrValue());

				staffRoleimportInfoResultList.add(staffRoleImportInfoResult);
			}
			ListModel dataList = new BindingListModelList(
					staffRoleimportInfoResultList, true);
			this.bean.getInfoListbox().setModel(dataList);
			this.bean.getInfoListbox().setSizedByContent(true);
		}
	}

}
