package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.OrganizationTreeNodeEditBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.util.OrgFullCodeUtil;

@Controller
@Scope("prototype")
public class OrganizationTreeNodeEditComposer extends BasePortletComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1014502602479906821L;
	/**
	 * 页面bean
	 */
	private OrganizationTreeNodeEditBean bean = new OrganizationTreeNodeEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 选择的组织
	 */
	private Organization organization;
	/**
	 * manager
	 */
	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	private MdsionOrgRelationManager mdsionOrgRelationManager = (MdsionOrgRelationManager) ApplicationContextUtil
			.getBean("mdsionOrgRelationManager");
	
	/**
     * 日志服务队列
     */
    private LogService logService = (LogService) ApplicationContextUtil
        .getBean("logService");

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
	public void onCreate$organizationTreeNodeEditWindow() throws Exception {
		this.bindBean();
		this.bindBox();
		/**
		 * 代理商新增根节点初始化组织选择bandbox：只选择代理商组织
		 */
		if ("addAgentRootNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt().setAgentInit(true);
		}
		/**
		 * 内部经营实体新增根节点初始化组织选择bandbox：只选择内部经营实体组织
		 */
		if ("addIbeRootNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt().setIbeInit(true);
		}
		/**
		 * 代理商新增下级节点初始化组织选择bandbox：只选择代理商组织和营业网点
		 */
		if ("addAgentChildNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt().setContainSalesNetworkInit(
					true);
		}
		/**
		 * 内部经营实体新增下级节点初始化组织选择bandbox：只选择内部经营实体组织和营业网点
		 */
		if ("addIbeChildNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt()
					.setContainIbeSalesNetworkInit(true);
		}

		/**
		 * 组织树页面添加下级节点排除代理商组织
		 */
		if ("addChildNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt().setExcluseAgentInit(true);
		}
		/**
		 * 组织树页面添加下级节点排除内部经营实体组织
		 */
		if ("addChildNode".equals(opType)) {
			this.bean.getOrganizationBandboxExt().setExcluseIbeInit(true);
		}
		/**
		 * 供应商界面：供应商界面，虚拟供应商下只能挂供应商，供应商下面可以挂供应商以及其他
		 */
		if ("addSupplierRootNode".equals(opType)
				|| "addSupplierChildNode".equals(opType)) {
			String orgTypeStr = "";
			String excludeOrgIdStr = "";
			if ("addSupplierChildNode".equals(opType)) {
				/**
				 * 添加子节点可以是供应商和其他4种类型
				 */
				orgTypeStr = OrganizationConstant.ORG_TYPE_N0902000000 + ","
						+ OrganizationConstant.ORG_TYPE_N1001010100 + ","
						+ OrganizationConstant.ORG_TYPE_N1001010200 + ","
						+ OrganizationConstant.ORG_TYPE_N1001020100 + ","
						+ OrganizationConstant.ORG_TYPE_N1001040100;
				excludeOrgIdStr = "9999999997";
			} else if ("addSupplierRootNode".equals(opType)) {
				/**
				 * 添加根节点是挂在999999997下只能是供应商
				 */
				orgTypeStr = OrganizationConstant.ORG_TYPE_N0902000000;
				excludeOrgIdStr = "9999999997";
			}
			Map map = new HashMap();
			map.put("orgTypeList", orgTypeStr);
			map.put("excludeOrgIdList", excludeOrgIdStr);
			this.bean.getOrganizationBandboxExt().setSupplierOrgTypeList(map);
		}
	}

	/**
	 * 绑定下拉框
	 */
	private void bindBox() {
	}

	/**
	 * bindBean
	 */
	private void bindBean() {
		opType = (String) arg.get("opType");
		if ("addOrgTreeChildNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加子节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
			this.bean.getOrgRelaCdMultipleRow().setVisible(true);
		} else if ("addRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addChildNode".equals(opType)
				|| "addAgentChildNode".equals(opType)
				|| "addIbeChildNode".equals(opType)
				|| "addSupplierChildNode".equals(opType)
				|| "addOssChildNode".equals(opType)
				|| "addEdwChildNode".equals(opType)
				|| "addMarketingChildNode".equals(opType)
				|| "addNewMarketingChildNode".equals(opType)
				|| "addNewSeventeenMarketingChildNode".equals(opType)
				|| "addCostChildNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加子节点");
			organization = (Organization) arg.get("organization");
			if (organization == null) {
				ZkUtil.showError("上级节点未选择", "提示信息");
				this.bean.getOrganizationTreeNodeEditWindow().onClose();
			}
		} else if ("addAgentRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加代理商根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addIbeRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle(
					"增加内部经营实体根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addSupplierRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加供应商根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addEdwRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加EDW根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addMarketingRootNode".equals(opType)
				|| "addNewMarketingRootNode".equals(opType)
				|| "addNewSeventeenMarketingRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle(
					"增加MARKETING根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addCostRootNode".equals(opType)) {
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("增加成本树根节点");
			this.bean.getOrgRelaCdRow().setVisible(false);
		} else if ("addOrgRootNode".equals(opType)) {// 添加组织树根节点
			this.bean.getOrganizationTreeNodeEditWindow().setTitle("添加组织树");
			this.bean.getOrgRelaCdRow().setVisible(true);
			this.bean.getOrgTypeCdMultipleRow().setVisible(true);
		} else {
			ZkUtil.showError("未定义操作类型", "提示信息");
			this.bean.getOrganizationTreeNodeEditWindow().onClose();
			return;
		}
		if ("addEdwRootNode".equals(opType) || "addEdwChildNode".equals(opType)) {
			/**
			 * 营销管理页面只显示营销关系类型
			 */
			List<String> optionOrgRelaCdList = new ArrayList<String>();
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXGL);
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXJH);
			this.bean.getOrgRelaCd().setOptionNodes(optionOrgRelaCdList);
			/**
			 * 营销管理页面增加子节点框组织bandbox只显示营销组织
			 */
			this.bean.getOrganizationBandboxExt().getOrganizationListboxExt()
					.setIsCustmsListbox(true);
			/*
			 * try {
			 * this.bean.getOrganizationBandboxExt().getOrganizationListboxExt
			 * ().onQueryOrganization(); } catch (Exception e)
			 * {e.printStackTrace();}
			 */
		} else if ("addMarketingRootNode".equals(opType)
				|| "addMarketingChildNode".equals(opType)) {
			/**
			 * 营销管理页面只显示营销关系类型
			 */
			List<String> optionOrgRelaCdList = new ArrayList<String>();
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXGL);
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXJH_2015);
			this.bean.getOrgRelaCd().setOptionNodes(optionOrgRelaCdList);
			/**
			 * 营销管理页面增加子节点框组织bandbox只显示营销组织
			 */
			this.bean.getOrganizationBandboxExt().getOrganizationListboxExt()
					.setIsMarketingListbox(true);
			/*
			 * try {
			 * this.bean.getOrganizationBandboxExt().getOrganizationListboxExt
			 * ().onQueryOrganization(); } catch (Exception e)
			 * {e.printStackTrace();}
			 */
		} else if ("addNewMarketingRootNode".equals(opType)
				|| "addNewMarketingChildNode".equals(opType)) {
			/**
			 * 营销管理页面只显示营销关系类型
			 */
			List<String> optionOrgRelaCdList = new ArrayList<String>();
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXGL);
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXJH_2016);
			this.bean.getOrgRelaCd().setOptionNodes(optionOrgRelaCdList);
			/**
			 * 营销管理页面增加子节点框组织bandbox只显示营销组织
			 */
			this.bean.getOrganizationBandboxExt().getOrganizationListboxExt()
					.setIsMarketingListbox(true);
			/*
			 * try {
			 * this.bean.getOrganizationBandboxExt().getOrganizationListboxExt
			 * ().onQueryOrganization(); } catch (Exception e)
			 * {e.printStackTrace();}
			 */
        } else if ("addNewSeventeenMarketingRootNode".equals(opType)
            || "addNewSeventeenMarketingChildNode".equals(opType)) {
            /**
             * 营销管理页面只显示营销关系类型
             */
            List<String> optionOrgRelaCdList = new ArrayList<String>();
            optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXGL);
            optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_YXJH_2017);
            this.bean.getOrgRelaCd().setOptionNodes(optionOrgRelaCdList);
            /**
             * 营销管理页面增加子节点框组织bandbox只显示营销组织
             */
            this.bean.getOrganizationBandboxExt().getOrganizationListboxExt()
                .setIsMarketingListbox(true);

        } else if ("addCostRootNode".equals(opType)
				|| "addCostChildNode".equals(opType)) {
			/**
			 * 营销管理页面只显示营销关系类型
			 */
			List<String> optionOrgRelaCdList = new ArrayList<String>();
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_COST_0500);
			optionOrgRelaCdList.add(OrganizationConstant.RELA_CD_COST_0501);
			this.bean.getOrgRelaCd().setOptionNodes(optionOrgRelaCdList);
			/**
			 * 营销管理页面增加子节点框组织bandbox只显示营销组织
			 */
			this.bean.getOrganizationBandboxExt().getOrganizationListboxExt()
					.setIsCostListbox(true);
			/*
			 * try {
			 * this.bean.getOrganizationBandboxExt().getOrganizationListboxExt
			 * ().onQueryOrganization(); } catch (Exception e)
			 * {e.printStackTrace();}
			 */
		}
	}

	/**
	 * 确定
	 * 
	 * @throws Exception
	 */
	public void onAdd() throws Exception {
	    
	    /**
         * 开始日志添加操作
         * 添加日志到队列需要：
         * 业务开始时间，日志消息类型，错误编码和描述
         */
        SysLog log = new SysLog();
        log.startLog(new Date(), SysLogConstrants.ORG);
	    
		Map map = new HashMap();
		Organization newOrganization = this.bean.getOrganizationBandboxExt()
				.getOrganization();
		if (newOrganization == null) {
			ZkUtil.showError("请选择组织", "提示信息");
			return;
		}
		
		String reason = this.bean.getReason().getValue();
		
		if (StrUtil.isEmpty(reason)) {
			ZkUtil.showError("请填写变更原因", "提示信息");
			return;
		}
		
		String relaCd = (String) this.bean.getOrgRelaCd().getAttrValue();
		OrganizationRelation organizationRelation = new OrganizationRelation();
		organizationRelation.setReason(reason);
		
		if (opType.equals("addChildNode") || "addEdwChildNode".equals(opType)
				|| "addMarketingChildNode".equals(opType)
				|| "addNewMarketingChildNode".equals(opType)
				|| "addNewSeventeenMarketingChildNode".equals(opType)
				|| "addCostChildNode".equals(opType)) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaOrgId(organization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			
			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}

			OrganizationRelation queryOrganizationRelation = new OrganizationRelation();
			queryOrganizationRelation.setOrgId(newOrganization.getOrgId());
			queryOrganizationRelation.setRelaCd(organizationRelation
					.getRelaCd());

			List<OrganizationRelation> queryOrganizationRelationList = this.organizationRelationManager
					.queryOrganizationRelationList(queryOrganizationRelation);

			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已有上级,不能再挂上级组织!", "提示信息");
				return;
			}

			OrganizationRelation or = organizationRelationManager
					.queryOrganizationRelation(organizationRelation);
			if (or != null) {
				ZkUtil.showError("该关系已经存在", "提示信息");
				return;
			}
			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}

			}

			// 验证上级组织进驻关系
			if (!organizationRelationManager.checkSuperiorOrgEnterRela(
					organizationRelation.getOrgId(),
					organizationRelation.getRelaOrgId(),
					organizationRelation.getRelaCd())) {
				return;
			}

			/**
			 * 上级组织是单位或者部门(团队当部门处理)
			 */
			if (organization.isCompany() || organization.isDepartment()
					|| organization.isTeam()) {
				List<Organization> parentOrgList = newOrganization
						.getParentOrgList(organizationRelation);// 修复营销树无法添加子节点的问题
				boolean hasCompanyOrDepartmentPatent = false;
				if (parentOrgList != null && parentOrgList.size() > 0) {
					for (Organization pTemp : parentOrgList) {
						if (pTemp.isCompany() || pTemp.isDepartment()
								|| organization.isTeam()) {
							hasCompanyOrDepartmentPatent = true;
							break;
						}
					}
				}
				if (hasCompanyOrDepartmentPatent) {
					ZkUtil.showError("只能有一个单位或者部门的上级", "提示信息");
					return;
				} else {
					if ("addChildNode".equals(opType)
							|| "addRootNode".equals(opType)) {// 解决集团编码生成错误的BUG：做组织关系类型限制即0101关系类型[上级管理机构]
						try {
							String[] strArray = OrgFullCodeUtil
									.getGroupOrgCodeInfo(organization,
											newOrganization);
							if (strArray != null && strArray.length > 0) {
								if (!StrUtil.isEmpty(strArray[0])
										&& !StrUtil.isEmpty(strArray[1])
										&& !StrUtil.isEmpty(strArray[2])) {
									newOrganization
											.setOrgGroupCodeLevel(strArray[0]);
									newOrganization
											.setOrgGroupCodeSalevelCode(strArray[1]);
									newOrganization
											.setOrgGroupCode(strArray[2]);
									organizationRelation
											.setUpdateOrganization(newOrganization);
								} else {
									ZkUtil.showError("请注意集团编码未生成", "提示信息");
								}
							}
						} catch (Exception e) {
							ZkUtil.showError("请注意集团编码未生成:" + e.getMessage(),
									"提示信息");
						}
					}
				}
			}
			/**
			 * 行政上级
			 */
			List<OrganizationRelation> parentTreeList = this.organizationRelationManager
					.queryParentTreeOrganizationRelationList(
							organization.getOrgId(), relaCd);
			boolean isInnerOrg = OrganizationConstant.RELA_CD_INNER
					.equals(organizationRelation.getRelaCd());
			// List<OrganizationRelation> parentTreeList =
			// this.organizationRelationManager
			// .queryParentTreeOrganizationRelationList(organization
			// .getOrgId(), OrganizationConstant.MANAGER_PRE);
			// 非上级管理机构 内部（0101） 都不生成组织全称
			if (parentTreeList != null && parentTreeList.size() > 0
					&& isInnerOrg) {
				StringBuffer fullName = new StringBuffer();
				for (int i = (parentTreeList.size() - 1); i >= 0; i--) {
					/**
					 * 顶层集团公司不包含
					 */
					if ((parentTreeList.get(i).getOrganization() != null && OrganizationConstant.ROOT_TREE_ORG_ID
							.equals(parentTreeList.get(i).getOrganization()
									.getOrgId()))
							|| (parentTreeList.get(i).getOrganization() != null && OrganizationConstant.ROOT_EDW_ORG_ID
									.equals(parentTreeList.get(i)
											.getOrganization().getOrgId()))
							|| (parentTreeList.get(i).getOrganization() != null && OrganizationConstant.ROOT_MARKETING_ORG_ID
									.equals(parentTreeList.get(i)
											.getOrganization().getOrgId()))) {
						continue;
					}
					if (parentTreeList.get(i) != null
							&& parentTreeList.get(i).getOrganization() != null) {
						fullName.append(parentTreeList.get(i).getOrganization()
								.getOrgName());
					}
				}
				/**
				 * 组织全称修改
				 */
				fullName.append(newOrganization.getOrgName());
				newOrganization.setOrgFullName(fullName.toString());
				organizationRelation.setUpdateOrganization(newOrganization);

				/**
				 * 级联更新组织全称（只更新归属关系的组织）
				 * 
				 * @param organization
				 */
				// organizationManager.updateOrgFullNameOnCascade(newOrganization);
				List<Organization> list = newOrganization
						.getRelacd0101SubOrganizationList();
				if (list != null && list.size() > 0) {
					ZkUtil.showInformation("该节点下的组织全称会全部修改请耐心等待...", "提示信息");
					newOrganization.setBatchNumber(OperateLog
							.gennerateBatchNumber());
					for (Organization org : list) {
						org.setBatchNumber(newOrganization.getBatchNumber());
						org.setOrgFullName(newOrganization.getOrgFullName()
								+ org.getOrgName());
						org.update();
						organizationManager.updateOrgFullNameOnCascade(org);
					}
				}
			} else {
				if (isInnerOrg) {
					ZkUtil.showError("请注意组织全称未生成", "提示信息");
				}
			}

			/**
			 * 行政中非单位(即部门)的区域编码自动生成，取其最近的上级部门区域编码
			 */
			if (parentTreeList != null && parentTreeList.size() > 0) {
				for (OrganizationRelation temp : parentTreeList) {
					Organization orgTemp = temp.getOrganization();
					if (newOrganization != null
							&& newOrganization.isAdministrative()
							&& newOrganization.isNotCompany()) {
						if (orgTemp != null && orgTemp.isDepartment()) {
							newOrganization.setAreaCodeId(orgTemp
									.getAreaCodeId());
							organizationRelation
									.setUpdateOrganization(newOrganization);
							break;
						}
					}
				}
			}
			/**
			 * 非行政组织，区域编码按上级组织生成
			 */
			if (OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS
					.equals(relaCd) && !newOrganization.isAdministrative()) {
				newOrganization.setAreaCodeId(organization.getAreaCodeId());
			}

			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			
			/*
			 * 这里日志添加子节点只添加一个内部管路树的日志
			 */
