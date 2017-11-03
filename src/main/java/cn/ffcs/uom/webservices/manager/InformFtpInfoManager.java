package cn.ffcs.uom.webservices.manager;

import cn.ffcs.uom.webservices.model.IntfTaskInstance;

/**
 * ftp通知
 * 
 * @author ZhaoF
 * 
 */
public interface InformFtpInfoManager {

	/**
	 * 通知ftp信息
	 * 
	 * @param inXml
	 * @return
	 */
	public cn.ffcs.uom.webservices.bean.ftpinform.Root informFtpInfo(
			IntfTaskInstance intfTaskInstance);
}
