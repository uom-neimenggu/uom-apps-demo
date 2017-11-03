package cn.ffcs.uom.common.exception;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class ExceptionProcess {
    
    /**
     * logger.
     */
    private static Logger logger          = Logger.getLogger(ExceptionProcess.class);
    /**
     * 异常提示内容层次.
     */
    private static int    stackHintLength = 1;
    /**
     * 异常打印层次.
     */
    private static int    stackLength     = 80;
    
    /**
     * proc.
     * 
     * @param e
     *            RtManagerException
     */
    public static void proc(RtManagerException e) {
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
        }
        try {
            if (e.getClass().equals(RtManagerException.class)) {
                // 可以再根据类，方法，配置是否进行保存数据库等操作
            	logger.info("yes");
            } else {
            	logger.info("no");
            }
        } catch (Exception ex) {
            // ignore
        }
    }
    
    /**
     * proc.
     * 
     * @param e
     *            Exception
     */
    public static void proc(Exception e) {
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
        }
        e.printStackTrace();
        try {
            if (e.getClass().equals(RtManagerException.class)) {
                // 可以再根据类，方法，配置是否进行保存数据库等操作
            	logger.info("yes");
            } else {
            	logger.info("no");
            }
        } catch (Exception ex) {
            // ignore
        }
    }
    
    /**
     * getExceptionStackTraceString.
     * 
     * @param e
     *            Exception
     * @return String
     */
    public static String getExceptionStackTraceDesc(Exception e) {
        return getExceptionStackTraceString(e, stackHintLength);
    }
    
    /**
     * getExceptionStackTraceString.
     * 
     * @param e
     *            Exception
     * @return String
     */
    public static String getExceptionStackTraceString(Exception e) {
        return getExceptionStackTraceString(e, stackLength);
    }
    
    /**
     * getExceptionStackTraceString.
     * 
     * @param e
     *            Exception
     * @return String
     */
    public static String getExceptionStackTraceString(Exception e, int dep) {
        if (e == null) {
            return null;
        }
        // Get the stack trace
        StackTraceElement[] stacka = e.getStackTrace();
        // root
        Throwable rootTh = findRootCause(e);
        StackTraceElement[] stack = rootTh.getStackTrace();
        
        // stack[0] contains the method that created the exception.
        // stack[stack.length-1] contains the oldest method call.
        // Enumerate each stack element.
        StringBuffer strBuf = new StringBuffer();
        // size
        int sum = stack == null ? 0
            : stack.length;
        // 倒数20行
        int j = 0;
        for (int i = stack.length - 1; i >= 0 && j < 20; i--) {
            strBuf.append(stack[i].getFileName());
            strBuf.append(" ");
            strBuf.append(stack[i].getClassName());
            strBuf.append(" ");
            strBuf.append(stack[i].getMethodName());
            strBuf.append(" ");
            strBuf.append(stack[i].getLineNumber());
            strBuf.append("\n");
            j++;
        }
        
        strBuf.append("\n");
        for (int i = 0; i < stack.length && i < dep; i++) {
            strBuf.append(stack[i].getFileName());
            strBuf.append(" ");
            strBuf.append(stack[i].getClassName());
            strBuf.append(" ");
            strBuf.append(stack[i].getMethodName());
            strBuf.append(" ");
            strBuf.append(stack[i].getLineNumber());
            strBuf.append("\n");
        }
        return strBuf.toString();
    }
    
    private static Throwable findRootCause(Throwable th) {
        if (th != null) {
            // Reflectively get any exception causes.
            try {
                Throwable targetException = null;
                
                // java.lang.reflect.InvocationTargetException
                String exceptionProperty = "targetException";
                if (PropertyUtils.isReadable(th, exceptionProperty)) {
                    targetException = (Throwable) PropertyUtils.getProperty(th, exceptionProperty);
                } else {
                    exceptionProperty = "causedByException";
                    //javax.ejb.EJBException
                    if (PropertyUtils.isReadable(th, exceptionProperty)) {
                        targetException = (Throwable) PropertyUtils.getProperty(th,
                            exceptionProperty);
                    }
                }
                if (targetException != null) {
                    th = targetException;
                }
            } catch (Exception ex) {
                // just print the exception and continue
                ex.printStackTrace();
            }
            
            if (th.getCause() != null) {
                th = th.getCause();
                th = findRootCause(th);
            }
        }
        return th;
    }
    
}
