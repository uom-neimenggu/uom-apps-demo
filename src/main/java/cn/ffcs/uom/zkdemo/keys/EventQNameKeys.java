package cn.ffcs.uom.zkdemo.keys;

import javax.xml.namespace.QName;

public interface EventQNameKeys {
	public static String DEFAULT_NAMESPACE = "http://raptornuke.ffcs.cn";
	public static String PLUGIN_NAMESPACE = "http://plugin.raptornuke.ffcs.cn";
	public static QName EVENT_QNAME_SIMPLE_VALUE = new QName(DEFAULT_NAMESPACE,
			"simpleValue");
	public static QName EVENT_QNAME_COMPLEX_VALUE = new QName(PLUGIN_NAMESPACE,
			"complexValue");
}
