package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.manager.SysClassManager;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Service("sysClassManager")
@Scope("prototype")
public class SysClassManagerImpl implements SysClassManager {

	@Override
	public PageInfo queryPageInfoByQuertSysClass(SysClass querySysClass,
			int currentPage, int pageSize) {
		StringBuffer hql = new StringBuffer("From SysClass where 1=1");
		List params = new ArrayList();
		if (querySysClass != null) {
			if (!StrUtil.isEmpty(querySysClass.getJavaCode())) {
				hql.append(" and javaCode=?");
				params.add(StringEscapeUtils.escapeSql(querySysClass.getJavaCode()));
			}
			if (!StrUtil.isEmpty(querySysClass.getClassName())) {
				hql.append(" and className like ?");
				params.add("%" + StringEscapeUtils.escapeSql(querySysClass.getClassName()) + "%");
			}
			if (!StrUtil.isEmpty(querySysClass.getTableName())) {
				hql.append(" and tableName=?");
				params.add(StringEscapeUtils.escapeSql(querySysClass.getTableName()));
			}
			if (null != querySysClass.getClassId()) {
				hql.append(" and classId=?");
				params.add(querySysClass.getClassId());
			}
		}
		hql.append(" order by classId");
		return querySysClass.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}

	@Override
	public void removeSysClass(SysClass sysClass) {
		sysClass.repository().removeObject(SysClass.class,
				sysClass.getClassId());
	}

	@Override
	public void updateSysClass(SysClass sysClass) {
		Date nowDate = DateUtil.getNowDate();
		sysClass.setUpdateDate(nowDate);
		sysClass.repository().updateObject(sysClass);
	}

	@Override
	public void saveSysClass(SysClass sysClass) {
		Date nowDate = DateUtil.getNowDate();
		sysClass.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		sysClass.setStatusDate(nowDate);
		sysClass.setCreateDate(nowDate);
		sysClass.repository().saveObject(sysClass);
	}
}
