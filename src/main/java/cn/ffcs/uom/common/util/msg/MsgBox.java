package cn.ffcs.uom.common.util.msg;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.exception.RtManagerException;
import cn.ffcs.uom.common.exception.ExceptionProcess;
import cn.ffcs.uom.common.zul.ErrorBox;
import cn.ffcs.uom.common.zul.MessageboxPlus;

@SuppressWarnings("unchecked")
public class MsgBox {
    
    /**
     * .
     */
    private static MsgBox      msgBox;
    /**
     * .
     */
    public static final String TYPE_QUEST = "question";
    /**
     * .
     */
    public static final String TYPE_INFO  = "info";
    /**
     * .
     */
    public static final String TYPE_WARN  = "warn";
    /**
     * .
     */
    public static final String TYPE_ERROR = "error";
    /**
     * .
     */
    private String             msgType;                // 信息类型
    /**
     * .
     */
    /**
     * .
     */
    private String             message;
    /**
     * .
     */
    private String             title;
    /**
     * .
     */
    private EventListener      eventListener;
    /**
     * .
     */
    private String             moreMessage;
    /**
     * .
     */
    private Map                idsMap;
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public EventListener getEventListener() {
        return eventListener;
    }
    
    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
    
    public String getMoreMessage() {
        return moreMessage;
    }
    
    public void setMoreMessage(String moreMessage) {
        this.moreMessage = moreMessage;
    }
    
    public Map getIdsMap() {
        return idsMap;
    }
    
    public void setIdsMap(Map idsMap) {
        this.idsMap = idsMap;
    }
    
    public String getMsgType() {
        return msgType;
    }
    
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    
    /**
     * .
     * 
     * @param key
     *            String
     */
    protected MsgBox(final String key) {
        MessageboxPlus.setTemplate("/public/zul/message_box.zul");
    }
    
    /**
     * .
     * 
     * @param clazz
     *            Class
     */
    protected MsgBox(final Class clazz) {
        MessageboxPlus.setTemplate("/pages/zul/message_box.zul");
    }
    
    /**
     * .
     * 
     * @param key
     *            Class
     * @return MsgBox
     */
    public static MsgBox getMsgBox(final String key) {
        return new MsgBox(key);
    }
    
    /**
     * .
     * 
     * @param clazz
     *            Class
     * @return MsgBox
     */
    public static MsgBox getMsgBox(final Class clazz) {
        return new MsgBox(clazz);
    }
    
    /**
     * .
     * 
     * @param key
     *            String
     * @return MsgBox
     */
    public static MsgBox instance(final String key) {
        if (MsgBox.msgBox == null) {
            MsgBox.msgBox = MsgBox.getMsgBox(key);
        }
        return MsgBox.msgBox;
    }
    
    /**
     * 弹出消息提示框。
     * 
     * @param e
     *            RtManagerException
     */
    public void showError(RtManagerException e) {
        this.showError(e.getMessage(), "错误信息", e.getEcodeForDisp());
    }
    
    /**
     * 弹出消息提示框。
     * 
     * @param e
     *            RtManagerException
     */
    public void showError(Exception e) {
        String msg = e.getMessage() == null ? (e.getCause() + "\n" + ExceptionProcess
            .getExceptionStackTraceDesc(e))
            : e.getMessage();
        this.showError(msg, "错误提示", ExceptionProcess.getExceptionStackTraceString(e));
    }
    
