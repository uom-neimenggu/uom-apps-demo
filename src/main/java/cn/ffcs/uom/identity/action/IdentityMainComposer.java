package cn.ffcs.uom.identity.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.ExcelUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.identity.bean.IdentityMainBean;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

/**
 * 全量增量发布界面.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class IdentityMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private IdentityMainBean bean = new IdentityMainBean();

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Resource
	private IdentityCardConfigManager identityCardConfigManager;

	@Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		// TODO Auto-generated method stub
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.setPortletInfoProvider(this);
		this.bindCombobox();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void bindCombobox() throws Exception {
		List<NodeVo> identityCardNameList = identityCardConfigManager
				.getValuesList();
		ListboxUtils.rendererForEdit(bean.getIdentityCardNameListbox(),
				identityCardNameList);
	}
	

	/**
	 * 生成临时身份证请求事件.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onIdentityRequest() throws Exception {

		ZkUtil.showQuestion("确定要生成临时身份证吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {

					try {
						IdentityCardConfig identityCardConfig = null;
						Long identityCardId = null;
						Long identityCardCount = null;

						if (bean.getIdentityCardNameListbox() != null
								&& bean.getIdentityCardNameListbox()
										.getSelectedItem().getValue() != null
								&& bean.getIdentityCardCount().getValue() != null) {

							identityCardId = Long.valueOf((String) bean
									.getIdentityCardNameListbox()
									.getSelectedItem().getValue());
							// if (identityCardId == 1) {
							// ZkUtil.showError("生成临时身份证不能选择"
							// + bean.getIdentityCardNameListbox()
							// .getSelectedItem().getLabel()
							// + "\n", "系统提示");
							// }

							identityCardCount = bean.getIdentityCardCount()
									.getValue();

							identityCardConfig = identityCardConfigManager
									.getIdentityCardConfig(identityCardId);

							// 设置字符集
							String charset = "UTF-8";
							// 项目根目录
							HttpServletRequest httpRequest = (HttpServletRequest) Executions
									.getCurrent().getNativeRequest();
							// 服务器文件名
							String fileName = "identityCard.xls";
							// 时间字符串
							String dataStr = DateUtil.dateToStr(new Date(),
									"yyyyMMddHHmmssS");

							if (!StrUtil.isEmpty(dataStr)) {
								fileName = dataStr + ".xls";
							}
							// 服务器文件存放相对路径
							String filePath = "/pages/identity/temp/";
							// 服务器文件存放真实路径
							String fileRealPath = httpRequest
									.getRealPath(filePath + fileName);

							File file = new File(fileRealPath);

							// 判断该文件是否存在，如果存在则删除
							if (file.exists()) {
								FileUtil.deletefile(fileRealPath);
							}
							// 删除已经下载过的文件
							if (ExcelUtil.identityCardsList.size() > 0) {
								for (int i = 0; i < ExcelUtil.identityCardsList
										.size(); i++) {
									File oldFile = new File(
											ExcelUtil.identityCardsList.get(i));
									if (oldFile.exists()) {
										FileUtil.deletefile(ExcelUtil.identityCardsList
												.get(i));
									}
								}
								ExcelUtil.identityCardsList.clear();
							}

							String[] headers = { "身份证类型", "证件号", "生成时间" };

							List identityCardTempList = null;

							if (!StrUtil.isNullOrEmpty(identityCardConfig)) {
								identityCardTempList = identityCardConfigManager
										.getIdentityCardTempList(
												identityCardConfig,
												identityCardCount);
							}

							if (identityCardTempList == null
									|| identityCardTempList.size() <= 0) {
								Messagebox.show("临时身份证没有生成数据,未下载！");
								return;
							}

							OutputStream out = new FileOutputStream(
									fileRealPath);

							ExcelUtil.exportExcel("临时身份证", headers,
									identityCardTempList, out,
									"yyyy-MM-dd HH:mm:ss");

							// 清空缓冲区
							out.flush();
							// 关闭文件输出流
							out.close();

							// 编码后文件名
							String encodedName = null;
							encodedName = URLEncoder.encode("临时身份证.xls",
									charset);
							// 将空格替换为+号
							encodedName = encodedName.replace("%20", "+");

							// 解决ie6 bug 或者是火狐浏览器
							if (encodedName.length() > 150
									|| Servlets.isGecko(httpRequest)
									|| Servlets.isGecko3(httpRequest)) {
								encodedName = new String("临时身份证.xls"
										.getBytes(charset), "ISO8859-1");
							}
							FileInputStream in = new FileInputStream(
									fileRealPath);
							Filedownload.save(in, "application/octet-stream",
									encodedName);
							// 关闭输入文件流
							// in.close();

							// 删除临时创建的文件
							if (file.exists()) {
								FileUtil.deletefile(fileRealPath);
							}

							ExcelUtil.identityCardsList.add(fileRealPath);
						} else {
							ZkUtil.showError("请选择身份证类型或输入正确的数字！\n", "系统提示");
						}
					} catch (FileNotFoundException e) {
						ZkUtil.showError("临时身份证生成失败\n", "系统提示");
					} catch (IOException e) {
						ZkUtil.showError("临时身份证生成失败\n", "系统提示");
					} catch (Exception e) {
						ZkUtil.showError("临时身份证生成失败\n", "系统提示");
					}
				}

			}
		});

	}
}
