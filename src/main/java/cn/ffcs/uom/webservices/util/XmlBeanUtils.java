package cn.ffcs.uom.webservices.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class XmlBeanUtils {
	private static XmlBeanUtils xmlBeanUtils = null;
	Logger logger = Logger.getLogger(XmlBeanUtils.class);

	private XmlBeanUtils() {
	}

	public static final XmlBeanUtils getInstance() {
		if (xmlBeanUtils == null) {
			xmlBeanUtils = new XmlBeanUtils();
		}
		return xmlBeanUtils;
	}

	// XML转换成Java对象
	@SuppressWarnings("rawtypes")
	public Object transformXml(String xmlString, Class transClass) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(transClass);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			Object returnObj = unmarshaller.unmarshal(reader);
			return returnObj;
		} catch (JAXBException e) {
			logger.info("xml解析异常，异常详情：" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	// Java对象转换成XML
	public String transformObj(Object obj) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(obj.getClass());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(obj, doc);

			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			String outXml = writer.toString();
			return outXml;
		} catch (JAXBException e) {
			e.printStackTrace();
			logger.info("实例化对象失败，异常详情：" + e.toString());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.info("解析配置失败，异常详情：" + e.toString());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			logger.info("解析传输配置失败，异常详情：" + e.toString());
		} catch (TransformerException e) {
			e.printStackTrace();
			logger.info("异常详情：" + e.toString());
		}
		return null;
	}
}
