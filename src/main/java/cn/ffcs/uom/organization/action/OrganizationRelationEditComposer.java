package cn.ffcs.uom.organization.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.OrganizationRelationEditBean;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

/**
 * 组织关系编辑Composer.
 * 
 * @author OUZHF
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OrganizationRelationEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrganizationRelationEditBean bean = new OrganizationRelationEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationRelationManager")
	private OrganizationRelationManager organizationRelationManager;

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;

	@Autowired
	@Qualifier("mdsionOrgRelationManager")
	private MdsionOrgRelationManager mdsionOrgRelationManager;

	/**
	 * 日志服务队列
	 */
	@Qualifier("logService")
	@Autowired
	private LogService logService;

	/**
	 * 组织关系.
	 */
	private OrganizationRelation organizationRelation;

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
	public void onCreate$organizationRelationEditWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
		this.bean.getRelaOrganizaitonRelation().getOrganizationRelationTree()
				.setRelaCdStr("0101");
		this.bean.getRelaOrganizaitonRelation().getOrganizationRelationTree()
				.bindTree();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> relaCdList = UomClassProvider.getValuesList(
				"OrganizationRelation", "relaCd");
		ListboxUtils.rendererForEdit(this.bean.getRelaCd(), relaCdList);
	}

	/**
	 * .
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if ("add".equals(opType)) {
			this.bean.getOrganizationRelationEditWindow().setTitle("组织关系新增");
			// 设置组织关系类型为上级管理机构【0101】
			ListboxUtils.selectByCodeValue(this.bean.getRelaCd(), "0101");
			this.bean.getRelaCd().setDisabled(true);
			organizationRelation = (OrganizationRelation) arg
					.get("organizationRelation");
			if (organizationRelation != null
					&& organizationRelation.getOrgId() != null) {
				this.bean.getOrg().setOrganization(
						organizationRelation.getOrganization());
				this.bean.getOrg().setDisabled(true);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		OrganizationRelation or = null;
		/**
		 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
		 */
		SysLog log = new SysLog();
		log.startLog(new Date(), SysLogConstrants.ORG);

		if ("add".equals(opType)) {
			or = new OrganizationRelation();
		}
		// 填充对象
		if (this.bean.getRelaCd().getSelectedItem() != null
				&& !StrUtil.isEmpty((String) this.bean.getRelaCd()
						.getSelectedItem().getValue())) {
			or.setRelaCd((String) this.bean.getRelaCd().getSelectedItem()
					.getValue());
		} else {
			ZkUtil.showError("关系类型不能为空", "提示信息");
			return;
		}
		Organization org = bean.getOrg().getOrganization();
		if (org == null || org.getOrgId() == null) {
			ZkUtil.showError("组织不能为空", "提示信息");
			return;
		}
		OrganizationRelation relaOrgRle = bean.getRelaOrganizaitonRelation()
				.getOrganizationRelation();
		Organization relaOrg = null;
		if (relaOrgRle != null) {
			relaOrg = relaOrgRle.getOrganization();
		}
		if (relaOrg == null || relaOrg.getOrgId() == null) {
			ZkUtil.showError("关联组织不能为空", "提示信息");
			return;
		}

		if (this.bean.getReason() == null
				|| StrUtil.isEmpty(this.bean.getReason().getValue())) {
			ZkUtil.showError("变更原因不能为空", "提示信息");
			return;
		}
		or.setOrgId(org.getOrgId());
		List<OrganizationRelation> organizationRelationList1 = this.organizationManager
				.queryOrganizationRelationList(or);
		if (organizationRelationList1 != null
				&& organizationRelationList1.size() > 0) {
			ZkUtil.showError("该组织已经存在上级管理机构", "提示信息");
			return;
		}
		or.setRelaOrgId(relaOrg.getOrgId());
		if (or.getOrgId().equals(or.getRelaOrgId())) {
			ZkUtil.showError("关联组织和组织不能是同一个", "提示信息");
			return;
		}

		// 验证下级组织进驻关系
		if (!organizationRelationManager.checkSubordinateOrgEnterRela(
				or.getOrgId(), or.getRelaOrgId(), or.getRelaCd())) {
			return;
		}

		List<OrganizationRelation> organizationRelationList2 = this.organizationManager
				.queryOrganizationRelationList(or);
		if (organizationRelationList2 != null
				&& organizationRelationList2.size() > 0) {
			ZkUtil.showError("该组织关系已经存在", "提示信息");
			return;
		}
		List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
				.querySubTreeOrganizationRelationList(or.getOrgId());
		if (subOrganizationRelationList != null) {
			for (OrganizationRelation organizationRelation : subOrganizationRelationList) {
				if (or.getRelaOrgId().equals(organizationRelation.getOrgId())) {
					ZkUtil.showError("存在环不可添加", "提示信息");
					return;
				}
			}

		}
		/**
		 * 上级只能有一个单位或者部门
		 */
		if (relaOrg.isCompany() || relaOrg.isDepartment()) {
			List<Organization> parentOrgList = org.getParentOrgList(or);// 修复营销树无法添加子节点的问题
			boolean hasCompanyOrDepartmentPatent = false;
			if (parentOrgList != null && parentOrgList.size() > 0) {
				for (Organization pTemp : parentOrgList) {
					if (pTemp.isCompany() || pTemp.isDepartment()) {
						hasCompanyOrDepartmentPatent = true;
						break;
					}
				}
			}
			if (hasCompanyOrDepartmentPatent) {
				ZkUtil.showError("只能有一个单位或者部门的上级", "提示信息");
				return;
			}
		}

		/**
		 * 行政上级
		 */
		if (relaOrg.getOrgFullName() != null) {
			/**
			 * 组织全称修改
			 */
			org.setOrgFullName(relaOrg.getOrgFullName() + org.getOrgName());
			or.setUpdateOrganization(org);
		} else {
			ZkUtil.showError("请注意组织全称未生成", "提示信息");
		}

		or.setReason(this.bean.getReason().getValue());

		this.organizationRelationManager.addOrganizationRelation(or);

		// LogTest logTest = new LogTest();
		/*
		 * 添加组织关系日志
		 */
		// if("add".equals(opType))
		// {
		// logTest.setMsg("组织关系新增成功");
		// logTest.setOperatorObject("组织关系新增");
		// logTest.setOperatorType(LogConstants.OPERATOR_ADD);
		// }
		// else
		// {
		// //修改
		// logTest.setMsg("组织关系修改成功");
		// logTest.setOperatorObject("组织关系修改");
		// logTest.setOperatorType(LogConstants.OPERATOR_UPDATE);
		// }
		//
		// logTestManager.saveLog(logTest.getMsg(),
		// LogConstants.ORGANIZATION_RELATION_SCENCODE,
		// logTest.getOperatorObject(), logTest.getOperatorType());

		/**
		 * 添加多维组织关系
		 */
		MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
		orgRela.setRelaCd(or.getRelaCd());
		MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
				.getMdsionOrgTree(orgRela);
		if (mdsionOrgTree != null
				&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(or.getRelaCd())) {
			orgRela.setOrgId(or.getOrgId());
			orgRela.setRelaOrgId(or.getRelaOrgId());
			mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
		}

		if ("add".equals(opType)) {
			// 添加组织关系
			Class clazz[] = { OrganizationRelation.class };
			log.endLog(logService, clazz, SysLogConstrants.ADD,
					SysLogConstrants.INFO, "添加组织关系记录日志");
		} else {
			// 这个是修改操作，以后可能要添加信息，所以这里还是做个判断
			Class clazz[] = { OrganizationRelation.class };
			log.endLog(logService, clazz, SysLogConstrants.EDIT,
					SysLogConstrants.INFO, "修改组织关系记录日志");
		}
		Events.postEvent(Events.ON_OK,
				bean.getOrganizationRelationEditWindow(), or);
		bean.getOrganizationRelationEditWindow().onClose();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getOrganizationRelationEditWindow().onClose();
	}
}
