package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.organization.model.MultidimensionalTree;

public interface MultidimensionalTreeManager {
	
	/**
	 * 获取多维树配置
	 * @param orgTreeId
	 * @return
	 */
	public MultidimensionalTree getMultidimensionalTreeConfigByOrgTreeId(Long orgTreeId);

	
}
