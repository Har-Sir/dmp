package com.uuzu.chinadep.config.ParamConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataServeBase {

	public static IdType getIdType(String idCode) {
		return idTypeMap.get(idCode);
	}
	
	public static Set<String> getIdTypeCodes() {
		return idTypeMap.keySet();
	}

	public static KeyType getKT(String keyCode) {
		if (getDemoKT(keyCode) != null) return getDemoKT(keyCode);
		if (getBasicidKT(keyCode) != null) return getBasicidKT(keyCode);
		if (getScoreKT(keyCode) != null) return getScoreKT(keyCode); 
		return null;
	}
	
	public static Set<String> getKTcodes() {
		Set<String> sets = new HashSet<String>();
		sets.addAll(getDemoKTcodes());
		sets.addAll(getBasicidKTcodes());
		sets.addAll(getScoreKTcodes());
		return sets;
	}

	
	public static KeyType getDemoKT(String keyCode) {
		return demoKTmap.get(keyCode);
	}
	
	public static Set<String> getDemoKTcodes() {
		return demoKTmap.keySet();
	}

	public static KeyType getBasicidKT(String keyCode) {
		return basicidKTmap.get(keyCode);
	}
	
	public static Set<String> getBasicidKTcodes() {
		return basicidKTmap.keySet();
	}

	public static KeyType getScoreKT(String keyCode) {
		return scoreKTmap.get(keyCode);
	}
	
	public static Set<String> getScoreKTcodes() {
		return scoreKTmap.keySet();
	}

	private static Map<String, IdType> idTypeMap = new HashMap<String, IdType>() {
		private static final long serialVersionUID = 1L;
		{
			put("000000", new IdType("000000", "deviceId", "id"));
			put("010101", new IdType("010101", "身份证号", ""));
			put("010102", new IdType("010102", "护照/通行证", ""));
			put("010103", new IdType("010103", "社保号", ""));
			put("010104", new IdType("010104", "姓名+身份证+手机号", ""));
			put("010104", new IdType("010104", "姓名+身份证+手机号", ""));
			put("010105", new IdType("010105", "手机号", "phone"));
			put("010106", new IdType("010106", "固定电话", ""));
			put("010207", new IdType("010207", "IMIE", "imei_idfa"));
			put("010208", new IdType("010208", "IMSI", ""));
			put("010209", new IdType("010209", "MAC地址", "mac"));
			put("010210", new IdType("010210", "IP地址", "ip"));
			put("010211", new IdType("010211", "IDFA", "imei_idfa"));
			put("010212", new IdType("010212", "Android ID", "androidid"));
			put("010213", new IdType("010213", "Cookie 信息", ""));
			put("010214", new IdType("010214", "网络宽带设备编号", ""));
			put("010215", new IdType("010215", "SSID", ""));
			put("010216", new IdType("010216", "BSSID", ""));
			put("010217", new IdType("010217", "IMEI(小写)的MD5", "encryptionImei"));
			put("010218", new IdType("010218", "IDFA的MD5", ""));
			put("010219", new IdType("010219", "MAC的MD5", "encryptionMac"));
			put("010220", new IdType("010220", "手机号的MD5", "encryptionPhone"));

			put("010301", new IdType("010301", "IMEI,手机号,纬经度", "imei_idfa"));
			put("010302", new IdType("010302", "mac,手机号,一级行业，二级行业，地点，纬经度", "mac"));


			put("040201", new IdType("040201", "应用的名字", ""));
			put("040202", new IdType("040202", "原始应用包名", ""));
			
			put("040301", new IdType("040301", "tag id", ""));
		}
	};
	
	private static Map<String, KeyType> demoKTmap = new HashMap<String, KeyType>() {
		private static final long serialVersionUID = 1L;
		{
			put("D001", new KeyType("D001", "国家", "country"));
			put("D002", new KeyType("D002", "省份", "province"));
			put("D003", new KeyType("D003", "城市", "city"));
			put("D004", new KeyType("D004", "性别", "gender"));
			put("D005", new KeyType("D005", "年龄", "agebin"));
			put("D006", new KeyType("D006", "人群", "segment"));
			put("D007", new KeyType("D007", "教育", "edu"));
			put("D008", new KeyType("D008", "有无小孩", "kids"));
			put("D009", new KeyType("D009", "收入", "income"));
			put("D010", new KeyType("D010", "设备品牌", "cell_factory"));
			put("D011", new KeyType("D011", "设备档次", "model_level"));
			put("D012", new KeyType("D012", "安装app个数", "tot_install_apps"));
			put("D013", new KeyType("D013", "标签列表", "tags"));
			put("D014", new KeyType("D014", "设备型号", "model"));
			put("D015", new KeyType("D015", "运营商代码", "carrier"));
			put("D016", new KeyType("D016", "网络", "network"));
			put("D017", new KeyType("D017", "分辨率", "screensize"));
			put("D018", new KeyType("D018", "操作系统", "sysver"));
			put("D019", new KeyType("D019", "城市等级", "city_level"));
			put("D020", new KeyType("D020", "常驻国家", "permanent_country"));
			put("D021", new KeyType("D021", "常驻省份", "permanent_peovince"));
			put("D022", new KeyType("D022", "常驻城市", "permanent_city"));
			put("D023", new KeyType("D023", "职业", "occupation"));
			put("D024", new KeyType("D024", "房产情况", "house"));
			put("D025", new KeyType("D025", "偿还能力", "repayment"));
			put("D026", new KeyType("D026", "车产情况", "car"));
			put("D027", new KeyType("D027", "工作地", "workplace"));
			put("D028", new KeyType("D028", "居住地", "residence"));
			put("D029", new KeyType("D029", "婚姻状况", "married"));
			put("D030", new KeyType("D030", "行业分类下app在装个数", "installed_cate_tag"));
			put("D031", new KeyType("D031", "金融划窗标签更新时间", "finance_time"));
			put("D032", new KeyType("D032", "金融划窗标签", "finance_action"));

		}
	};
	
	private static Map<String, KeyType> basicidKTmap = new HashMap<String, KeyType>() {
		private static final long serialVersionUID = 1L;
		{
			put("B001", new KeyType("B001", "", "androidid"));
			put("B002", new KeyType("B002", "", "adsid"));
			put("B003", new KeyType("B003", "", "serialno"));
			put("B004", new KeyType("B004", "", "ip"));
			put("B005", new KeyType("B005", "", "mac"));
			put("B006", new KeyType("B006", "", "openudid"));
			put("B007", new KeyType("B007", "", "imei"));
			put("B008", new KeyType("B008", "", "IDFA"));
//			put("B009", new KeyType("B009", "", "phone"));
		}
	};
	
	private static Map<String, KeyType> scoreKTmap = new HashMap<String, KeyType>() {
		private static final long serialVersionUID = 1L;
		{
			put("S001", new KeyType("S001", "", "profile_score"));
			put("S002", new KeyType("S002", "", "behavior_score"));
			put("S003", new KeyType("S003", "", "positive_action_score"));
			put("S004", new KeyType("S004", "", "active_score"));
			put("S005", new KeyType("S005", "", "summary_score"));
		}
	};


	
	public static class IdType implements Serializable {
		private static final long serialVersionUID = 1L;
		public String code;
		public String desc;
		public String key;

		private IdType(String code, String desc, String key) {
			this.code = code;
			this.desc = desc;
			this.key = key;
		}
	}
	
	public static class KeyType implements Serializable  {
		private static final long serialVersionUID = 1L;
		public String code;
		public String desc;
		public String key;
		
		private KeyType(String code, String desc, String key) {
			this.code = code;
			this.desc = desc;
			this.key = key;
		}
	}
	
}
