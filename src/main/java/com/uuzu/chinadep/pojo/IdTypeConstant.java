package com.uuzu.chinadep.pojo;

public class IdTypeConstant {
    public static final String MAC = "010209";
    public static final String IMEI_IDFA = "010207";
    public static final String ENCRYPTION_IMEI = "010217";
    public static final String ENCRYPTION_MAC = "010219";
    public static final String IDFA = "010211";
    public static final String PHONE = "010105";
    public static final String ENCRYPTION_PHONE = "010220";
    public static final String IMEI_PHONE_LON_LAT = "010301";
    public static final String IMEI_PHONE_LON_LAT_ADDRESS = "010302";
    public static final String ANDROID_ID = "010212";



    /*@Test
    public void test() throws Exception{
        String token = "{\"key_algorithm\":\"AES\", \"cipher_algorithm\":\"AES/CBC/PKCS5Padding\", \"key\":\"1234667890123456\", \"iv_parameter\":\"1234567892546398\"}";
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        Aes.AesToken at = OBJECT_MAPPER.readValue(token, Aes.AesToken.class);
//        String a = "64:cc:2e:4e:1d:dc";
        String a = "18:e3:bc:41:1f:c8";
        String exid = Aes.encodeBase64(a, at, BaseConfig.DEFAULT_CHARSET);
        System.out.println("exid:" + exid);

//        String re = "JIGHuDZO50Ii+R8STZCxuw==";
        String re = "IlYSolAs15nnQ68Injln8w==";
        System.out.println(Aes.decodeBase64(re, at, BaseConfig.DEFAULT_CHARSET));

    }*/
}
