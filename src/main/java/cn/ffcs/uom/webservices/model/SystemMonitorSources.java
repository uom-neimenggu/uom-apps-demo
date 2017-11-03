package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.dao.SystemMonitorSourcesDao;

/**
 * 短信接口信息配置表.
 * 
 * @author
 * 
 **/
public class SystemMonitorSources extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	  @Getter
	  @Setter
	  private Long sourcesId;
	  @Getter
	  @Setter
	  private String sourcesName;
	  @Getter
	  @Setter
	  private Long upSourcesId;
	  @Getter
	  @Setter
	  private String sourcesType;
	  
	  
	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static SystemMonitorSourcesDao repository() {
		return (SystemMonitorSourcesDao) ApplicationContextUtil.getBean("systemMonitorSourcesDao");
	}
}
