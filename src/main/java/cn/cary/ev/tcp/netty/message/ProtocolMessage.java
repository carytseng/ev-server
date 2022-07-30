package cn.cary.ev.tcp.netty.message;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;


@Data
public class ProtocolMessage {
	//分类
	int classfiy;
	//命令
	int order ;
	//设备ID
	int deviceId ;
	//信息代码
	int packetFlag ;
	//数据码
	String data;
	private Map dateAttr = new HashMap();

	JSONObject json;

	public ProtocolMessage(JSONObject json) {
		this.json = json;
		this.classfiy =  json.getIntValue("Classfiy"); 
		this.order =  json.getIntValue("Order");
		this.deviceId =  json.getIntValue("DeviceId");
		this.packetFlag =  json.getIntValue("PacketFlag");
		this.data  = json.getString("Data");
	}

	public byte[] toBytes() {
		return json.toString().getBytes();
	}

	@Override
	public String toString() {
		String s = String.format("DeviceId:%d PacketFlag:%08X Classfiy:%02X Order:%02X Data:%s",getDeviceId(),getPacketFlag(), getClassfiy(),getOrder(),getData());
		return "Vcard2Message ["+s+"]";
	}

} 
