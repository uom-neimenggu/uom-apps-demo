package cn.ffcs.uom.common.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtil implements ApplicationContextAware {
    
    private static ApplicationContext context; 
                                               
    public void setApplicationContext(final ApplicationContext contex)
        throws BeansException {
        ApplicationContextUtil.context = contex;
    }
    
    public static ApplicationContext getContext() {
        return ApplicationContextUtil.context;
    }
    
    public static Object getBean(final String beanId) {
        return ApplicationContextUtil.context.getBean(beanId);
    }
}
