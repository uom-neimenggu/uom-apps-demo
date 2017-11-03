package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.restservices.dao.GrpIntfLogDao;

/***
 * 员工组织业务关系 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-9
 * @Email wangyong@ffcs.cn
 * @功能说明：
 * 
 */
public class GrpIntfLog implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Long grpIntfLogId;

	/**
	 * 消息ID
	 */
	@Getter
	@Setter
	private String msgId;

	/**
	 * 业务流水号
	 */
	@Getter
	@Setter
	private String transId;

	/**
	 * 集团请求报文
	 */
	@Getter
	@Setter
	private String grpRequestContent;

	/**
	 * 主数据请求报文
	 */
	@Getter
	@Setter
	private String uomRequestContent;

	/**
	 * 主数据响应报文
	 */
	@Getter
	@Setter
	private String uomResponseContent;

	/**
	 * 渠道响应报文
	 */
	@Getter
	@Setter
	private String channelResponseContent;

	/**
	 * 主数据结果
	 */
	@Getter
	@Setter
	private Long uomResult;

	/**
	 * 渠道结果
	 */
	@Getter
	@Setter
	private Long channelResult;

	/**
	 * 错误编码
	 */
	@Getter
	@Setter
	private String errCode;

	/**
	 * 错误信息
	 */
	@Getter
	@Setter
	private String errMsg;

	/**
	 * 操作人编码
	 */
	@Setter
	@Getter
	private String operatorNbr;

	/**
	 * 接口类型 1表示渠道视图信息下发接口 2表示渠道视图信息下发校验单接口
	 */
	@Setter
	@Getter
	private String interfaceType;
	/**
     * 生效时间.
     **/
    @Getter
    @Setter
    private Date effDate;
    /**
     * 失效时间.
     **/
    @Getter
    @Setter
    private Date expDate;

    /**
     * 状态.
     **/
    @Getter
    @Setter
    private String statusCd;
    /**
     * 状态时间.
     **/
    @Getter
    @Setter
    private Date statusDate;
    /**
     * 创建时间.
     **/
    @Getter
    @Setter
    private Date createDate;

    /**
     * 创建员工.
     **/
    @Getter
    @Setter
    private Long createStaff;
    /**
     * 更新时间.
     **/
    @Getter
    @Setter
    private Date updateDate;
    /**
     * 更新员工.
     **/
    @Getter
    @Setter
    private Long updateStaff;

    /**
     * 获取dao
     * 
     * @return
     */
    public static GrpIntfLogDao repository() {
        return (GrpIntfLogDao) ApplicationContextUtil.getBean("grpIntfLogDao");
    }
}
