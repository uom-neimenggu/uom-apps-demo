package cn.ffcs.uom.orgTreeCalc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.filter.BaseFilter;
import cn.ffcs.uom.orgTreeCalc.filter.BaseStaffOrgFilter;
import cn.ffcs.uom.orgTreeCalc.filter.StaffOrgFilter;
import cn.ffcs.uom.orgTreeCalc.filter.TreeFilter;
import cn.ffcs.uom.orgTreeCalc.filter.orgTypeFilter;
import cn.ffcs.uom.orgTreeCalc.filter.refOrgRelaCdFilter;
import cn.ffcs.uom.orgTreeCalc.filter.refStaffOtFilter;
import cn.ffcs.uom.orgTreeCalc.filter.refStaffTypeCdFilter;
import cn.ffcs.uom.orgTreeCalc.filter.relaCdFilter;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaManager;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgStaffRelaManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRela;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgStaffRela;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;

public class treeCalcAction {
	private Tree tree;
	private TreeFilter treeFilter = new BaseFilter();
	private StaffOrgFilter staffOrgFilter = new BaseStaffOrgFilter();
	private Logger logger = Logger.getLogger(this.getClass());

	private HashMap<Long, Set<StaffOrganization>> staffOrganizationMap = new HashMap<Long, Set<StaffOrganization>>();

	@Getter
	private HashMap<Long, Organization> organizationMap = new HashMap<Long, Organization>();

	public treeCalcAction() {
		try {
			cacheOrganization();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * 实时构建推导树
	 * 
	 * @param orgId
	 *            根组织Id
	 * @param orgTreeId
	 *            组织树标识
	 * @throws Exception
	 */
	public void createRealTimeTree(Long orgId, Long orgTreeId) throws Exception {
		logger.info("开始实时构建推导树...");
		String sql = "SELECT t.* FROM (select a.* from organization_relation a, organization c WHERE C.STATUS_CD = ? AND C.ORG_ID = A.ORG_ID AND A.STATUS_CD = ?) t START WITH t.rela_org_id = ? CONNECT BY PRIOR t.org_id = t.rela_org_id ";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgId);
		List<OrganizationRelation> list = OrganizationRelation.repository()
				.jdbcFindList(sql, params, OrganizationRelation.class);
		tree = new Tree(list);
		logger.info("推导树生成成功，节点数：" + list.size());

		Init(orgTreeId);
		cacheStaffOrganization();
	}

	/**
	 * 构建推导树,包含历史表
	 * 
	 * @param orgId
	 *            根组织Id
	 * @param orgTreeId
	 *            组织树标识
	 * @param date
	 */
	public void createTree(Long orgId, Long orgTreeId, Date date)
			throws Exception {
		logger.info("开始构建推导树...");
		String sql = "SELECT t.* FROM (select a.* from v_organization_relation a, v_organization c WHERE C.EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND C.EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND C.ORG_ID = A.ORG_ID AND A.EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND A.EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND A.STATUS_CD != ? AND C.STATUS_CD != ? ) t START WITH t.rela_org_id = ? CONNECT BY PRIOR t.org_id = t.rela_org_id ";
		List params = new ArrayList();
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		params.add(orgId);
		List<OrganizationRelation> list = OrganizationRelation.repository()
				.jdbcFindList(sql, params, OrganizationRelation.class);
		tree = new Tree(list);
		logger.info("推导树生成成功，节点数：" + list.size());

		Init(orgTreeId, date);
		cacheStaffOrganization(date);
	}

	/**
	 * 发布组织树
	 * @param orgTreeId
	 * @param date
	 * @return
	 */
	public String publishOrganizationTree(Long orgTreeId, Date date) {
		try {
			createTree(OrganizationConstant.ROOT_ORG_ID, orgTreeId, date);
			compareTreeOrganizationRelation(orgTreeId, date);
			compareTreeOrganizationStaffRelation(orgTreeId, date);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "";
	}

	public TreeNode getTreeNode(final Long id) {
		TreeNode node = tree.getAllNodes().get(id);
		return new TreeNodeByFilter(node);
	}

	public TreeNode getRoot() {
		return new TreeNodeByFilter(tree.getRoot());
	}

	private void Init(Long orgTreeId) {
		if (orgTreeId == null) {
			treeFilter = new BaseFilter();
			staffOrgFilter = new BaseStaffOrgFilter();
			return;
		}
		// 查询组织树引用组织关系类型配置
		String sql = "SELECT * FROM TREE_ORG_RELA_TYPE_RULE where ORG_TREE_ID = ? and STATUS_CD = ?";
		List params = new ArrayList();
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<TreeOrgRelaTypeRule> listTreeOrgRelaTypeRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeOrgRelaTypeRule.class);
		List<String> relaCds = new ArrayList<String>();
		for (int i = 0; i < listTreeOrgRelaTypeRule.size(); ++i) {
			relaCds.add(listTreeOrgRelaTypeRule.get(i).getRefOrgRelaCd());
		}
		treeFilter.setFilter(new relaCdFilter(relaCds));

		// 查询组织树引用组织类型配置
		sql = "SELECT * FROM TREE_ORG_TYPE_RULE where ORG_TREE_ID = ? and STATUS_CD = ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<TreeOrgTypeRule> listTreeOrgTypeRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeOrgTypeRule.class);
		List<String> orgTypes = new ArrayList<String>();
		for (int i = 0; i < listTreeOrgTypeRule.size(); ++i) {
			orgTypes.add(listTreeOrgTypeRule.get(i).getRefTypeValue());
		}
		treeFilter.setFilter(new orgTypeFilter(orgTypes));

		// 查询组织树员工汇总引用关联类型配置
		sql = "SELECT * FROM TREE_STAFF_ORT_RULE where ORG_TREE_ID = ? and STATUS_CD = ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<TreeStaffOrtRule> listTreeStaffOrtRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffOrtRule.class);
		List<String> relaCds2 = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffOrtRule.size(); ++i) {
			relaCds2.add(listTreeStaffOrtRule.get(i).getRefOrgRelaCd());
		}
		staffOrgFilter.setFilter(new refOrgRelaCdFilter(relaCds2));

		// 查询组织树员工汇总引用员工类型配置
		sql = "SELECT * FROM TREE_STAFF_SFT_RULE where ORG_TREE_ID = ? and STATUS_CD = ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<TreeStaffSftRule> listTreeStaffSftRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffSftRule.class);
		List<String> staffTypes = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffSftRule.size(); ++i) {
			staffTypes.add(listTreeStaffSftRule.get(i).getRefStaffTypeCd());
		}
		staffOrgFilter.setFilter(new refStaffTypeCdFilter(staffTypes));

