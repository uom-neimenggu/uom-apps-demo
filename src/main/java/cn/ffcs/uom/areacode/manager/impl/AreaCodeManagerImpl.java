package cn.ffcs.uom.areacode.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.areacode.manager.AreaCodeManager;
import cn.ffcs.uom.areacode.model.AreaCode;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.StrUtil;

@Service("areaCodeManager")
@Scope("prototype")
public class AreaCodeManagerImpl implements AreaCodeManager {

	@Override
	public List<AreaCode> queryAreaCodeList(AreaCode queryAreaCode) {
		if (queryAreaCode != null) {
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM AREA_CODE A WHERE A.STATUS_CD=?");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (!StrUtil.isEmpty(queryAreaCode.getAreaCode())) {
				sb.append(" AND AREA_CODE=?");
				params.add(StringEscapeUtils.escapeSql(queryAreaCode.getAreaCode()));
			}
			return DefaultDaoFactory.getDefaultDao().jdbcFindList(
					sb.toString(), params, AreaCode.class);
		}
		return null;
	}

	@Override
	public List<AreaCode> getAreaCodeListByAreaCode(String areaCode) {
		AreaCode queryAreaCode = new AreaCode();
		queryAreaCode.setAreaCode(areaCode);
		return this.queryAreaCodeList(queryAreaCode);
	}
}
