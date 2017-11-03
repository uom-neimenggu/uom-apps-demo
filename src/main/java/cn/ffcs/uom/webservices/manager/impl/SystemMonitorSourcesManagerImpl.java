package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.webservices.dao.SystemMonitorSourcesDao;
import cn.ffcs.uom.webservices.manager.SystemMonitorSourcesManager;
import cn.ffcs.uom.webservices.model.SystemMonitorSources;

@Service("systemMonitorSourcesManager")
@Scope("prototype")
public class SystemMonitorSourcesManagerImpl implements SystemMonitorSourcesManager {

	@Resource
	private SystemMonitorSourcesDao systemMonitorSourcesDao;

	/**
	 * 方法功能: 根据属性名称， 获取属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	@Override
	public List<NodeVo> getValuesListByType(String type,String eventName) {
		
		List<SystemMonitorSources> list = null;
		Long sourcesId = null;
		
		if(!StrUtil.isEmpty(eventName)){
			sourcesId = getSourcesIdByEventName(eventName);
		}
		
		list = querySystemMonitorSourcesList(type,sourcesId);
		List<NodeVo> ret = new ArrayList();
		for (SystemMonitorSources sms : list) {
			if (sms != null) {
				NodeVo vo = new NodeVo();
				vo.setId(sms.getSourcesName());
				vo.setName(sms.getSourcesName());
				vo.setDesc(sms.getSourcesName());
				ret.add(vo);
			}
		}
		return ret;
	}
	
	
	public List<SystemMonitorSources> querySystemMonitorSourcesList(String type,Long upSourcesId) {
		StringBuffer hql = new StringBuffer("From SystemMonitorSources where statusCd = ? ");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		
		if(!StrUtil.isEmpty(type)){
			hql.append("and sourcesType = ? ");
			params.add(type);
		}
		if(upSourcesId!=null){
			hql.append("and upSourcesId = ? ");
			params.add(upSourcesId);
		}

		hql.append(" order by sourcesId desc");

		return SystemMonitorSources.repository().findListByHQLAndParams(hql.toString(), params);
	}

	
	public Long getSourcesIdByEventName(String eventName) {
		StringBuffer hql = new StringBuffer("From SystemMonitorSources where sourcesName = ?");
		List params = new ArrayList();
		params.add(eventName);

		hql.append(" order by sourcesId desc");
		List<SystemMonitorSources> list = SystemMonitorSources.repository().findListByHQLAndParams(hql.toString(), params);
		if(list!=null){
			SystemMonitorSources sms = (SystemMonitorSources)list.get(0);
			if(sms!=null&& sms.getSourcesId()!=null)
			return sms.getSourcesId();
		}
		return null;
	}
}
