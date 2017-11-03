package cn.ffcs.uom.comparehr.manager;

import cn.ffcs.uom.comparehr.model.OperateHr;

/**
 * 人力中间表的管理接口
 * .
 * @版权：福富软件 版权所有 (c) 2013
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-8-9
 * @功能说明：
 *
 */
public interface OperateHrManager {
	
	/**
	 * 根据身份证号查找人力中间表实体
	 * @param CertNum
	 * @return
	 */
	public OperateHr queryOperateHrByCertNum(String certNum);
	
}
