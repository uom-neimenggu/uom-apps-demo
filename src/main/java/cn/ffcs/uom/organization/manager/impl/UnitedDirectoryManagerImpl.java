package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.manager.UnitedDirectoryManager;
import cn.ffcs.uom.organization.model.UnitedDirectory;

@Service("unitedDirectoryManager")
@Scope("prototype")
public class UnitedDirectoryManagerImpl implements UnitedDirectoryManager
{
	@Override
	public PageInfo queryPageInfoByUnitedDirectory(
			UnitedDirectory unitedDirectory, int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        if(unitedDirectory != null)
        {
            sb.append("select * from CT_ORGANIZATION c where c.del_status=1000 ");
            
            
            if (!StrUtil.isNullOrEmpty(unitedDirectory.getDeptname()))
            {
                sb.append(" AND c.deptname like ?");
                params.add("%" + StringEscapeUtils.escapeSql(unitedDirectory.getDeptname()) + "%");
            }
            
            if (!StrUtil.isNullOrEmpty(unitedDirectory.getCtou()))
            {
                sb.append(" AND c.ctou=?");
                params.add(StringEscapeUtils.escapeSql(unitedDirectory.getCtou()));
            }
            
        }
        
        return UnitedDirectory.repository().jdbcFindPageInfo(sb.toString(),
            params, currentPage, pageSize, UnitedDirectory.class);
	}
    
    

}
