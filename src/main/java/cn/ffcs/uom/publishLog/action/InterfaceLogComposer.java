/**
 * 
 */
package cn.ffcs.uom.publishLog.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.publishLog.action.bean.InterfaceLogMainBean;
import cn.ffcs.uom.publishLog.manager.InterfaceLogManager;
import cn.ffcs.uom.publishLog.model.InterfaceLog;

/**
 * @author wenyaopeng
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class InterfaceLogComposer extends BasePortletComposer{
	
	private static final long serialVersionUID = 1L;

	/**
	 * bean
	 */
	private InterfaceLogMainBean bean =new InterfaceLogMainBean();
	
	@Resource(name = "interfaceLogManager")
	private InterfaceLogManager interfaceLogManager;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	/**
	 * window 初始化
	 */
	public void onCreate$interfaceLogMainWin() throws Exception{
		bindData(interfaceLogManager.queryInterfaceLog(arg.get("ftpTaskInstanceId").toString(), arg.get("targetSystem").toString()));
	}
	    
	public void bindData(List<InterfaceLog> intfLog){
		
		if(intfLog!=null && intfLog.size() > 0){
			
			Div div = (Div)bean.getInterfaceLogQueryDiv();//获取Div
			div.setStyle("height:436px;overflow:auto;");
			
			for(InterfaceLog interfaceLog : intfLog){
				
				Panel panel = new Panel();
				panel.setParent(div);

				Panelchildren panelChildren = new Panelchildren();
				panelChildren.setParent(panel);

				Listbox listResult = new Listbox();
				listResult.setParent(panelChildren);
				
				listResult.setRows(7);
				
				Listhead listHead = new Listhead();// 创建行标题
				listHead.setSizable(true);//行可调整
				listHead.setParent(listResult);// 设置父容器
				Listheader pListheader=new Listheader("属性");
				pListheader.setWidth("30%");

				Listheader fListheader=new Listheader("字段");
				fListheader.setWidth("70%");

				listHead.appendChild(pListheader);
				listHead.appendChild(fListheader);

				//创建行
			    Listitem transIdli = new Listitem();
				Listitem logIdli = new Listitem();
				Listitem beginDateli = new Listitem();
				Listitem msgIdli = new Listitem();
				Listitem msgTypeli = new Listitem();
				Listitem systemli = new Listitem();
				Listitem errCodeli = new Listitem();
				
				//设置父容器
				transIdli.setParent(listResult);
				logIdli.setParent(listResult);
				beginDateli.setParent(listResult);
				msgIdli.setParent(listResult);
				msgTypeli.setParent(listResult);
				systemli.setParent(listResult);
				errCodeli.setParent(listResult);
				
				//添加属性和值
				logIdli.appendChild(new Listcell("日志ID"));
				logIdli.appendChild(new Listcell(StrUtil.strnull(interfaceLog.getLogId())));
				beginDateli.appendChild(new Listcell("生效时间"));
				beginDateli.appendChild(new Listcell(interfaceLog.getBeginDate().toString()));
				msgIdli.appendChild(new Listcell("消息ID"));
				msgIdli.appendChild(new Listcell(interfaceLog.getMsgId()));
				transIdli.appendChild(new Listcell("批次号"));
				transIdli.appendChild(new Listcell(interfaceLog.getTransId()));
				msgTypeli.appendChild(new Listcell("消息类型"));
				msgTypeli.appendChild(new Listcell(interfaceLog.getMsgType()));
				systemli.appendChild(new Listcell("系统编码"));
				systemli.appendChild(new Listcell(interfaceLog.getSystem()));
				errCodeli.appendChild(new Listcell("错误代码"));
				errCodeli.appendChild(new Listcell(interfaceLog.getErrCode()));
					
					//错误信息
				if(interfaceLog.getErrMsg() != null){
					Listbox errMsgList= new Listbox();
					errMsgList.setParent(panelChildren);
					errMsgList.setRows(1);
					errMsgList.setWidth("100%");
					Listitem errMsgListitem = new Listitem();
					errMsgListitem.setParent(errMsgList);
					errMsgListitem.appendChild(new Listcell("错误信息"));
					Textbox errMsgbox =new Textbox();
					errMsgbox.setWidth("100%");
					errMsgbox.setRows(3);
					errMsgbox.setParent(panelChildren);
					errMsgbox.setValue(interfaceLog.getErrMsg());
				 }
					
					//请求报文
				if(interfaceLog.getRequestContent() != null){
					Listbox  requestContentList= new Listbox();
					requestContentList.setParent(panelChildren);
					requestContentList.setRows(1);
					requestContentList.setWidth("100%");
					Listitem reqListitem = new Listitem();
					reqListitem.setParent(requestContentList);
					reqListitem.appendChild(new Listcell("请求报文"));
					Textbox requestContentbox =new Textbox();
					requestContentbox.setWidth("100%");
					requestContentbox.setRows(11);
					requestContentbox.setParent(panelChildren);
					requestContentbox.setValue(interfaceLog.getRequestContent());
				}
					//响应报文
				if(interfaceLog.getResponseContent()!= null){
					Listbox  responseContentList= new Listbox();
					responseContentList.setParent(panelChildren);
					responseContentList.setRows(1);
					responseContentList.setWidth("100%");
					Listitem resListitem = new Listitem();
					resListitem.setParent(responseContentList);
					resListitem.appendChild(new Listcell("响应报文"));
					Textbox responseContentbox =new Textbox();
					responseContentbox.setWidth("100%");
					responseContentbox.setRows(11);
					responseContentbox.setParent(panelChildren);
					responseContentbox.setValue(interfaceLog.getResponseContent());
				  }		

					transIdli.setStyle("background-color:yellow;");
					
			}
		}else{
			Div div =(Div)bean.getSystemInfoDiv();
			div.setVisible(true);
					
		}
	}
}
