package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * 信息实体类封装
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-7-29
 * @功能说明：
 *
 */
public class ImportInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
     * 信息标识
     */
    @Setter
    @Getter
    private String infoId;
    /**
     * 信息内容
     */
    @Setter
    @Getter
    private String infoContent;   
}
