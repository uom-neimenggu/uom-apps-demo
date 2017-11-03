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
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.action.bean.StaffImportResultBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.ImportInfoResult;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffImportResultComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private StaffImportResultBean bean = new StaffImportResultBean();

	List<ImportInfoResult> importInfoResultList;

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
	public void onCreate$staffImportResultWindow() throws Exception {
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
			bean.getStaffImportResultWindow().setTitle("导入结果信息");
			List<Party> partyList = (List<Party>) arg.get("partyList");
			List<Party> updatePartyList = (List<Party>) arg.get("updatePartyList");
			List<Staff> delStaffList = (List<Staff>) arg.get("delStaffList");
			
			importInfoResultList = new ArrayList<ImportInfoResult>();
			
			for (Party party : partyList) {
				Staff staff = party.getStaff();
				if (staff != null) {
					ImportInfoResult ImportInfoResult = new ImportInfoResult();
					ImportInfoResult.setOptType("新增");
					ImportInfoResult.setStaffId(staff.getStaffId());
					ImportInfoResult.setStaffName(staff.getStaffName());
					ImportInfoResult.setStaffCode(staff.getStaffCode());
					ImportInfoResult.setStaffNbr(staff.getStaffNbr());
					ImportInfoResult.setStaffFixId(staff.getStaffFixId());
					ImportInfoResult.setUuid(staff.getUuid());
					if (party.getStaffOrganization() != null
							&& party.getStaffOrganization().getOrgId() != null) {
						ImportInfoResult.setOrgId(party.getStaffOrganization()
								.getOrgId());
						ImportInfoResult.setOrgName(party
								.getStaffOrganization().getOrganizationName());
					}
					importInfoResultList.add(ImportInfoResult);
				}
			}
			for (Party party : updatePartyList) {
				Staff staff = party.getStaff();
				if (staff != null) {
					ImportInfoResult ImportInfoResult = new ImportInfoResult();
					ImportInfoResult.setOptType("修改");
					ImportInfoResult.setStaffId(staff.getStaffId());
					ImportInfoResult.setStaffName(staff.getStaffName());
					ImportInfoResult.setStaffCode(staff.getStaffCode());
					ImportInfoResult.setStaffNbr(staff.getStaffNbr());
					ImportInfoResult.setStaffFixId(staff.getStaffFixId());
					ImportInfoResult.setUuid(staff.getUuid());
					if (party.getStaffOrganization() != null
							&& party.getStaffOrganization().getOrgId() != null) {
						ImportInfoResult.setOrgId(party.getStaffOrganization()
								.getOrgId());
						ImportInfoResult.setOrgName(party
								.getStaffOrganization().getOrganizationName());
					}
					importInfoResultList.add(ImportInfoResult);
				}
			}
			for (Staff staff : delStaffList) {
				ImportInfoResult ImportInfoResult = new ImportInfoResult();
				ImportInfoResult.setOptType("删除");
				ImportInfoResult.setStaffId(staff.getStaffId());
				ImportInfoResult.setStaffName(staff.getStaffName());
				ImportInfoResult.setStaffCode(staff.getStaffCode());
				ImportInfoResult.setStaffNbr(staff.getStaffNbr());
				ImportInfoResult.setStaffFixId(staff.getStaffFixId());
				ImportInfoResult.setUuid(staff.getUuid());
				if (staff.getStaffOrganization() != null
						&& staff.getStaffOrganization().getOrgId() != null) {
					ImportInfoResult.setOrgId(staff.getStaffOrganization()
							.getOrgId());
					ImportInfoResult.setOrgName(staff.getStaffOrganization()
							.getOrganizationName());
				}
				importInfoResultList.add(ImportInfoResult);
			}
		}

		ListModel dataList = new BindingListModelList(importInfoResultList,
				true);
		this.bean.getInfoListbox().setModel(dataList);
		this.bean.getInfoListbox().setSizedByContent(true);
	}
}
