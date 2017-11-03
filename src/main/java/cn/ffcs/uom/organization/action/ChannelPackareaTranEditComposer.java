package cn.ffcs.uom.organization.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.FtpUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.ChannelPackareaTranEditBean;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.constants.ProcessStatusConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：20170727
 * @功能说明：代理商网点与包区业务关系
 *
 */
@Controller
@Scope("prototype")
public class ChannelPackareaTranEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = -7433238874805066530L;

	@Getter
	@Setter
	private ChannelPackareaTranEditBean bean = new ChannelPackareaTranEditBean();

	/**
	 * 页面操作参数
	 */
	private String opType;

	/**
	 * 承包人身份证照片
	 */
	private Media certMedia;

	/**
	 * 五级承包协议照片
	 */
	private Media fiveMedia;

	/**
	 * 门店合作协议照片
	 */
	private Media storeMedia;

	/**
	 * 网点和包区之间关系
	 */
	private ChannelPackareaRelationVo channelPackareaRelationVo = new ChannelPackareaRelationVo();

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrganizationTranManager organizationTranManager = (OrganizationTranManager) ApplicationContextUtil
			.getBean("organizationTranManager");

	/**
	 * 构造函数 {@inheritDoc}
	 * 
	 * @see cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 * @author zhanglu 2016年9月21日 zhanglu
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);

		// 设置包区box标识为true，传递到box，使页面初始化
		// 这个根据zk加载顺序进行设置
		bean.getPackAreaOrg().getOrganizationListboxExt().setIsPackArea(true);
	}

	/**
	 * window初始化. ChannelPackareaTranEditComposer
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$channelPackareaTranEditComposer() {
		bindEvent();
		bindBean();
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author zhanglu
	 */
	private void bindEvent() {
		this.bean.getChannelPackareaTranEditComposer().addEventListener(
				"onChannelPackareaTranChange", new EventListener() {
					@SuppressWarnings("rawtypes")
					public void onEvent(final Event event) throws Exception {
						if (!StrUtil.isNullOrEmpty(event.getData())) {
							ChannelPackareaTranEditComposer.this.arg = (HashMap) event
									.getData();
							bindBean();
						}
					}
				});
	}

	private void bindBean() {
		opType = StrUtil.strnull(arg.get("opType"));

		List<NodeVo> liTp = UomClassProvider.getValuesList("UomGroupOrgTran",
				"storeAreaFlag");
		ListboxUtils.rendererForEdit(bean.getStoreAreaFlag(), liTp);

		if (arg.get("channelPackareaRelationVo") != null)
			channelPackareaRelationVo = (ChannelPackareaRelationVo) arg
					.get("channelPackareaRelationVo");

		if ("add".equals(opType)) {
			bean.getChannelPackareaTranEditComposer().setTitle("网点包区业务关系新增");
			bean.getApproveToolBar().setVisible(false);
			bean.getInputToolBar().setVisible(true);

			bean.getUploadCertButton().setVisible(true);
			bean.getUploadFiveButton().setVisible(true);
			bean.getUploadStoreButton().setVisible(true);
		} else if ("view".equals(opType)) {
			// 显示工单
			bean.getChannelPackareaTranEditComposer().setTitle("网点包区业务关系查看");
			// 设置工单主题和描述无法编辑,以及网点包区，都无法编辑，并隐藏提交按钮
			bean.getAgentChannelOrg().setDisabled(true);
			bean.getPackAreaOrg().setDisabled(true);
			bean.getStoreAreaFlag().setDisabled(true);
			bean.getInputToolBar().setVisible(false);
			bean.getApproveToolBar().setVisible(false);

			// 查看操作类型，然后设置下拉框展示数据
			ListboxUtils.selectByCodeValue(bean.getStoreAreaFlag(),
					channelPackareaRelationVo.getStoreAreaFlag());
			showStoreAreaUploadDiv(channelPackareaRelationVo.getStoreAreaFlag());

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelId())) {
				// bean.getAgentChannelOrg().setDisabled(true);
				// 根据id号，取出相应的组织
				Organization org = organizationManager
						.getById(channelPackareaRelationVo.getAgentChannelId());
				bean.getAgentChannelOrg().setOrganization(org);
			}

			// 设置包区组织
			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaId())) {
				// bean.getPackAreaOrg().setDisabled(true);
				Organization tranOrg = organizationManager
						.getById(Long.valueOf(channelPackareaRelationVo
								.getCustPackareaId()));
				bean.getPackAreaOrg().setOrganization(tranOrg);
			}
		} else if ("approve".equals(opType)) {
			// 显示工单
			bean.getChannelPackareaTranEditComposer().setTitle("网点包区业务关系审批");
			// 设置工单主题和描述无法编辑,以及网点包区，都无法编辑，并隐藏提交按钮
			bean.getAgentChannelOrg().setDisabled(true);
			bean.getPackAreaOrg().setDisabled(true);
			bean.getStoreAreaFlag().setDisabled(true);
			bean.getInputToolBar().setVisible(false);
			bean.getApproveToolBar().setVisible(true);

			bean.getDownloadCertButton().setVisible(true);
			bean.getDownloadFiveButton().setVisible(true);
			bean.getDownloadStoreButton().setVisible(true);

			// 查看操作类型，然后设置下拉框展示数据
			ListboxUtils.selectByCodeValue(bean.getStoreAreaFlag(),
					channelPackareaRelationVo.getStoreAreaFlag());

			showStoreAreaUploadDiv(channelPackareaRelationVo.getStoreAreaFlag());

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelId())) {
				// bean.getAgentChannelOrg().setDisabled(true);
				// 根据id号，取出相应的组织
				Organization org = organizationManager
						.getById(channelPackareaRelationVo.getAgentChannelId());
				bean.getAgentChannelOrg().setOrganization(org);
			}

			// 设置包区组织
			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaId())) {
				// bean.getPackAreaOrg().setDisabled(true);
				Organization tranOrg = organizationManager
						.getById(Long.valueOf(channelPackareaRelationVo
								.getCustPackareaId()));
				bean.getPackAreaOrg().setOrganization(tranOrg);
			}
		} else if ("mod".equals(opType)) {
			// 显示工单
			bean.getChannelPackareaTranEditComposer().setTitle("网点包区业务关系修改");
			// 设置功能按钮可见性
			bean.getInputToolBar().setVisible(true);
			bean.getApproveToolBar().setVisible(false);

			bean.getUploadCertButton().setVisible(true);
			bean.getUploadFiveButton().setVisible(true);
			bean.getUploadStoreButton().setVisible(true);

			// 查看操作类型，然后设置下拉框展示数据
			ListboxUtils.selectByCodeValue(bean.getStoreAreaFlag(),
					channelPackareaRelationVo.getStoreAreaFlag());
			showStoreAreaUploadDiv(channelPackareaRelationVo.getStoreAreaFlag());

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelId())) {
				// bean.getAgentChannelOrg().setDisabled(true);
				// 根据id号，取出相应的组织
				Organization org = organizationManager
						.getById(channelPackareaRelationVo.getAgentChannelId());
				bean.getAgentChannelOrg().setOrganization(org);
			}

			// 设置包区组织
			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaId())) {
				// bean.getPackAreaOrg().setDisabled(true);
				Organization tranOrg = organizationManager
						.getById(Long.valueOf(channelPackareaRelationVo
								.getCustPackareaId()));
				bean.getPackAreaOrg().setOrganization(tranOrg);
			}
		}
	}

	/**
	 * 图片上传 承包人身份证
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadCertButton(ForwardEvent event) throws Exception {
		certMedia = ((UploadEvent) event.getOrigin()).getMedia();
		
		if (null == certMedia) {
			ZkUtil.showError("请选择要上传的图片!", "系统提示");
			return;
		} else if (!(certMedia.getName().endsWith(".jpg"))) {
			certMedia = null;
			ZkUtil.showError("上传的图片不是jpg类型!", "系统提示");
			return;
		}

		showPic(certMedia, bean.getCertPic());
	}

	/**
	 * 图片上传 五级承包协议
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadFiveButton(ForwardEvent event) throws Exception {
		fiveMedia = ((UploadEvent) event.getOrigin()).getMedia();
		
		if (null == fiveMedia) {
			ZkUtil.showError("请选择要上传的图片!", "系统提示");
			return;
		} else if (!(fiveMedia.getName().endsWith(".jpg"))) {
			fiveMedia = null;
			ZkUtil.showError("上传的图片不是jpg类型!", "系统提示");
			return;
		}

		showPic(fiveMedia, bean.getFivePic());
	}

	/**
	 * 图片上传 门店合作协议
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadStoreButton(ForwardEvent event) throws Exception {
		storeMedia = ((UploadEvent) event.getOrigin()).getMedia();
		
		if (null == storeMedia) {
			ZkUtil.showError("请选择要上传的图片!", "系统提示");
			return;
		} else if (!(storeMedia.getName().endsWith(".jpg"))) {
			storeMedia = null;
			ZkUtil.showError("上传的图片不是jpg类型!", "系统提示");
			return;
		}

		showPic(storeMedia, bean.getStorePic());
	}

	/**
	 * 下载图片 承包人身份证
	 */
	public void onDownloadCert() {
		onDownloadPic("cert");
	}

	/**
	 * 下载图片 五级承包协议
	 */
	public void onDownloadFive() {
		onDownloadPic("five");
	}

	/**
	 * 下载图片 门店合作协议
	 */
	public void onDownloadStore() {
		onDownloadPic("store");
	}

	/**
	 * 下载图片 根据图片类型flag
	 */
	public void onDownloadPic(String flag) {
		try {
			if (arg.get("channelPackareaRelationVo") != null) {
				UomGroupOrgTran uomGroupOrgTran = new UomGroupOrgTran();
				uomGroupOrgTran.setOrgId(channelPackareaRelationVo
						.getAgentChannelId());
				uomGroupOrgTran.setTranOrgId(channelPackareaRelationVo
						.getCustPackareaId());

				String path = getChannelPackareaPicPath(uomGroupOrgTran, flag,
						false);

				String charset = "UTF-8";
				// 服务器文件名
				String fileName = uomGroupOrgTran.getOrgId() + "_"
						+ uomGroupOrgTran.getTranOrgId() + "_" + flag + ".jpg";

				// 编码后文件名
				String encodedName = null;
				encodedName = URLEncoder.encode(fileName, charset);
				// 将空格替换为+号
				encodedName = encodedName.replace("%20", "+");
				HttpServletRequest httpRequest = (HttpServletRequest) Executions
						.getCurrent().getNativeRequest();
				// 解决ie6 bug 或者是火狐浏览器
				if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
						|| Servlets.isGecko3(httpRequest)) {
					encodedName = new String(fileName.getBytes(charset),
							"ISO8859-1");
				}

				File file = new File(path);
				// 判断文件是否存在
				if (file.exists()) {
					InputStream is = new FileInputStream(file);
					Filedownload.save(is, "application/x-jpg", encodedName);
				} else {
					ZkUtil.showError("下载图片失败,图片不存在", "系统提示");
				}
			}
		} catch (UnsupportedEncodingException e) {
			ZkUtil.showError("下载图片失败，文件名不支持", "系统提示");
		} catch (FileNotFoundException e) {
			ZkUtil.showError("下载图片失败,图片存在异常", "系统提示");
		}
	}

	public void showStoreAreaUploadDiv(String storeAreaFlag) {
		if ("1".equals(storeAreaFlag)) {
			bean.getStoreAreaUploadDiv().setVisible(true);

			if (arg.get("channelPackareaRelationVo") != null) {
				UomGroupOrgTran uomGroupOrgTran = new UomGroupOrgTran();
				uomGroupOrgTran.setOrgId(channelPackareaRelationVo
						.getAgentChannelId());
				uomGroupOrgTran.setTranOrgId(channelPackareaRelationVo
						.getCustPackareaId());

				String certPath = getChannelPackareaPicPath(uomGroupOrgTran,
						"cert", true);
				bean.getCertPic().setSrc(certPath);
				bean.getCertPic().setHeight("50px");
				bean.getCertPic().setWidth("50px");

				String fivePath = getChannelPackareaPicPath(uomGroupOrgTran,
						"five", true);
				bean.getFivePic().setSrc(fivePath);
				bean.getFivePic().setHeight("50px");
				bean.getFivePic().setWidth("50px");

				String storePath = getChannelPackareaPicPath(uomGroupOrgTran,
						"store", true);
				bean.getStorePic().setSrc(storePath);
				bean.getStorePic().setHeight("50px");
				bean.getStorePic().setWidth("50px");
			} else {
				bean.getCertPic().setSrc("");
				bean.getFivePic().setSrc("");
				bean.getStorePic().setSrc("");
			}

		} else {
			bean.getStoreAreaUploadDiv().setVisible(false);
		}
	}

	/**
	 * 选择是否以店包区触发事件
	 */
	public void onSelect$storeAreaFlag() {
		String storeAreaFlag = (String) bean.getStoreAreaFlag()
				.getSelectedItem().getValue();
		showStoreAreaUploadDiv(storeAreaFlag);
	}

	public void showPic(Media picMedia, org.zkoss.zul.Image picImage) {
		Image img = (Image) picMedia;
		picImage.setHeight("50px");
		picImage.setWidth("50px");

		picImage.setContent(img);
	}

	public void savePic(Media picMedia, UomGroupOrgTran uomGroupOrgTran,
			String flag) {
		ByteArrayInputStream bais = new ByteArrayInputStream(
				picMedia.getByteData());

		String hostname = UomClassProvider.getSystemConfig("ftpHost");
		String username = UomClassProvider.getSystemConfig("ftpUsername");
		String password = UomClassProvider.getSystemConfig("ftpPassword");
		String pathname = UomClassProvider.getSystemConfig("uploadPicFtpPath");
		String filename = uomGroupOrgTran.getOrgId() + "_"
				+ uomGroupOrgTran.getTranOrgId() + "_" + flag + ".jpg";
		FtpUtil.uploadFile(hostname, 21, username, password, pathname,
				filename, bais);
	}

	public String getChannelPackareaPicPath(UomGroupOrgTran uomGroupOrgTran,
			String flag, boolean isShow) {
		String uploadPicDir = UomClassProvider.getSystemConfig("uploadPicDir");

		String hostname = UomClassProvider.getSystemConfig("ftpHost");
		String username = UomClassProvider.getSystemConfig("ftpUsername");
		String password = UomClassProvider.getSystemConfig("ftpPassword");
		String pathname = UomClassProvider.getSystemConfig("uploadPicFtpPath");
		String filename = uomGroupOrgTran.getOrgId() + "_"
				+ uomGroupOrgTran.getTranOrgId() + "_" + flag + ".jpg";
		String localpath = this.session.getWebApp().getRealPath(uploadPicDir);
		boolean isDownload = FtpUtil.downloadFile(hostname, 21, username,
				password, pathname, filename, localpath);

		String path = "";
		
		if (isDownload) {
			if (isShow) {
				path = "/" + uploadPicDir + "/" + flag + ".jpg" + "?tempid="
						+ Math.random();
			} else {
				path = localpath + "/" + flag + ".jpg";
			}
		}

		return path;
	}

	// 待删
	public String getShowPicPath(UomGroupOrgTran uomGroupOrgTran, String flag) {
		String uploadPicDir = UomClassProvider.getSystemConfig("uploadPicDir");

		String path = "/" + uploadPicDir + "/" + uomGroupOrgTran.getOrgId()
				+ "_" + uomGroupOrgTran.getTranOrgId() + "_" + flag + ".jpg";

		return path;
	}

	/**
	 * 页面点击确定添加按钮
	 * 
	 */
	public void onOk() {

		String msg = this.getDoValidOrganizationTran();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		UomGroupOrgTran uomGroupOrgTran = new UomGroupOrgTran();
		// 默认新增组织关系
		uomGroupOrgTran.setOrgId(this.bean.getAgentChannelOrg()
				.getOrganization().getOrgId());
		// 工单信息存放
		/**
		 * 目前这种关系只有一种，前台也没有选择的下拉，所以这里新增直接写这个
		 */
		uomGroupOrgTran
				.setTranRelaType(OrganizationTranConstant.CHANNEL_PACKAREA_RELATION);

		// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系,查出所有的关系
		// 一个代理商对应的关系，按理说这个只能一条关系，不能对应多个包区，这里查询的时候，查询非失效状态的所有数据
		List<UomGroupOrgTran> existlist = organizationTranManager
				.queryChannelPackAreaTranList(uomGroupOrgTran);
		// 设置包区组织id
		uomGroupOrgTran.setTranOrgId(String.valueOf(this.bean.getPackAreaOrg()
				.getOrganization().getOrgId()));

		// 设置以店包区标识
		if (bean.getStoreAreaFlag().getSelectedItem() != null
				&& bean.getStoreAreaFlag().getSelectedItem().getValue() != null) {
			uomGroupOrgTran.setStoreAreaFlag(this.bean.getStoreAreaFlag()
					.getSelectedItem().getValue().toString());
		} else {
			uomGroupOrgTran.setStoreAreaFlag("0");
		}

		if ("1".equals(uomGroupOrgTran.getStoreAreaFlag())) {
			// 以店包区列表
			List<UomGroupOrgTran> storeAreaList = organizationTranManager
					.queryUomGroupOrgTranStoreAreaList(uomGroupOrgTran);

			if (storeAreaList != null && storeAreaList.size() > 0) {
				ZkUtil.showInformation("该包区已存在以店包区,不能再添加以店包区", "提示信息");
				return;
			}

			if (bean.getCertPic().getContent() == null) {
				ZkUtil.showInformation("未上传承包人身份证", "提示信息");
				return;
			}

			if (bean.getFivePic().getContent() == null) {
				ZkUtil.showInformation("未上传五级承包协议", "提示信息");
				return;
			}

			if (bean.getStorePic().getContent() == null) {
				ZkUtil.showInformation("未上传门店合作协议", "提示信息");
				return;
			}
		}

		if (existlist != null && existlist.size() > 0) {
			ZkUtil.showInformation("该网点已存在业务关系或流程未结束,不能重复添加包区", "提示信息");
			return;
		}

		uomGroupOrgTran
				.setStatusCd(ProcessStatusConstant.ENTT_STATE_IN_APPROVAL);

		if ("add".equals(opType)) {
			organizationTranManager.addChannelPackAreaTran(uomGroupOrgTran);
		} else if ("mod".equals(opType)) {
			uomGroupOrgTran.setId(channelPackareaRelationVo.getId());
			organizationTranManager.updateChannelPackAreaTran(uomGroupOrgTran);
		}

		saveMedia(uomGroupOrgTran);
		clearMedia();
		bean.getChannelPackareaTranEditComposer().onClose();
		Events.postEvent("onOK", this.self, uomGroupOrgTran);
	}

	public void saveMedia(UomGroupOrgTran uomGroupOrgTran) {
		if (certMedia != null) {
			savePic(certMedia, uomGroupOrgTran, "cert");
		}

		if (fiveMedia != null) {
			savePic(fiveMedia, uomGroupOrgTran, "five");
		}

		if (storeMedia != null) {
			savePic(storeMedia, uomGroupOrgTran, "store");
		}
	}

	public void delPic(UomGroupOrgTran uomGroupOrgTran) {
		String[] flagArray = { "cert", "five", "store" };

		for (int i = 0; i < flagArray.length; i++) {
			String hostname = UomClassProvider.getSystemConfig("ftpHost");
			String username = UomClassProvider.getSystemConfig("ftpUsername");
			String password = UomClassProvider.getSystemConfig("ftpPassword");
			String pathname = UomClassProvider
					.getSystemConfig("uploadPicFtpPath");
			String filename = uomGroupOrgTran.getOrgId() + "_"
					+ uomGroupOrgTran.getTranOrgId() + "_" + flagArray[i]
					+ ".jpg";
			FtpUtil.deleteFile(hostname, 21, username, password, pathname,
					filename);
		}
	}

	public void onApprove() {
		UomGroupOrgTran uomGroupOrgTran = (UomGroupOrgTran) UomGroupOrgTran
				.repository().getObject(UomGroupOrgTran.class,
						channelPackareaRelationVo.getId());
		uomGroupOrgTran.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		uomGroupOrgTran.update();

		bean.getChannelPackareaTranEditComposer().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	public void onRefuse() {
		UomGroupOrgTran uomGroupOrgTran = (UomGroupOrgTran) UomGroupOrgTran
				.repository().getObject(UomGroupOrgTran.class,
						channelPackareaRelationVo.getId());
		uomGroupOrgTran.remove();
		delPic(uomGroupOrgTran);

		bean.getChannelPackareaTranEditComposer().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getChannelPackareaTranEditComposer().onClose();
		clearMedia();
	}

	public void clearMedia() {
		certMedia = null;
		fiveMedia = null;
		storeMedia = null;
	}

	public String getDoValidOrganizationTran() {
		if (bean.getAgentChannelOrg() == null
				|| bean.getAgentChannelOrg().getOrganization() == null) {
			return "网点组织未选择";
		}

		if (bean.getPackAreaOrg() == null
				|| bean.getPackAreaOrg().getOrganization() == null) {
			return "包区组织未选择";
		}

		if (StrUtil.isNullOrEmpty(bean.getAgentChannelOrg().getOrganization()
				.getOrgId())) {
			return "选中网点组织id不存在";
		}

		if (StrUtil.isNullOrEmpty(bean.getPackAreaOrg().getOrganization()
				.getOrgId())) {
			return "包区组织id不存在";
		}

		if (bean.getStoreAreaFlag().getSelectedItem() == null
				|| bean.getStoreAreaFlag().getSelectedItem().getValue() == null) {
			return "请选择以店包区标识";
		}

		return "";
	}
}
