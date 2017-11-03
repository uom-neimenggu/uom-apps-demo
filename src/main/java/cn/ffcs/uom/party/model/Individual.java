package cn.ffcs.uom.party.model;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.uom.common.model.UomEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 个人
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 *
 */
public class Individual extends UomEntity implements Serializable{
    
    /**
     * .
     */
    private static final long serialVersionUID = 1L;

    public Individual() {
    }

    /**
     * 创建对象实例.
     * 
     * @return Staff
     */
    public static Individual newInstance() {
        return new Individual();
    }
    /**
     * 个人标识
     */    
	public Long getIndividualId() {
		return super.getId();
	}

	public void setIndividualId(Long individualId) {
		super.setId(individualId);
	}
	/**
     * 参与人标识
     */
    @Setter
    @Getter
    private Long partyId;
    
    /**
     * 出生日期
     */
    @Setter
    @Getter
    private Date birthday;
    
    /**
     * 婚姻状况
     */
    @Setter
    @Getter
    private String marriageStatus;
    
    /**
     * 政治面貌
     */
    @Setter
    @Getter
    private String politicsStatus;
    
    /***
     * 教育水平
     */
    @Setter
    @Getter
    private String educationLevel;
    /**
     * 性别
     */
    @Setter
    @Getter
    private String gender;
    /**
     * 国籍
     */
    @Setter
    @Getter
    private String nationality;
    /**
     * 民族
     */
    @Setter
    @Getter
    private String nation;
    /**
     * 祖籍
     */
    @Setter
    @Getter
    private String nativePlace;
    /**
     * 单位
     */
    @Setter
    @Getter
    private String employer;
    /**
     * 宗教
     */
    @Setter
    @Getter
    private String religion;
    /**
     * 同名编码
     */
    @Setter
    @Getter
    private String sameNameCode;
    
}
