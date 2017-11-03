package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.manager.NumberSegmentManager;
import cn.ffcs.uom.systemconfig.model.NumberSegment;

@Service("numberSegmentManager")
@Scope("prototype")
public class NumberSegmentManagerImpl implements NumberSegmentManager {

	@Override
	public PageInfo queryPageInfoByQuertNumberSegment(NumberSegment queryNumberSegment,
			int currentPage, int pageSize) {
		StringBuffer hql = new StringBuffer("From NumberSegment where 1=1 ");
		//StringBuffer hql = new StringBuffer("From NumberSegment where status_cd = ? ");
		List params = new ArrayList();
		//params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryNumberSegment != null) {
			if (!StrUtil.isEmpty(queryNumberSegment.getNumberSegment())) {
				hql.append(" and numberSegment=?");
				params.add(StringEscapeUtils.escapeSql(queryNumberSegment.getNumberSegment()));
			}
		}
		hql.append(" order by systemNumberSegmentId");
		return queryNumberSegment.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}
	/**
	 * 获取所有生效的号码段
	 * @return
	 */
	public List<NumberSegment> findAllByActive(){
		StringBuffer hql = new StringBuffer("select * from SYSTEM_NUMBER_SEGMENT_CONFIG where status_cd = ? ");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(hql.toString(), params,NumberSegment.class);
	}
	
	@Override
	public void removeNumberSegment(NumberSegment numberSegment) {
		numberSegment.repository().removeObject(NumberSegment.class,
				numberSegment.getSystemNumberSegmentId());
	}

	@Override
	public void updateNumberSegment(NumberSegment numberSegment) {
		Date nowDate = DateUtil.getNowDate();
		numberSegment.setUpdateDate(nowDate);
		numberSegment.repository().updateObject(numberSegment);
	}

	@Override
	public void saveNumberSegment(NumberSegment numberSegment) {
		Date nowDate = DateUtil.getNowDate();
		numberSegment.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		numberSegment.setStatusDate(nowDate);
		numberSegment.setCreateDate(nowDate);
		numberSegment.repository().saveObject(numberSegment);
	}
}
