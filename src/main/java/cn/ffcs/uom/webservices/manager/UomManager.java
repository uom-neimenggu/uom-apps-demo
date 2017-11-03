package cn.ffcs.uom.webservices.manager;

public interface UomManager {
	/**
	 * 更新员工
	 * 
	 * @param inXml
	 * @return
	 */
	public String updateStaff(String inXml) throws Exception;
	
	/**
	 * 查询员工
	 * 
	 * @param inXml
	 * @return
	 */
	public String queryStaff(String inXml) throws Exception;
}
