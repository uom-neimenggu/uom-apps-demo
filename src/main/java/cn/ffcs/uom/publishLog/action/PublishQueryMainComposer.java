package cn.ffcs.uom.publishLog.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;
import oracle.jdbc.OracleTypes;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.ftpsyncfile.dao.BuildFileSqlDao;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.manager.SyncTableColumnConfigManager;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.publishLog.action.bean.PublishQueryMainBean;
import cn.ffcs.uom.publishLog.manager.PublishQueryManager;
import cn.ffcs.uom.publishLog.model.PublishQuery;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;


/**
 * @author wenyaopeng
 *
 *  下发查询
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class PublishQueryMainComposer extends BasePortletComposer implements IPortletInfoProvider{

	private static final long serialVersionUID = 1L;
	
	private int treeId;
	
	private String syncType;
	
	private Date lastTime;
	
	private Date thisTime;
	
	private String tableName;
	
	private int systemCode;
	
	private Boolean canPublish;
	
	private Boolean canPublishLog;
	
	private Boolean canftpTaskInstanceInfo;
	
	private PublishQuery publishQuery;
	
	/**
	 * 换行符
	 */
	private static String LINE_CHAR = "\r\n";
	/**
	 * 制表符
	 */
	private static String TAB_CHAR = "\t";
	
	
	/**
	 *bean 
	 */
	private PublishQueryMainBean bean = new PublishQueryMainBean();
	
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
        Components.wireVariables(comp,bean);
        this.bindCombobox();
        initPage();
	}
	

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name = "publishQueryManager")
	private PublishQueryManager publishQueryManager;
	
	@Resource(name = "syncTableColumnConfigManager")
	private SyncTableColumnConfigManager syncTableColumnConfigManager;
	
	@Resource(name = "buildFileSqlDao")
	private BuildFileSqlDao buildFileSqlDao;
	
	
	/**
	 *  windows 初始化
	 *  
	 * @throws Exception
	 */
	public void onCreate$publishQueryMainWin() throws Exception{
		bindCombobox();
		initPage();
	}
	
	/**
	 * 绑定combobox
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception{
		
		List<NodeVo> listBoxTree  = publishQueryManager.traversalOrgTree();
		ListboxUtils.rendererForEdit(bean.getOrgTreeId(), listBoxTree);
		
		List<NodeVo> listBoxTableName = publishQueryManager.queryTableName();
		ListboxUtils.rendererForEdit(bean.getTableName(), listBoxTableName);
		
		List<NodeVo> listBoxbusinessSystem = publishQueryManager.queryBusinessSystem();
		ListboxUtils.rendererForEdit(bean.getBusinessSystemListbox(), listBoxbusinessSystem);
	}
	
	/**
	 * 页面 初始化
	 * 
	 * @throws Exception
	 */
	public void initPage() throws Exception{
		  Calendar cal = Calendar.getInstance();
		  cal.add(Calendar.DATE, -1);
		  String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());  //设置开始下发时间为昨天     
		  bean.getLastTime().setText(yesterday);
		  
		  canPublish =false;
		  canPublishLog =false; 
		  canftpTaskInstanceInfo=false;
		  setPublishLogButtonValid(canPublish, canPublishLog,canftpTaskInstanceInfo);
	}
	
	/**
	 * 设置按钮 可用属性
	 * 
	 * @param canPublish
	 * @param canPublishLog
	 * @param canStatisticalData
	 * @throws Exception
	 */
	public void setPublishLogButtonValid(Boolean canPublish,
			 Boolean canPublishLog,Boolean canftpTaskInstanceInfo) throws Exception {
		
		if(canPublish != null){
			bean.getPublishAgainToolbarbutton().setDisabled(!canPublish);
		}
		if(canPublishLog != null){
			bean.getPublishLogToolbarbutton().setDisabled(!canPublishLog);
		}
		if(canftpTaskInstanceInfo != null){
			bean.getFtpTaskInstanceInfoToolbarbutton().setDisabled(!canftpTaskInstanceInfo);
		}	
	}
	
	
	/**
	 * 业务系统查询 请求事件
	 * 
	 * @throws Exception
	 */
	public void onBusinessSystemQueryRequest() throws Exception{
		  PageInfo pageInfo = publishQueryManager.queryBusinessSystemResults(bean.getBusinessSystemListbox(),bean.getBusinessSystemPaging().getActivePage() + 1, bean.getBusinessSystemPaging().getPageSize());
	      ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
	      bean.getBusinessSystemQueryResults().setModel(dataList);
	      bean.getBusinessSystemPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	      this.setPublishLogButtonValid(false,false,false);
	}
	
	/**
	 * 重发 请求事件
	 */
	public void onPublishLogRequest() throws Exception{
		publishQuery = (PublishQuery)(bean.getBusinessSystemQueryResults().getSelectedItem().getValue());
		publishQueryManager.msgResendCheck(publishQuery.getSyncType().toString(), publishQuery.getFtpTaskInstanceId(), publishQuery.getIntfTaskInstanceId());
	}
	
	/**
	 * 数据量统计 响应事件
	 */
	public void onFtpTaskInstanceInfoRequest() throws Exception{
		publishQuery = (PublishQuery)(bean.getBusinessSystemQueryResults().getSelectedItem().getValue());
		Map arg = new HashMap();
		if(publishQuery.getFtpTaskInstanceId()!=null){
			arg.put("ftpTaskInstanceId", publishQuery.getFtpTaskInstanceId());
			arg.put("syncType", publishQuery.getSyncType());
			arg.put("lastTime", publishQuery.getLastDate());
			arg.put("thisTime", publishQuery.getThisDate());
		}
		Window win = (Window) Executions.createComponents("/pages/publishLog/ftpTaskInstanceInfo.zul", this.self, arg);
		win.doModal();
		
	}
	
	/**
	 * 下发检测 响应事件
	 */
	public void onPublishDetectionRequest() throws Exception{
		Window win = (Window) Executions.createComponents("/pages/publishLog/publish_detection_main.zul", this.self, arg);
		win.setTitle("下发检测");
		win.doModal();
	}
	
	
	
	/**
	 * 接口日志 响应事件
	 */
	public void onInterfaceLogResponse() throws Exception{
		publishQuery = (PublishQuery)(bean.getBusinessSystemQueryResults().getSelectedItem().getValue());
		Map arg = new HashMap();
		if(publishQuery.getFtpTaskInstanceId()!=null && publishQuery.getTargetSystem()!=null){
			arg.put("ftpTaskInstanceId", publishQuery.getFtpTaskInstanceId());
			arg.put("targetSystem", publishQuery.getTargetSystem());
			
			Window win = (Window) Executions.createComponents("/pages/publishLog/interfaceLog_main.zul", this.self, arg);
			win.setTitle("接口日志");
			win.doModal();
		}
	}
	
	/**
	 * 业务系统查询结果 选中 响应事件
	 */
	public void onSelectBusinessSystemRequest() throws Exception{
		if(bean.getBusinessSystemQueryResults().getSelectedIndex() != -1){
		   publishQuery = (PublishQuery)(bean.getBusinessSystemQueryResults().getSelectedItem().getValue());
		   this.setPublishLogButtonValid(true,true,true);
		   ListboxUtils.selectByCodeValue(bean.getOrgTreeId(), publishQuery.getTreeId());
		   ListboxUtils.selectByCodeValue(bean.getTableName(), "STAFF");	   
           ListboxUtils.selectByCodeValue(bean.getSyncType(), publishQuery.getSyncType());
		   bean.getLastTime().setValue(publishQuery.getLastDate());
		   bean.getThisTime().setValue(publishQuery.getThisDate());
		}
	}
	
	/**
	 * 下发查询 响应
	 * 
	 * @throws Exception
	 */
	public void onPublishQueryIssuedRequest() throws Exception{

		if (this.bean.getOrgTreeId().getSelectedItem() == null || this.bean.getOrgTreeId().getSelectedItem().getValue() == null) {
			Messagebox.show("组织树 不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (this.bean.getSyncType().getSelectedItem() == null || this.bean.getSyncType().getSelectedItem().getValue() == null) {
			Messagebox.show("同步类型 不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (this.bean.getTableName().getSelectedItem() == null || this.bean.getTableName().getSelectedItem().getValue() == null) {
			Messagebox.show("下发表名 不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (this.bean.getLastTime().getValue() == null) {
			Messagebox.show("开始下发时间 不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (this.bean.getThisTime().getValue() == null) {
			Messagebox.show("结束下发时间 不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
		}else{
			treeId=Integer.parseInt((String)this.bean.getOrgTreeId().getSelectedItem().getValue());
			lastTime=this.bean.getLastTime().getValue();
			thisTime=this.bean.getThisTime().getValue();
			syncType=(String)this.bean.getSyncType().getSelectedItem().getValue();
			tableName=(String)this.bean.getTableName().getSelectedItem().getValue();
									
			buildFileSqlDao.createSyncTempTable((long)treeId, DateUtil.dateToStr(lastTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.dateToStr(thisTime, "yyyy-MM-dd HH:mm:ss"));
			//关键字、发布日志回填
			jdbcTemplate.execute(new ConnectionCallback<Object>() {

			@Override
			public Object doInConnection(Connection conn) throws SQLException,DataAccessException {
				CallableStatement cstmt = conn.prepareCall("{CALL PKG_OPERATELOG_PUBLISH_FTPDATA.buildFileSqlGen(?,?,?,?,?,?)}");
				cstmt.setInt(1,treeId);
				cstmt.setString(2,DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",lastTime));
				cstmt.setString(3,DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",thisTime));
				cstmt.setString(4,syncType);
				cstmt.setString(5,tableName);
				cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
				cstmt.execute();
				
				StringBuffer sb = new StringBuffer();
				SyncTableColumnConfig  stcc = SyncTableColumnConfig.newInstance();
				stcc.setOrgTreeId(Long.valueOf(treeId));
				stcc.setTableName(tableName);
				
				List<SyncTableColumnConfig> syncTableColumnConfigList = syncTableColumnConfigManager.querySyncTableColumnConfigList(stcc);
				for(SyncTableColumnConfig syncTableColumnConfig:syncTableColumnConfigList){
					if(syncTableColumnConfig!=null){
						if(!StrUtil.isEmpty(syncTableColumnConfig.getAliasName())){
							sb.append(syncTableColumnConfig.getAliasName()).append(TAB_CHAR);
						}else{
							sb.append(syncTableColumnConfig.getColumnName()).append(TAB_CHAR);
						}
					}
				}
				sb.append(LINE_CHAR);
			
				String sql = cstmt.getString(6);
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				String key = bean.getPublishKeyWordQuery().getValue();
				StringBuffer rowDataBuffer = new StringBuffer();
				while(rs.next()){
					rowDataBuffer.setLength(0);
					for(SyncTableColumnConfig syncTableColumnConfig:syncTableColumnConfigList){
						if (!StrUtil.isEmpty(syncTableColumnConfig.getColumnName())) {
							Object object = rs.getObject(syncTableColumnConfig.getColumnName());
							if (object != null && object instanceof Date) {// 时间格式化
								object = DateUtil.dateToStr((Date) object,"yyyy-MM-dd HH:mm:ss");
							}
							if (object != null && object instanceof String) {// 字符串去除带有的制表符换行符等
								object = StrUtil.removeTabAndEnter((String) object);
							}
							rowDataBuffer.append(object).append(TAB_CHAR);
						}
					}
					if(StrUtil.isEmpty(key)||rowDataBuffer.toString().indexOf(key)!=-1){
						sb.append(rowDataBuffer).append(LINE_CHAR);
					}
				}
				
				bean.getPublishQueryResults().setValue(sb.toString());
				
				return null;
			}
			
		});
			
		}
	}
	
	/**
	 * 重置 输入数据
	 * 
	 * @throws Exception
	 */
	public void onQueryReset() throws Exception{
		bean.getOrgTreeId().setSelectedItem(null);
		bean.getTableName().setSelectedItem(null);
		bean.getPublishKeyWordQuery().setValue(null);
	}
		
	
}