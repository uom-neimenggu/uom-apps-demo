package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.manager.AttrSpecManager;
import cn.ffcs.uom.systemconfig.model.AttrSpec;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Service("attrSpecManager")
@Scope("prototype")
public class AttrSpecManagerImpl implements AttrSpecManager {

	@Override
	public PageInfo queryPageInfoByClassId(Long classId, int currentPage,
			int pageSize) {
		String sql = "SELECT * FROM ATTR_SPEC  WHERE STATUS_CD=? AND CLASS_ID=?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(classId);
		return AttrSpec.repository().jdbcFindPageInfo(sql, params, currentPage,
				pageSize, AttrSpec.class);
	}

	@Override
	public void removeAttrSpec(AttrSpec attrSpec) {
		AttrSpec.repository()
				.removeObject(AttrSpec.class, attrSpec.getAttrId());

	}

	@Override
	public void saveAttrSpec(AttrSpec attrSpec) {
		Date nowDate = DateUtil.getNowDate();
		attrSpec.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		attrSpec.setStatusDate(nowDate);
		attrSpec.setCreateDate(nowDate);
		attrSpec.repository().saveObject(attrSpec);

	}

	@Override
	public void updateAttrSpec(AttrSpec attrSpec) {
		Date nowDate = DateUtil.getNowDate();
		attrSpec.setUpdateDate(nowDate);
		attrSpec.repository().updateObject(attrSpec);
	}

	@Override
	public List queryAttrSpecListByQueryAttrSpec(AttrSpec attrSpec) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("from AttrSpec where  statusCd=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (attrSpec != null && !StrUtil.isEmpty(attrSpec.getAttrCd())) {
			sb.append(" and attrCd=?");
			params.add(attrSpec.getAttrCd());
		}
		if (attrSpec != null && !StrUtil.isEmpty(attrSpec.getJavaCode())) {
			sb.append(" and javaCode=?");
			params.add(attrSpec.getJavaCode());
		}
		if (attrSpec != null && attrSpec.getClassId() != null) {
			sb.append(" and classId=?");
			params.add(attrSpec.getClassId());
		}
		return AttrSpec.repository().findListByHQLAndParams(sb.toString(),
				params);
	}
}
