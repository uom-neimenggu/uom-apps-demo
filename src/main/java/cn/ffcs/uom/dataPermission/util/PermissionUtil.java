package cn.ffcs.uom.dataPermission.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class PermissionUtil {

	/**
	 * 根据角色id获取角色电信管理区域标识
	 * 
	 * @param aroleId
	 */
	public static Long getPermissionTelcomRegionId(long[] aroleIdList) {
		List<AroleTelcomRegion> list = getAroleTelcomRegionList(aroleIdList);
		if (list != null && list.size() > 0) {
			AroleTelcomRegion aroleTelcomRegion = list.get(0);
			if (aroleTelcomRegion != null) {
				return aroleTelcomRegion.getTelcomRegionId();
			}
		}
		return null;
	}

	/**
	 * 根据角色id获取角色电信管理区域标识
	 * 
	 * @param aroleId
	 */
	public static Organization getPermissionOrganization(long[] aroleIdList) {
		List<AroleOrganization> list = getAroleOrganizationList(aroleIdList);
		if (list != null && list.size() > 0) {
			AroleOrganization aroleOrganization = list.get(0);
			if (aroleOrganization != null) {
				return aroleOrganization.getOrganization();
			}
		}
		return null;
	}
	
	/**
	 * 根据角色id获取角色组织列表
	 * 
	 * @param aroleId
	 */
	public static List<Organization> getPermissionOrganizationList(
			long[] aroleIdList) {
		List<AroleOrganization> list = getAroleOrganizationList(aroleIdList);
		List<Organization> orgList = new ArrayList();
		if (list != null && list.size() > 0) {
			for (AroleOrganization aroleOrganization : list) {
				if (aroleOrganization != null) {
					Organization org = aroleOrganization.getOrganization();
					if (org != null) {
						orgList.add(org);
					}
				}
			}
		}
		return orgList;
	}

	/**
	 * 根据角色id获取角色电信管理区域标识
	 * 
	 * @param aroleId
	 */
	public static TelcomRegion getPermissionTelcomRegion(long[] aroleIdList) {
		List<AroleTelcomRegion> list = getAroleTelcomRegionList(aroleIdList);
		if (list != null && list.size() > 0) {
			AroleTelcomRegion aroleTelcomRegion = list.get(0);
			if (aroleTelcomRegion != null) {
				return aroleTelcomRegion.getTelcomRegion();
			}
		}
		return null;
	}
	
	/**
	 * 根据角色id获取角色电信管理区域列表
	 * 
	 * @param aroleId
	 */
	private static List<AroleTelcomRegion> getAroleTelcomRegionList(
			long[] aroleIdList) {
		HashMap<Long, Set<AroleTelcomRegion>> roleTelcomRegionSet = getRoleTelcomRegionSet();
		List list = new ArrayList();
		if (aroleIdList != null) {
			for (Long aroleId : aroleIdList) {
				Set set = roleTelcomRegionSet.get(aroleId);
				if (set != null) {
					Iterator it = set.iterator();
					while (it.hasNext()) {
						AroleTelcomRegion aroleTelcomRegion = (AroleTelcomRegion) it
								.next();
						if (aroleTelcomRegion != null
								&& aroleTelcomRegion.getTelcomRegionId() != null) {
							list.add(aroleTelcomRegion);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 根据角色id获取角色组织
	 * 
	 * @param aroleIdArray
	 * @return
	 */
	public static Long getPermissionOrganizationId(long[] aroleIdArray) {
		List<AroleOrganization> list = getAroleOrganizationList(aroleIdArray);
		if (list != null && list.size() > 0) {
			AroleOrganization aroleOrganization = list.get(0);
			if (aroleOrganization != null) {
				return aroleOrganization.getOrgId();
			}
		}
		return null;
	}

	/**
	 * 根据角色id获取角色组织id
	 * 
	 * @param aroleIdArray
	 *            ：角色id列表
	 * @param rootOrgId
	 *            ：根组织id（直接挂在0下面的组织）
	 * @return
	 */
	public static Long getPermissionOrganizationId(long[] aroleIdArray,
			Long rootOrgId) {
		List<AroleOrganization> list = PermissionUtil
				.getPermissionOrganizationList(aroleIdArray, rootOrgId);
		if (list != null && list.size() > 0) {
			AroleOrganization ao = list.get(0);
			if (ao != null && ao.getOrgId() != null) {
				return ao.getOrgId();
			}
		}
		return null;
	}
	
	/**
	 * 根据角色id获取角色组织列表
	 * 
	 * @param aroleIdArray
	 * @param rootOrgId
	 * @return
	 */
	public static List<AroleOrganization> getPermissionOrganizationList(
			long[] aroleIdArray, Long rootOrgId) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM AROLE_ORGANIZATION oot WHERE STATUS_CD = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleIdArray != null && aroleIdArray.length > 0) {
			sb.append(" AND AROLE_ID IN(");
			for (int i = 0; i < aroleIdArray.length; i++) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append(",?");
				}
				params.add(aroleIdArray[i]);
			}
			sb.append(")");
			if (rootOrgId != null) {
				sb
						.append(" AND ORG_ID IN (SELECT ORG_ID " +
                   " FROM (select a.org_id, a.rela_org_id  "
                   + "from organization_relation a "
                   + " where a.status_cd = ? and a.rela_cd in "
                   + "(select t.rela_cd from organization_relation t where t.org_id = ?)) A "
                   + "START WITH A.ORG_ID = ? CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID) order by oot.org_id asc");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(rootOrgId);
				params.add(rootOrgId);
				return DefaultDaoFactory.getDefaultDao().jdbcFindList(
						sb.toString(), params, AroleOrganization.class);
			}
		}
		return null;
	}

	/**
	 * 根据角色id获取角色组织列表
	 * 
	 * @param aroleId
	 */
	private static List<AroleOrganization> getAroleOrganizationList(
			long[] aroleIdArray) {
		HashMap<Long, Set<AroleOrganization>> roleOrganizationCacheSet = getRoleOrganizationSet();
		List list = new ArrayList();
		if (aroleIdArray != null) {
			for (Long aroleId : aroleIdArray) {
				Set set = roleOrganizationCacheSet.get(aroleId);
				if (set != null) {
					Iterator it = set.iterator();
					while (it.hasNext()) {
						AroleOrganization aroleOrganization = (AroleOrganization) it
								.next();
						if (aroleOrganization != null
								&& aroleOrganization.getOrgId() != null) {
							list.add(aroleOrganization);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取角色电信管理区域
	 */
	private static HashMap<Long, Set<AroleTelcomRegion>> getRoleTelcomRegionSet() {
	    HashMap<Long, Set<AroleTelcomRegion>> roleTelcomRegionSet = new HashMap<Long, Set<AroleTelcomRegion>>();
        String sql = "select t1.* from AROLE_TELCOM_REGION t1 where t1.status_cd = 1000 and t1.arole_id in (SELECT DISTINCT AROLE_ID FROM AROLE_TELCOM_REGION WHERE STATUS_CD = ?)";
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        
        List<AroleTelcomRegion> list = AroleTelcomRegion.repository().jdbcFindList(sql, params, AroleTelcomRegion.class);
        //首先统计有多少个id号
        Set<Long> idNum = new HashSet<Long>();
        for(AroleTelcomRegion atr : list)
        {
            idNum.add(atr.getAroleId());
        }
        
        //遍历list，吧map的set对象先创建出来
        for(Long aroleid : idNum)
        {
            roleTelcomRegionSet.put(aroleid, new HashSet<AroleTelcomRegion>());
        }
        
        //吧数据放进去
        for(AroleTelcomRegion atr : list)
        {
            roleTelcomRegionSet.get(atr.getAroleId()).add(atr);
        }
        
//      List<AroleTelcomRegion> list = AroleTelcomRegion.repository()
//              .jdbcFindList(sql, params, AroleTelcomRegion.class);
//
//      if (list != null && list.size() > 0) {
//          for (AroleTelcomRegion at : list) {
//              if (at != null && at.getAroleId() != null) {
//                  sql = "SELECT * FROM AROLE_TELCOM_REGION WHERE STATUS_CD = ? AND AROLE_ID = ?";
//                  params = new ArrayList();
//                  params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
//                  params.add(at.getAroleId());
//                  List<AroleTelcomRegion> atList = DefaultDaoFactory
//                          .getDefaultDao().jdbcFindList(sql, params,
//                                  AroleTelcomRegion.class);
//                  if (atList != null && atList.size() > 0) {
//                      Set<AroleTelcomRegion> aroleTelcomRegions = new HashSet<AroleTelcomRegion>();
//                      for (AroleTelcomRegion temp : atList) {
//                          aroleTelcomRegions.add(temp);
//                      }
//                      roleTelcomRegionSet.put(at.getAroleId(),
//                              aroleTelcomRegions);
//                  }
//              }
//          }
//      }
        return roleTelcomRegionSet;
	}

	/**
	 * 获取角色组织
	 */
	private static HashMap<Long, Set<AroleOrganization>> getRoleOrganizationSet() {
		HashMap<Long, Set<AroleOrganization>> roleOrganizationSet = new HashMap<Long, Set<AroleOrganization>>();
		/**
		 * 查询角色id列表
		 */
		String sql = "SELECT DISTINCT AROLE_ID FROM AROLE_ORGANIZATION WHERE STATUS_CD = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<AroleOrganization> list = AroleOrganization.repository()
				.jdbcFindList(sql, params, AroleOrganization.class);

		if (list != null && list.size() > 0) {
			for (AroleOrganization ao : list) {
				if (ao != null && ao.getAroleId() != null) {
					sql = "SELECT * FROM AROLE_ORGANIZATION WHERE STATUS_CD = ? AND AROLE_ID = ?";
					params = new ArrayList();
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(ao.getAroleId());
					List<AroleOrganization> orList = DefaultDaoFactory
							.getDefaultDao().jdbcFindList(sql, params,
									AroleOrganization.class);
					if (orList != null && orList.size() > 0) {
						Set<AroleOrganization> organizations = new HashSet<AroleOrganization>();
						for (AroleOrganization temp : orList) {
							organizations.add(temp);
						}
						roleOrganizationSet.put(ao.getAroleId(), organizations);
					}
				}
			}
		}
		return roleOrganizationSet;
	}
}
