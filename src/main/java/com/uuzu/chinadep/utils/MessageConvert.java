package com.uuzu.chinadep.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.common.codec.Aes;
import com.uuzu.common.codec.BaseConfig;
import com.uuzu.common.pojo.DmpRequest;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;

public class MessageConvert {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	public static RetState decode(DmpRequest dmpReq, String aesToken) {
		try {
			Aes.AesToken at = OBJECT_MAPPER.readValue(aesToken, Aes.AesToken.class);
			String inId = Aes.decodeBase64(dmpReq.getExid(), at, BaseConfig.DEFAULT_CHARSET);
			dmpReq.setExid(inId);
		} catch (Exception e) {
			return new RetState(false, "解码错误.");
		}
		
		return new RetState(true, "通过.");
	}

	public static RetState encode(DmpResponse dmpResp, String aesToken) {
		try {
			Aes.AesToken at = OBJECT_MAPPER.readValue(aesToken, Aes.AesToken.class);
			String dataRange = Aes.encodeBase64(dmpResp.getDataRange(), at, BaseConfig.DEFAULT_CHARSET);
			dmpResp.setDataRange(dataRange);
		} catch (Exception e) {
			return new RetState(false, "编码错误.");
		}
		
		return new RetState(true, "通过.");
	}
}
