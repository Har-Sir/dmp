package com.uuzu.chinadep.utils;

import com.uuzu.common.codec.Aes;
import com.uuzu.common.utils.JsonUtil;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AesCodecTest extends TestCase {

//	private String aesTokenStr = "{\"key_algorithm\":\"AES\", \"cipher_algorithm\":\"AES/ECB/PKCS5Padding\", \"key\":\"1234667890123456\", \"iv_parameter\":\"\"}";
	private String airunTokenStr = "{\"key_algorithm\":\"AES\", \"cipher_algorithm\":\"AES/CBC/PKCS5Padding\", \"key\":\"1234667890123456\", \"iv_parameter\":\"1234567892546398\"}";

	@Test
	public void testDecodeBase64() throws Exception {
		String text = "9YldI5MQ6M9pREyMp4PNu4rOuGFqso1T6xND9CVJLvKuHXxTiL5VeQ76ikp1Z+ZTT5rnHW8PW64PglCKlCssoyvdOzUsggNd8H3WWbnN2ZDZYgUJxms/XCPDxhRv57Bj6NRLCJh8Ghkl2VXXQe0xRvEh0Lm/HKeyoFtRtmyFRNI=";
//				"tbPo5nLmfXiG9W2xPwH0hA==";
//				"SUtTLACLFEkbqYFssmyjW+ApucIuoQUMgEXb2EiTDSorvPRxDtR4qtFx+BtK+5vPg2Ur/j1gVB4GBi7k/N3zPctAg27T3dUx/I7otaxV/meTYoJSrz9Fetw1QkRJfKPJ";
//				"Q/cBszyjE2MmFu5Hrb45ikt3CE+ewvI1UrVW7wjRWyaUbK8+FuOvw991Ym4DCCWs31gC/+4RhI7Vfj2l8FQ6vgrouQvZAfoqyOa2uKGnjvLdFAV0KZSqFO/ibrTkRaogiT2YLp47k0HG/2Xkl2MPDr2m6zqjt54y81bkOXU2Av3V6Cu8x1gY95ShdXwdNtGcjBiaDFF2u2SQu32f/7rsCrIHbA8tmhfDTegAsua55CIJpWwWhPUZpNo69rMZGQV03dj+zTU9zPqWgv9mBwNWN+xWROSq9JQwjMdWOZ9K/hWelumQQ5gOIB6Y3lRX7wlrgjsgzbmfUpc4qvw5lwM/hgqmp5xOSIgpDz1SuH+R4QZaIBNeF+qiE6hP5vA+LryIDdtquxbvKArQ1IWwl7SOkxwDKyHhB9Drl2e6pK9McoMAxqAA7RnbGqSE+OHq4RXJez2cN2UBn3c9eLX1XAq67g==";
//				"1FYjiDqYWcNf4XR8XaCYSfVjAPmvVxIeOjnqmDXYkHEuGpFaiHNa7Lmvo1/b1GJEKrjEYefj4KmK07xVhFc2zvOqShQ5sTW1Aio9+l1L0pZSgtFd/HjeHgEb+J/e2FtiUzkGJydjsCAFydbCrockL3ixPk89plzy3gyb2Vahv0A=";
//				"ROVzjdet4xiMHlXTZl5UkSFxrRp7/wQod26yep5bYuo=";
//				"JE03JkgMzFY9BRFLhc862v3MOb91Enr2+hC7czR+V4ReVUmPpFIPH5S/ERIQr0p+74UVKXQ6kHq/x6aSZUU1dXwykOZvetIdpPfGUFq+GNkQCEV6/HpD8QDGGXHqp9I+";
//				"Ep14qdstRE0fJhZEGEIMz9yoz8+TMUyg0il8XLWknO6B0bzwqByubckqPqaaEjP/";
//				"UO41SfdxyH3CwctegGZLu6IElFlg9x1C3FyGC4OnbepYTrs7fwvDVXG7hEz7hQDC";
//				"fVvEAdvkMK77L4M4pNqQKVjE/gW/m0DJgzEvfHR/PBc=";
		Aes.AesToken at = JsonUtil.JsonToModel(airunTokenStr, Aes.AesToken.class);
		String ret = Aes.decodeBase64(text, at, "UTF-8");
		System.out.println(ret);
	}

	@Test
	public void testEncodeBase64() throws Exception {
		String text = "25d78f56d2ea59a300d3281e76174e6f55dd5a28";
//				"866298025003404";
//				"com.tencent.qq|@|com.netease.wx";
		Aes.AesToken at = JsonUtil.JsonToModel(airunTokenStr, Aes.AesToken.class);
		String ret = Aes.encodeBase64(text, at, "UTF-8");
		System.out.println(ret);
	}


	@Test
	public void testEncodeBase64_file() throws Exception {
		List<String> strings = FileUtils.readLines(new File("D:\\imei.txt"));
		Aes.AesToken at = JsonUtil.JsonToModel(airunTokenStr, Aes.AesToken.class);
		File file = new File("D:\\imei_encode.txt");
		List<String> result = new ArrayList<>();
		for (String string : strings) {
			result.add(Aes.encodeBase64(string.trim(), at, "UTF-8"));

		}
		FileUtils.writeLines(file,result,false);
	}


}
