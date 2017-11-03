package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.FilterConfigDao;

/**
 *类实体.
 * 
 * @author
 * 
 **/
public class FilterConfig extends UomEntity implements Serializable {
	/**
	 *类主键.
	 **/
	public Long getSystemFilterConfigId() {
		return super.getId();
	}

	public void setSystemFilterConfigId(Long systemFilterConfigId) {
		super.setId(systemFilterConfigId);
	}
	/**
	 *过滤字符
	 **/
	@Getter
	@Setter
	private String filterChar;
	/**
	 * 创建对象实例.
	 * 
	 * @return FilterConfig
	 */
	public FilterConfig() {
		super();
	}

	public FilterConfig(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(FilterConfig.class);
			if (!StrUtil.isEmpty(seqName)) {
				this.setSystemFilterConfigId(FilterConfig.repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static FilterConfigDao repository() {
		return (FilterConfigDao) ApplicationContextUtil.getBean("filterConfigDao");
	}

	
	public String getStatusCdName(){
        if(!StrUtil.isNullOrEmpty(this.getStatusCd())){
            if(BaseUnitConstants.ENTT_STATE_ACTIVE.equals(this.getStatusCd())){
                return "生效";
            }else {
                return "失效";
            }
        }
        return "";
    }
}
