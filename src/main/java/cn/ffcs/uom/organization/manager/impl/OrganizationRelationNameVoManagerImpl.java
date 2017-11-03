package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.organization.manager.OrganizationRelationNameVoManager;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.vo.OrganizationRelationNameVo;

@Service("organizationRelationNameVoManager")
@Scope("prototype")
public class OrganizationRelationNameVoManagerImpl implements
		OrganizationRelationNameVoManager {
	@Override
	public List<OrganizationRelationNameVo> getOrganizationTreePathVo(
			OrganizationRelation organizationRelation) {
		String sql = "SELECT B.ORG_NAME,A.ORG_ID,A.RELA_ORG_ID,A.ORG_REL_ID,SYS_CONNECT_BY_PATH(B.ORG_NAME,'->') AS ORG_FULL_NAME FROM ( SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ? ) A,( SELECT * FROM ORGANIZATION WHERE STATUS_CD = ? ) B WHERE A.ORG_ID = B.ORG_ID START WITH A.ORG_ID = ? CONNECT BY PRIOR A.RELA_ORG_ID = A.ORG_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(organizationRelation.getRelaCd());
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(organizationRelation.getOrgId());
		List<OrganizationRelationNameVo> list = OrganizationRelationNameVo
				.repository().jdbcFindList(sql, params,
						OrganizationRelationNameVo.class);
		return list;
	}

	@Override
	public List<String> getOrganizationTreePath(
			List<OrganizationRelation> organizationRelationList) {

		List<String> allList = new ArrayList<String>();

		for (OrganizationRelation organizationRelation : organizationRelationList) {

			List<String> list = new ArrayList<String>();
			List<String> newList = new ArrayList<String>();

			if (organizationRelation.getOrgId() == null
					&& organizationRelation.getGroupOrganization() != null) {

				allList.add(organizationRelation.getGroupOrganization()
						.getOrgFullName());

			} else {

				list = this.buildOrganizationPath(
						organizationRelation.getOrgId(),
						this.getOrganizationTreePathVo(organizationRelation),
						null);

				for (String str : list) {

					StringBuffer sb = new StringBuffer();

					String[] strArray = null;

					strArray = str.split("->");

					for (int i = strArray.length - 1; i >= 0; --i) {

						sb.append(strArray[i]);

						if (i != 0) {

							sb.append("->");

						}

					}

					newList.add(sb.toString());

				}

			}

			if (newList != null && newList.size() > 0) {
				allList.addAll(newList);
			}

		}

		return allList;
	}

	private List<String> buildOrganizationPath(Long orgId,
			List<OrganizationRelationNameVo> list, List<Integer> idx) {

		List<String> strings = new ArrayList<String>();

		if (idx == null) {

			idx = new ArrayList<Integer>();

			idx.add(0);

		}

		while (list.size() > idx.get(0)) {

			OrganizationRelationNameVo item = list.get(idx.get(0));

			if (item.getOrgId().equals(orgId)) {

				idx.set(0, idx.get(0) + 1);

				List<String> subStrings = buildOrganizationPath(
						item.getRelaOrgId(), list, idx);

				for (int i = 0; i < subStrings.size(); ++i) {

					strings.add(item.getOrgName() + "->" + subStrings.get(i));

				}

				if (strings.size() == 0) {

					strings.add(item.getOrgName());

				}

			} else {

				break;

			}

		}

		return strings;

	}
}
