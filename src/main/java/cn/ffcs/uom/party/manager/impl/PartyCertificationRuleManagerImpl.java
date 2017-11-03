package cn.ffcs.uom.party.manager.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyCertificationRuleManager;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;

/**
 * 参与人证件号规则管理实现类 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-29
 * @功能说明：
 * 
 */
@Service("partyCertificationRuleManager")
@Scope("prototype")
public class PartyCertificationRuleManagerImpl implements
		PartyCertificationRuleManager {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "partyManager")
	private PartyManager partyManager;

	@Resource(name = "identityCardConfigManager")
	private IdentityCardConfigManager identityCardConfigManager;

	/**
	 * 生成证件号
	 * 
	 * @param certNumber
	 *            身份证号
	 * @param partyName
	 *            参与人名称
	 * @return
	 */
	@Override
	public String genCertNumber(String certNumber, String partyName) {
		if (!checkCertNumberIsExist(certNumber)) {// 参与人身份证存在
			if (checkCertNumberAndParytNameIsExist(certNumber, partyName)) {// 同名同证
				return PartyConstant.STAFF_TYPE_TMP
						+ certNumber.substring(0, 6) + getNextSeqValue(9);
			} else {// 同证不同名
				return null;
			}
		} else {
			return certNumber;
		}
	}

	/**
	 * 生成证件号
	 * 
	 * @param certNumber
	 *            身份证号
	 * @param staffType
	 *            帐号类别
	 * @param partyName
	 *            参与人姓名
	 * @return
	 */
	@Override
	public String genCertNumber(String staffType, String certNumber,
			String partyName) {
		if (PartyConstant.STAFF_TYPE_TMP.equals(staffType)) {// 正常员工
			if (!checkCertNumberIsExist(certNumber)) {// 参与人身份证存在
				if (checkCertNumberAndParytNameIsExist(certNumber, partyName)) {// 同名同证
					return staffType + certNumber.substring(0, 6)
							+ getNextSeqValue(9);
				} else {// 同证不同名
					return null;
				}
			} else {
				return certNumber;
			}
		} else {
			// 帐号类别+身份证号前6位（省份+地市+县（区））+ 序列
			return staffType + getNextSeqValue(15);
		}
	}

	/**
	 * 重新生成身份证号
	 * 
	 * @param certNumber
	 *            格式：TMPXXXXXXXXXXXXXXX
	 * @return
	 */
	public String reGenCertNumber(String staffType, String certNumber) {
		return staffType + certNumber.substring(3);
	}

	/**
	 * 验证参与人身份证是否存在
	 */
	@Override
	public boolean checkCertNumberIsExist(String certNumber) {
		return partyManager.checkIsExistCertificate(certNumber);
	}

	/**
	 * 验证参与人身份证是否存在并且参与人同名
	 */
	@Override
	public boolean checkCertNumberAndParytNameIsExist(String certNumber,
			String partyNme) {
		return partyManager.checkCertNumberAndParytNameIsExist(certNumber,
				partyNme);
	}

	/**
	 * 获取下一序列值
	 * 
	 * @return
	 */
	public String getNextSeqValue(int pendingReplaceStrBit) {
		String pendingReplace = "";
		StringBuffer pendingReplaceStr = new StringBuffer("");
		for (int i = 0; i < pendingReplaceStrBit; i++) {
			pendingReplaceStr.append("0");
		}
		String nextVal = String
				.valueOf(jdbcTemplate
						.queryForLong("SELECT SEQ_PARTY_CERTIFICATION_RULE.Nextval FROM DUAL"));

		if (nextVal.length() <= pendingReplaceStrBit) {
			pendingReplace = pendingReplaceStr.substring(0,
					(pendingReplaceStrBit - nextVal.length()));
		} else {
			new Exception("下一序列值长度超出" + pendingReplaceStrBit + "位，生成失败!");
		}
		return pendingReplace + nextVal;
	}

	/**
	 * 根据用工性质重新加载帐号类别下拉框列表
	 * 
	 * @param listbox
	 * @param workProp
	 */
	public void reloadStaffTypeListboxItems(org.zkoss.zul.Listbox listbox,
			String workProp) {
		listbox.getChildren().clear();
		ListboxUtils.rendererForEdit(listbox,
				identityCardConfigManager.getValuesList());
		listbox.setDisabled(false);
	}

	/**
	 * 根据参与人ID获取帐号类别
	 * 
	 * @param partyId
	 * @return
	 */
	public String getStaffTypeByPartyId(Long partyId) {
		String sql = "select nvl(max(s.staff_type),'"
				+ PartyConstant.STAFF_TYPE_TMP
				+ "') staff_type from staff s inner join  party_role pr on (s.party_role_id = pr.party_role_id) where pr.party_id = '"
				+ partyId + "' and pr.status_cd = 1000";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		return String.valueOf(map.get("staff_type"));
	}

}
