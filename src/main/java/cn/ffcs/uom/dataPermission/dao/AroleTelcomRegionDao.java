package cn.ffcs.uom.dataPermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;

public interface AroleTelcomRegionDao extends BaseDao {

	public List<AroleTelcomRegion> queryAroleTelcomRegionList(
			AroleTelcomRegion aroleTelcomRegion);

	public AroleTelcomRegion queryAroleTelcomRegion(
			AroleTelcomRegion aroleTelcomRegion);

}
