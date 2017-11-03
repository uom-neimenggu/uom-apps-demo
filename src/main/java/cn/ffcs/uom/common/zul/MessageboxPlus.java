package cn.ffcs.uom.common.zul;

/*
 * Messagebox.java Purpose: Description: History: Mon Jul 18 19:07:13 2005,
 * Created by tomyeh Copyright (C) 2004 Potix Corporation. All Rights Reserved.
 * {{IS_RIGHT This program is distributed under LGPL Version 3.0 in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY. }}IS_RIGHT
 */

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.impl.MessageboxDlg;

/**
 * Represents the message box.
 * <p>
 * You don't create {@link Messagebox} directly. Rather, use {@link #show}.
 * <p>
 * A non-XUL extension.
 * 
 * @author tomyeh
 */
public class MessageboxPlus extends Messagebox {
    /**
     * .
     */
    private static Log         log         = Log.lookup(Messagebox.class);
    /**
     * .
     */
    private static String      zulTempl    = "/public/zul/message_box.zul";   //~./zul/html/messagebox.zul
                                                                              
    /**
     * A symbol consisting of a question mark in a circle.
     * <p>
     * Since 3.5.0, they are actually style class names to display the icon.
     */
    public static final String QUESTION    = "z-msgbox z-msgbox-question";
    /**
     * A symbol consisting of an exclamation point in a triangle with a yellow
     * background
     * <p>
     * Since 3.5.0, they are actually style class names to display the icon.
     */
    public static final String EXCLAMATION = "z-msgbox z-msgbox-exclamation";
    /**
     * A symbol of a lowercase letter i in a circle.
     * <p>
     * Since 3.5.0, they are actually style class names to display the icon.
     */
    public static final String INFORMATION = "z-msgbox z-msgbox-information";
    /**
     * A symbol consisting of a white X in a circle with a red background.
     * <p>
     * Since 3.5.0, they are actually style class names to display the icon.
     */
    public static final String ERROR       = "/public/zul/message_box.zul";  //"z-msgbox z-msgbox-error";
    /** Contains no symbols. */
    public static final String NONE        = null;
    
    /** A OK button. */
    public static final int    OK          = 0x0001;
    /** A Cancel button. */
    public static final int    CANCEL      = 0x0002;
    /** A Yes button. */
    public static final int    YES         = 0x0010;
    /** A No button. */
    public static final int    NO          = 0x0020;
    /** A Abort button. */
    public static final int    ABORT       = 0x0100;
    /** A Retry button. */
    public static final int    RETRY       = 0x0200;
    /** A IGNORE button. */
    public static final int    IGNORE      = 0x0400;
    
    /**
    * Shows a message box and returns what button is pressed.
    * 
    * @param message message
    * 
    * @param title
    *            the title. If null, {@link WebApp#getAppName} is used.
    * @param buttons
    *            a combination of {@link #OK}, {@link #CANCEL}, {@link #YES},
    *            {@link #NO}, {@link #ABORT}, {@link #RETRY}, and
    *            {@link #IGNORE}. If zero, {@link #OK} is assumed
    * @param icon
    *            one of predefined images: {@link #QUESTION},
    *            {@link #EXCLAMATION}, {@link #ERROR}, {@link #NONE}, or any
    *            style class name(s) to show an image.
    * @param moreMessage 更多信息
    * @param idsMsg 预留
    * @return the button being pressed (one of {@link #OK}, {@link #CANCEL},
    *         {@link #YES}, {@link #NO}, {@link #ABORT}, {@link #RETRY}, and
    *         {@link #IGNORE}). Note: if the event processing thread is
    *         disable, it always returns {@link #OK}.
    * @throws InterruptedException 异常
    */
    public static int show(final String message, final String title, final int buttons,
        final String icon, final String moreMessage, final Map<Object, Object> idsMsg,
        final EventListener listener) throws InterruptedException {
        return MessageboxPlus.show(message, title, buttons, icon, 0, listener, moreMessage, idsMsg);
    }
    