//			if("addChildNode".equals(opType))
//			{
//			    /**
//                 * 日志需要具体操作内容
//                 * 场景编码（这个配置常量）
//                 * 操作业务对象
//                 * 操作类型
//                 */
//                LogTest logTest = new LogTest();
//                logTest.setMsg("内部管理树添加子节点成功");
//                logTest.setOperatorObject("组织关系新增");
//                logTest.setOperatorType(LogConstants.OPERATOR_ADD);
//                logTestManager.saveLog(logTest.getMsg(), LogConstants.ORGANIZATION_RELATION_SCENCODE, logTest.getOperatorObject(), logTest.getOperatorType());
//			}
			if("addChildNode".equals(opType))
			{
			    Class clazz[] = {OrganizationRelation.class};
			    log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "组织关系子节点添加记录日志");
			}
			

			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}

			if ("addMarketingChildNode".equals(opType)) {// 生成EDA_CODE
				// 获取本级组织的层级
				int orgLevel = organization
						.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE) + 1;
				boolean isGrid = newOrganization.isGrid(newOrganization
						.getOrgTypeList());
				if (StrUtil.isEmpty(newOrganization.getEdaCode())) {
					newOrganization.setEdaCode(newOrganization.generateEdaCode(
							orgLevel, isGrid));
					newOrganization.update();
				} else {
					if (!isGrid
							&& !(newOrganization.getEdaCode().substring(0, 1)
									.equals(orgLevel + ""))) {
						newOrganization.setEdaCode(newOrganization
								.generateEdaCode(orgLevel, isGrid));
						newOrganization.update();
					}
				}
			} else if ("addNewMarketingChildNode".equals(opType)) {
				// 生成EDA_CODE
				// 获取本级组织的层级
				int orgLevel = organization
						.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403) + 1;
				boolean isGrid = newOrganization.isGrid(newOrganization
						.getOrgTypeList());
				if (StrUtil.isEmpty(newOrganization.getEdaCode())) {
					newOrganization.setEdaCode(newOrganization.generateEdaCode(
							orgLevel, isGrid));
					newOrganization.update();
				} else {
					if (!isGrid
							&& !(newOrganization.getEdaCode().substring(0, 1)
									.equals(orgLevel + ""))) {
						newOrganization.setEdaCode(newOrganization
								.generateEdaCode(orgLevel, isGrid));
						newOrganization.update();
					}
				}

            } else if ("addNewSeventeenMarketingChildNode".equals(opType)) {
                // 生成EDA_CODE
                // 获取本级组织的层级
                int orgLevel = organization
                    .getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404) + 1;
                boolean isGrid = newOrganization.isGrid(newOrganization.getOrgTypeList());
                if (StrUtil.isEmpty(newOrganization.getEdaCode())) {
                    newOrganization.setEdaCode(newOrganization.generateEdaCode(orgLevel, isGrid));
                    newOrganization.update();
                } else {
                    if (!isGrid
                        && !(newOrganization.getEdaCode().substring(0, 1).equals(orgLevel + ""))) {
                        newOrganization.setEdaCode(newOrganization
                            .generateEdaCode(orgLevel, isGrid));
                        newOrganization.update();
                    }
                }
            }

		} else if (opType.equals("addRootNode")) {
			organizationRelation.setOrgId(organization.getOrgId());
			organizationRelation.setRelaOrgId(OrganizationConstant.ROOT_ORG_ID);
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
             * 日志需要具体操作内容
             * 场景编码（这个配置常量）
             * 操作业务对象
             * 操作类型
             */
			Class clazz[] = {OrganizationRelation.class};
		    log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "组织关系根节点添加记录日志");
		} else if (opType.equals("addAgentRootNode")) {
			if (newOrganization == null || newOrganization.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
				return;
			}
			OrganizationRelation queryOrganizationRelation = new OrganizationRelation();
			queryOrganizationRelation.setOrgId(newOrganization.getOrgId());
			queryOrganizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			List<OrganizationRelation> queryOrganizationRelationList = this.organizationRelationManager
					.queryOrganizationRelationList(queryOrganizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已有上级,不能设置为代理商根节点", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation
					.setRelaOrgId(OrganizationConstant.ROOT_AGENT_ORG_ID);
			organizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if (opType.equals("addAgentChildNode")) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			List<OrganizationRelation> queryOrganizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			organizationRelation.setRelaOrgId(organization.getOrgId());

			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}

			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if (opType.equals("addIbeRootNode")) {
			if (newOrganization == null || newOrganization.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
				return;
			}
			OrganizationRelation queryOrganizationRelation = new OrganizationRelation();
			queryOrganizationRelation.setOrgId(newOrganization.getOrgId());
			queryOrganizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			List<OrganizationRelation> queryOrganizationRelationList = this.organizationRelationManager
					.queryOrganizationRelationList(queryOrganizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已有上级,不能设置为内部经营实体根节点", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation
					.setRelaOrgId(OrganizationConstant.ROOT_IBE_ORG_ID);
			organizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if (opType.equals("addIbeChildNode")) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			List<OrganizationRelation> queryOrganizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			organizationRelation.setRelaOrgId(organization.getOrgId());
			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}
			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if ("addSupplierRootNode".equals(opType)) {
			if (newOrganization == null || newOrganization.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
				return;
			}
			OrganizationRelation queryOrganizationRelation = new OrganizationRelation();
			queryOrganizationRelation.setOrgId(newOrganization.getOrgId());
			queryOrganizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			List<OrganizationRelation> queryOrganizationRelationList = this.organizationRelationManager
					.queryOrganizationRelationList(queryOrganizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已有上级,不能设置为供应商商根节点", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation
					.setRelaOrgId(OrganizationConstant.ROOT_SUPPLIER_ORG_ID);
			organizationRelation
					.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if ("addSupplierChildNode".equals(opType)) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			List<OrganizationRelation> queryOrganizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			organizationRelation.setRelaOrgId(organization.getOrgId());
			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}
			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if ("addOssChildNode".equals(opType)) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			List<OrganizationRelation> queryOrganizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			organizationRelation.setRelaOrgId(organization.getOrgId());
			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}
			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if ("addCostChildNode".equals(opType)) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("请选择关联关系", "提示信息");
				return;
			}
			organizationRelation.setOrgId(newOrganization.getOrgId());
			organizationRelation.setRelaCd(relaCd);
			List<OrganizationRelation> queryOrganizationRelationList = organizationRelationManager
					.queryOrganizationRelationList(organizationRelation);
			if (queryOrganizationRelationList != null
					&& queryOrganizationRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			organizationRelation.setRelaOrgId(organization.getOrgId());
			if (organizationRelation.getOrgId() != null
					&& organizationRelation.getOrgId().equals(
							organizationRelation.getRelaOrgId())) {
				ZkUtil.showError("该组织的上级不能是本身", "提示信息");
				return;
			}
			List<OrganizationRelation> subOrganizationRelationList = this.organizationManager
					.querySubTreeOrganizationRelationList(organizationRelation
							.getOrgId());
			if (subOrganizationRelationList != null) {
				for (OrganizationRelation subOrganizationRelation : subOrganizationRelationList) {
					if (organizationRelation.getRelaOrgId().equals(
							subOrganizationRelation.getOrgId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}
			organizationRelationManager
					.addOrganizationRelation(organizationRelation);
			/**
			 * 添加多维组织关系
			 */
			MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
			orgRela.setRelaCd(organizationRelation.getRelaCd());

			MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
					.getMdsionOrgTree(orgRela);

			if (mdsionOrgTree != null
					&& mdsionOrgTree.getMdsionOrgRelTypeCd().equals(
							organizationRelation.getRelaCd())) {
				orgRela.setOrgId(organizationRelation.getOrgId());
				orgRela.setRelaOrgId(organizationRelation.getRelaOrgId());
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
			}
		} else if ("addOrgRootNode".equals(opType)) {
			if (StrUtil.isEmpty(relaCd)) {
				ZkUtil.showError("关联关系不能为空", "提示信息");
				return;
			}
			List<Pair<String, String>> resultArr = this.bean.getOrgTypeCd()
					.getResultArr();
			if (resultArr != null) {
				map.put("relaResultArr", resultArr);
			} else {
				ZkUtil.showError("组织类型不能为空！", "提示信息");
				return;
			}
			MdsionOrgTree mdsionOrgTree = new MdsionOrgTree();
			MdsionOrgRelation mdsionOrgRelation = MdsionOrgRelation
					.newInstance();
			mdsionOrgRelation.setOrgId(newOrganization.getOrgId());
			mdsionOrgRelation.setRelaOrgId(OrganizationConstant.ROOT_ORG_ID);
			mdsionOrgRelation.setRelaCd(relaCd);
			List<MdsionOrgRelation> queryMdsionOrgRelationList = mdsionOrgRelationManager
					.queryMdsionOrgRelationList(mdsionOrgRelation);
			if (queryMdsionOrgRelationList != null
					&& queryMdsionOrgRelationList.size() > 0) {
				ZkUtil.showError("该组织已存在该关系", "提示信息");
				return;
			}
			mdsionOrgTree.setOrgId(newOrganization.getOrgId());
			mdsionOrgTree.setOrgTreeName(newOrganization.getOrgName());
			mdsionOrgTree.setMdsionOrgRelTypeCd(relaCd);
			mdsionOrgTree.setIsShow(1);
			mdsionOrgTree.addOnly();
			long id = mdsionOrgTree.getMdsionOrgTreeId();
			mdsionOrgRelationManager.addMdsionOrgRelation(mdsionOrgRelation);
			map.put("mdsionOrgRelation", mdsionOrgRelation);
			map.put("orgTreeId", id);
			Events.postEvent("onOK", this.self, map);
			return;
		} else if ("addOrgTreeChildNode".equals(opType)) {
			List<Pair<String, String>> resultArr = this.bean.getOrgRelaCds()
					.getResultArr();
			map.put("orgId", newOrganization.getOrgId() + "");
			map.put("relaResultArr", resultArr);
		}
		
		map.put("organizationRelation", organizationRelation);
		bean.getOrganizationTreeNodeEditWindow().onClose();
		Events.postEvent("onOK", this.self, map);
	}

	/**
	 * 取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getOrganizationTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增组织
	 * 
	 * @throws Exception
	 */
	public void onAddOrg() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("organization", organization);
		Window win = (Window) Executions.createComponents(
				"/pages/organization/agent_organization_edit.zul", this.self,
				map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Organization org = (Organization) event.getData();
				if (org != null) {
					bean.getOrganizationBandboxExt().setOrganization(org);
				}
			}
		});
	}
}
