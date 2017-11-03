package cn.ffcs.uac.staff.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uac.staff.constant.EnumUacStaffInfo;
import cn.ffcs.uac.staff.dao.RemindInforDao;
import cn.ffcs.uac.staff.dao.UacAttachedInfoDao;
import cn.ffcs.uac.staff.dao.UacCertDao;
import cn.ffcs.uac.staff.dao.UacContactDao;
import cn.ffcs.uac.staff.dao.UacExpandInfoDao;
import cn.ffcs.uac.staff.dao.UacStaffDao;
import cn.ffcs.uac.staff.manager.UacStaffManager;
import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.dao.GroupDao;
import cn.ffcs.uom.group.model.Group;

/**
 * @Title:UacStaffManagerImpl
 * @Description:
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年9月1日上午9:45:11
 */
@Service("uacStaffManager")
@Scope("prototype")
@Transactional
public class UacStaffManagerImpl implements UacStaffManager {
	@Resource
	private UacStaffDao uacStaffDao;
	@Resource
	private UacCertDao uacCertDao;
	@Resource
	private UacContactDao uacContactDao;
	@Resource
	private UacAttachedInfoDao uacAttachedInfoDao;
	@Resource
	private UacExpandInfoDao uacExpandInfoDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private RemindInforDao RemindInforDao;

	@Override
	public String addUacStaffAllInfo(UacStaff uacStaff) {
		String info = null;

		UacCert uacCert = uacStaff.getUacCert();
		UacContact uacContact = uacStaff.getUacContact();

		// 根据规则生成必须值
		uacStaff.setUuid(StrUtil.getUUID());
		uacStaff.setEcode(generateEcode());
		// 生成账号，检查账号是否生成成功
		EnumUacStaffInfo returnEnum = generateAccount(uacStaff.getStaffName(),
				uacStaff.getProperty(), uacCert.getCertNumber());
		boolean isAccSuccess = Boolean.parseBoolean(returnEnum.name());

		if (isAccSuccess) {
			uacStaff.setAccount(returnEnum.getValue());
		} else {
			info = returnEnum.getValue();
			return info;
		}

		Group group = getGroup(uacStaff.getStaffName(), uacCert.getCertNumber());
		if (group != null) {
			uacStaff.setGuid(group.getUniCode());
			uacStaff.setHrCode(group.getCtHrUserCode());
		}

		// 新增UacCert,如果有,用已经存在的cert，没有则新增
		if (uacCertDao.queryUacCert(uacCert) != null) {
			uacCert = uacCertDao.queryUacCert(uacCert);
		} else {
			if (EnumUacStaffInfo.CERT_TYPE_IDCARD.getValue().equals(
					uacCert.getCertType())) {
				uacCert.setIsReal(EnumUacStaffInfo.REAL_NAME_YES.getValue());
			} else {
				uacCert.setIsReal(EnumUacStaffInfo.REAL_NAME_NO.getValue());
			}

			uacCert.addOnly();
		}

		uacStaff.setCertId(uacCert.getId());

		// 新增uacContact,如果有,用已经存在的uacContact，没有则新增
		if (uacContactDao.queryUacContact(uacContact) != null) {
			uacContact = uacContactDao.queryUacContact(uacContact);
		} else {
			uacContact.addOnly();
		}

		uacStaff.setContactId(uacContact.getId());

		uacStaff.addOnly();

		UacAttachedInfo uacAttachedInfo = uacStaff.getUacAttachedInfo();
		uacAttachedInfo.setStaffId(uacStaff.getId());
		uacAttachedInfo.addOnly();

		return info;
	}

	/**
	 * 根据人员姓名，性质以及身份证号码生成账号
	 * 
	 * @param staffName
	 * @param property
	 * @param certNumber
	 * @return EnumUacStaffInfo(TRUE(value) FALSE(value))
	 */
	@Override
	public EnumUacStaffInfo generateAccount(String staffName, String property,
			String certNumber) {
		EnumUacStaffInfo uacStaffEnum = EnumUacStaffInfo.FALSE;
		String result = "";

		UacStaff queryUacStaff = UacStaff.newInstance();

		Group group = getGroup(staffName, certNumber);

		if (group != null) {
			String staffNbr = group.getCtHrUserCode();

			if (!StrUtil.isNullOrEmpty(staffNbr)) {
				staffNbr = staffNbr.trim();

				if (staffNbr.startsWith("34")) {
					staffNbr = staffNbr.substring(2, staffNbr.length());
				} else if (staffNbr.startsWith("W34")
						|| staffNbr.startsWith("w34")) {
					staffNbr = "W9" + staffNbr.substring(3, staffNbr.length());
				}

				queryUacStaff.setAccount(staffNbr);

				if (uacStaffDao.queryUacStaffList(queryUacStaff).size() > 0) {
					result = "侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在";
					uacStaffEnum.setValue(result);
					return uacStaffEnum;
				} else {
					result = staffNbr;
					uacStaffEnum = EnumUacStaffInfo.TRUE;
					uacStaffEnum.setValue(result);
					return uacStaffEnum;
				}
			} else {
				result = "人力中间表对应的工号为空";
				uacStaffEnum.setValue(result);
				return uacStaffEnum;
			}
		} else {
			result = genneratePropertyAccNumber(property);
			queryUacStaff.setAccount(result);
			if (uacStaffDao.queryUacStaffList(queryUacStaff).size() > 0) {
				result = "规则生成账号" + queryUacStaff.getAccount() + "在主数据中已经存在";
				uacStaffEnum.setValue(result);
				return uacStaffEnum;
			} else {
				uacStaffEnum = EnumUacStaffInfo.TRUE;
				uacStaffEnum.setValue(result);
				return uacStaffEnum;
			}
		}
	}

