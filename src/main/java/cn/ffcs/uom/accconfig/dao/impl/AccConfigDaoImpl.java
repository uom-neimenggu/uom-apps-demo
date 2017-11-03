package cn.ffcs.uom.accconfig.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.accconfig.dao.AccConfigDao;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.syslist.model.SysList;

@Repository("accConfigDao")
@Transactional
public class AccConfigDaoImpl extends BaseDaoImpl implements AccConfigDao {

	public List<AccConfig> queryAccConfig(AccConfig accConfig){
		StringBuilder sb = new  StringBuilder("select * from RBAC_ACC_CONFIG a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != accConfig){
			if(null != accConfig.getAccParentId()){
				sb.append(" and a.ACC_PARENT_ID = ?");
				params.add(accConfig.getAccParentId());
			}
		}
		sb.append(" order by a.ACC_CONFIG_ID asc");
		return super.jdbcFindList(sb.toString(), params, AccConfig.class);
	}
	
	@Override
	public PageInfo querySysAccRela(SysAccRela sysAccRela, int currentPage, int pageSize) {
		StringBuilder sb = new  StringBuilder("select b.* from RBAC_SYSTEM_LIST a,RBAC_ACC_SYSTEM_LIST_RELA b,RBAC_ACC_CONFIG c where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sysAccRela){
			if(null != sysAccRela.getSysListId()){
				sb.append(" and b.SYS_LIST_ID = ?");
				params.add(sysAccRela.getSysListId());
			}
			if(null != sysAccRela.getAccConfigId()){
				sb.append(" and b.ACC_CONFIG_ID = ?");
				params.add(sysAccRela.getAccConfigId());
			}
			SysList sysList = sysAccRela.getQrySysList();
			if(null != sysList && !StrUtil.isNullOrEmpty(sysList.getSysName())){
				sb.append(" and a.SYS_NAME LIKE ?");
				params.add("%"+ StringEscapeUtils.escapeSql(sysList.getSysName()) +"%");
			}
			if(null != sysList && !StrUtil.isNullOrEmpty(sysList.getClientCode())){
				sb.append(" and a.CLIENT_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(sysList.getClientCode()));
			}
		}
		sb.append(" and a.SYS_LIST_ID = b.SYS_LIST_ID and b.ACC_CONFIG_ID = c.ACC_CONFIG_ID");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, SysAccRela.class);
	}

	@Override
	public SysAccRela querySysAccRela(SysAccRela sysAccRela) {
		StringBuilder sb = new  StringBuilder("select * from RBAC_ACC_SYSTEM_LIST_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sysAccRela){
			if(null != sysAccRela.getAccConfigId()){
				sb.append(" and a.ACC_CONFIG_ID = ?");
				params.add(sysAccRela.getAccConfigId());
			}
			if(null != sysAccRela.getSysListId()){
				sb.append(" and a.SYS_LIST_ID = ?");
				params.add(sysAccRela.getSysListId());
			}
		}
		List<SysAccRela> rars = super.jdbcFindList(sb.toString(), params, SysAccRela.class);
		if(null != rars && rars.size() > 0){
			return rars.get(0);
		}
		return null;
	}

}
