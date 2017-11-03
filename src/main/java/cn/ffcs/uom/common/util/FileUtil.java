package cn.ffcs.uom.common.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.Media;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnAliasConfig;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;

public class FileUtil {

	/**
	 * 换行符
	 */
	private static String LINE_CHAR = "\r\n";
	/**
	 * 制表符
	 */
	private static String TAB_CHAR = "\t";

	public static void main(String[] args) {
		try {
			FtpUtil.connect("134.64.110.122", 21, "cpmis", "cpmis");
			List<String> filePaht = readfile("D:\\users\\1000000012");
			FtpUtil.makeDirectory("/uomtemp/20130716/CRM/1000001");
			for (String path : filePaht) {
				String fileName = path.substring(path.lastIndexOf("\\") + 1);
				FtpUtil.uploadFile(fileName, path,
						"/uomtemp/20130716/CRM/1000001");
			}
			FtpUtil.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成txt文件
	 * 
	 * @param dataList
	 *            map数据
	 * @param fileName
	 *            文件名称xxx.txt
	 * @param path
	 *            D:\\file
	 * @return
	 * @throws Exception
	 */
	public static void createFile(List<Map<String, Object>> dataList,
			String fileName, String path) throws Exception {
		FileOutputStream outputStream = null;
		try {
			File file = new File(path);
			/**
			 * 路径不存在创建路径
			 */
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new FileOutputStream(path + "/" + fileName);
			/**
			 * 写行头
			 */
			if (dataList != null && dataList.size() > 0) {
				StringBuffer rowHeader = new StringBuffer();
				Map map = dataList.get(0);
				Set keySet = map.keySet();
				Iterator it = keySet.iterator();
				while (it.hasNext()) {
					rowHeader.append(((String) it.next()) + TAB_CHAR);
				}
				rowHeader.append(LINE_CHAR);
				outputStream.write(rowHeader.toString().getBytes());
				/**
				 * 写记录
				 */
				for (Map dataMap : dataList) {
					StringBuffer rowData = new StringBuffer();
					Iterator iterator = keySet.iterator();
					while (iterator.hasNext()) {
						Object object = dataMap.get(iterator.next());
						if (object != null && object instanceof Date) {
							/**
							 * 时间格式化
							 */
							object = DateUtil.dateToStr((Date) object,
									"yyyy-MM-dd HH:mm:ss");
						}
						if (object != null && object instanceof String) {
							/**
							 * 字符串去除带有的制表符换行符等
							 */
							object = StrUtil.removeTabAndEnter((String) object);
						}
						rowData.append(object + TAB_CHAR);
					}
					rowData.append(LINE_CHAR);
					outputStream.write(rowData.toString().getBytes());
				}
			}
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取输入流
	 * 
	 * @param inputString
	 * @return
	 */
	public static InputStream getStringStream(String inputString) {
		if (inputString != null && !inputString.trim().equals("")) {
			try {
				ByteArrayInputStream inputStringStream = new ByteArrayInputStream(
						inputString.getBytes());
				return inputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取路径下的所有文件
	 * 
	 * @param filepath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> readfile(String filepath)
			throws FileNotFoundException, IOException {
		List<String> filePathList = new ArrayList<String>();
		try {
			File file = new File(filepath);
			/**
			 * 是否是文件夹
			 */
			if (!file.isDirectory()) {
				filePathList.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "/" + filelist[i]);
					if (!readfile.isDirectory()) {
						filePathList.add(readfile.getAbsolutePath());
					} else if (readfile.isDirectory()) {
						readfile(filepath + "/" + filelist[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			Logger logger = Logger.getLogger(FileUtil.class);
			logger.error("FileNotFoundException:" + e.getMessage());
		}
		return filePathList;
	}

	/**
	 * 删除路径下的所有文件及文件夹
	 * 
	 * @param delpath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean deletefile(String delpath)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "/" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + "/" + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (FileNotFoundException e) {
			Logger logger = Logger.getLogger(FileUtil.class);
			logger.error("1.20、删除本地FTP文件失败 delpath = " + delpath + " ->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logger.error("FileNotFoundException:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 从Excel文件得到二维数组，每个sheet的第一行为标题.
	 * 
	 * @param media
	 *            Excel文件
	 * @return String String
	 * @throws Exception
	 *             Exception
	 */

	public static String[][] readExcel(final Media media) throws Exception {
		return FileUtil.readExcel(media, 1);
	}

	/**
	 * 方法功能: 直接输入需要忽略的行数 .
	 * 
	 * @param media
	 *            media
	 * @param ignoreRows
	 *            ignoreRows
	 * @return String[][] String
	 * @throws Exception
	 *             Exception
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-2-10 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String[][] readExcel(final Media media, final int ignoreRows)
			throws Exception {
		final String format = media.getFormat().toLowerCase();
		if (!format.equals("xls") && !format.equals("xlsx")
				&& !"msexcel".equals(format)) {
			// MessageBox.alert("对不起，只能导入excel文件！");
			ZkUtil.showError("对不起，只能导入excel文件！", "系统提示 ");
			return null;
		}
		Workbook wb = null;
		if (format.equals("xls") || format.equals("msexcel")) {
			wb = new HSSFWorkbook(media.getStreamData());
		}
		if (format.equals("xlsx")) {
			wb = new XSSFWorkbook(media.getStreamData());
		}
		return ExcelUtil.readExcel(wb, ignoreRows);
	}

	/**
	 * 方法功能: 直接输入需要忽略的行数 .
	 * 
	 * @param media
	 *            media
	 * @param ignoreRows
	 *            ignoreRows
	 * @return String[][] String
	 * @throws Exception
	 *             Exception
	 * @author: linzhiqiang
	 * @修改记录：
	 */
	public static String[][] readExcel(final Media media, final int ignoreRows,
			int sheetNum) throws Exception {
		final String format = media.getFormat().toLowerCase();
		if (!format.equals("xls") && !format.equals("xlsx")
				&& !"msexcel".equals(format)) {
			// MessageBox.alert("对不起，只能导入excel文件！");
			ZkUtil.showError("对不起，只能导入excel文件！", "系统提示 ");
			return null;
		}
		Workbook wb = null;
		if (format.equals("xls") || format.equals("msexcel")) {
			wb = new HSSFWorkbook(media.getStreamData());
		}
		if (format.equals("xlsx")) {
			wb = new XSSFWorkbook(media.getStreamData());
		}
		return ExcelUtil.readExcel(wb, ignoreRows, sheetNum);
	}

	/**
	 * 生成txt文件
	 * 
	 * @param dataList
	 *            map数据
	 * @param fileName
	 *            文件名称xxx.txt
	 * @param path
	 *            D:\\file
	 * @return
	 * @throws Exception
	 */
	public static void createFile(List<Map<String, Object>> dataList,
			String fileName, String path, List<String> configColunmList)
			throws Exception {
		FileOutputStream outputStream = null;
		try {
			File file = new File(path);
			/**
			 * 路径不存在创建路径
			 */
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new FileOutputStream(path + "/" + fileName);
			/**
			 * 写行头
			 */
			if (dataList != null && dataList.size() > 0) {
				StringBuffer rowHeader = new StringBuffer();
				Map map = dataList.get(0);
				Set keySet = map.keySet();
				Iterator it = keySet.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (!StrUtil.isEmpty(key) && configColunmList.contains(key)) {
						rowHeader.append(key + TAB_CHAR);
					}
				}
				/**
				 * 没有配置任何字段
				 */
				if (rowHeader.length() <= 0) {
					throw new Exception("未配置任何字段");
				}

				rowHeader.append(LINE_CHAR);
				outputStream.write(rowHeader.toString().getBytes());
				/**
				 * 写记录
				 */
				for (Map dataMap : dataList) {
					StringBuffer rowData = new StringBuffer();
					Iterator iterator = keySet.iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						if (!StrUtil.isEmpty(key)
								&& configColunmList.contains(key)) {
							Object object = dataMap.get(key);
							if (object != null && object instanceof Date) {
								/**
								 * 时间格式化
								 */
								object = DateUtil.dateToStr((Date) object,
										"yyyy-MM-dd HH:mm:ss");
							}
							if (object != null && object instanceof String) {
								/**
								 * 字符串去除带有的制表符换行符等
								 */
								object = StrUtil
										.removeTabAndEnter((String) object);
							}
							rowData.append(object + TAB_CHAR);
						}
					}
					rowData.append(LINE_CHAR);
					outputStream.write(rowData.toString().getBytes());
				}
			}
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成txt文件
	 * 
	 * @param dataList
	 *            map数据
	 * @param fileName
	 *            文件名称xxx.txt
	 * @param path
	 *            D:\\file
	 * @return
	 * @throws Exception
	 */
	public static void createFile(List<Map<String, Object>> dataList,
			String fileName, String path, List<String> configColunmList,
			List<SyncTableColumnAliasConfig> syncTableColumnAliasConfigList)
			throws Exception {
		FileOutputStream outputStream = null;
		try {
			File file = new File(path);
			/**
			 * 路径不存在创建路径
			 */
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new FileOutputStream(path + "/" + fileName);
			/**
			 * 写行头
			 */
			if (dataList != null && dataList.size() > 0) {
				StringBuffer rowHeader = new StringBuffer();
				Map map = dataList.get(0);
				Set keySet = map.keySet();
				Iterator it = keySet.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (!StrUtil.isEmpty(key) && configColunmList.contains(key)) {
						/**
						 * 是否有设置别名
						 */
						String tableName = fileName.substring(0,
								fileName.indexOf("."));
						String aliasName = SyncTableColumnAliasConfig
								.getAliasNameByTableNameAndColunmName(key,
										tableName,
										syncTableColumnAliasConfigList);
						if (!StrUtil.isEmpty(aliasName)) {
							rowHeader.append(aliasName + TAB_CHAR);
						} else {
							rowHeader.append(key + TAB_CHAR);
						}
					}
				}
				/**
				 * 没有配置任何字段
				 */
				if (rowHeader.length() <= 0) {
					throw new Exception("未配置任何字段");
				}

				rowHeader.append(LINE_CHAR);
				outputStream.write(rowHeader.toString().getBytes());
				/**
				 * 写记录
				 */
				for (Map dataMap : dataList) {
					StringBuffer rowData = new StringBuffer();
					Iterator iterator = keySet.iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						if (!StrUtil.isEmpty(key)
								&& configColunmList.contains(key)) {
							Object object = dataMap.get(key);
							if (object != null && object instanceof Date) {
								/**
								 * 时间格式化
								 */
								object = DateUtil.dateToStr((Date) object,
										"yyyy-MM-dd HH:mm:ss");
							}
							if (object != null && object instanceof String) {
								/**
								 * 字符串去除带有的制表符换行符等
								 */
								object = StrUtil
										.removeTabAndEnter((String) object);
							}
							rowData.append(object + TAB_CHAR);
						}
					}
					rowData.append(LINE_CHAR);
					outputStream.write(rowData.toString().getBytes());
				}
			}
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成txt文件,增加字段顺序
	 * 
	 * @param dataList
	 *            map数据
	 * @param fileName
	 *            文件名称xxx.txt
	 * @param path
	 *            D:\\file
	 * @return
	 * @throws Exception
	 */
	public static void createSyncFtpFile(List<Map<String, Object>> dataList,
			String fileName, String path,
			List<SyncTableColumnConfig> configColunmList) throws Exception {

		BufferedWriter bw = null;
		StringBuffer rowData = new StringBuffer();
		Logger logger = Logger.getLogger(FileUtil.class);

		try {
			File file = new File(path);
			/**
			 * 路径不存在创建路径
			 */
			if (!file.exists()) {
				file.mkdirs();
			}
			bw = new BufferedWriter(new FileWriter(path + "/" + fileName));

			/**
			 * 写行头
			 */
			if (dataList != null && dataList.size() > 0) {
				logger.info("3.4、"
						+ fileName
						+ "文件开始写行头->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				StringBuffer rowHeader = new StringBuffer();
				Map map = dataList.get(0);
				Set keySet = map.keySet();
				for (SyncTableColumnConfig syncTableColumnConfig : configColunmList) {
					Boolean hasConfig = false;
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if (!StrUtil.isEmpty(key)
								&& !StrUtil.isEmpty(syncTableColumnConfig
										.getColumnName())
								&& key.equals(syncTableColumnConfig
										.getColumnName())) {
							hasConfig = true;
							break;
						}
					}
					// 配置的字段不在sql中
					if (!hasConfig) {
						throw new Exception("SyncTableColumnConfig:"
								+ syncTableColumnConfig.getColumnName()
								+ " not in BuildFileSql");
					}
					/**
					 * 是否有设置别名
					 */
					if (!StrUtil.isEmpty(syncTableColumnConfig.getAliasName())) {
						rowHeader.append(syncTableColumnConfig.getAliasName())
								.append(TAB_CHAR);
					} else {
						rowHeader.append(syncTableColumnConfig.getColumnName())
								.append(TAB_CHAR);
					}

				}
				/**
				 * 没有配置任何字段
				 */
				if (rowHeader.length() <= 0) {
					throw new Exception("未配置任何字段");
				}

				rowHeader.append(LINE_CHAR);
				bw.append(rowHeader.toString());
				logger.info("3.4、"
						+ fileName
						+ "文件结束写行头->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				/**
				 * 写记录
				 */
				logger.info("3.5、"
						+ fileName
						+ "文件开始写记录->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				for (Map dataMap : dataList) {
					rowData.setLength(0);
					for (SyncTableColumnConfig syncTableColumnConfig : configColunmList) {
						if (!StrUtil.isEmpty(syncTableColumnConfig
								.getColumnName())) {
							Object object = dataMap.get(syncTableColumnConfig
									.getColumnName());
							if (object != null && object instanceof Date) {
								// 时间格式化
								object = DateUtil.dateToStr((Date) object,
										"yyyy-MM-dd HH:mm:ss");
							}
							if (object != null && object instanceof String) {
								// 字符串去除带有的制表符换行符等
								object = StrUtil
										.removeTabAndEnter((String) object);
							}
							rowData.append(object).append(TAB_CHAR);
						}
					}
					rowData.append(LINE_CHAR);
					bw.append(rowData.toString());

				}
				logger.info("3.5、"
						+ fileName
						+ "文件结束写记录->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
			}
			logger.info("3.6、"
					+ fileName
					+ "文件开始清空缓存->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new Date()));
			bw.flush();
			logger.info("3.6、"
					+ fileName
					+ "文件结束清空缓存->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成txt文本内容
	 * 
	 * @param dataList
	 *            map数据
	 * @return
	 * @return
	 * @throws Exception
	 */

	public static String createSyncFtpFile(List<Map<String, Object>> dataList,
			List<SyncTableColumnConfig> configColunmList) throws Exception {

		StringBuffer bw = null;
		StringBuffer rowData = new StringBuffer();
		try {

			if (dataList != null && dataList.size() > 0) {
				StringBuffer rowHeader = new StringBuffer();
				Map map = dataList.get(0);
				Set keySet = map.keySet();
				for (SyncTableColumnConfig syncTableColumnConfig : configColunmList) {
					Boolean hasConfig = false;
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if (!StrUtil.isEmpty(key)
								&& !StrUtil.isEmpty(syncTableColumnConfig
										.getColumnName())
								&& key.equals(syncTableColumnConfig
										.getColumnName())) {
							hasConfig = true;
							break;
						}
					}

				}

				rowHeader.append(LINE_CHAR);
				bw.append(rowHeader.toString());

				for (Map dataMap : dataList) {
					rowData.setLength(0);
					for (SyncTableColumnConfig syncTableColumnConfig : configColunmList) {
						if (!StrUtil.isEmpty(syncTableColumnConfig
								.getColumnName())) {
							Object object = dataMap.get(syncTableColumnConfig
									.getColumnName());
							if (object != null && object instanceof Date) {
								// 时间格式化
								object = DateUtil.dateToStr((Date) object,
										"yyyy-MM-dd HH:mm:ss");
							}
							if (object != null && object instanceof String) {
								// 字符串去除带有的制表符换行符等
								object = StrUtil
										.removeTabAndEnter((String) object);
							}
							rowData.append(object).append(TAB_CHAR);
						}
					}
					rowData.append(LINE_CHAR);
					bw.append(rowData.toString());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bw.toString();
	}
}
