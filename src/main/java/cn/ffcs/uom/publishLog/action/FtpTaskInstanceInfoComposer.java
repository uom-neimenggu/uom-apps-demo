/**
 * 
 */
package cn.ffcs.uom.publishLog.action;

import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.publishLog.action.bean.FtpTaskInstanceInfoMainBean;
import cn.ffcs.uom.webservices.constants.WsConstants;

/**
 * @author wenyaopeng
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class FtpTaskInstanceInfoComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private String ftpTaskInstanceId;

	private String syncType;

	private String lastTime;

	private String thisTime;

	private List<FtpTaskInstance> ftpTaskInstanceList;

	/**
	 * bean
	 */
	private FtpTaskInstanceInfoMainBean bean = new FtpTaskInstanceInfoMainBean();

	@Resource(name = "ftpTaskInstanceManager")
	private FtpTaskInstanceManager ftpTaskInstanceManager;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window 初始化
	 */
	public void onCreate$ftpTaskInstanceInfoMainWin() throws Exception {

		ftpTaskInstanceId = arg.get("ftpTaskInstanceId").toString();
		syncType = arg.get("syncType").toString();
		lastTime = arg.get("lastTime").toString();
		thisTime = arg.get("thisTime").toString();

		queryftpTaskInstanceInfo();
	}

	public void queryftpTaskInstanceInfo() {

		FtpTaskInstance ftpTaskInstance = new FtpTaskInstance();

		ftpTaskInstance.setFtpTaskInstanceId(Long.parseLong(ftpTaskInstanceId));

		ftpTaskInstanceList = ftpTaskInstanceManager.queryFtpTaskInstanceList(ftpTaskInstance);

		FtpTaskInstance Ftp = ftpTaskInstanceList.get(0);
		int rows = Ftp.getFtpTaskInstanceInfoList().size();

		Div div = bean.getFtpTaskInstanceInfoDiv();
		div.setStyle("height:436px;overflow:auto;");

		Panel panel = new Panel();
		panel.setParent(div);

		Panelchildren panelChildren = new Panelchildren();
		panelChildren.setParent(panel);

		Listbox listResult = new Listbox();
		listResult.setParent(panelChildren);
		listResult.setRows(3);

		// 创建行
		Listitem syncTypeLi = new Listitem();
		Listitem lastTimeLi = new Listitem();
		Listitem thisTimeLi = new Listitem();

		// 设置父容器
		syncTypeLi.setParent(listResult);
		lastTimeLi.setParent(listResult);
		thisTimeLi.setParent(listResult);

		// 填充值
		syncTypeLi.appendChild(new Listcell("同步类型"));
		syncTypeLi.appendChild(new Listcell(syncType.equals(WsConstants.SYNC_ALL)?"全量":"增量"));
		lastTimeLi.appendChild(new Listcell("开始下发时间"));
		lastTimeLi.appendChild(new Listcell(lastTime));
		thisTimeLi.appendChild(new Listcell("结束下发时间"));
		thisTimeLi.appendChild(new Listcell(thisTime));

		for (FtpTaskInstance a : ftpTaskInstanceList) {

			Listbox listResult2 = new Listbox();
			listResult2.setParent(panelChildren);
			listResult2.setRows(rows);

			Listhead listHead = new Listhead();// 创建行标题
			listHead.setSizable(true);// 行可调整
			listHead.setParent(listResult2);// 设置父容器
			Listheader pListheader = new Listheader("文件名");
			pListheader.setWidth("50%");

			Listheader fListheader = new Listheader("数据量");
			fListheader.setWidth("50%");

			listHead.appendChild(pListheader);
			listHead.appendChild(fListheader);

			for (FtpTaskInstanceInfo ftpTaskInstanceInfo : a.getFtpTaskInstanceInfoList()) {
				Listitem Li = new Listitem();
				Li.setParent(listResult2);
				Li.appendChild(new Listcell(ftpTaskInstanceInfo.getFileName()));
				Li.appendChild(new Listcell(ftpTaskInstanceInfo.getDataCount().toString()));
			}

		}
	}
}
