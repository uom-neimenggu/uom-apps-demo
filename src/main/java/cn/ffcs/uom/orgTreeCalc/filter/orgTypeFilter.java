package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.TreeNode;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;

public class orgTypeFilter extends BaseFilter {
	private List<String> orgTypes;
	private Set<Long> orgIdSet = new HashSet<Long>();
	private Logger logger = Logger.getLogger(this.getClass());

	public orgTypeFilter(List<String> orgTypes) {
		this(orgTypes, null);
	}

	public orgTypeFilter(List<String> orgTypes, Date date) {
		this.orgTypes = orgTypes;
		cacheOrgType(date);
		logger.debug("orgTypeFilter : orgTypes:" + orgTypes.toString() + " orgIdSet:" + orgIdSet.toString());
	}

	@Override
	public HashMap<String, List<TreeNode>> nodesVisible(
			HashMap<String, List<TreeNode>> nodes) {
		HashMap<String, List<TreeNode>> vibibleNodes = new HashMap<String, List<TreeNode>>();
		if (nodes != null) {
			Iterator<String> iterator = nodes.keySet().iterator();
			while (iterator.hasNext()) {
				String relaCd = iterator.next();
				List<TreeNode> list = nodes.get(relaCd);
				for (int i = 0; i < list.size(); i++) {
					try {
						Long orgId = Long.valueOf(list.get(i).getValue());
						
						if (orgIdSet.contains(orgId)) {
							List<TreeNode> vibibleList = vibibleNodes
									.get(relaCd);
							if (vibibleList == null) {
								vibibleList = new ArrayList<TreeNode>();
								vibibleNodes.put(relaCd, vibibleList);
							}
							vibibleList.add(list.get(i));
						}
					} catch (Exception e) {
					}
				}
			}
		}
		return super.nodesVisible(vibibleNodes);
	}

	private void cacheOrgType(Date date) {
		String sql = "";
		List params = new ArrayList();
		if (date == null) {
			sql += "SELECT * FROM ORG_TYPE where 1=1 and STATUS_CD = ?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		} else {
			sql += "SELECT * FROM V_ORG_TYPE where 1=1 AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		}

		List<OrgType> orgTypeList = DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, OrgType.class);
		//将组织关系类型落在orgTypes里的OrgId存放到orgIdSet
		for (int i = 0; i < orgTypeList.size(); i++) {
			if (orgTypes.contains(orgTypeList.get(i).getOrgTypeCd())) {
				orgIdSet.add(orgTypeList.get(i).getOrgId());
			}
		}
	}
}
