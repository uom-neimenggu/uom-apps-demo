package cn.ffcs.uom.webservices.services;

import org.apache.log4j.Logger;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.webservices.util.WsClientUtil;

public class HelloWorldService {

	private Logger logger = Logger.getLogger(this.getClass());

	private BuildFileSqlManager buildFileSqlManager = (BuildFileSqlManager) ApplicationContextUtil
			.getBean("buildFileSqlManager");

	private int requestCount = 0;

	public String sayHello(String name) {
		logger.info("requestCount->" + requestCount);
		logger.info("sayHello->" + name);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Hello" + name;
	}

	public Float add(Float a, Float b) {
		requestCount++;
		Float result = a + b;
		logger.info("requestCount->" + requestCount);
		logger.info("a->" + a + ",b->" + b);
		logger.info("result->" + result);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成数据
	 * 
	 * @param treeId
	 * @param lastDate
	 * @param thisDate
	 * @param syncType
	 */
	public void createIncrementalData(Long treeId, String lastDate,
			String thisDate, String syncType, String username, String password) {
		try {
			if ("UOM".equals(username) && "UOMPWD".equals(password)) {
				logger.info("treeId->" + treeId);
				logger.info("lastDate->" + lastDate);
				logger.info("thisDate->" + thisDate);
				logger.info("syncType->" + syncType);
				logger.info("lastDate->"
						+ DateUtil.str2date(lastDate, "yyyy-MM-dd hh:mm:ss"));
				logger.info("thisDate->"
						+ DateUtil.str2date(thisDate, "yyyy-MM-dd hh:mm:ss"));
				buildFileSqlManager.createLocalFtpFiles(treeId,
						DateUtil.str2date(lastDate, "yyyy-MM-dd hh:mm:ss"),
						DateUtil.str2date(thisDate, "yyyy-MM-dd hh:mm:ss"),
						syncType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param inXml
	 * @param callUrl
	 * @param method
	 * @param paramName
	 * @return
	 */
	public String callWsIntf(String inXml, String callUrl, String method,
			String paramName) {
		try {
			if (StrUtil.isEmpty(paramName)) {
				paramName = "arg0";
			}
			return WsClientUtil.wsCall(inXml, callUrl, method, paramName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
