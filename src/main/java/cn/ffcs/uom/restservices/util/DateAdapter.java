package cn.ffcs.uom.restservices.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

	// private String pattern = "yyyy-MM-dd HH:mm:ss";
	private String pattern = "yyyyMMddHHmmss";
	private SimpleDateFormat fmt = new SimpleDateFormat(pattern);

	@Override
	public Date unmarshal(String dateStr) throws Exception {
		if (dateStr == null) {
			return null;
		}
		return fmt.parse(dateStr);
	}

	@Override
	public String marshal(Date date) throws Exception {
		if (date == null) {
			return null;
		}
		return fmt.format(date);
	}

}
