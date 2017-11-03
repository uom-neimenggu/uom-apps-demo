package cn.ffcs.uom.common.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.OperateLogDao;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.position.dao.PositionDao;

@Service("operateLogManager")
@Scope("prototype")
public class OperateLogManagerImpl implements OperateLogManager {

	@Resource
	private OperateLogDao operateLogDao;

	@Override
	public PageInfo queryPageInfoByOrgTree(OperateLog queryOperateLog,
			int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM OPERATE_LOG A WHERE A.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryOperateLog != null) {
			if (queryOperateLog.getCreateDate() != null) {
				sql.append(" AND A.CREATE_DATE > ?");
			}
			params.add(queryOperateLog.getCreateDate());
		}

		sql.append(" ORDER BY A.CREATE_DATE DESC");

		return queryOperateLog.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, OperateLog.class);
	}
	
	// 查询最新记录
	@SuppressWarnings("unchecked")
	@Override
	public List<OperateLog> queryMaxOperateLogList(OperateLog operateLog) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM OPERATE_LOG WHERE STATUS_CD = ?");
		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		sb.append(" AND CREATE_DATE in ( SELECT MAX(CREATE_DATE) FROM OPERATE_LOG WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (operateLog != null) {

			if (operateLog.getCreateDate() != null) {
				sb.append(" AND CREATE_DATE > ?");
				params.add(operateLog.getCreateDate());
			}

		}

		sb.append(" )");

		List<OperateLog> operateLogList = OperateLog.repository().jdbcFindList(
				sb.toString(), params, OperateLog.class);

		if (operateLogList != null && operateLogList.size() > 0) {
			return operateLogList;
		} else {
			return null;
		}

	}

	//通过批次号来进行多页展示
	@Override
	public List<OperateLog> queryOperateLogList(OperateLog operateLog) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM OPERATE_LOG WHERE STATUS_CD = ?");
		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (operateLog != null) {

			if (!StrUtil.isEmpty(operateLog.getOpeType())) {
				sb.append(" AND OPE_TYPE = ?");
				params.add(operateLog.getOpeType());
			}

			if (!StrUtil.isEmpty(operateLog.getBatchNumber())) {
				sb.append(" AND BATCH_NUMBER = ?");
				params.add(operateLog.getBatchNumber());
			} else {
				sb.append(" AND OPERATE_LOG_ID = ?");
				params.add(operateLog.getOperateLogId());
			}


			/*if (!StrUtil.isEmpty(operateLog.getOpeObject())) {
				sb.append(" AND OPE_OBJECT != ?");
				params.add(operateLog.getOpeObject());
			}*/

			sb.append(" ORDER BY OPE_OBJECT,OPERATE_LOG_ID");

		}

		List<OperateLog> operateLogList = OperateLog.repository().jdbcFindList(
				sb.toString(), params, OperateLog.class);

		return operateLogList;
	}
	
	//通过建立中间表来进行多页展示
	/*@Override
	public List<OperateLog> queryOperateLogList(OperateLog operateLog) {
		
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM OPERATE_LOG WHERE STATUS_CD = ?");
		List params = new ArrayList();
		
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		
		if (operateLog != null) {
			
			if (!StrUtil.isEmpty(operateLog.getOpeType())) {
				sb.append(" AND OPE_TYPE = ?");
				params.add(operateLog.getOpeType());
			}
			
			if (operateLog.getCreateDate() != null) {
				sb.append(" AND CREATE_DATE = ?");
				params.add(operateLog.getCreateDate());
			}
			
			sb.append(" AND OPE_OBJECT IN (SELECT REL_TABLE_NAME FROM RELEVANCE WHERE STATUS_CD = ? AND REL_TYPE = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add("1");
			
			if (!StrUtil.isEmpty(operateLog.getOpeObject())) {
				sb.append(" AND TABLE_NAME = ?");
				params.add(operateLog.getOpeObject());
			}
			
			sb.append(") ORDER BY OPE_OBJECT,OPERATE_LOG_ID");
			
		}
		
		List<OperateLog> operateLogList = OperateLog.repository().jdbcFindList(
				sb.toString(), params, OperateLog.class);
		
		return operateLogList;
	}*/

	@Override
	public void removeOperateLog(OperateLog operateLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOperateLog(OperateLog operateLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOperateLog(OperateLog operateLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOperateLog(OperateLog operateLog, boolean isAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public OperateLog getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NodeVo> getValuesList() {

		List params = new ArrayList();
		String sql = "SELECT * FROM ORG_TREE A WHERE A.STATUS_CD = ? AND A.IS_CALC = ?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.IS_CALC_ACTIVE);

		List<OrgTree> orgTreeList = OrgTree.repository().jdbcFindList(sql,
				params, OrgTree.class);
		List<NodeVo> retAttrValues = new ArrayList();
		if (orgTreeList != null) {
			for (OrgTree orgTree : orgTreeList) {
				if (orgTree != null) {
					NodeVo vo = new NodeVo();
					vo.setId(orgTree.getOrgTreeId().toString());
					vo.setName(orgTree.getOrgTreeName().trim());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@Override
	public String getSeqBatchNumber() {
		return operateLogDao.getSeqBatchNumber();
	}

    @Override
    public List<NodeVo> getValuesListDw() {
        // TODO Auto-generated method stub
        List params = new ArrayList();
        String sql = "SELECT DISTINCT T.ORG_TREE_NAME,T.MDSION_ORG_TREE_ID FROM MDSION_ORG_TREE T WHERE T.STATUS_CD = ? and t.isshow=1";
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

        List<Map<String, Object>> mdsionorgTreeList = MdsionOrgTree.repository().getJdbcTemplate().queryForList(sql, params.toArray());
        List<NodeVo> retAttrValues = new ArrayList();
        if (mdsionorgTreeList != null) {
            for (Map<String, Object> orgTree : mdsionorgTreeList) {
                if (orgTree != null) {
                    NodeVo vo = new NodeVo();
                    vo.setId(orgTree.get("MDSION_ORG_TREE_ID").toString());
                    vo.setName(orgTree.get("ORG_TREE_NAME").toString());
                    retAttrValues.add(vo);
                }
            }
        }
        return retAttrValues;
    }

}