    /**
     * 弹出消息提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     * @param idsMap
     *            预留，控件消息map
     */
    public void showInfo(final String message, final String title, final String moreMessage,
        final Map idsMap) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.INFORMATION, moreMessage,
                idsMap, this.eventListener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 弹出消息提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     */
    public void showInfo(final String message, final String title, final String moreMessage) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.INFORMATION, moreMessage,
                null, this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 弹出消息提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     */
    public void showInfo(final String message, final String title, final String moreMessage,
        String methodName, String code) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.INFORMATION, moreMessage,
                null, this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 弹出警告提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     * @param idsMap
     *            预留，控件消息map
     */
    public void showWarn(final String message, final String title, final String moreMessage,
        final Map idsMap) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.EXCLAMATION, moreMessage,
                idsMap, this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 弹出警告提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     */
    public void showWarn(final String message, final String title, final String moreMessage) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.EXCLAMATION, moreMessage,
                null, this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 弹出消息提示框
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     * @param idsMap
     *            预留，控件消息map
     */
    public void showError(final String message, final String title, final String moreMessage,
        final Map idsMap) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.ERROR, moreMessage,
                idsMap, this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 弹出消息提示框.
     * 
     * @param message
     *            提示消息
     * @param title
     *            提示框标题
     * @param moreMessage
     *            更多信息
     */
    public void showError(final String message, final String title, final String moreMessage) {
        try {
            MessageboxPlus.show(message, title, Messagebox.OK, Messagebox.ERROR, moreMessage, null,
                this.eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
    }
    
    /**
     * 询问提示框
     * <p>
     * 如果禁用事件处理线程，该方法会立即返回，返回值永远为true。 如果作为if判断语句的条件，
     * 那么else部分永远不会执行，启用和开启事件处理请查看zk.xml配置: <br />
     * &lt;system-config&gt;<br />
     * &lt;disable-event-thread&gt;false&lt;/disable-event-thread&gt;<br />
     * &lt;/system-config&gt;
     * 
     * @param message
     *            提示消息 提示框标题
     * @param title
     *            标题
     * @return 禁用事件处理线程该方法永远返回true，启用事件处理相称时，如果用户点击ok按钮，返回true,反之false
     */
    public boolean showQuestion(final String message, final String title) {
        try {
            return Messagebox.OK == Messagebox.show(message, title, Messagebox.OK
                | Messagebox.CANCEL, Messagebox.QUESTION);
        } catch (final InterruptedException e) {
            // ignore
            return false;
        }
    }
    
    /**
     * 询问提示框
     * <p>
     * 该方法是一个类似 {@link #showQuestion(String, String)}
     * 的方法，但与其不同的是，当禁用事件处理线程时，该方法非常有用。
     * <p>
     * <p>
     * 示例:<br />
     * <hr>
     * 
     * <pre>
     * ZkUtils.showQuestion(&quot;您确定删除该记录吗？&quot;, &quot;询问&quot;, new EventListener() {
     *     &#064;Override
     *     public void onEvent(Event event) throws Exception {
     *         int clickedButton = (Integer) event.getData();
     *         if (clickedButton == Messagebox.OK) {
     *             // 用户点击的是确定按钮
     *         } else {
     *             // 用户点击的是取消按钮
     *         }
     *     }
     *     
     * });
     * </pre>
     * <hr>
     * <p>
     * <table border="1">
     *<tr>
     * <td>按钮名称</td>
     * <td>事件名称</td>
     * </tr>
     *<tr>
     * <td>确定</td>
     * <td>onOK</td>
     * </tr>
     *<tr>
     * <td>取消</td>
     * <td>onCancel</td>
     * </tr>
     *</table>
     * 
     * @param message
     *            信息
     * @param title
     *            标题
     * @param eventListener
     *            事件监听方法
     */
    public void showQuestion(final String message, final String title,
        final EventListener eventListener) {
        try {
            Messagebox.show(message, title, Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                eventListener);
        } catch (final InterruptedException e) {
            // ignore
        }
        
    }
    
    /**
     * .
     */
    public void showMsg() {
        if (MsgBox.TYPE_QUEST.equals(this.msgType)) {
            if (this.eventListener == null) {
                showQuestion(this.message, this.title);
            } else {
                showQuestion(this.message, this.title, this.eventListener);
            }
        } else if (MsgBox.TYPE_INFO.equals(this.msgType)) {
            
            showInfo(this.message, this.title, this.moreMessage);
            
        } else if (MsgBox.TYPE_WARN.equals(this.msgType)) {
            
            showWarn(this.message, this.title, this.moreMessage);
            
        } else if (MsgBox.TYPE_ERROR.equals(this.msgType)) {
            
            showError(this.message, this.title, this.moreMessage);
            
        }
    }
    
    /**
     * 方法功能:
     * 只需要传入提示信息即可 .
     * 
     * @param msg
     *            String
     * @author: Liuzhuangfei
     * @修改记录：
     *        ==============================================================<br>
     *        日期:2011-2-25 Liuzhuangfei 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    public void showWarn(final String msg) {
        this.showWarn(msg, "提示信息", "");
    }
    
    /**
     * .
     * 
     * @param comp
     *            parant Component
     * @param msg
     *            String
     * @author wuyx
     *         2011-5-28 wuyx
     */
    public void showPopInfo(Component comp, String msg) {
        ErrorBox eb = ErrorBox.newInstance();
        eb.showError(comp, msg);
        eb = null;
    }
    
}
