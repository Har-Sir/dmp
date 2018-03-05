package com.uuzu.chinadep.utils;

import java.math.BigDecimal;

/**
 * 算数运算
 */
public class ArithUtil {

	/**
	 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
	 */
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private ArithUtil() {
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 多个数字相加减
	 * 
	 * @param args
	 * @return
	 */
	public static double adds(double... args) {
		double sum = 0;
		if (args != null) {
			for (double d : args) {
				sum = add(sum, d);
			}
		}
		return sum;
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */

	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算,(10,1,1.1)=10-1-1.1=7.9
	 * 
	 * @param args
	 * @return
	 */
	public static double subs(double... args) {
		double result = 0;
		if (args != null && args.length >= 1) {
			if (args.length == 1) {
				return args[0];
			}
			result = args[0];
			for (int i = 1; i < args.length; i++) {
				result = sub(result, args[i]);
			}
		}
		return result;
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @return 两个参数的积
	 */

	public static double muls(double... args) {
		double total = 1;
		if (args != null) {
			for (double d : args) {
				total = mul(total, d);
			}
		}
		return total;
	}

	/**
	 * 幂运算
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double pow(double a, int b) {
		double total = 1;
		for (int i = 0; i < b; i++) {
			total = mul(total, a);
		}
		return total;
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */

	public static double divs(double... args) {
		double result = 0;
		if (args != null && args.length >= 1) {
			if (args.length == 1) {
				return args[0];
			}
			result = args[0];
			for (int i = 1; i < args.length; i++) {
				result = div(result, args[i]);
			}
		}
		return result;
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	/**
	 * 除法运算,总是在非   0   舍弃小数(即截断)之前增加数字。     
	* @param v1
	* @param v2
	* @param scale
	* @return double
	 */
	public static double div(double v1, double v2, int scale, int round) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, round).doubleValue();
	}
	
	

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 取绝对值
	 * 
	 * @param v
	 * @return
	 */
	public static double abs(double v) {
		return v < 0 ? ArithUtil.sub(0, v) : v;
	}
	
	/**
	* 取模
	* @param v1
	* @param v2
	* @return
	 */
	public static double mod(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.remainder(b2).doubleValue();
	}

}
