package cn.ffcs.uom.staff.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.staff.manager.StaffPositionManager;
import cn.ffcs.uom.staff.model.StaffPosition;

@Service("staffPositionManager")
@Scope("prototype")
public class StaffPositionManagerImpl implements StaffPositionManager {

	@Override
	public void addStaffPosition(StaffPosition staffPosition) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffPosition.setBatchNumber(batchNumber);
		staffPosition.add();//wangy 调用基类的CRUD方法
	}

	@Override
	public void removeStaffPosition(StaffPosition staffPosition) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffPosition.setBatchNumber(batchNumber);
		staffPosition.remove();//wangy 调用基类的CRUD方法
	}

	@Override
	public StaffPosition queryStaffPosition(StaffPosition staffPosition) {
		List<StaffPosition> list = this.queryStaffPositionList(staffPosition);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    @Override
	public List<StaffPosition> queryStaffPositionList(
			StaffPosition staffPosition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_POSITION A WHERE A.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (staffPosition != null && staffPosition.getStaffId() != null) {
			sb.append(" AND A.STAFF_ID=?");
			params.add(staffPosition.getStaffId());
		}
		if (staffPosition != null
				&& staffPosition.getOrgPositionRelaId() != null) {
			sb.append(" AND A.ORG_POSITION_RELA_ID=?");
			params.add(staffPosition.getOrgPositionRelaId());

		}
		return StaffPosition.repository().jdbcFindList(sb.toString(), params,
				StaffPosition.class);
	}

}
