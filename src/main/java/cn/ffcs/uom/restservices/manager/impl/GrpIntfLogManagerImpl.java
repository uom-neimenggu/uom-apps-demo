package cn.ffcs.uom.restservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.restservices.dao.GrpIntfLogDao;
import cn.ffcs.uom.restservices.manager.GrpIntfLogManager;
import cn.ffcs.uom.restservices.model.GrpIntfLog;

@Service("grpIntfLogManager")
@Scope("prototype")
public class GrpIntfLogManagerImpl implements GrpIntfLogManager {

	@SuppressWarnings("unused")
	@Resource
	private GrpIntfLogDao grpIntfLogDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<GrpIntfLog> queryGrpIntfLogList(GrpIntfLog queryGrpIntfLog) {
		StringBuffer hql = new StringBuffer(
				"From GrpIntfLog where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryGrpIntfLog != null) {

			if (!StrUtil.isEmpty(queryGrpIntfLog.getMsgId())) {
				hql.append(" and msgId = ?");
				params.add(queryGrpIntfLog.getMsgId());
			}

			if (!StrUtil.isEmpty(queryGrpIntfLog.getTransId())) {
				hql.append(" and transId = ?");
				params.add(queryGrpIntfLog.getTransId());
			}

			if (!StrUtil.isNullOrEmpty(queryGrpIntfLog.getUomResult())) {
				hql.append(" and uomResult = ?");
				params.add(queryGrpIntfLog.getUomResult());
			}

			if (!StrUtil.isNullOrEmpty(queryGrpIntfLog.getChannelResult())) {
				hql.append(" and channelResult = ?");
				params.add(queryGrpIntfLog.getChannelResult());
			}

			if (!StrUtil.isEmpty(queryGrpIntfLog.getErrCode())) {
				hql.append(" and errCode = ?");
				params.add(queryGrpIntfLog.getErrCode());
			}

			if (!StrUtil.isNullOrEmpty(queryGrpIntfLog.getErrMsg())) {
				hql.append(" and errMsg like ?");
				params.add("%" + queryGrpIntfLog.getErrMsg() + "%");
			}

			if (!StrUtil.isNullOrEmpty(queryGrpIntfLog.getOperatorNbr())) {
				hql.append(" and operatorNbr like ?");
				params.add("%" + queryGrpIntfLog.getOperatorNbr() + "%");
			}

			if (!StrUtil.isNullOrEmpty(queryGrpIntfLog.getInterfaceType())) {
				hql.append(" and interfaceType = ?");
				params.add(queryGrpIntfLog.getInterfaceType());
			}

		}

		hql.append(" order by grpIntfLogId");

		return GrpIntfLog.repository().findListByHQLAndParams(hql.toString(),
				params);
	}

	/**
	 * 集团接口日志新增功能
	 * 
	 * @param grpIntfLog
	 */
	@Override
	public void addGrpIntfLog(GrpIntfLog grpIntfLog) {
		if (grpIntfLog != null) {
//			grpIntfLog.addOnly();
		}
	}

	/**
	 * 集团接口日志修改功能
	 * 
	 * @param grpIntfLog
	 */
	@Override
	public void updateGrpIntfLog(GrpIntfLog grpIntfLog) {
		if (grpIntfLog != null) {
//			grpIntfLog.updateOnly();
		}
	}

	/**
	 * 集团接口日志删除功能
	 * 
	 * @param grpIntfLog
	 */
	@Override
	public void removeGrpIntfLog(GrpIntfLog grpIntfLog) {
		if (grpIntfLog != null && grpIntfLog.getGrpIntfLogId() != null) {
//			grpIntfLog.removeOnly();
		}
	}

}
