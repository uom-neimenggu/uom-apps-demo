package cn.ffcs.uom.dataPermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationLevelDao;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.model.Organization;

@Service("aroleOrganizationLevelManager")
@Scope("prototype")
public class AroleOrganizationLevelManagerImpl implements
		AroleOrganizationLevelManager {

	@Resource
	private AroleOrganizationLevelDao aroleOrganizationLevelDao;

	@Override
	public PageInfo queryPageInfoByAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel, int currentPage,
			int pageSize) throws Exception {
		return aroleOrganizationLevelDao.queryPageInfoByAroleOrganizationLevel(
				aroleOrganizationLevel, currentPage, pageSize);
	}

	@Override
	public List<AroleOrganizationLevel> queryAroleOrganizationLevelList(
			AroleOrganizationLevel aroleOrganizationLevel) {
		return aroleOrganizationLevelDao
				.queryAroleOrganizationLevelList(aroleOrganizationLevel);
	}

	@Override
	public void addAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel) {
		aroleOrganizationLevelDao
				.addAroleOrganizationLevel(aroleOrganizationLevel);
	}

	@Override
	public void removeAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel) {
		aroleOrganizationLevelDao
				.removeAroleOrganizationLevel(aroleOrganizationLevel);
	}

	@Override
	public boolean aroleOrganizationLevelValid(
			AroleOrganizationLevel aroleOrganizationLevel,
			Organization organization) {

		try {

			AroleOrganizationLevel newAroleOrganizationLevel = null;
			// 获取当前用户ID
			Long userId = PlatformUtil.getCurrentUserId();

			if (!StrUtil.isNullOrEmpty(userId)) {

				// List<AroleOrganizationLevel> aroleOrganizationLevelList =
				// this
				// .queryAroleOrganizationLevelList(aroleOrganizationLevel);

				// if (aroleOrganizationLevelList != null
				// && aroleOrganizationLevelList.size() > 0) {

				// 获取当前用户所属角色
				List<Role> roleList = RoleLocalServiceUtil.getUserRoles(userId);

				if (roleList != null && roleList.size() > 0) {

					for (Role role : roleList) {

						// for (AroleOrganizationLevel oldAroleOrganizationLevel
						// : aroleOrganizationLevelList) {
						//
						// if (role.getRoleId() == oldAroleOrganizationLevel
						// .getAroleId()) {
						// newAroleOrganizationLevel =
						// oldAroleOrganizationLevel;
						// break;
						// }
						// }

						List<AroleOrganizationLevel> aroleOrganizationLevelList = null;

						aroleOrganizationLevel.setAroleId(role.getRoleId());

						aroleOrganizationLevelList = this
								.queryAroleOrganizationLevelList(aroleOrganizationLevel);

						if (aroleOrganizationLevelList != null
								&& aroleOrganizationLevelList.size() > 0) {

							if (role.getRoleId() == aroleOrganizationLevelList
									.get(0).getAroleId()) {

								newAroleOrganizationLevel = aroleOrganizationLevelList
										.get(0);
								break;
							}

						}

					}

					if (!StrUtil.isNullOrEmpty(newAroleOrganizationLevel)) {

						int orgLevel = organization
								.getOrganizationLevel(newAroleOrganizationLevel
										.getRelaCd());

						if (orgLevel >= newAroleOrganizationLevel
								.getLowerLevel()
								&& orgLevel <= newAroleOrganizationLevel
										.getHigherLevel()) {

							return true;

						}

					}

				}

				// }

			}

		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
