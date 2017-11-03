package cn.ffcs.uom.organization.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.organization.dao.OrgAttrValueDao;
import cn.ffcs.uom.organization.manager.OrgAttrValueManager;
import cn.ffcs.uom.organization.model.OrgAttrValue;

@Service("orgAttrValueManager")
@Scope("prototype")
public class OrgAttrValueManagerImpl implements OrgAttrValueManager {
	@Resource
	private OrgAttrValueDao orgAttrValueDao;

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@Override
	public List<OrgAttrValue> queryOrgAttrValueList(OrgAttrValue orgAttrValue,
			String orgAttrValuePosition) {

		return orgAttrValueDao.queryOrgAttrValueList(orgAttrValue,
				orgAttrValuePosition);

	}

}
