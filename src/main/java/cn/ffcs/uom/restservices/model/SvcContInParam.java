package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class SvcContInParam {

	@Setter
	@Getter
	@XmlElement(name = "CHANNEL_INFO")
	private ChannelInfoInParam channelInfo;

}
