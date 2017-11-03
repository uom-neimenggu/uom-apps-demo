package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.dao.AttrValueDao;
import cn.ffcs.uom.systemconfig.manager.AttrValueManager;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Service("attrValueManager")
@Scope("prototype")
public class AttrValueManagerImpl implements AttrValueManager {

	@Resource
	private AttrValueDao attrValueDao;

	@Override
	public PageInfo queryPageInfoByAttrId(Long attrId, int currentPage,
			int pageSize) {
		String sql = "SELECT * FROM ATTR_VALUE  WHERE STATUS_CD=? AND ATTR_ID=?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(attrId);
		return AttrValue.repository().jdbcFindPageInfo(sql, params,
				currentPage, pageSize, AttrValue.class);
	}

	@Override
	public void removeAttrValue(AttrValue attrValue) {
		AttrValue.repository().removeObject(AttrValue.class,
				attrValue.getAttrValueId());
	}

	@Override
	public void saveAttrValue(AttrValue attrValue) {
		Date nowDate = DateUtil.getNowDate();
		attrValue.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		attrValue.setStatusDate(nowDate);
		attrValue.setCreateDate(nowDate);
		attrValue.repository().saveObject(attrValue);

	}

	@Override
	public void updateAttrValue(AttrValue attrValue) {
		Date nowDate = DateUtil.getNowDate();
		attrValue.setUpdateDate(nowDate);
		attrValue.repository().updateObject(attrValue);
	}

	@Override
	public List<AttrValue> queryAttrValueListByQueryAttrValue(
			AttrValue attrValue) {

		List params = new ArrayList();

		StringBuffer sb = new StringBuffer("from AttrValue where statusCd=?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (attrValue != null && !StrUtil.isEmpty(attrValue.getAttrValue())) {
			sb.append(" and attrValue=?");
			params.add(attrValue.getAttrValue());
		}

		if (attrValue != null && attrValue.getAttrId() != null) {
			sb.append(" and attrId=?");
			params.add(attrValue.getAttrId());
		}

		return AttrValue.repository().findListByHQLAndParams(sb.toString(),
				params);
	}

	@Override
	public List<AttrValue> queryAttrValueList(AttrValue attrValue,
			String attrValuePosition) {
		return attrValueDao.queryAttrValueList(attrValue, attrValuePosition);

	}

}
