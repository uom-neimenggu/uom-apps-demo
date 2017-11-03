package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.ChannelPackareaRelationListboxComposer;


/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年9月19日
 * @功能说明：包区和网点关系的主页面bean
 *
 */
public class ChannelPackareaRelationMainBean {
    /**
     * 包区和网点的页面window
     */
    @Setter
    @Getter
    private Window channelPackareaRelationMainWin;
    
    /**
     * 包区网点关系展示列表
     */
    @Setter
    @Getter
    private ChannelPackareaRelationListboxComposer channelPackareaRelationListbox;
}
