/**
 * 
 */
package cn.ffcs.uom.information.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.ffcs.raptornuke.plugin.common.json.jackson.JsonUtil;

/**
 * @author 曾臻
 * @date 2013-1-15
 *
 */
public class UploadUtil {
	private static HashMap<String, String> extMap = new HashMap<String, String>();
	static{
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
	}
	public static String getFileExt(FileItem item){
		String fileName = item.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		return fileExt;
	}
	public static void checkFileType(FileItem item){
		String fileExt=getFileExt(item);
		if(!Arrays.<String>asList(extMap.get("image").split(",")).contains(fileExt))
			throw new RuntimeException("只允许图片格式。");
	}
	public static String getError(String message) {
		Map<String,Object> obj=new HashMap<String,Object>();
		obj.put("error", 1);
		obj.put("message", message);
		return JsonUtil.object2Json(obj);
	}
	public static String getSuccess(String message) {
		Map<String,Object> obj=new HashMap<String,Object>();
		obj.put("error", 0);
		obj.put("message", message);
		return JsonUtil.object2Json(obj);
	}
}