    /**
     * Shows a message box and returns what button is pressed.
     * 
     * @param message message
     * @param title
     *            the title. If null, {@link WebApp#getAppName} is used.
     * @param buttons
     *            a combination of {@link #OK}, {@link #CANCEL}, {@link #YES},
     *            {@link #NO}, {@link #ABORT}, {@link #RETRY}, and
     *            {@link #IGNORE}. If zero, {@link #OK} is assumed
     * @param icon
     *            one of predefined images: {@link #QUESTION},
     *            {@link #EXCLAMATION}, {@link #ERROR}, {@link #NONE}, or any
     *            style class name(s) to show an image.
     * @param focus
     *            one of button to have to focus. If 0, the first button will
     *            gain the focus. One of {@link #OK}, {@link #CANCEL},
     *            {@link #YES}, {@link #NO}, {@link #ABORT}, {@link #RETRY}, and
     *            {@link #IGNORE}.
     * @param listener
     *            the event listener which is invoked when a button is clicked.
     *            Ignored if null. It is useful if the event processing thread
     *            is disabled (
     *            {@link org.zkoss.zk.ui.util.Configuration#enableEventThread}).
     *            If the event processing thread is disable, this method always
     *            return {@link #OK}. To know which button is pressed, you have
     *            to pass an event listener. Then, when the user clicks a
     *            button, the event listener is invoked. You can identify which
     *            button is clicked by examining the event name (
     *            {@link org.zkoss.zk.ui.event.Event#getName}) as shown in the
     *            following table. Alternatively, you can examine the value of
     *            {@link org.zkoss.zk.ui.event.Event#getData}, which must be an
     *            integer represetinng the button, such as {@link #OK},
     *            {@link #YES} and so on.
     *            <table border="1">
     *            <tr>
     *            <td>Button</td>
     *            <td>Event Name</td>
     *            </tr>
     *            <tr>
     *            <td>OK</td>
     *            <td>onOK</td>
     *            </tr>
     *            <tr>
     *            <td>Cancel</td>
     *            <td>onCancel</td>
     *            </tr>
     *            <tr>
     *            <td>Yes</td>
     *            <td>onYes</td>
     *            </tr>
     *            <tr>
     *            <td>No</td>
     *            <td>onNo</td>
     *            </tr>
     *            <tr>
     *            <td>Retry</td>
     *            <td>onRetry</td>
     *            </tr>
     *            <tr>
     *            <td>Abort</td>
     *            <td>onAbort</td>
     *            </tr>
     *            <tr>
     *            <td>Ignore</td>
     *            <td>onIgnore</td>
     *            </tr>
     *            </table>
     * @param moreMessage 更多信息
     * @param idsMsg 预留
     * @return the button being pressed (one of {@link #OK}, {@link #CANCEL},
     *         {@link #YES}, {@link #NO}, {@link #ABORT}, {@link #RETRY}, and
     *         {@link #IGNORE}). Note: if the event processing thread is
     *         disable, it always returns {@link #OK}.
     * @throws InterruptedException 异常
     * @since 3.0.4
     */
    @SuppressWarnings("unchecked")
    public static int show(final String message, final String title, final int buttons,
        final String icon, final int focus, final EventListener listener, final String moreMessage,
        final Map idsMsg) throws InterruptedException {
        final Map params = new HashMap();
        params.put("message", message);
        params.put("title", title != null ? title
            : Executions.getCurrent().getDesktop().getWebApp().getAppName());
        params.put("icon", icon);
        params
            .put("buttons", new Integer((buttons & (MessageboxPlus.OK | MessageboxPlus.CANCEL
                | MessageboxPlus.YES | MessageboxPlus.NO | MessageboxPlus.ABORT
                | MessageboxPlus.RETRY | MessageboxPlus.IGNORE)) != 0 ? buttons
                : MessageboxPlus.OK));
        if ((buttons & MessageboxPlus.OK) != 0) {
            params.put("OK", new Integer(MessageboxPlus.OK));
        }
        if ((buttons & MessageboxPlus.CANCEL) != 0) {
            params.put("CANCEL", new Integer(MessageboxPlus.CANCEL));
        }
        if ((buttons & MessageboxPlus.YES) != 0) {
            params.put("YES", new Integer(MessageboxPlus.YES));
        }
        if ((buttons & MessageboxPlus.NO) != 0) {
            params.put("NO", new Integer(MessageboxPlus.NO));
        }
        if ((buttons & MessageboxPlus.RETRY) != 0) {
            params.put("RETRY", new Integer(MessageboxPlus.RETRY));
        }
        if ((buttons & MessageboxPlus.ABORT) != 0) {
            params.put("ABORT", new Integer(MessageboxPlus.ABORT));
        }
        if ((buttons & MessageboxPlus.IGNORE) != 0) {
            params.put("IGNORE", new Integer(MessageboxPlus.IGNORE));
        }
        params.put("moreMessage", moreMessage);
        
        final MessageboxDlg dlg = (MessageboxDlg) Executions.createComponents(
            MessageboxPlus.zulTempl, null, params);
        dlg.setButtons(buttons);
        dlg.setEventListener(listener);
        if (focus > 0) {
            dlg.setFocus(focus);
        }
        if (moreMessage != null && moreMessage.length() > 0) {
            dlg.getFellow("moreMessageDiv").setVisible(true);
            dlg.getFellow("moreMessageBtn").addEventListener(Events.ON_CLICK, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    dlg.getFellow("moreMessage").setVisible(
                        !dlg.getFellow("moreMessage").isVisible());
                    dlg.getFellow("moreSep").setVisible(!dlg.getFellow("moreSep").isVisible());
                }
            });
            if (idsMsg != null && idsMsg.containsKey("width")) {
                dlg.setWidth(idsMsg.get("width") + "");
            }
            if (idsMsg != null && idsMsg.containsKey("height")) {
                dlg.setHeight(idsMsg.containsKey("height") + "");
            }
        }
        if (dlg.getDesktop().getWebApp().getConfiguration().isEventThreadEnabled()) {
            try {
                dlg.doModal();
            } catch (final Throwable ex) {
                try {
                    dlg.detach();
                } catch (final Throwable ex2) {
                    MessageboxPlus.log.warningBriefly(
                        "Failed to detach when recovering from an error", ex2);
                }
                if (ex instanceof InterruptedException) {
                    throw (InterruptedException) ex;
                }
                throw UiException.Aide.wrap(ex);
            }
            return dlg.getResult();
        } else {
            dlg.doHighlighted();
            return MessageboxPlus.OK;
        }
    }
    
    /**
     * Sets the template used to create the message dialog.
     * <p>
     * The template must follow the default template: ~./zul/html/messagebox.zul
     * <p>
     * In other words, just adjust the label and layout and don't change the
     * component's ID.
     * 
     * @param uri
     *            模板uri
     */
    public static void setTemplate(final String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("empty");
        }
        MessageboxPlus.zulTempl = uri;
    }
    
    /**
     * Returns the template used to create the message dialog.
     * 
     * @return String
     */
    public static String getTemplate() {
        return MessageboxPlus.zulTempl;
    }
}
