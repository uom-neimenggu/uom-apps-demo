package cn.ffcs.uom.organization.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.organization.action.bean.OrganizationEditBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;

/**
 * 组织编辑Composer.
 * 
 * @author OUZHF
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OrganizationEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrganizationEditBean bean = new OrganizationEditBean();

	/**
	 * 操作类型.
	 */
	private String opType;

	/**
	 * 修改时传的组织
	 */
	private Organization oldOrganization;

	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * Manager.
	 */
	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");
	
	/**
     * 日志服务队列
     */
    private LogService logService = (LogService) ApplicationContextUtil.getBean("logService");

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
	public void onCreate$organizationEditWindow() throws Exception {
		this.bindBean();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("mod".equals(opType)) {
			oldOrganization = (Organization) arg.get("oldOrganization");
			this.bean.getOrganizationEditWindow().setTitle("组织修改");
		} else if ("add".equals(opType)) {
			this.bean.getOrganizationEditWindow().setTitle("组织新增");
		} else if ("show".equals(opType)) {
			this.bean.getOrganizationEditWindow().setTitle("组织信息查看");
			oldOrganization = (Organization) arg.get("oldOrganization");
			this.bean.getSaveOrg().setVisible(false);
			this.bean.getCancelOrg().setVisible(false);
		}
		this.bean.getOrganizationInfoExt().setOpType(opType);
		this.bean.getOrganizationInfoExt().setOrganization(oldOrganization);
		this.bean.getOrganizationInfoExt().onChooseOrgTypeCd();
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		String msg = this.bean.getOrganizationInfoExt()
				.getDoValidOrganizationInfo();
		boolean marketingUnit = this.bean.getOrganizationInfoExt().marketingUnit;
		boolean marketingListbox = this.bean.getOrganizationInfoExt().marketingListbox;
		boolean newMarketingListbox = this.bean.getOrganizationInfoExt().newMarketingListbox;
		boolean newSeventeenMarketingListbox = this.bean.getOrganizationInfoExt().newSeventeenMarketingListbox;
		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		Organization organization = this.bean.getOrganizationInfoExt()
				.getOrganization();
		if (organization.getIsChangeOrgName() && !marketingUnit) {
			ZkUtil.showInformation("组织名称修改，该节点下的组织全称会全部修改请耐心等待...", "提示信息");
		}
		SysLog log = new SysLog();
		if ("add".equals(opType)) {
		    /**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
		    log.startLog(new Date(), SysLogConstrants.ORG);
			organizationManager.addOrganization(organization);
			
			/**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
			Class clazz[] = {Organization.class};
		    log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "组织新增记录日志");
		} else if ("mod".equals(opType)) {
		    /**
             * 开始日志添加操作
             * 添加日志到队列需要：
             * 业务开始时间，日志消息类型，错误编码和描述
             */
		    log.startLog(new Date(), SysLogConstrants.ORG);
            //获取当前操作用户
//            log.setUser(PlatformUtil.getCurrentUser());

			if (marketingListbox || newMarketingListbox || newSeventeenMarketingListbox) {// 生成EDA_CODE

				// 获取本级组织的层级
				int orgLevel = 0;

				if (marketingListbox) {
					orgLevel = organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
				} else if (newMarketingListbox) {
					orgLevel = organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
                } else if (newSeventeenMarketingListbox) {
                    orgLevel = organization
                        .getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                }

				if (orgLevel > 0) {

					boolean isGrid = organization.isGrid(organization
							.getAddOrgTypeList());

					if (!isGrid) {

						List<OrgType> orgTypeList = organization
								.getOrgTypeList();

						orgTypeList.removeAll(organization.getDelOrgTypeList());// 删除界面已经去除的组织类型

						isGrid = organization.isGrid(orgTypeList);

					}

					if (StrUtil.isEmpty(organization.getEdaCode())) {
						organization.setEdaCode(organization.generateEdaCode(
								orgLevel, isGrid));
					} else {
						if (!isGrid) {
							if (!(organization.getEdaCode().substring(0, 1)
									.equals(orgLevel + ""))) {
								organization.setEdaCode(organization
										.generateEdaCode(orgLevel, isGrid));
							} else if (!(organization.getTelcomRegion()
									.getAreaCode().equals(oldOrganization
									.getTelcomRegion().getAreaCode()))) {
								organization.setEdaCode(organization
										.getEdaCode().replaceFirst(
												oldOrganization
														.getTelcomRegion()
														.getAreaCode(),
												organization.getTelcomRegion()
														.getAreaCode()));
							}
						}
					}

					// 调用全息网格接口进行校验
					if (isGrid && orgLevel == 7) {

						msg = organizationManager
								.getDoValidOrganizationExtendAttrGrid(organization);

						if (!StrUtil.isEmpty(msg)) {
							ZkUtil.showError(msg, "提示信息");
							return;
						}

						boolean gridInterFaceSwitch = UomClassProvider
								.isOpenSwitch("gridInterFaceSwitch");// 校验开关

						// if (gridInterFaceSwitch && marketingListbox)
						// {//只有营销2015调用接口
						if (gridInterFaceSwitch) {// 营销2015和营销2016都调用接口
							msg = organizationManager.getGridValid(
									organization, oldOrganization, null,
									OrganizationConstant.OPT_TYPE_3);
							if (!StrUtil.isEmpty(msg)) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}
						}
					}

				}
			}

			OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
			organizationExtendAttr.setOrgId(organization.getOrgId());
			organizationExtendAttr
					.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

			organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(organizationExtendAttr);

			List<Organization> list = organization
					.getChildrenOrganization(OrganizationConstant.RELA_CD_INNER);

			if (organization
					.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null
					|| (organizationExtendAttr != null && !StrUtil
							.isEmpty(organizationExtendAttr.getOrgAttrValue()))
					|| (list != null && list.size() > 0)) {

				boolean groupMailInterFaceSwitch = UomClassProvider
						.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

				if (groupMailInterFaceSwitch
						&& organization.isUploadGroupMail()) {
					msg = groupMailManager.groupMailPackageInfo(
							GroupMailConstant.GROUP_MAIL_BIZ_ID_16, null,
							organization);
					if (!StrUtil.isEmpty(msg)) {
						ZkUtil.showError(msg, "提示信息");
						return;
					}

					if (organization.getIsChangeOrgName()) {

						if (organizationExtendAttr != null
								&& !StrUtil.isEmpty(organizationExtendAttr
										.getOrgAttrValue())) {
							OrganizationRelation orgRela = new OrganizationRelation();
							orgRela.setOrgId(organization.getOrgId());
							Organization org = orgRela.getOrganization();
							organizationExtendAttr
									.setOrgAttrValue(organizationExtendAttr
											.getOrgAttrValue().replaceFirst(
													org.getOrgName(),
													organization.getOrgName()));
							organizationExtendAttr.update();
						} else if (organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {
							organizationManager
									.updateOrganizationExtendAttr(organization);
						}

						if (list != null && list.size() > 0) {
							for (Organization o : list) {
								organizationExtendAttr = new OrganizationExtendAttr();
								organizationExtendAttr.setOrgId(o.getOrgId());
								organizationExtendAttr
										.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

								organizationExtendAttr = organizationExtendAttrManager
										.queryOrganizationExtendAttr(organizationExtendAttr);
								if (organizationExtendAttr != null
										&& !StrUtil
												.isEmpty(organizationExtendAttr
														.getOrgAttrValue())) {
									OrganizationRelation orgRela = new OrganizationRelation();
									orgRela.setOrgId(organization.getOrgId());
									Organization org = orgRela
											.getOrganization();
									organizationExtendAttr
											.setOrgAttrValue(organizationExtendAttr
													.getOrgAttrValue()
													.replaceFirst(
															org.getOrgName(),
															organization
																	.getOrgName()));
									organizationExtendAttr.update();
								} else if (o
										.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {
									organizationManager
											.updateOrganizationExtendAttr(o);
								}
							}
						}
					}
				}
			}

			organizationManager.updateOrganization(organization);
			/**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
			Class clazz[] = {Organization.class};
		    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "组织修改记录日志");
			organization.setOrgTypeList(null);
		}
		bean.getOrganizationEditWindow().onClose();
		Events.postEvent(Events.ON_OK, this.self, organization);
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getOrganizationEditWindow().onClose();
	}

}