	/**
	 * 查询Group对象
	 * 
	 * @param staffName
	 * @param certNumber
	 * @return
	 */
	@Override
	public Group getGroup(String staffName, String certNumber) {
		Group group = new Group();
		group.setUserName(staffName);
		group.setCtIdentityNumber(certNumber);

		List<Group> groupList = groupDao.queryGroupList(group);

		if (groupList != null && groupList.size() > 0) {
			return groupList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 内部使用方法生成AccNumber
	 * 
	 * @param property
	 * @return
	 */
	@Override
	public String genneratePropertyAccNumber(String property) {
		if (!StrUtil.isNullOrEmpty(property)) {
			String staffSeq = uacStaffDao.getSeqStaffNbr();
			if (!StrUtil.isNullOrEmpty(staffSeq)) {
				if (EnumUacStaffInfo.WORKPROP_W_AGENT.getValue().equals(
						property)
						|| EnumUacStaffInfo.WORKPROP_W_OTHER.getValue().equals(
								property)
						|| EnumUacStaffInfo.WORKPROP_W_PROVIDER.getValue()
								.equals(property)) {
					return "H8" + staffSeq;
				} else {
					return "L7" + staffSeq;
				}
			}
		}
		return null;
	}

	@Override
	public EnumUacStaffInfo delUacStaffAllInfo(UacStaff uacStaff) {
			if (uacStaff.getUacAttachedInfo() != null) {
				uacAttachedInfoDao.delUacAttachedInfo(uacStaff
						.getUacAttachedInfo());
			}

			if (uacStaff.getUacExpandInfoList() != null
					&& !uacStaff.getUacExpandInfoList().isEmpty()) {
				uacExpandInfoDao.delUacExpandInfoList(uacStaff
						.getUacExpandInfoList());
			}
			
			uacStaff.removeOnly();

			return EnumUacStaffInfo.OPERATE_SUCCESS;
	}

	@Override
	public void updateUacStaffAllInfo(UacStaff uacStaff) {
		UacCert uacCert = uacStaff.getUacCert();
		UacContact uacContact = uacStaff.getUacContact();
		UacAttachedInfo uacAttachedInfo = uacStaff.getUacAttachedInfo();
		
		Group group = getGroup(uacStaff.getStaffName(), uacCert.getCertNumber());
		if (group != null) {
			uacStaff.setGuid(group.getUniCode());
			uacStaff.setHrCode(group.getCtHrUserCode());
		}
		
		uacStaff.updateOnly();
		uacCert.updateOnly();
		uacContact.updateOnly();
		uacAttachedInfo.updateOnly();
	}

	@Override
	public PageInfo queryUacStaffPage(UacStaff uacStaff, int currentPage,
			int pageSize) {
		return uacStaffDao.queryUacStaffPage(uacStaff, currentPage, pageSize);
	}
	@Override
	public PageInfo queryDemoStaffPage(DemoStaff demoStaff, int currentPage,
			int pageSize) {
		return uacStaffDao.queryDemoStaffPage(demoStaff, currentPage, pageSize);
	}
	
	/**
	 * 自动生成人员编码
	 * @return
	 */
	@Override
	public String generateEcode() {
		String staffCd = uacStaffDao.getSeqEcode();
		if (!StrUtil.isNullOrEmpty(staffCd)) {
			int legh = 8 - staffCd.length();
			if (legh >= 0) {
				String px = "85";
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < legh; i++) {
					sb.append("0");
				}
				sb.append(staffCd);
				return px + sb.toString();
			}
		}
		return null;
	}

	@Override
	public void saveRemindInfo(String msg) {
		// TODO Auto-generated method stub
		RemindInforDao.saveRemindInfo(msg);
	}
}
