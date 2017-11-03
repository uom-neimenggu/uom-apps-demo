
package cn.ffcs.uom.zkdemo.keys;


/**
 * 页面key，用在request、session、application或Struts的Action对象、ZK的Ctrl对象
 * 
 * @author lianch@ffcs.cn
 */
public class WebKeys
	implements cn.ffcs.raptornuke.portal.kernel.util.WebKeys,
	cn.ffcs.raptornuke.plugin.common.struts2.util.WebKeys,
	EventQNameKeys,
	PublicRenderParameterIdentifierKeys {

	public static final String ZK_USER_NAME = "zkUserName";

	// public static final String WEBCONTENT_CTX = "demoPlugins";

	public static final String WEBCONTENT_M = "pages";

	public static final String FORM_RESULT = "form_result";
	
	public static String CHECK_CODE_KEY = "check_code_key";

}
