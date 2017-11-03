package cn.ffcs.uom.staff.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.staff.manager.CtStaffPositionRefManager;
import cn.ffcs.uom.staff.model.CtStaffPositionRef;
import cn.ffcs.uom.staff.model.StaffPosition;

@Service("ctStaffPositionRefManager")
@Scope("prototype")
public class CtStaffPositionRefManagerImpl implements CtStaffPositionRefManager {

	@Override
	public void addCtStaffPositionRef(CtStaffPositionRef ctStaffPositionRef) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		ctStaffPositionRef.setBatchNumber(batchNumber);
		ctStaffPositionRef.add();//wangy 调用基类的CRUD方法
	}

	@Override
	public void removeCtStaffPositionRef(CtStaffPositionRef ctStaffPositionRef) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		ctStaffPositionRef.setBatchNumber(batchNumber);
		ctStaffPositionRef.remove();//wangy 调用基类的CRUD方法
	}

	@Override
	public List<CtStaffPositionRef> queryCtStaffPositionRefList(
			CtStaffPositionRef ctStaffPositionRef) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM CT_STAFF_POSITION_REF A WHERE A.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (ctStaffPositionRef != null && ctStaffPositionRef.getStaffId() != null) {
			sb.append(" AND A.STAFF_ID=?");
			params.add(ctStaffPositionRef.getStaffId());
		}
		if (ctStaffPositionRef != null
				&& ctStaffPositionRef.getCtPositionId() != null) {
			sb.append(" AND A.CT_POSITION_ID=?");
			params.add(ctStaffPositionRef.getCtPositionId());

		}
		if (ctStaffPositionRef != null
				&& ctStaffPositionRef.getRalaCd() != null) {
			sb.append(" AND A.RALA_CD=?");
			params.add(ctStaffPositionRef.getRalaCd());

		}
		return CtStaffPositionRef.repository().jdbcFindList(sb.toString(), params,
				CtStaffPositionRef.class);
	}

	@Override
	public CtStaffPositionRef queryCtStaffPositionRef(
			CtStaffPositionRef ctStaffPositionRef) {
		List<CtStaffPositionRef> list = this.queryCtStaffPositionRefList(ctStaffPositionRef);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
