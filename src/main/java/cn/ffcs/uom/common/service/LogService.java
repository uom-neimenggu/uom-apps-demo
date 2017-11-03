package cn.ffcs.uom.common.service;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.uom.common.dao.SysLogDao;
import cn.ffcs.uom.common.key.WebKeys;
import cn.ffcs.uom.common.manager.SysLogManager;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.PlatformUtil;

/**
 * 保存日志消费者类，保存日志服务
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年11月8日
 * @功能说明：
 *
 */
@Component  //保持单例，不用加
//@Scope("prototype")
public class LogService {
    
    @Qualifier("sysLogManager")
    @Autowired
    private SysLogManager sysLogManager;
    
    private final BlockingQueue<SysLog> queue;
    private final LoggerThread loggerThread;
    private boolean isShutdown = false;  //这个以后可以作为一个开关控制日志的记录
  //如果线程停止提交任务，线程不能停，先得吧剩余的任务提交结束
    private int reservations = 0;
    private final static int CAPACITY = 500; //设置一个阈值，队列长度最大为500

    public LogService() 
    {
        //队列长度
        this.queue = new LinkedBlockingQueue<SysLog>(CAPACITY);
        this.loggerThread = new LoggerThread();
        start();
    }
    
    /**
     * 启动日志服务
     * .
     * 
     * @author xiaof
     * 2016年11月8日 xiaof
     */
    public void start()
    {
        //判断这个线程是否已经启动
        if(!loggerThread.isAlive())
        {
            loggerThread.start();
        }
    }
    
    /**
     * 存放日志进入队列
     * .
     * 
     * @param sysLog
     * @throws InterruptedException
     * @author xiaof
     * 2016年11月8日 xiaof
     */
    public void log(SysLog sysLog) throws InterruptedException
    {
        //放入日志队列并阻塞队列
        synchronized(this)
        {
            if(isShutdown)
                throw new IllegalStateException("日志开关没有打开");
            ++reservations;
            Session session = Executions.getCurrent().getSession();
            User user = (User) session.getAttribute(WebKeys.RAPTORNUKE_SHARED_USER);
            sysLog.setUser(user);
        }
        queue.put(sysLog);
        start();
    }
    
    /**
     * 关闭日志线程，当开关关闭的时候
     * .
     * 
     * @author xiaof
     * 2016年11月8日 xiaof
     */
    public void stop()
    {
        synchronized(this)
        {
            isShutdown = true;
        }
        //中断线程
        loggerThread.interrupt();
    }
    
    private class LoggerThread extends Thread
    {
        @Override
        public void run() {
            //不断消费队列，吧里面的内容保存到数据库
            while(true)
            {
                try 
                {
                    //判断是否需要停止队列
                    synchronized(LogService.this)
                    {
                        //上锁，保证在判断的过程中不会有进程对队列进行操作
                        if(isShutdown && reservations == 0)
                        {
                            //判断，日志开关关闭，并且队列中没有剩余的数据没有保存到数据库
                            break;
                        }
                    }
                    
                    //去除日志信息，保存到数据库
                    SysLog tempLog = queue.take();//取出数据，并阻塞队列
                    //吧队列中残余对象减少一个
                    synchronized(LogService.this)
                    {
                        --reservations;
                    }
                    //保存进入数据库，这里记录日志保存进入的时间，也就是保存日志的结束时间
                    tempLog.setEndDate(new Date());
                    sysLogManager.saveLog(tempLog);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
