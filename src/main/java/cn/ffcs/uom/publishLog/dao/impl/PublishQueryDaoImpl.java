package cn.ffcs.uom.publishLog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.zkoss.zul.Listbox;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.ftpsyncfile.model.BuildFileSql;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.publishLog.dao.PublishQueryDao;
import cn.ffcs.uom.publishLog.model.PublishQuery;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;

@Repository("publishQueryDao")
public class PublishQueryDaoImpl extends BaseDaoImpl implements PublishQueryDao {

	@Resource(name = "orgTreeManager")
	private OrgTreeManager orgTreeManager;
	
	@Resource(name ="buildFileSqlManager")
	private BuildFileSqlManager buildFileSqlManager;
	
	@Resource(name ="businessSystemManager")
	private BusinessSystemManager BusinessSystemManager;
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public  List<NodeVo> queryTableName() {
		
		List<NodeVo> tableNameNodeVo = new ArrayList();
		List<BuildFileSql> tableNamelist = buildFileSqlManager.queryBuildFileSqlList(null);
		if(tableNamelist !=null){
			for(BuildFileSql BuildFileSql : tableNamelist){
				NodeVo vo = new NodeVo();
				vo.setId(String.valueOf(BuildFileSql.getObjectName()));
				vo.setName(BuildFileSql.getObjectNameCn());
				vo.setDesc(BuildFileSql.getObjectNameCn());
				tableNameNodeVo.add(vo);
			}
		}
		return tableNameNodeVo;
	}
	
	@Override
	public  List<NodeVo> traversalOrgTree(){
		
		List<NodeVo> treeNodeVo = new ArrayList();
		List<OrgTree> treeList = orgTreeManager.queryOrgTreeList(null);
		if(treeList!=null){
			for(OrgTree orgTree :treeList){
				NodeVo vo = new NodeVo();
				vo.setId(String.valueOf(orgTree.getOrgTreeId()));
				vo.setName(orgTree.getOrgTreeName());
				vo.setDesc(orgTree.getOrgTreeName());
				treeNodeVo.add(vo);
			}
		}
		
		return treeNodeVo;
	}

	@Override
	public List<NodeVo> queryBusinessSystem(){
		
		List<NodeVo> businessSystemVo = new ArrayList();
		List<BusinessSystem> businessSystemList = BusinessSystemManager.queryBusinessSystemList();
		if(businessSystemList !=null){
			for(BusinessSystem BusinessSystem : businessSystemList){
				NodeVo vo = new NodeVo();
				vo.setId(BusinessSystem.getSystemCode());
				vo.setName(BusinessSystem.getSystemDesc());
				vo.setDesc(BusinessSystem.getSystemDesc());
				businessSystemVo.add(vo);
			}
		}
		return businessSystemVo;
	}
	
	/**
	 * 查询  业务系统
	 */
	public PageInfo queryBusinessSystemResults(Listbox businessListbox,int currentPage, int pageSize){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select fti.tree_id,fti.sync_type,iti.ftp_task_instance_id,iti.intf_task_instance_id,bs.system_desc,iti.target_system,iti.msg_type,case when(iti.invoke_resule=1) then '成功' else '失败' end ");
		sb.append("invoke_resule,iti.invoke_times,fti.last_date,fti.this_date,fti.remote_path,fti.notice_path from intf_task_instance iti ");
		sb.append("left join business_system bs on bs.system_code = iti.target_system left join ftp_task_instance fti on fti.ftp_task_instance_id = iti.ftp_task_instance_id");
		
		if(businessListbox!=null && businessListbox.getSelectedItem().getValue() != null){
			sb.append("  where iti.target_system = ?");
			params.add(businessListbox.getSelectedItem().getValue());
		}
		
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,pageSize, PublishQuery.class);
	}

	/**
	 * 修改数据结果（ 下发结果和下发次数）
	 * @param intfTaskInstanceId
	 * @return
	 */
	public boolean updateDataInvoke(String intfTaskInstanceId){
		boolean relust = false;
		try {
			if(intfTaskInstanceId !=null){
				StringBuilder sb = new StringBuilder("update intf_task_instance set INVOKE_RESULE=0 , INVOKE_TIMES=0 where INTF_TASK_INSTANCE_ID = ").append(intfTaskInstanceId);
				super.getJdbcTemplate().update(sb.toString());
				relust = true;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} finally{
			return relust;
		}
	}
}
