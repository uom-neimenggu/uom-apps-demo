package cn.ffcs.uom.position.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.position.manager.CtPositionManager;
import cn.ffcs.uom.position.model.CtPosition;

@Service("ctPositionManager")
@Scope("prototype")
public class CtPositionManagerImpl implements CtPositionManager {

	@Override
	public List<CtPosition> queryCtPositionList(CtPosition ctPosition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM CT_POSITION A WHERE A.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (ctPosition != null && ctPosition.getPositionName() != null) {
			sb.append(" AND A.POSITION_NAME=?");
			params.add(StringEscapeUtils.escapeSql(ctPosition.getPositionName()));
		}
		if (ctPosition != null
				&& ctPosition.getPositionCode() != null) {
			sb.append(" AND A.POSITION_CODE like ?");
			params.add("%"+StringEscapeUtils.escapeSql(ctPosition.getPositionCode()));
		}
		return CtPosition.repository().jdbcFindList(sb.toString(), params,
				CtPosition.class);
	}

	@Override
	public CtPosition queryCtPosition(CtPosition ctPosition) {
		List<CtPosition> list = this.queryCtPositionList(ctPosition);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
