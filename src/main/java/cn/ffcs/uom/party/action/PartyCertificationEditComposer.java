package cn.ffcs.uom.party.action;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.party.action.bean.PartyCertificationBean;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;
import cn.ffcs.uom.webservices.manager.IntfLogManager;

@Controller
@Scope("prototype")
public class PartyCertificationEditComposer extends BasePortletComposer {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private String opType;

	private Party party;

	private PartyCertification partyCertification;

	private List<PartyRole> partyRoleList;

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	@Resource
	private IdentityCardConfigManager identityCardConfigManager;

	@Resource
	private OrganizationManager organizationManager;
	/**
	 * 接口日志
	 */
	@Resource
	private IntfLogManager intfLogManager;

	private PartyCertificationBean bean = new PartyCertificationBean();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);

	}

	public void onCreate$partyCertificationEditWin() throws Exception {
		bindEvent();
		bindCombobox();
		bindBean();
		onIdentityCardIdSelect();
	}

	/**
	 * 绑定下拉框. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-30 Wong
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> liTp = UomClassProvider.getValuesList(
				"PartyCertification", "certType");
		ListboxUtils.rendererForEdit(bean.getCertType(), liTp);

		liTp = UomClassProvider.getValuesList("PartyCertification", "certSort");
		ListboxUtils.rendererForEdit(bean.getCertSort(), liTp);

		List<NodeVo> identityCardIdList = identityCardConfigManager
				.getValuesList();
		ListboxUtils.rendererForEdit(bean.getIdentityCardId(),
				identityCardIdList);
	}

	/**
	 * 页面初始化.
	 * 
	 * @throws
	 * @author
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if (SffOrPtyCtants.ADD.equals(opType)) {
			bean.getPartyCertificationEditWin().setTitle("参与人证件新增");
		} else if (SffOrPtyCtants.MOD.equals(opType)) {
			bean.getPartyCertificationEditWin().setTitle("参与人证件修改");
			partyCertification = (PartyCertification) arg
					.get("partyCertification");
			PubUtil.fillBeanFromPo(partyCertification, bean);
		} else if (SffOrPtyCtants.MODSPE.equals(opType)) {
			bean.getPartyCertificationEditWin().setTitle("证件信息修改");
			bean.getCertType().setDisabled(true);
			bean.getCertSort().setDisabled(true);
			partyCertification = (PartyCertification) arg
					.get("partyCertification");
			PubUtil.fillBeanFromPo(partyCertification, bean);
		}
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-25 Wong
	 */
	private void bindEvent() throws Exception {
		bean.getPartyCertificationEditWin().addEventListener(
				"onPartyCertificationChange", new EventListener() {
					@SuppressWarnings("rawtypes")
					public void onEvent(final Event event) throws Exception {
						if (!StrUtil.isNullOrEmpty(event.getData())) {
							PartyCertificationEditComposer.this.arg = (HashMap) event
									.getData();

							bindBean();
						}
					}
				});
	}

	public void onCancel() {
		bean.getPartyCertificationEditWin().onClose();
	}

	public void onOk() {
		party = (Party) arg.get("party");
		if (null == party) {
			ZkUtil.showInformation("请选择相应的参与人", "提示信息");
			return;
		}
		String msg = checkPartyCertifiDate();
		if (null != msg) {
			ZkUtil.showInformation(msg, "提示信息");
			return;
		}

		IdentityCardConfig identityCardConfig = null;
		Long identityCardId = null;

		if (SffOrPtyCtants.ADD.equals(opType)) {
			String certType = bean.getCertType().getSelectedItem().getValue()
					.toString();
			String certNum = bean.getCertNumber().getValue().trim();
			if (!StrUtil.isNullOrEmpty(certType)) {
				boolean certIsNotExist = partyManager
						.checkIsExistCertificate(certNum);
				if (!certIsNotExist) { // 证件已达到使用上限
					String partyCertificationUsedMax = UomClassProvider
							.getSystemConfig("partyCertificationUsedMax");
					String fieldErrorCertAlreadyUseStr = SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE_STR
							.replace("%", partyCertificationUsedMax);

					ZkUtil.showError(fieldErrorCertAlreadyUseStr, "提示信息");
					return;
				}
			}
			partyCertification = new PartyCertification();
			PubUtil.fillPoFromBean(bean, partyCertification);
			partyCertification.setPartyId(party.getPartyId());
			
			partyCertification
					.setIsRealName(PartyConstant.PARTY_CERTIFICATION_IS_REAL_NAME_Y);
			partyManager.save(partyCertification);
		} else if (SffOrPtyCtants.MOD.equals(opType)
				|| SffOrPtyCtants.MODSPE.equals(opType)) { // 修改

			PubUtil.fillPoFromBean(bean, partyCertification);

			PartyCertification queryPartyCertification = new PartyCertification();
			queryPartyCertification.setCertType(partyCertification
					.getCertType());
			queryPartyCertification.setCertNumber(partyCertification
					.getCertNumber());

			if (PartyConstant.ATTR_VALUE_IDNO.equals(partyCertification
					.getCertType())) {
				// 如果身份证号是临时18位号，则换前三个字母，后15们不变
				if (IdcardValidator.is18TempIdcard(partyCertification
						.getCertNumber())) {

					identityCardId = Long.valueOf((String) bean
							.getIdentityCardId().getSelectedItem().getValue());

					identityCardConfig = identityCardConfigManager
							.getIdentityCardConfig(identityCardId);

					queryPartyCertification.setCertNumber(identityCardConfig
							.getIdentityCardPrefix()
							+ partyCertification.getCertNumber().substring(3));

				}
			}

			if (!StrUtil.isNullOrEmpty(queryPartyCertification.getCertType())
					&& !StrUtil.isNullOrEmpty(queryPartyCertification
							.getCertNumber())) {

				boolean certIsNotExist = partyManager
						.checkIsExistCertificate(partyCertification
								.getCertNumber());
				if (!certIsNotExist) { // 证件已达到使用上限
					String partyCertificationUsedMax = UomClassProvider
							.getSystemConfig("partyCertificationUsedMax");
					String fieldErrorCertAlreadyUseStr = SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE_STR
							.replace("%", partyCertificationUsedMax);

					ZkUtil.showError(fieldErrorCertAlreadyUseStr, "提示信息");
					return;
				}
			}
			
			partyCertification.setCertNumber(queryPartyCertification
					.getCertNumber());
			partyCertification
					.setIsRealName(PartyConstant.PARTY_CERTIFICATION_IS_REAL_NAME_Y);
			partyManager.update(partyCertification);
		}
		// 修改参与人证件信息时，同时更新参与人和参与人角色
		Long partyId = party.getPartyId();
		if (partyId != null) {
			partyRoleList = partyManager.getPartyRoleByPtId(partyId);
		}
		if (partyRoleList != null && partyRoleList.size() > 0) {
			for (PartyRole oldPartyRole : partyRoleList) {
				partyManager.updatePartyRole(oldPartyRole);
			}
		}
		partyManager.updateParty(party);
		Events.postEvent(SffOrPtyCtants.ON_OK,
				bean.getPartyCertificationEditWin(), partyCertification);
		bean.getPartyCertificationEditWin().onClose();
	}

	/**
	 * . 身份证校验
	 * 
	 * @return
	 * @author Wong 2013-5-25 Wong
	 */
	private String checkPartyCertifiDate() {

		if (StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem()
						.getValue())) {
			return "请选择证件类型";
		}

		if (StrUtil.isNullOrEmpty(bean.getIdentityCardId().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getIdentityCardId()
						.getSelectedItem().getValue())) {
			return "请选择类型";
		}

		if (StrUtil.isNullOrEmpty(bean.getCertNumber().getValue().trim())) {
			return "请填写证件号码";
		}

		if (StrUtil.isNullOrEmpty(bean.getCertName().getValue().trim())) {
			return "请填写证件名";
		}
		if (StrUtil.checkLowerCase(bean.getCertNumber().getValue().trim())) {
			return "请将证件号码中小写字母改成大写字母";
		}
		if (PartyConstant.ATTR_VALUE_IDNO.equals(bean.getCertType()
				.getSelectedItem().getValue())) {
			if (!IdcardValidator.isValidatedAllIdcard(bean.getCertNumber()
					.getValue().trim())) {
				return "身份证格式不正确，请填写真实身份证信息!";
			}
		}

		if (StrUtil.isNullOrEmpty(bean.getCertSort().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getCertSort().getSelectedItem()
						.getValue())) {
			return "请选择证件种类";
		}

		if (StrUtil.isNullOrEmpty(bean.getReason().getValue())) {
			return "请填写变更原因";
		}

		if (PartyConstant.ATTR_VALUE_IDNO.equals(bean.getCertType()
				.getSelectedItem().getValue())) {
			String idNum = bean.getCertNumber().getValue().trim();
			String idName = bean.getCertName().getValue().trim();
			try {
				boolean isRealName = CertUtil.checkIdCard(idNum, idName);

				if (isRealName) {
					return null;
				} else {
					return "实名认证未通过";
				}
			} catch (Exception e) {
				return "调用国政通接口失败";
			}

		}

		return null;
	}

	public void onCertTypeSelect() {
		if (bean.getCertType() != null
				&& bean.getCertType().getSelectedItem() != null
				&& bean.getCertType().getSelectedItem().getValue() != null) {
			bean.getCertNumber().setDisabled(false);
			bean.getIdentityCardId().setDisabled(false);
			bean.getIdentityCardId().selectItem(null);
		}
	}

	public void onIdentityCardIdSelect() {
		if (bean.getIdentityCardId() != null
				&& bean.getIdentityCardId().getSelectedItem() != null
				&& bean.getIdentityCardId().getSelectedItem().getValue() != null) {
			bean.getCertNumber().setDisabled(false);
		}
	}
}
