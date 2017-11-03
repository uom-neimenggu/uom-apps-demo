package cn.ffcs.uom.party.manager;

/**
 * 参与人证件号规则管理
 * .
 * @版权：福富软件 版权所有 (c) 2014
 * @author 孙毅立
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-8-7
 * @功能说明：
 *
 */
public interface PartyCertificationRuleManager {
	
	/**
	 * 生成证件号
	 * @param certNumber 身份证号
	 * @param partyName 参与人姓名
	 * @return
	 */
	public String genCertNumber(String certNumber, String partyName);
	
	/**
	 * 生成证件号
	 * @param certNumber 身份证号
	 * @param staffType 帐号类别
	 * @param partyName 参与人姓名
	 * @return
	 */
	public String genCertNumber(String staffType , String certNumber, String partyName);
	
	/**
	 * 重新生成身份证号
	 * @param certNumber   格式：TMPXXXXXXXXXXXXXXX
	 * @return
	 */
	public String reGenCertNumber(String staffType , String certNumber);
	/**
	 * 验证参与人身份证是否存在
	 * @param certNumber
	 * @return
	 */
	public boolean checkCertNumberIsExist(String certNumber);
	
	/**
	 * 验证参与人身份证是否存在并且参与人同名
	 * @param certNumber 证件号
	 * @param partyNme   参与人姓名
	 * @return
	 */
	public boolean checkCertNumberAndParytNameIsExist(String certNumber,String partyNme);
	
	/**
	 * 获取下一序列值
	 * @return 
	 */
	public String getNextSeqValue(int pendingReplaceStrBit);
	
	
	/**
	 * 根据用工性质重新加载帐号类别下拉框列表
	 * @param listbox
	 * @param workProp 
	 */
	public void reloadStaffTypeListboxItems(org.zkoss.zul.Listbox listbox,String workProp);
	
	
	/**
	 * 根据参与人ID获取帐号类别
	 * @param partyId
	 * @return
	 */
	public String getStaffTypeByPartyId(Long partyId);
	
}
