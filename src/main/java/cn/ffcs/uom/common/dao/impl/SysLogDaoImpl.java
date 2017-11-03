package cn.ffcs.uom.common.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.dao.SysLogDao;
import cn.ffcs.uom.common.model.SysLog;

@Repository("sysLogDao")
@Transactional
public class SysLogDaoImpl extends BaseDaoImpl implements SysLogDao {

}
