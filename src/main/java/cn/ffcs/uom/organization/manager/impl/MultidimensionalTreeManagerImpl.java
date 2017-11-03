package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.organization.dao.MultidimensionalTreeDao;
import cn.ffcs.uom.organization.manager.MultidimensionalTreeManager;
import cn.ffcs.uom.organization.model.MultidimensionalTree;

@Service("multidimensionalTreeManager")
@Scope("prototype")
public class MultidimensionalTreeManagerImpl implements MultidimensionalTreeManager {
	
	
	@Resource(name = "multidimensionalTreeDao")
	private MultidimensionalTreeDao multidimensionalTreeDao;
	
	/**
	 * 获取多维树配置
	 * @param orgTreeId
	 * @return
	 */
	@Override
	public MultidimensionalTree getMultidimensionalTreeConfigByOrgTreeId(Long orgTreeId) {
		StringBuffer sb = new StringBuffer("From MultidimensionalTree where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (orgTreeId != null) {
			sb.append(" and orgTreeId=?");
			params.add(orgTreeId);
		}
		sb.append(" ORDER BY defaultRela desc");
		List<MultidimensionalTree> list = multidimensionalTreeDao.findListByHQLAndParams(sb.toString(), params);
		
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
