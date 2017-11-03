/**
 * 
 */
package cn.ffcs.uom.publishLog.action;

import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.publishLog.action.bean.PublishDetectionBean;
import cn.ffcs.uom.publishLog.manager.PublishDetectionManager;

/**
 * @author wenyaopeng
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class PublishDetectionComposer extends BasePortletComposer{
	
	private static final long serialVersionUID = 1L;

	/**
	 * bean
	 */
	private PublishDetectionBean bean =new PublishDetectionBean();
	
	@Resource(name = "publishDetectionManager")
	private PublishDetectionManager publishDetectionManager;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	/**
	 * window 初始化
	 */
	public void onCreate$publishDetectionInfoMainWin() throws Exception{
		try {
			bindData(publishDetectionManager.queryPublishRange(arg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	    
	public void bindData(List<Map> list){
		Div div = (Div)bean.getPublishDetectionInfoDiv();//获取Div
		div.setStyle("height:436px;overflow:auto;");
		Panel panel = new Panel();
		panel.setParent(div);
		Panelchildren panelChildren = new Panelchildren();
		panelChildren.setParent(panel);
		Listbox listResult = new Listbox();
		listResult.setParent(panelChildren);
		Listhead listHead = new Listhead();// 创建行标题
		listHead.setSizable(true);//行可调整
		listHead.setParent(listResult);// 设置父容器
		Listheader treeNameHeader=new Listheader("组织树");
		treeNameHeader.setWidth("25%");
		Listheader intfNameHeader=new Listheader("接口名称");
		intfNameHeader.setWidth("25%");
		Listheader allHeader=new Listheader("全量开关");
		allHeader.setWidth("25%");
		Listheader increaseHeader=new Listheader("增量开关");
		increaseHeader.setWidth("25%");
		listHead.appendChild(treeNameHeader);
		listHead.appendChild(intfNameHeader);
		listHead.appendChild(allHeader);
		listHead.appendChild(increaseHeader);
		for (Map map : list){
			Listitem listItem = new Listitem();
		    listItem.appendChild(new Listcell(map.get("ORG_TREE_NAME").toString()));
		    listItem.appendChild(new Listcell(map.get("SYSTEM_DESC").toString()));
		    listItem.appendChild(new Listcell(map.get("INTF_SWITCH_ALL").toString()));
		    listItem.appendChild(new Listcell(map.get("INTF_SWITCH_INCREASE").toString()));
		    listItem.setParent(listResult);
		}
	}
}
