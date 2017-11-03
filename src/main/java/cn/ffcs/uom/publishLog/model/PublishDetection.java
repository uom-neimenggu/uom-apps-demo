/**
 * 
 */
package cn.ffcs.uom.publishLog.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wenyaopeng
 *
 */
public class PublishDetection {

	@Getter
	@Setter
	private String orgTreeName;
	@Getter
	@Setter
	private String systemDesc;
	@Getter
	@Setter
	private String intfSwitchAll;
	@Getter
	@Setter
	private String intfSwitchIncrease;
	
	public PublishDetection() {
		super();
	}
	
	/**
	 * 创建对象实例.
	 * 
	 * @return
	 */
	public static PublishDetection newInstance(){
		return new PublishDetection();
	}
	
}
