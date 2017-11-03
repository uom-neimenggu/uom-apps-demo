package cn.ffcs.uom.audit.manager.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.audit.manager.OrgAuditManager;
import cn.ffcs.uom.audit.manager.SendMailManager;
import cn.ffcs.uom.audit.manager.StaffAuditManager;
import cn.ffcs.uom.audit.model.Mail;
import cn.ffcs.uom.audit.model.OrgAudit;
import cn.ffcs.uom.audit.model.StaffAudit;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.ExcelUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.GetPath;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;
import cn.ffcs.uom.webservices.model.SystemMessageLog;

@Component("sendMailManager")
public class SendMailManagerImpl implements SendMailManager {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "orgAuditManager")
	private OrgAuditManager orgAuditManager;

	@Resource(name = "staffAuditManager")
	private StaffAuditManager staffAuditManager;

	@Resource(name = "messageConfigManager")
	private MessageConfigManager messageConfigManager;

	@Resource(name = "systemMessageLogManager")
	private SystemMessageLogManager systemMessageLogManager;

	/**
	 * 短信日志
	 */
	private SystemMessageLog systemMessageLog;

	public void sendMail() {

		ApplicationContext ctx = null;
		String[] toAddress = null;
		systemMessageLog = new SystemMessageLog();
		systemMessageLog.setSystemCode(WsConstants.SYSTEM_CODE_UOM_EMAIL);
		systemMessageLog.setCreateDate(new Date());

		// 获取上下文
		ctx = new ClassPathXmlApplicationContext("application-context-mail.xml");
		JavaMailSender sender = (JavaMailSender) ctx.getBean("javaMailSender");
		Mail mail = (Mail) ctx.getBean("mail");

		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		MimeMessage mailMessage = senderImpl.createMimeMessage();

		try {

			List<SystemConfigUser> systemMessageConfigList = querySystemMessageConfigList();
			if (systemMessageConfigList != null
					&& systemMessageConfigList.size() > 0) {

				toAddress = new String[systemMessageConfigList.size()];

				for (int i = 0; i < systemMessageConfigList.size(); i++) {
					toAddress[i] = systemMessageConfigList.get(i)
							.getEmailAddress();
				}

				// 设置UTF-8或GBK编码，否则邮件会有乱码
				MimeMessageHelper messageHelper = new MimeMessageHelper(
						mailMessage, true, "UTF-8");
				messageHelper.setFrom(mail.getFrom(), mail.getDisplayName());// 发送者
				messageHelper.setTo(toAddress);// 接受者
				messageHelper.setSubject(DateUtil.dateToStr(new Date(),
						"yyyy年MM月dd日") + mail.getSubject());// 主题
				messageHelper.setText(mail.getContent());// 邮件内容

				// 附件内容
				// messageHelper.addInline("a", new File("E:/xiezi.jpg"));
				// messageHelper.addInline("b", new File("E:/logo.png"));

				String orgFileRealPath = onExportOrgStatisticsBill();
				logger.info("orgFileRealPath "
						+ DateUtil.convertDateTimeToString(new Date())
						+ " ==>>" + orgFileRealPath);
				if (!StrUtil.isEmpty(orgFileRealPath)) {
					File file = new File(orgFileRealPath);
					// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
					messageHelper.addAttachment(MimeUtility
							.encodeWord("组织稽核统计清单"
									+ DateUtil
											.dateToStr(new Date(), "yyyyMMdd")
									+ ".xls"), file);
					// messageHelper.addAttachment(
					// MimeUtility.encodeWord(file.getName()), file);
				} else {
					systemMessageLog.setResult(0L);
					systemMessageLog.setSystemMessageInfo("没有生成组织稽核统计清单");
					systemMessageLogManager
							.saveSystemMessageLog(systemMessageLog);
				}

				String staffFileRealPath = onExportStaffStatisticsBill();
				logger.info("staffFileRealPath "
						+ DateUtil.convertDateTimeToString(new Date())
						+ " ==>>" + staffFileRealPath);
				if (!StrUtil.isEmpty(staffFileRealPath)) {
					File file = new File(staffFileRealPath);
					// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
					messageHelper.addAttachment(MimeUtility
							.encodeWord("员工稽核统计清单"
									+ DateUtil
											.dateToStr(new Date(), "yyyyMMdd")
									+ ".xls"), file);
					// messageHelper.addAttachment(
					// MimeUtility.encodeWord(file.getName()), file);
				} else {
					systemMessageLog.setResult(0L);
					systemMessageLog.setSystemMessageInfo("没有生成员工稽核统计清单");
					systemMessageLogManager
							.saveSystemMessageLog(systemMessageLog);
				}

				sender.send(mailMessage);

				File orgFile = new File(orgFileRealPath);

				// 判断该文件是否存在，如果存在则删除
				if (orgFile.exists()) {
					FileUtil.deletefile(orgFileRealPath);
				}

				File staffFile = new File(staffFileRealPath);

				// 判断该文件是否存在，如果存在则删除
				if (staffFile.exists()) {
					FileUtil.deletefile(staffFileRealPath);
				}

				systemMessageLog.setResult(1L);
				systemMessageLog.setSystemMessageInfo("邮件发送成功");
				systemMessageLogManager.saveSystemMessageLog(systemMessageLog);

			} else {
				systemMessageLog.setResult(0L);
				systemMessageLog.setSystemMessageInfo("收件人邮件地址未配置");
				systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			}
		} catch (Exception e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("邮件发送出现异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		}
	}

	public List<SystemConfigUser> querySystemMessageConfigList() {
		BusinessSystem businessSystem = new BusinessSystem();
		businessSystem.setSystemCode(WsConstants.SYSTEM_CODE_UOM_EMAIL);// 邮件系统编码

		List<SystemConfigUser> systemMessageConfigList = messageConfigManager
				.querySystemConfigUserListByBusiSys(businessSystem);

		return systemMessageConfigList;
	}

	/**
	 * 导出组织稽核统计清单
	 */
	public String onExportOrgStatisticsBill() throws Exception {
		try {

			// 服务器文件名
			String fileName = "organization"
					+ DateUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";
			// String fileName = "组织稽核统计清单"
			// + DateUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";

			// 服务器文件存放相对路径
			String filePath = "/pages/audit/temp/";

			// 服务器文件存放真实路径
			String fileRealPath = GetPath.getWebRootPath() + filePath
					+ fileName;

			File file = new File(fileRealPath);

			// 判断该文件是否存在，如果存在则删除
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}

			String[] headers = { "系统域ID", "系统名", "组织名或区域名", "组织总数", "异常数",
					"组织合格率", "更新时间" };

			OrgAudit queryOrgAudit = new OrgAudit();

			// queryOrgAudit.setSystemDomainId(1L);
			queryOrgAudit.setUpdateDate(new Date());
			List orgAuditList = orgAuditManager
					.queryOrgAuditList(queryOrgAudit);

			logger.info("onExportOrgStatisticsBill==>" + fileRealPath);
			OutputStream out = new FileOutputStream(fileRealPath);

			ExcelUtil.exportExcel("组织稽核统计清单", headers, orgAuditList, out,
					"yyyy-MM-dd");

			// 清空缓冲区
			out.flush();
			// 关闭文件输出流
			out.close();

			return fileRealPath;

		} catch (FileNotFoundException e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("组织稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		} catch (IOException e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("组织稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		} catch (Exception e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("组织稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		}
		return null;
	}

	/**
	 * 导出员工稽核统计清单
	 */
	public String onExportStaffStatisticsBill() throws Exception {
		try {

			// 服务器文件名
			String fileName = "staff"
					+ DateUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";
			// String fileName = "员工稽核统计清单"
			// + DateUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";

			// 服务器文件存放相对路径
			String filePath = "/pages/audit/temp/";

			// 服务器文件存放真实路径
			String fileRealPath = GetPath.getWebRootPath() + filePath
					+ fileName;

			File file = new File(fileRealPath);

			// 判断该文件是否存在，如果存在则删除
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}

			String[] headers = { "系统域ID", "系统名", "组织名或区域名", "员工总数", "异常数",
					"员工合格率", "更新时间" };

			StaffAudit queryStaffAudit = new StaffAudit();
			// queryStaffAudit.setSystemDomainId(1L);
			queryStaffAudit.setUpdateDate(new Date());
			List staffAuditList = staffAuditManager
					.queryStaffAuditList(queryStaffAudit);

			logger.info("onExportStaffStatisticsBill==>" + fileRealPath);
			OutputStream out = new FileOutputStream(fileRealPath);

			ExcelUtil.exportExcel("员工稽核统计清单", headers, staffAuditList, out,
					"yyyy-MM-dd");

			// 清空缓冲区
			out.flush();
			// 关闭文件输出流
			out.close();

			return fileRealPath;

		} catch (FileNotFoundException e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("员工稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		} catch (IOException e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("员工稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		} catch (Exception e) {
			systemMessageLog.setResult(0L);
			systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
					.getMessage() : e.getMessage().substring(0, 500));
			systemMessageLog.setSystemMessageInfo("员工稽核统计清单生成异常");
			systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
			e.getMessage();
		}
		return null;
	}

}
