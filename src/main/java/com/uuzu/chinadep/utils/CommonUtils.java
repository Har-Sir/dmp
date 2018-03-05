package com.uuzu.chinadep.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shieh
 */
public class CommonUtils {

    /**
     * check phone no start with 13,15,18 and length must be 11
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[6-6])|(17[^2,\\D])|(18[0-9])|(19[8-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /*
    @Test
    public void testIsMobileNO () {
        Assert.assertEquals(false, isMobileNO("12112121212"));
        Assert.assertEquals(true, isMobileNO("14112121212"));
        Assert.assertEquals(true, isMobileNO("15112121212"));
        Assert.assertEquals(true, isMobileNO("16612121212"));
        Assert.assertEquals(true, isMobileNO("17112121212"));
        Assert.assertEquals(true, isMobileNO("18112121212"));
        Assert.assertEquals(true, isMobileNO("19812121212"));
        Assert.assertEquals(true, isMobileNO("13789089999"));
        Assert.assertEquals(false, isMobileNO("23232232322"));
    }*/

}
