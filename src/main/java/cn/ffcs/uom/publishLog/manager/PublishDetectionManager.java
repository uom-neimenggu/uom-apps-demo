/**
 * 
 */
package cn.ffcs.uom.publishLog.manager;

import java.util.List;
import java.util.Map;

/**
 * @author wenyaopeng
 *
 */
public interface PublishDetectionManager {

	/**
	 * 查询下发范围
	 * @param arg
	 * @return
	 */
	public List queryPublishRange(Map arg);

}
