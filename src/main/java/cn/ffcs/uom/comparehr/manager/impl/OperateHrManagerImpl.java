package cn.ffcs.uom.comparehr.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.comparehr.dao.OperateHrDao;
import cn.ffcs.uom.comparehr.manager.OperateHrManager;
import cn.ffcs.uom.comparehr.model.OperateHr;
/**
 * 人力中间表实现类
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-8-9
 * @功能说明：
 *
 */
@Service("operateHrManager")
@Scope("prototype")
public class OperateHrManagerImpl implements OperateHrManager {
	
	@Resource(name = "operateHrDao")
	private OperateHrDao operateHrDao;
	
	public OperateHr queryOperateHrByCertNum(String certNum){
		return operateHrDao.queryOperateHrByCertNum(certNum);
	}
	
}
