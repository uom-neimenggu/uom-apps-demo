package cn.ffcs.uom.zkdemo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.util.StringPool;
import cn.ffcs.raptornuke.plugin.common.util.Validator;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkPortletUtil;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.zkdemo.BaseComposer;
import cn.ffcs.uom.zkdemo.keys.PageKeys;
import cn.ffcs.uom.zkdemo.keys.PortletKeys;
import cn.ffcs.uom.zkdemo.keys.WebKeys;
import cn.ffcs.uom.zkdemo.pojo.FormResult;

/**
 * 表单例子
 * 
 * @author lianch@ffcs.cn
 * 
 */
@SuppressWarnings("serial")
public class FormViewCtrl extends BaseComposer {

	private Textbox usernameTxtBox;
	private Textbox passwordTxtBox;
	private Datebox dateDateBox;
	private Checkbox rememberCheckBox;
	private Label mesgLbl;
	private String _stateStr;
	private String _urlStr;
	private TreeChooserBandbox treeChooserBandbox;
	private TreeChooserBandbox orgTypeCd;
	private Html html;

	// 绝对地址(从WEBCONTENT开始)
	private static final String A_FORM_RESULT = StringPool.FORWARD_SLASH
			+ WebKeys.WEBCONTENT_M + StringPool.FORWARD_SLASH
			+ PortletKeys.ZK_DEMO + StringPool.FORWARD_SLASH
			+ PageKeys.FORM_RESULT;

	// 绝对地址(从WEBCONTENT开始)
	private static final String A_FORM_EDIT = StringPool.FORWARD_SLASH
			+ WebKeys.WEBCONTENT_M + StringPool.FORWARD_SLASH
			+ PortletKeys.ZK_DEMO + StringPool.FORWARD_SLASH
			+ PageKeys.FORM_EDIT;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		rememberCheckBox.setLabel("记住");
		// 只能在doAfterCompose取得portlet共享信息
		StringBuffer sb = new StringBuffer();

		List<String> list=new ArrayList<String>();
		list.add("N0202020400");
		list.add("N0501000000");
		orgTypeCd.setInitialValue(list);
		
		HttpServletRequest req = (HttpServletRequest) ZkUtil.getNativeRequest();
		Enumeration reqNames = req.getAttributeNames();
		String attributeName = "";
		while (reqNames.hasMoreElements()) {
			attributeName = (String) reqNames.nextElement();
			if (attributeName.startsWith("RAPTORNUKE_SHARED_"))
				sb.append("Request:" + attributeName + "="
						+ this.getRequestAttribute(attributeName) + "\n");
		}

		Enumeration sessNames = req.getSession().getAttributeNames();
		attributeName = "";
		while (sessNames.hasMoreElements()) {
			attributeName = (String) sessNames.nextElement();
			if (attributeName.startsWith("COMPANY_")
					|| attributeName.startsWith("USER_")
					|| attributeName.startsWith("RAPTORNUKE_SHARED_"))
				sb.append("Session:" + attributeName + "="
						+ this.getSessionAttribute(attributeName) + "\n");
		}
		_stateStr = sb.toString();

		_urlStr = ZkPortletUtil.buildUrl(this.getPortletRequest(),
				this.getPortletResponse(), A_FORM_EDIT,
				new HashMap<String, Object>());

		sb = new StringBuffer(StringPool.BLANK);
		String prp = req.getParameter(WebKeys.PUB_RENDER_PARAM_ID_PRP1);
		if (Validator.isNotNull(prp))
			sb.append("共享呈现参数:" + prp + "\n");
		mesgLbl.setValue(sb.toString());
	}

	/**
	 * forward方法触发
	 */
	public void onSubmit() {
		//test bandbox by zz
		String text="";
		text+="returnType="+treeChooserBandbox.getReturnType()+" ;";
		text+="resultText="+treeChooserBandbox.getResultText()+" ;";
		ZkUtil.showInformation(text, "debug");
		//end
		
		if (!"123456".equals(passwordTxtBox.getValue())) {
			mesgLbl.setValue("密码错误，密码为123456");
		} else {
			mesgLbl.setValue(StringPool.BLANK);
			// 建立参数表
			Map<String, FormResult> mapArgs = new HashMap<String, FormResult>();
			FormResult formResult = new FormResult();
			formResult.setUsername(usernameTxtBox.getValue());
			formResult.setPassword(passwordTxtBox.getValue());
			formResult.setDate(dateDateBox.getValue());
			formResult.setRemember(rememberCheckBox.isChecked());
			mapArgs.put(WebKeys.FORM_RESULT, formResult);
			// 新建窗口
			final Window win = (Window) ZkUtil.createComponents(A_FORM_RESULT,
					null, mapArgs);
			win.setTitle("结果");
			win.setMaximizable(false);
			win.setWidth("485px");
			win.setClosable(false);
			try {
				win.doModal();
			} catch (Exception e) {
			}
			// "okBtn"必须与A_FORM_RESULT的zul中的元素id匹配
			win.getFellow("okBtn").addEventListener(Events.ON_CLICK,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							ZkUtil.showQuestion("确定关闭结果窗口?", "结果窗口关闭",
									new EventListener() {
										public void onEvent(Event event)
												throws Exception {
											if (Messagebox.ON_OK.equals(event
													.getName()))
												win.detach();// 关闭窗口
										}
									});
						}
					});
		}
	}

	/**
	 * forward方法触发
	 */
	public void onReset() {
		dateDateBox.setValue(new Date());
		rememberCheckBox.setChecked(false);
		mesgLbl.setValue(StringPool.BLANK);
		usernameTxtBox.setFocus(true);
	}

	/**
	 * 动作$ID触发
	 */
	public void onClick$stateBtn() {
		StringBuffer sb = new StringBuffer(_stateStr);
		sb.append("getLayoutId=" + this.getLayout().getLayoutId() + "\n");
		sb.append("getPlid=" + this.getThemeDisplay().getPlid() + "\n");
		sb.append("getCompanyId=" + this.getCompanyId() + "\n");
		sb.append("getLocale=" + this.getLocale().toString() + "\n");
		ZkUtil.showInformation(sb.toString(), "状态信息");
	}

	/**
	 * 动作$ID触发
	 */
	public void onClick$jumpBtn() {
		if (Validator.isNotNull(_urlStr)) {
			ZkUtil.showQuestion("确定跳转?", "", new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (Messagebox.ON_OK.equals(event.getName()))
						ZkUtil.sendRedirect(_urlStr);
				}
			});
		}
	}

	public void onClick$testBtn(){
		html.setContent("<h4>Hi, ${parent.title}</h4>"+
        "<p>It is the content of the html component.</p>");
	}
}
