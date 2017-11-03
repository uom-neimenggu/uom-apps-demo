package cn.ffcs.uom.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * File Name: ExportExcel.java Description: 利用开源组件POI3.0.2动态导出多个EXCEL文档 ！
 * Copyright(c) 2010-2013 yutian.com Inc. All Rights Reserved. Other:
 * Date：2013-10-25 Modification Record 1:
 * 
 * @version 1.0 应用泛型，代表任意一个符合javabean风格的类
 *          注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *          byte[]表jpg格式的图片数据
 */
public class ExportExcelNew {
	private final static Logger log = Logger.getLogger(ExportExcel.class);

	// 声明一个工作薄
	private static XSSFWorkbook workbook = null;
	// 生成一个表格
	private static XSSFSheet sheet = null;
	// 产生表格标题行
	private static XSSFRow row = null;
	// 声明一个画图的顶级管理器
	private static XSSFDrawing patriarch = null;

	public static void exportExcel(String headerName,
			List<Map<String, Object>> list, OutputStream out) {
		exportExcel(headerName, list, out, "yyyy-MM-dd");
	}

	public static void exportExcel(String headerName, String[] headers,
			List<Map<String, Object>> list, OutputStream out) {
		exportExcel(headerName, list, out, "yyyy-MM-dd");
	}

	public static void exportExcel(String[] headers,
			List<Map<String, Object>> list, OutputStream out) {
		exportExcel(null, list, out, "yyyy-MM-dd");
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	public static void exportExcel(String title,
			List<Map<String, Object>> list, OutputStream out, String pattern) {
		workbook = new XSSFWorkbook();
		if (title == null || title == "") {
			title = "sheet1";
		}
		// 生成一个样式
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		// 设置这些样式
		headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		headerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		headerStyle.setFont(font);
		
		// 生成并设置另一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font2);

		sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		row = sheet.createRow(0);
		String[] header = {};
		List<String> headerList = new ArrayList<String>();
		Map<String, Object> map = list.get(0);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			headerList.add(entry.getKey());
		}
		header = (String[]) headerList.toArray(new String[headerList.size()]);
		for (int i = 0; i < header.length; i++) {
			XSSFCell cell = row.createCell(i);
			XSSFRichTextString text = new XSSFRichTextString(header[i]);
			cell.setCellValue(text);
			cell.setCellStyle(headerStyle);
		}
		patriarch = sheet.createDrawingPatriarch();
		// 遍历集合数据，产生数据行
		Iterator<Map<String, Object>> it = list.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			// length++;
			row = sheet.createRow(index);
			Map<String, Object> m = (Map<String, Object>) it.next();
			for (short j = 0; j < header.length; j++) {
				XSSFCell cell = row.createCell((int) j);
				cell.setCellStyle(style);
				Object value = m.get(header[j]);
				String textValue = null;
				// 判断值的类型后进行强制类型转换
				if (null != value) {
					if (value instanceof Integer) {
						int intValue = (Integer) value;
						cell.setCellValue(intValue);
					} else if (value instanceof Float) {
						float fValue = (Float) value;
						textValue = Float.toString(fValue);
						cell.setCellValue(textValue);
					} else if (value instanceof Double) {
						double dValue = (Double) value;
						textValue = Double.toString(dValue);
						cell.setCellValue(textValue);
					} else if (value instanceof Long) {
						long longValue = (Long) value;
						cell.setCellValue(longValue);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(j, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(AnchorType.byId(2));
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
				} else {
					textValue = "";
				}
				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
				if (textValue != null) {
					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches()) {
						// 是数字当作double处理
						cell.setCellValue(Double.parseDouble(textValue));
					} else {
						XSSFRichTextString richString = new XSSFRichTextString(
								textValue);
						cell.setCellValue(richString);
					}
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error(ExceptionUtils.getStackTrace(e));
			log.error("导出数据出错");
		}
	}
}
