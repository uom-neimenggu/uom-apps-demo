package cn.ffcs.uom.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpUtil {
	private static FTPClient ftpClient = new FTPClient();

	/**
	 * 
	 * @param filename
	 *            生成文件名
	 * @param localFilePath
	 *            文件路径：路径加文件名
	 * @param remotePath
	 *            远程路径：/uomtemp/test
	 * @return
	 */
	public static boolean uploadFile(String filename, String localFilePath,
			String remotePath) throws IOException {

		Logger logger = Logger.getLogger(FtpUtil.class);

		// 初始表示上传失败
		boolean success = false;
		try {
			// 转到指定上传目录
			ftpClient.changeWorkingDirectory(remotePath);
			// // 将上传文件存储到指定目录
			logger.info("1.14、将上传文件存储到指定目录开始: " + filename + " ->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			InputStream input = new FileInputStream(localFilePath);
			ftpClient.storeFile(filename, input);
			logger.info("1.15、将上传文件存储到指定目录结束: " + filename + " ->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 关闭输入流
			input.close();
			logger.info("1.16、关闭输入流: " + filename + " ->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 表示上传成功
			success = true;
		} catch (IOException e) {
			logger.info("1.17、上传远程FTP文件失败: " + filename + " ->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			disconnect();
			throw e;
		}
		return success;
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public static void connect(String hostname, int port, String username,
			String password) throws IOException {
		Logger logger = Logger.getLogger(FtpUtil.class);
		try {
			ftpClient.connect(hostname, port);
			ftpClient.login(username, password);
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("UTF-8");
		} catch (IOException e) {
			logger.info("1.3、连接远程FTP服务器失败->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			throw e;
		}
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public static void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 查看文件
	 * 
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static FTPFile[] findFileList(String dest) throws IOException {
		FTPFile[] ftpfiles = null;
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpfiles = ftpClient.listFiles(dest);
		return ftpfiles;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static void makeDirectory(String path) {

		Logger logger = Logger.getLogger(FtpUtil.class);

		try {
			String pathArray[] = path.split("/");
			String directory = "";
			if (pathArray != null && pathArray.length > 0) {
				for (String div : pathArray) {
					if (!StrUtil.isEmpty(div)) {
						directory += "/" + div;
						logger.info("1.8、正在创建远程FTP文件路径 "
								+ directory
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						ftpClient.makeDirectory(directory);
						logger.info("1.9、成功创建远程FTP文件路径 "
								+ directory
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
					}
				}
			}
		} catch (IOException e) {
			logger.info("1.10、创建远程FTP文件路径失败->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param fileName
	 *            上传到FTP服务器后的文件名称
	 * @param inputStream
	 *            输入文件流
	 * @return
	 */
	public static boolean uploadFile(String hostname, int port,
			String username, String password, String pathname, String fileName,
			InputStream inputStream) {
		boolean flag = false;

		ftpClient.setControlEncoding("UTF-8");
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				return flag;
			}

			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器文件目录
	 * @param filename
	 *            文件名称
	 * @param localpath
	 *            下载后的文件路径
	 * @return
	 */
	public static boolean downloadFile(String hostname, int port,
			String username, String password, String pathname, String filename,
			String localpath) {
		boolean flag = false;
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				return flag;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					String ftpFileName = file.getName();
					String[] ftpFileNameArray = ftpFileName.split("_");

					if (ftpFileNameArray.length != 3) {
						return flag;
					}

					File localFile = new File(localpath + "/"
							+ ftpFileNameArray[2]);
					OutputStream os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					flag = true;
					os.close();
				}
			}
			ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	/**
	 * 删除文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param filename
	 *            要删除的文件名称
	 * @return
	 */
	public static boolean deleteFile(String hostname, int port,
			String username, String password, String pathname, String filename) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				return flag;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

}
