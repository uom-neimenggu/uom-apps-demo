package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.model.FilterConfig;

@Service("filterConfigManager")
@Scope("prototype")
public class FilterConfigManagerImpl implements FilterConfigManager {

	@Override
	public PageInfo queryPageInfoByQuertFilterConfig(FilterConfig queryFilterConfig,
			int currentPage, int pageSize) {
		StringBuffer hql = new StringBuffer("From FilterConfig where 1=1 ");
		//StringBuffer hql = new StringBuffer("From FilterConfig where status_cd = ? ");
		List params = new ArrayList();
		//params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryFilterConfig != null) {
		}
		hql.append(" order by systemFilterConfigId");
		return queryFilterConfig.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}
	/**
	 * 获取所有生效的号码段
	 * @return
	 */
	public List<FilterConfig> findAllByActive(){
		StringBuffer hql = new StringBuffer("select * from SYSTEM_FILTER_CHAR_CONFIG where status_cd = ? ");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(hql.toString(), params,FilterConfig.class);
	}
	/**
	 * 过滤所有生效的配置信息
	 * @return
	 */
	public String filterAllByActive(String content){
//		StringBuffer sb = new StringBuffer();
		List<FilterConfig> list = findAllByActive();
		for(FilterConfig fc : list){
			content = content.replaceAll(fc.getFilterChar(), "");
			/*if(content.indexOf(fc.getFilterChar())!=-1){
				String[] contentArray = content.split(fc.getFilterChar());
				for(int i = 0;i<contentArray.length;i++)
				sb.append(contentArray[i]);
				content = sb.toString();
				sb.setLength(0);
			}*/
		}
		return content;
	}
	
	@Override
	public void removeFilterConfig(FilterConfig filterConfig) {
		filterConfig.repository().removeObject(FilterConfig.class,
				filterConfig.getSystemFilterConfigId());
	}

	@Override
	public void updateFilterConfig(FilterConfig filterConfig) {
		Date nowDate = DateUtil.getNowDate();
		filterConfig.setUpdateDate(nowDate);
		filterConfig.repository().updateObject(filterConfig);
	}

	@Override
	public void saveFilterConfig(FilterConfig filterConfig) {
		Date nowDate = DateUtil.getNowDate();
		filterConfig.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		filterConfig.setStatusDate(nowDate);
		filterConfig.setCreateDate(nowDate);
		filterConfig.repository().saveObject(filterConfig);
	}
}