		// 查询组织树员工汇总引用组织类型规则配置
		sql = "SELECT * FROM TREE_STAFF_OT_RULE where ORG_TREE_ID = ? and STATUS_CD = ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<TreeStaffOtRule> listTreeStaffOtRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffOtRule.class);
		List<String> staffOts = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffOtRule.size(); ++i) {
			staffOts.add(listTreeStaffOtRule.get(i).getRefTypeValue());
		}
		staffOrgFilter.setFilter(new refStaffOtFilter(staffOts));
	}

	private void Init(Long orgTreeId, Date date) {
		if (orgTreeId == null) {
			treeFilter = new BaseFilter();
			staffOrgFilter = new BaseStaffOrgFilter();
			return;
		}
		// 查询组织树引用组织关系类型配置
		String sql = "SELECT * FROM V_TREE_ORG_RELA_TYPE_RULE where ORG_TREE_ID = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
		List params = new ArrayList();
		params.add(orgTreeId);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<TreeOrgRelaTypeRule> listTreeOrgRelaTypeRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeOrgRelaTypeRule.class);
		List<String> relaCds = new ArrayList<String>();
		for (int i = 0; i < listTreeOrgRelaTypeRule.size(); ++i) {
			relaCds.add(listTreeOrgRelaTypeRule.get(i).getRefOrgRelaCd());
		}
		treeFilter.setFilter(new relaCdFilter(relaCds));

		// 查询组织树引用组织类型配置
		sql = "SELECT * FROM V_TREE_ORG_TYPE_RULE where ORG_TREE_ID = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<TreeOrgTypeRule> listTreeOrgTypeRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeOrgTypeRule.class);
		List<String> orgTypes = new ArrayList<String>();
		for (int i = 0; i < listTreeOrgTypeRule.size(); ++i) {
			orgTypes.add(listTreeOrgTypeRule.get(i).getRefTypeValue());
		}
		treeFilter.setFilter(new orgTypeFilter(orgTypes, date));

		// 查询组织树员工汇总引用关联类型配置
		sql = "SELECT * FROM V_TREE_STAFF_ORT_RULE where ORG_TREE_ID = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<TreeStaffOrtRule> listTreeStaffOrtRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffOrtRule.class);
		List<String> relaCds2 = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffOrtRule.size(); ++i) {
			relaCds2.add(listTreeStaffOrtRule.get(i).getRefOrgRelaCd());
		}
		staffOrgFilter.setFilter(new refOrgRelaCdFilter(relaCds2));

		// 查询组织树员工汇总引用员工类型配置
		sql = "SELECT * FROM V_TREE_STAFF_SFT_RULE where ORG_TREE_ID = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<TreeStaffSftRule> listTreeStaffSftRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffSftRule.class);
		List<String> staffTypes = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffSftRule.size(); ++i) {
			staffTypes.add(listTreeStaffSftRule.get(i).getRefStaffTypeCd());
		}
		staffOrgFilter.setFilter(new refStaffTypeCdFilter(staffTypes, date));

		// 查询组织树员工汇总引用组织类型规则配置
		sql = "SELECT * FROM V_TREE_STAFF_OT_RULE where ORG_TREE_ID = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
		params = new ArrayList();
		params.add(orgTreeId);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<TreeStaffOtRule> listTreeStaffOtRule = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeStaffOtRule.class);
		List<String> staffOts = new ArrayList<String>();
		for (int i = 0; i < listTreeStaffOtRule.size(); ++i) {
			staffOts.add(listTreeStaffOtRule.get(i).getRefTypeValue());
		}
		staffOrgFilter.setFilter(new refStaffOtFilter(staffOts, date));
	}

	private void cacheStaffOrganization() throws Exception {
		String sql = "SELECT * FROM STAFF_ORGANIZATION where STATUS_CD = ? order by ORG_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<StaffOrganization> list = StaffOrganization.repository()
				.jdbcFindList(sql, params, StaffOrganization.class);
		for (int i = 0; i < list.size(); ++i) {
			StaffOrganization staffOrganization = list.get(i);
			Set<StaffOrganization> staffOrganizationSet = staffOrganizationMap
					.get(staffOrganization.getOrgId());
			if (staffOrganizationSet == null) {
				staffOrganizationSet = new HashSet<StaffOrganization>();
				staffOrganizationMap.put(staffOrganization.getOrgId(),
						staffOrganizationSet);
			}
			staffOrganizationSet.add(staffOrganization);
		}
	}

	private void cacheStaffOrganization(Date date) throws Exception {
		String sql = "SELECT * FROM V_STAFF_ORGANIZATION WHERE EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ? order by ORG_ID";
		List params = new ArrayList();
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		List<StaffOrganization> list = StaffOrganization.repository()
				.jdbcFindList(sql, params, StaffOrganization.class);
		for (int i = 0; i < list.size(); ++i) {
			StaffOrganization staffOrganization = list.get(i);
			Set<StaffOrganization> staffOrganizationSet = staffOrganizationMap
					.get(staffOrganization.getOrgId());
			if (staffOrganizationSet == null) {
				staffOrganizationSet = new HashSet<StaffOrganization>();
				staffOrganizationMap.put(staffOrganization.getOrgId(),
						staffOrganizationSet);
			}
			staffOrganizationSet.add(staffOrganization);
		}
	}

	private void cacheOrganization() throws Exception {
		String sql = "SELECT * FROM ORGANIZATION";
		List params = new ArrayList();
		List<Organization> list = Organization.repository().jdbcFindList(sql,
				params, Organization.class);
		for (int i = 0; i < list.size(); ++i) {
			Organization organization = list.get(i);
			organizationMap.put(organization.getOrgId(), organization);
		}
	}

	/**
	 * 统计树上所有节点员工信息
	 * @return
	 */
	public HashMap<Long, Set<StaffOrganization>> statisticStaff() {
		HashMap<Long, Set<StaffOrganization>> staffs = new HashMap<Long, Set<StaffOrganization>>();
		statisticOrganizationStaff(tree.getRoot(), staffs, true, null);
		return staffs;
	}

	/**
	 * 递归统计当前节点员工信息
	 * @param node
	 * @param staffs
	 * @param include
	 * @param orgRelaCd
	 * @return
	 */
	private Set<StaffOrganization> statisticOrganizationStaff(TreeNode node,
			HashMap<Long, Set<StaffOrganization>> staffs, boolean include,
			String orgRelaCd) {
		Set<StaffOrganization> orgStaffs = null;
		List<Long> orgStaffIds = new ArrayList<Long>();	//存储已汇总员工Id（包含本级和下级）
		Long orgId = null;
		try {
			orgId = Long.valueOf(node.getValue());
			orgStaffs = staffs.get(orgId);
			//orgStaffs不为null表示该节点已经统计过员工
			if (orgStaffs == null) {
				orgStaffs = new HashSet<StaffOrganization>();
				if (include) {
					staffs.put(orgId, orgStaffs);
				}
				Set<StaffOrganization> set = staffOrganizationMap.get(orgId);
				if (set != null) {
					Iterator<StaffOrganization> iterator = set.iterator();
					while (iterator.hasNext()) {
						StaffOrganization staffOrganization = iterator.next();
						//对员工组织关系应用过滤条件
						if (staffOrgFilter.validate(staffOrganization,
								orgRelaCd)) {
							orgStaffs.add(staffOrganization);
							orgStaffIds.add(staffOrganization.getStaffId());
						}
					}
				}
			} else {
				return orgStaffs;
			}
		} catch (Exception e) {

		}

		Iterator<String> keysIterator = node.getChildNodes().keySet()
				.iterator();
		TreeNode nodeByFilter = null;
		if (orgId != null) {
			nodeByFilter = getTreeNode(orgId);
		} else {
			nodeByFilter = getRoot();
		}
		while (keysIterator.hasNext()) {
			String key = keysIterator.next();
			//保证树节点的上下级组织关系是连续的，不出现跳跃汇总
			if (!StrUtil.isNullOrEmpty(orgRelaCd) && !orgRelaCd.equals(key)) {
				continue;
			}

			List<TreeNode> nodes = node.getChildNodes().get(key);
			for (int i = 0; i < nodes.size(); ++i) {
				TreeNode item = nodes.get(i);
				List<TreeNode> childsByFilter = nodeByFilter.getChildNodes()
						.get(key);
				boolean subInclude = false;
				//判断当前节点item是否包含在生成树中，如果父节点不在树上，则当前节点也不在树上
				if (include) {
					if (childsByFilter != null) {
						for (int j = 0; j < childsByFilter.size(); j++) {
							if (item.getValue().equals(
									childsByFilter.get(j).getValue())) {
								subInclude = true;
								break;
							}
						}
					}
				}
				Set<StaffOrganization> subOrgStaffs = statisticOrganizationStaff(
						item, staffs, subInclude, key);
				if (orgStaffs != null) {
					//包含在树中的下级节点不汇总员工到当前节点
					if (!subInclude && subOrgStaffs != null) {
						Iterator<StaffOrganization> subOrgStaffIterator = subOrgStaffs.iterator();
						while (subOrgStaffIterator.hasNext()) {
							StaffOrganization subOrgStaffItem = subOrgStaffIterator.next();
							//如果orgStaffIds尚未包含该员工
							if(!orgStaffIds.contains(subOrgStaffItem.getStaffId())){
								StaffOrganization tmpStaffOrganization = new StaffOrganization();
								tmpStaffOrganization.setOrgTreeId(subOrgStaffItem.getOrgTreeId());
								tmpStaffOrganization.setOrgId(subOrgStaffItem.getOrgId());
								tmpStaffOrganization.setStaffId(subOrgStaffItem.getStaffId());
								tmpStaffOrganization.setRalaCd(StaffOrganizationConstant.STATISTIC_STAFF_RALA_CD);
								
								orgStaffs.add(tmpStaffOrganization);
								orgStaffIds.add(subOrgStaffItem.getStaffId());
							}
						}
						// 子调用中已经验证
						// Iterator<StaffOrganization> iterator = subOrgStaffs
						// .iterator();
						// while (iterator.hasNext()) {
						// StaffOrganization staffOrganization = iterator
						// .next();
						// if (staffOrgFilter.validate(staffOrganization)) {
						// orgStaffs.add(staffOrganization);
						// }
						// }
					}
				}
			}
		}

		return orgStaffs;
	}

	/**
	 * 将树中表示的组织关系和TREE_ORG_RELA表中数据做对比，生成增量发布内容
	 * @param orgTreeId
	 * @param date
	 */
	private void compareTreeOrganizationRelation(Long orgTreeId, Date date) {
		String sql = "SELECT * FROM TREE_ORG_RELA WHERE STATUS_CD = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND ORG_TREE_ID = ? order by ORG_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(orgTreeId);
		List<TreeOrgRela> list = DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, TreeOrgRela.class);
		List<TreeOrgRela> addList = new ArrayList<TreeOrgRela>();
		compareTreeNode(getRoot(), list, addList, orgTreeId);

		// 设置生、失效时间
		for (int i = 0; i < list.size(); i++) {
			TreeOrgRela item = list.get(i);
			item.setEffDate(date);
			item.setExpDate(date);
			item.setStatusDate(date);
			item.setUpdateDate(date);
		}
		for (int i = 0; i < addList.size(); i++) {
			TreeOrgRela item = addList.get(i);
			item.setEffDate(date);
			item.setStatusDate(date);
			item.setUpdateDate(date);
		}

		// 执行添加、删除
		TreeOrgRelaManager treeOrgRelaManager = (TreeOrgRelaManager) ApplicationContextUtil
				.getBean("treeOrgRelaManager");
		logger.info("删除树组织关系，数量：" + list.size());
		treeOrgRelaManager.batchRemove(list);
		logger.info("增加树组织关系，数量：" + addList.size());
		treeOrgRelaManager.batchInsert(addList);
	}

	/**
	 * 递归比对树组织关系和TreeOrgRela List,生成增加列表addList和删除列表list
	 * @param node
	 * @param list 包含TREE_ORG_RELA表数据，在递归过程中逐步删除其中的项，最后剩下的项将用于删除TREE_ORG_RELA表数据
	 * @param addList 包含对比后应新增到TREE_ORG_RELA表的数据
	 * @param orgTreeId
	 */
	private void compareTreeNode(TreeNode node, List<TreeOrgRela> list,
			List<TreeOrgRela> addList, Long orgTreeId) {
		HashMap<String, List<TreeNode>> childs = node.getChildNodes();
		Iterator<String> iteratorKey = childs.keySet().iterator();
		while (iteratorKey.hasNext()) {
			String key = iteratorKey.next();
			List<TreeNode> nodes = childs.get(key);
			for (int i = 0; i < nodes.size(); i++) {
				if (!node.getValue().equals(getRoot().getValue())) {
					boolean needAdd = true;
					Long orgId = Long.valueOf(nodes.get(i).getValue());
					Long relaOrgId = Long.valueOf(node.getValue());
					for (int j = 0; j < list.size(); j++) {
						TreeOrgRela item = list.get(j);
						if (orgId.equals(item.getOrgId())
								&& relaOrgId.equals(item.getRelaOrgId())
								&& key.equals(item.getRelaCd())) {
							list.remove(j);
							needAdd = false;
							break;
						}
					}
					if (needAdd) {
						TreeOrgRela tor = new TreeOrgRela();
						tor.setOrgTreeId(orgTreeId);
						tor.setOrgId(orgId);
						tor.setRelaOrgId(relaOrgId);
						tor.setRelaCd(key);
						addList.add(tor);
					}
				}
				compareTreeNode(nodes.get(i), list, addList, orgTreeId);
			}
		}
	}

	/**
	 * 将树中表示的组织员工关系和TREE_ORG_STAFF_RELA表中数据做对比，生成增量发布内容
	 * @param orgTreeId
	 * @param date
	 */
	private void compareTreeOrganizationStaffRelation(Long orgTreeId, Date date) {
		String sql = "SELECT * FROM TREE_ORG_STAFF_RELA WHERE STATUS_CD = ? AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND ORG_TREE_ID = ? order by ORG_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(DateUtil.getYYYYMMDDHHmmss(date));
		params.add(orgTreeId);
		List<TreeOrgStaffRela> list = DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, TreeOrgStaffRela.class);
		List<TreeOrgStaffRela> addList = new ArrayList<TreeOrgStaffRela>();

		/**
		 * <组织id,该组织的员工组织关系>
		 */
		// 比较
		HashMap<Long, Set<StaffOrganization>> orgStaffRelationMap = statisticStaff();
		Iterator<Long> keyIterator = orgStaffRelationMap.keySet().iterator();
		while (keyIterator.hasNext()) {
			Long key = keyIterator.next();
			Set<StaffOrganization> valueSet = orgStaffRelationMap.get(key);
			Iterator<StaffOrganization> staffOrgIterator = valueSet.iterator();
			while (staffOrgIterator.hasNext()) {
				boolean needAdd = true;
				StaffOrganization staffOrganization = staffOrgIterator.next();
				for (int i = 0; i < list.size(); i++) {
					TreeOrgStaffRela item = list.get(i);
					if (item.getOrgId().equals(key)
							&& item.getStaffId().equals(
									staffOrganization.getStaffId())
							&& item.getRelaCd().equals(
									staffOrganization.getRalaCd())) {
						list.remove(i);
						needAdd = false;
						break;
					}
				}
				if (needAdd) {
					TreeOrgStaffRela tosr = new TreeOrgStaffRela();
					tosr.setOrgTreeId(orgTreeId);
					tosr.setOrgId(key);
					tosr.setStaffId(staffOrganization.getStaffId());
					tosr.setRelaCd(staffOrganization.getRalaCd());
					tosr.setOriOrgId(0L);
					addList.add(tosr);
				}
			}
		}

		// 设置生、失效时间
		for (int i = 0; i < list.size(); i++) {
			TreeOrgStaffRela item = list.get(i);
			item.setEffDate(date);
			item.setExpDate(date);
			item.setStatusDate(date);
			item.setUpdateDate(date);
		}
		for (int i = 0; i < addList.size(); i++) {
			TreeOrgStaffRela item = addList.get(i);
			item.setEffDate(date);
			item.setStatusDate(date);
			item.setUpdateDate(date);
		}

		// 执行添加、删除
		TreeOrgStaffRelaManager treeOrgStaffRelaManager = (TreeOrgStaffRelaManager) ApplicationContextUtil
				.getBean("treeOrgStaffRelaManager");
		logger.info("删除树组织员工关系，数量：" + list.size());
		treeOrgStaffRelaManager.batchRemove(list);
		logger.info("增加树组织员工关系，数量：" + addList.size());
		treeOrgStaffRelaManager.batchInsert(addList);
	}

	/**
	 * 应用了过滤条件的TreeNode
	 * @author xuxs@ffcs.cn
	 *
	 */
	public class TreeNodeByFilter extends TreeNode {
		private TreeNode node;

		public TreeNodeByFilter(TreeNode node) {
			this.node = node;
		}

		@Override
		public HashMap<String, List<TreeNode>> getChildNodes() {
			HashMap<String, List<TreeNode>> rtnMap = new HashMap<String, List<TreeNode>>();
			if (node == null) {
				return rtnMap;
			}
			HashMap<String, List<TreeNode>> visibleNodes = treeFilter
					.nodesVisible(node.getChildNodes());
			Iterator<Entry<String, List<TreeNode>>> iterator = visibleNodes
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<TreeNode>> entry = iterator.next();
				List<TreeNode> listTreeNode = entry.getValue();
				List<TreeNode> listTreeNodeByFilter = new ArrayList<TreeNode>();
				for (int i = 0; i < listTreeNode.size(); i++) {
					listTreeNodeByFilter.add(new TreeNodeByFilter(listTreeNode
							.get(i)));
				}
				rtnMap.put(entry.getKey(), listTreeNodeByFilter);
			}
			return rtnMap;
		}

		@Override
		public String getValue() {
			if (node != null)
				return node.getValue();
			return null;
		}
	}
}
