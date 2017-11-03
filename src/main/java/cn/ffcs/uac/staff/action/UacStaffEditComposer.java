package cn.ffcs.uac.staff.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uac.staff.action.bean.UacStaffEditBean;
import cn.ffcs.uac.staff.constant.EnumUacStaffInfo;
import cn.ffcs.uac.staff.manager.UacStaffManager;
import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.InputFieldUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;

@Controller
@Scope("prototype")
public class UacStaffEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = -6096343177536382090L;

	private UacStaffEditBean bean = new UacStaffEditBean();

	private String opType;

	@Resource
	private UacStaffManager staffInfoAdapter;// staffInfoAdapter

	@Resource
	private IdentityCardConfigManager identityCardConfigManager;

	UacStaff uacStaff;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$uacStaffEditWin() throws Exception {
		bindCombobox();
		bindBean();
	}

	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		// 类型
		List<NodeVo> identityCardIdList = identityCardConfigManager
				.getValuesList();
		ListboxUtils.rendererForEdit(bean.getType(), identityCardIdList);

		// 证件类型
		List<NodeVo> nodeVoList = UomClassProvider.getValuesList(
				"PartyCertification", "certType");
		ListboxUtils.rendererForEdit(bean.getCertType(), nodeVoList);

		// 是否实名
		nodeVoList = UomClassProvider.getValuesList("PartyCertification",
				"isRealName");
		ListboxUtils.rendererForEdit(bean.getIsReal(), nodeVoList);

		// 性别
		nodeVoList = UomClassProvider.getValuesList("Individual", "gender");
		ListboxUtils.rendererForEdit(bean.getGender(), nodeVoList);

		// 民族
		nodeVoList = UomClassProvider.getValuesList("Individual", "nation");
		ListboxUtils.rendererForEdit(bean.getNation(), nodeVoList);

		// 宗教
		nodeVoList = UomClassProvider.getValuesList("Individual", "religion");
		ListboxUtils.rendererForEdit(bean.getReligion(), nodeVoList);

		// 教育水平
		nodeVoList = UomClassProvider.getValuesList("Individual",
				"educationLevel");
		ListboxUtils.rendererForEdit(bean.getEducationLevel(), nodeVoList);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if (EnumUacStaffInfo.ADD.getValue().equals(opType)) {
			bean.getUacStaffEditWin().setTitle("员工综合新增");
		} else if (EnumUacStaffInfo.MOD.getValue().equals(opType)) {
			bean.getUacStaffEditWin().setTitle("员工综合修改");
			initPageBean();
		} else {
			bean.getUacStaffEditWin().setTitle("员工综合查看");
			bean.getBtnToolBar().setVisible(false);
			PubUtil.disableComponent(bean.getUacStaffDiv(), true);
			initPageBean();
		}
	}

	public void initPageBean() throws Exception {
		uacStaff = (UacStaff) arg.get("uacStaff");

		// 人员基本信息
		PubUtil.fillBeanFromPo(uacStaff, bean);

		List<String> propertyList = new ArrayList<String>();
		propertyList.add(uacStaff.getProperty());
		bean.getProperty().setInitialValue(propertyList);

		// 证件信息
		UacCert uacCert = uacStaff.getUacCert();

		if (uacCert != null) {
			PubUtil.fillBeanFromPo(uacCert, bean);
		}

		// 联系方式
		UacContact uacContact = uacStaff.getUacContact();

		if (uacContact != null) {
			PubUtil.fillBeanFromPo(uacContact, bean);
		}

		// 人员附加信息
		UacAttachedInfo uacAttachedInfo = uacStaff.getUacAttachedInfo();

		if (uacAttachedInfo != null) {
			PubUtil.fillBeanFromPo(uacAttachedInfo, bean);
		}

		List<String> maritalStatusList = new ArrayList<String>();
		maritalStatusList.add(uacAttachedInfo.getMaritalStatus());
		bean.getMaritalStatus().setInitialValue(maritalStatusList);
	}

	/**
	 * 保存.
	 * 
	 * @throws Exception
	 */
	public void onOk() throws Exception {
		String msg = checkUacStaffData();
		if (msg != null) {
			Messagebox.show(msg);
			return;
		}

		if (EnumUacStaffInfo.ADD.getValue().equals(opType)) {
			uacStaff = UacStaff.newInstance();
			PubUtil.fillPoFromBean(bean, uacStaff);
			uacStaff.setProperty(bean.getProperty().getAttrValue());

			UacCert uacCert = UacCert.newInstance();
			PubUtil.fillPoFromBean(bean, uacCert);
			uacStaff.setUacCert(uacCert);

			UacContact uacContact = UacContact.newInstance();
			PubUtil.fillPoFromBean(bean, uacContact);
			uacStaff.setUacContact(uacContact);

			UacAttachedInfo uacAttachedInfo = UacAttachedInfo.newInstance();
			PubUtil.fillPoFromBean(bean, uacAttachedInfo);
			uacAttachedInfo.setMaritalStatus(bean.getMaritalStatus()
					.getAttrValue());
			uacStaff.setUacAttachedInfo(uacAttachedInfo);

			// 保存员工
			String info = staffInfoAdapter.addUacStaffAllInfo(uacStaff);

			if (info != null) {
				Messagebox.show(info);
				return;
			} else {
				Events.postEvent(Events.ON_OK, bean.getUacStaffEditWin(),
						uacStaff);
			}
		} else if (SffOrPtyCtants.MOD.equals(opType)) {
			PubUtil.fillPoFromBean(bean, uacStaff);
			uacStaff.setProperty(bean.getProperty().getAttrValue());

			UacCert uacCert = uacStaff.getUacCert();
			PubUtil.fillPoFromBean(bean, uacCert);
			uacStaff.setUacCert(uacCert);

			UacContact uacContact = uacStaff.getUacContact();
			PubUtil.fillPoFromBean(bean, uacContact);
			uacStaff.setUacContact(uacContact);

			UacAttachedInfo uacAttachedInfo = uacStaff.getUacAttachedInfo();
			PubUtil.fillPoFromBean(bean, uacAttachedInfo);
			uacAttachedInfo.setMaritalStatus(bean.getMaritalStatus()
					.getAttrValue());
			uacStaff.setUacAttachedInfo(uacAttachedInfo);

			// 更新员工
			staffInfoAdapter.updateUacStaffAllInfo(uacStaff);
		}

		onCancel();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getUacStaffEditWin().onClose();
	}

	private String checkUacStaffData() {
		// BaseInfo Check
		if (StrUtil.isNullOrEmpty(bean.getStaffName().getValue())) {
			return "请填写人员名";
		} else if (StrUtil.checkBlank(bean.getStaffName().getValue())) {
			return "人员名中有空格";
		} else if (StrUtil.isNullOrEmpty(bean.getProperty().getAttrValue())) {
			return "请选择人员性质";
		} else if (StrUtil.isNullOrEmpty(bean.getType().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getType().getSelectedItem()
						.getValue())) {
			return "请选择类型";
		} else if (StrUtil
				.isNullOrEmpty(bean.getTransactionRecord().getValue())) {
			return "请填写操作单据";
		} else if (StrUtil.isNullOrEmpty(bean.getCertName().getValue())) {
			return "请填写证件名";
		} else if (StrUtil.checkBlank(bean.getCertName().getValue())) {
			return "证件名中有空格";
		} else if (StrUtil.isNullOrEmpty(bean.getCertNumber().getValue())) {
			return "请填写证件号码";
		} else if (StrUtil.checkBlank(bean.getCertNumber().getValue())) {
			return "证件号码中有空格";
		} else if (StrUtil.checkLowerCase(bean.getCertNumber().getValue())) {
			return "请将证件号码中小写字母改成大写字母";
		} else if (StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem()
						.getValue())) {
			return "请选择证件类型";
		} else if (StrUtil.isNullOrEmpty(bean.getMobilePhone().getValue())) {
			return "请填写移动手机号码";
		} else if (StrUtil.checkBlank(bean.getMobilePhone().getValue())) {
			return "移动手机号码中有空格";
		} else if (!InputFieldUtil.checkPhone(bean.getMobilePhone().getValue())) {
			return "移动手机号码格式有误";
		} else if (StrUtil.isNullOrEmpty(bean.getGender().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getGender().getSelectedItem()
						.getValue())) {
			return "请选择性别";
		}

		// IDCARD Real Name Check
		if (EnumUacStaffInfo.CERT_TYPE_IDCARD.getValue().equals(
				bean.getCertType().getSelectedItem().getValue())) {
			String idNum = bean.getCertNumber().getValue();
			String idName = bean.getCertName().getValue();
			try {
				boolean isRealName = CertUtil.checkIdCard(idNum, idName);

				if (!isRealName) {
					return "实名认证未通过";
				}
			} catch (Exception e) {
				return "调用国政通接口失败";
			}

		}

		return null;
	}
}
