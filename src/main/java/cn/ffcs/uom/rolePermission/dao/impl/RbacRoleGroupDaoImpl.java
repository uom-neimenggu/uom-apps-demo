package cn.ffcs.uom.rolePermission.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.rolePermission.dao.RbacRoleGroupDao;

@Repository("rbacRoleGroupDao")
@Transactional
public class RbacRoleGroupDaoImpl extends BaseDaoImpl implements
		RbacRoleGroupDao {
}
