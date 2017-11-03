package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * 人力组织.组织关系实体.
 * 
 * @author
 * 
 **/
public class HanaHiPsnorg implements Serializable {
	
	private static final long serialVersionUID = 5187037627476389332L;
	
	@Getter
	@Setter
	private Long id;
	
	@Getter
	@Setter
	private Long monthId;

	@Getter
	@Setter
	private String unitId;

	@Getter
	@Setter
	private String begindate;

	@Getter
	@Setter
	private String creationtime;

	@Getter
	@Setter
	private String creator;

	@Getter
	@Setter
	private Long dr;

	@Getter
	@Setter
	private String empforms;

	@Getter
	@Setter
	private String enddate;

	@Getter
	@Setter
	private String endflag;

	@Getter
	@Setter
	private Long indocSource;

	@Getter
	@Setter
	private String indocflag;

	@Getter
	@Setter
	private String joinsysdate;

	@Getter
	@Setter
	private String lastflag;

	@Getter
	@Setter
	private String modifiedtime;

	@Getter
	@Setter
	private String modifier;

	@Getter
	@Setter
	private String orgglbdef1;

	@Getter
	@Setter
	private Long orgglbdef10;

	@Getter
	@Setter
	private String orgglbdef2;
	
	@Getter
	@Setter
	private Long orgglbdef3;
	
	@Getter
	@Setter
	private Long orgglbdef4;
	
	@Getter
	@Setter
	private String orgglbdef5;
	
	@Getter
	@Setter
	private String orgglbdef6;
	
	@Getter
	@Setter
	private String orgglbdef7;
	
	@Getter
	@Setter
	private Long orgglbdef8;
	
	@Getter
	@Setter
	private String orgglbdef9;
	
	@Getter
	@Setter
	private Long orgrelaid;
	
	@Getter
	@Setter
	private String pkGroup;
	
	@Getter
	@Setter
	private String pkHrorg;
	
	@Getter
	@Setter
	private String pkOrg;
	
	@Getter
	@Setter
	private String pkPsndoc;
	
	@Getter
	@Setter
	private String pkPsnorg;
	
	@Getter
	@Setter
	private Long psntype;
	
	@Getter
	@Setter
	private String startpaydate;
	
	@Getter
	@Setter
	private String stoppaydate;
	
	@Getter
	@Setter
	private String ts;
	
	@Getter
	@Setter
	private Long workage;
	
	@Getter
	@Setter
	private Date loadDate;
	
	@Getter
	@Setter
	private Long etlDay;
	
	@Getter
	@Setter
	private Long localPartitionId;
	
	/**
	 * 构造方法
	 */
	public HanaHiPsnorg() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HiPsnorg
	 */
	public static HanaHiPsnorg newInstance() {
		return new HanaHiPsnorg();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static BaseDao repository() {
		return (BaseDao) ApplicationContextUtil.getBean("baseDao");
	}

}
