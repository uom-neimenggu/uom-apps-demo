package cn.ffcs.uom.webservices.util;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import cn.ffcs.uom.webservices.exception.EjbException;

/**
 * @版权：福富软件 版权所有 (c) 2007
 * @文件：com.ffcs.crm.common.util.crm.CastorParser.java
 * @所含类：CastorParser
 * @author: wuq
 * @version: V1.0
 */
public class CastorParser {

	private static Logger logger = Logger.getLogger(CastorParser.class);

	/**
	 * 构造函数.
	 */
	protected CastorParser() {

	}

	/**
	 * @param xmlstr
	 *            String
	 * @param objClass
	 *            Class
	 * @return Object
	 * @throws EjbException
	 *             EjbException
	 * @author: wuq
	 */
	public static Object toObject(final String xmlstr, final Class objClass)
			throws EjbException {
		try {
			return Unmarshaller.unmarshal(objClass, new StringReader(xmlstr));
		} catch (final Exception ex) {
			CastorParser.logger.error("castor failed to parse xml:" + xmlstr
					+ ".to class:" + objClass, ex);
			throw new EjbException(EjbException.CASTOR_XML_ERROR_MSG + xmlstr,
					ex);
		}
	}

	/**
	 * @param read
	 *            Reader
	 * @param objClass
	 *            Class
	 * @return Object
	 * @throws EjbException
	 *             EjbException
	 * @author: wuq
	 */
	public static Object toObject(final Reader read, final Class objClass)
			throws EjbException {

		try {
			return Unmarshaller.unmarshal(objClass, read);
		} catch (final Exception ex) {
			CastorParser.logger.error("castor failed to parse read:" + read
					+ ".to class:" + objClass, ex);
			throw new EjbException(EjbException.CASTOR_XML_ERROR_MSG);
		}
	}

	/**
	 * @param obj
	 *            Object
	 * @return String
	 * @throws EjbException
	 *             EjbException
	 * @author: wuq
	 */
	public static String toXML(final Object obj) {
		final Writer writer = new StringWriter();
		try {
			Marshaller.marshal(obj, writer);
			return writer.toString();
		} catch (final Exception ex) {
			CastorParser.logger.error(
					"castor failed to generate xml by Object:" + obj, ex);
			return ex.getMessage().toString();
		}
	}

}
