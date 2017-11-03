package cn.ffcs.uom.common.util;

import java.util.Collection;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

/**
 * @author 曾臻
 * @date 2013-06-22
 */
public class UomZkUtil {
	/**
	 * 高度自动适应（匹配iframe高度）
	 * 在doAfterCompose中调用
	 * @author 曾臻
	 * @date 2013-6-22
	 * @param comp
	 */
	public static void autoFitHeight(Component comp){
		final Component c=comp;
		comp.addEventListener(Events.ON_CLIENT_INFO, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				ClientInfoEvent evt = (ClientInfoEvent) event;
				int h=evt.getDesktopHeight();
				Collection<Component> roots=c.getPage().getRoots();
				for(Component root:roots){
					if(root instanceof HtmlBasedComponent){
						HtmlBasedComponent w=(HtmlBasedComponent)root;
						w.setHeight((h-30)+"px");
						break;
					}
				}
			}
		});
	}
}
