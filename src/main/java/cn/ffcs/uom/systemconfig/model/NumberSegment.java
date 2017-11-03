package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.NumberSegmentDao;

/**
 *类实体.
 * 
 * @author
 * 
 **/
public class NumberSegment extends UomEntity implements Serializable {
	/**
	 *类主键.
	 **/
	public Long getSystemNumberSegmentId() {
		return super.getId();
	}

	public void setSystemNumberSegmentId(Long systemNumberSegmentId) {
		super.setId(systemNumberSegmentId);
	}
	/**
	 *运营商.
	 **/
	@Getter
	@Setter
	private String operationBusiness;
	/**
	 *号码段.
	 **/
	@Getter
	@Setter
	private String numberSegment;
	
	/**
	 *备注.
	 **/
	@Getter
	@Setter
	private String remarks;
	/**
	 * 创建对象实例.
	 * 
	 * @return NumberSegment
	 */
	public NumberSegment() {
		super();
	}

	public NumberSegment(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(NumberSegment.class);
			if (!StrUtil.isEmpty(seqName)) {
				this.setSystemNumberSegmentId(NumberSegment.repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static NumberSegmentDao repository() {
		return (NumberSegmentDao) ApplicationContextUtil.getBean("numberSegmentDao");
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
