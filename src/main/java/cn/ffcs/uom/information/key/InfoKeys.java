/**
 * 
 */
package cn.ffcs.uom.information.key;

/**
 * @author 曾臻
 * @date 2013-1-14
 *
 */
public class InfoKeys {
	
	/**
	 *	新上传和加载出来的图片标识。用于发布时对比最终标识集，以得出需要清除的数据库临时图片。
	 */
	public static final String UPLOADED_AND_LOADED_IMG_IDS="UPLOADED_AND_LOADED_IMG_IDS";
	
	/**
	 * 上次上传的缩略图标识。用于新上传后将其从数据库中清除。
	 */
	public static final String PREV_THUMBNAIL_ID="PREV_THUMBNAIL_ID";
}
