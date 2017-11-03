package cn.ffcs.uom.dataPermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.dataPermission.dao.AroleTelcomRegionDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class AroleTelcomRegion extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限角色电信管理区域标识
	 */
	@Getter
	@Setter
	private Long aroleTelcomRegionId;

	/**
	 * 权限角色标识
	 */
	@Getter
	@Setter
	private Long aroleId;

	/**
	 * 电信管理区域标识
	 */
	@Getter
	@Setter
	private Long telcomRegionId;

	/**
	 * 包含下级标志 0.不含；1.含
	 */
	@Getter
	@Setter
	private Long flag;

	public static AroleTelcomRegionDao repository() {
		return (AroleTelcomRegionDao) ApplicationContextUtil
				.getBean("aroleTelcomRegionDao");
	}

	/**
	 * 获取电信管理区域
	 * 
	 * @return
	 */
	public TelcomRegion getTelcomRegion() {
		if (this.telcomRegionId != null) {
			String sql = "from TelcomRegion where statusCd=? and telcomRegionId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.telcomRegionId);
			List<TelcomRegion> list = repository().findListByHQLAndParams(sql,
					params);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
	

}
