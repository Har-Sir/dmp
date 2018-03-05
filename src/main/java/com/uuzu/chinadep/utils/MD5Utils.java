package com.uuzu.chinadep.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String encode(String text) {
        return DigestUtils.md5Hex(text);
    }
    
}
