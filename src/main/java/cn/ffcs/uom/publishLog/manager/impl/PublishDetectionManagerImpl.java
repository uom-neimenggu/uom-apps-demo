/**
 * 
 */
package cn.ffcs.uom.publishLog.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.publishLog.dao.PublishDetectionDao;
import cn.ffcs.uom.publishLog.manager.PublishDetectionManager;

/**
 * @author wenyaopeng
 *
 */
@Service("publishDetectionManager")
@Scope("prototype")
@SuppressWarnings("static-access")
public class PublishDetectionManagerImpl implements PublishDetectionManager{

	@Resource
	private PublishDetectionDao publishDetectionDao;
	
	 /**
	 * 查询下发范围
	 * @param arg
	 * @return
	 */
	public List queryPublishRange(Map arg){
		return publishDetectionDao.queryPublishRange(arg);
	}

}
