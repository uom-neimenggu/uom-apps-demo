package cn.ffcs.uom.rolePermission.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.rolePermission.dao.RbacResouceRoleDao;

@Repository("rbacResouceRoleDao")
@Transactional
public class RbacResouceRoleDaoImpl extends BaseDaoImpl implements
		RbacResouceRoleDao {
}
