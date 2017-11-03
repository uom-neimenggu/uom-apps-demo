package cn.ffcs.uom.dataPermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;

public interface AroleOrganizationDao extends BaseDao {

	public List<AroleOrganization> queryAroleOrganizationList(
			AroleOrganization aroleOrganization);

	public AroleOrganization queryAroleOrganization(
			AroleOrganization aroleOrganization);

}
