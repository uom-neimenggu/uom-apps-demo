package cn.ffcs.uom.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ChineseSpellUtil {

	/**
	 * 汉字转换为汉语拼音首字母,其他字符不变
	 * 
	 * @param chinese
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chinese) {
		if (chinese != null && !chinese.trim().equals("")) {
			String spellName = "";

			// 转化为字符
			char[] nameChar = chinese.trim().toCharArray();

			// 汉语拼音格式输出类
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

			// 输出设置,大小写,音标方式,遇到“ü” 显示成v 等
			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			for (int i = 0; i < nameChar.length; i++) {
				// 判断是否为中文
				if (Character.toString(nameChar[i])
						.matches("[\\u4E00-\\u9FA5]")) {
					try {
						spellName += PinyinHelper.toHanyuPinyinStringArray(
								nameChar[i], defaultFormat)[0].charAt(0);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else {
					spellName += nameChar[i];
				}
			}
			return spellName;
		}
		return null;
	}

	/**
	 * 汉字转换位汉语拼音,其他字符不变
	 * 
	 * @param chinese
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chinese) {
		if (chinese != null && !chinese.trim().equals("")) {
			String spellName = "";
			char[] nameChar = chinese.trim().toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

			// 输出设置,大小写,音标方式,遇到“ü” 显示成v 等
			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			for (int i = 0; i < nameChar.length; i++) {
				// 判断是否为中文
				if (Character.toString(nameChar[i])
						.matches("[\\u4E00-\\u9FA5]")) {
					try {
						spellName += PinyinHelper.toHanyuPinyinStringArray(
								nameChar[i], defaultFormat)[0];
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else {
					spellName += nameChar[i];
				}
			}
			return spellName;
		}
		return null;
	}

	public static void main(String[] args) {

	}
}