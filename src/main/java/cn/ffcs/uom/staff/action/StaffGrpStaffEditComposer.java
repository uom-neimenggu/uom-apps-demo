package cn.ffcs.uom.staff.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;
import cn.ffcs.uom.staff.action.bean.StaffGrpStaffEditBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffExtendAttrManager;
import cn.ffcs.uom.staff.manager.StaffGrpStaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staff.model.StaffGrpStaff;

@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffGrpStaffEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507005075323523801L;
	/**
	 * 页面bean
	 */
	private StaffGrpStaffEditBean bean = new StaffGrpStaffEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 添加的员工
	 */
	private Staff staff;

	private StaffGrpStaffManager staffGrpStaffManager = (StaffGrpStaffManager) ApplicationContextUtil
			.getBean("staffGrpStaffManager");

	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");

	private StaffExtendAttrManager staffExtendAttrManager = (StaffExtendAttrManager) ApplicationContextUtil
			.getBean("staffExtendAttrManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onCreate$staffGrpStaffEditWindow() throws Exception {
		this.bean.getGrpStaffBandboxExt().setReadonly(true);
		this.bindBean();
	}

	/**
	 * bindBean
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getStaffGrpStaffEditWindow().setTitle("新增员工业务关系");
			staff = (Staff) arg.get("staff");
			if (staff != null) {
				bean.getStaffBandboxExt().setValue(staff.getStaffName());
				bean.getStaffBandboxExt().setDisabled(true);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {

			Staff newStaff = null;

			if (null == staff) {
				newStaff = bean.getStaffBandboxExt().getStaff();
			} else {
				newStaff = staff;
			}

			if (StrUtil.isNullOrEmpty(newStaff)
					|| newStaff.getStaffId() == null) {
				ZkUtil.showError("请选择员工", "提示信息");
				return;
			}

			GrpStaff grpStaff = this.bean.getGrpStaffBandboxExt().getGrpStaff();
			if (StrUtil.isNullOrEmpty(grpStaff)
					|| StrUtil.isEmpty(grpStaff.getSalesCode())) {
				ZkUtil.showError("集团销售员编码不存在,请重新选择集团销售员", "提示信息");
				return;
			}

			StaffGrpStaff staffGrpStaff = new StaffGrpStaff();
			staffGrpStaff.setStaffId(newStaff.getStaffId());
			/**
			 * 是否已存在
			 */
			StaffGrpStaff sgs = staffGrpStaffManager
					.queryStaffGrpStaff(staffGrpStaff);
			if (sgs != null) {
				ZkUtil.showError("该员工已存在与集团渠道账号对应关系", "提示信息");
				return;
			}

			StaffExtendAttr staffExtendAttr = new StaffExtendAttr();
			staffExtendAttr.setStaffId(newStaff.getStaffId());
			staffExtendAttr
					.setStaffAttrSpecId(SffOrPtyCtants.STAFF_ATTR_SPEC_ID_3);

			StaffExtendAttr sea = staffExtendAttrManager
					.queryStaffExtendAttr(staffExtendAttr);

			if (sea != null && sea.getStaffExtendAttrId() != null) {
				sea.setStaffAttrValue(grpStaff.getSalesCode());
				sea.update();
			} else {
				staffExtendAttr.setStaffAttrValue(grpStaff.getSalesCode());
				staffExtendAttr.add();
			}

			GrpStaffChannelRela grpStaffChannelRela = new GrpStaffChannelRela();
			grpStaffChannelRela.setSalesCode(grpStaff.getSalesCode());
			List<GrpStaffChannelRela> grpStaffChannelRelaList = channelInfoManager
					.queryGrpStaffChannelRelaList(grpStaffChannelRela);

			if (grpStaffChannelRelaList != null
					&& grpStaffChannelRelaList.size() > 0) {

				OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();

				organizationExtendAttr
						.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
				organizationExtendAttr.setOrgAttrValue(grpStaffChannelRelaList
						.get(0).getChannelNbr());

				organizationExtendAttr = organizationExtendAttrManager
						.queryOrganizationExtendAttr(organizationExtendAttr);

				if (organizationExtendAttr != null
						&& organizationExtendAttr.getOrgId() != null) {

					Organization sourceOrganization = null;
					StaffOrganization sourceStaffOrganization = null;
					Organization targetOrganization = new Organization();
					targetOrganization.setOrgId(organizationExtendAttr
							.getOrgId());

					// 查询该员工是否有其他员工组织关系
					StaffOrganization vo = new StaffOrganization();
					vo.setStaffId(newStaff.getStaffId());
					List<StaffOrganization> existList = staffOrganizationManager
							.queryStaffOrganizationList(vo);

					if (existList != null && existList.size() > 0) {
						for (StaffOrganization staffOrg : existList) {
							Organization organization = new Organization();
							organization.setOrgId(staffOrg.getOrgId());
							List<OrgType> orgTypeList = null;
							orgTypeList = organization.getOrgTypeList();

							// 判断该组织是否是网点
							if (orgTypeList != null && orgTypeList.size() > 0) {
								for (OrgType orgType : orgTypeList) {
									if (orgType
											.getOrgTypeCd()
											.equals(OrganizationConstant.ORG_TYPE_N0202010000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202030000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202040000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202020000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202050000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202060000)) {
										sourceOrganization = organization;
										sourceStaffOrganization = staffOrg;
										break;
									}
								}
							}
						}
					}

					// 添加员工组织关系规则校验 zhulintao
					if (true) {

						String msg = staffOrganizationManager
								.doStaffOrgRelRule(newStaff,
										sourceOrganization, targetOrganization);

						if (!StrUtil.isEmpty(msg)) {
							ZkUtil.showError(msg + "请修改后再进行关联", "提示信息");
							return;
						} else {

							StaffOrganization staffOrganization = new StaffOrganization();
							staffOrganization.setStaffId(newStaff.getStaffId());
							staffOrganization.setOrgId(organizationExtendAttr
									.getOrgId());
							if (sourceStaffOrganization != null
									&& !StrUtil.isEmpty(sourceStaffOrganization
											.getRalaCd())) {// 存在网点

								if (!sourceStaffOrganization.getOrgId().equals(
										staffOrganization.getOrgId())) {// 目标组织和原组织不同时，则直接用原组织的员工组织关系类型
									staffOrganization
											.setRalaCd(sourceStaffOrganization
													.getRalaCd());
									sourceStaffOrganization.remove();
									staffOrganization.add();
								}

							} else {// 不存在网点
								boolean isRalaCd = false;
								for (StaffOrganization staffOrg : existList) {
									if (staffOrg.getRalaCd().equals(
											BaseUnitConstants.RALA_CD_1)) {
										isRalaCd = true;
										break;
									}
								}
								if (isRalaCd) {// 原员工组织关系中存在主归属的，将新的员工组织关系设置成兼职类型
									staffOrganization
											.setRalaCd(BaseUnitConstants.RALA_CD_3);
								} else {// 原员工组织关系中不存在主归属的，将新的员工组织关系设置成主归属类型
									staffOrganization
											.setRalaCd(BaseUnitConstants.RALA_CD_1);
								}
								staffOrganization.add();
							}
						}

					}
				}

			}

			staffGrpStaff.setSalesCode(grpStaff.getSalesCode());
			staffGrpStaffManager.addStaffGrpStaff(staffGrpStaff);
			Events.postEvent("onOK", this.self, staffGrpStaff);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getStaffGrpStaffEditWindow().onClose();
	}
}
