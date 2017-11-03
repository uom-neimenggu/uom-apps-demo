package cn.ffcs.uom.webservices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.IntfTaskInstanceDao;

/**
 *接口任务实例表实体.
 * 
 * @author
 * 
 **/
public class IntfTaskInstance extends UomEntity implements Serializable {
	/**
	 * 接口任务实例标识
	 */
	@Getter
	@Setter
	private Long intfTaskInstanceId;
	
	/**
	 *FTP任务实例标识.
	 **/
	@Getter
	@Setter
	private Long ftpTaskInstanceId;
	/**
	 *目标系统.
	 **/
	@Getter
	@Setter
	private String targetSystem;
	/**
	 *接口编码.
	 **/
	@Getter
	@Setter
	private String intfCode;
	/**
	 *请求类型.
	 **/
	@Getter
	@Setter
	private String msgType;
	/**
	 *通知结果.
	 **/
	@Getter
	@Setter
	private Long invokeResule;
	/**
	 *通知次数.
	 **/
	@Getter
	@Setter
	private Long invokeTimes;
	/**
	 *请求参数.
	 **/
	@Getter
	@Setter
	private String requestContent;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static IntfTaskInstanceDao repository() {
		return (IntfTaskInstanceDao) ApplicationContextUtil
				.getBean("intfTaskInstanceDao");
	}

	/**
	 * 是否有未通知成功的记录
	 * 
	 * @return
	 */
	public Boolean hasPrevFailEdIntfTaskInstance() {
		if (this.getIntfTaskInstanceId() != null
				&& !StrUtil.isEmpty(this.getTargetSystem())
				&& !StrUtil.isEmpty(this.getIntfCode())) {
			boolean result = false;
			String sql = "SELECT * FROM INTF_TASK_INSTANCE A WHERE A.TARGET_SYSTEM=? AND A.INTF_CODE=? AND A.INVOKE_RESULE!=? AND A.INTF_TASK_INSTANCE_ID<? ";
			List params = new ArrayList();
			params.add(this.getTargetSystem());
			params.add(this.getIntfCode());
			params.add(WsConstants.TASK_SUCCESS);
			params.add(this.getIntfTaskInstanceId());
			List<IntfTaskInstance> list = repository().jdbcFindList(sql,
					params, IntfTaskInstance.class);
			if (list != null && list.size() > 0) {
				result = true;
			}
			return result;
		}
		return null;
	}
}
