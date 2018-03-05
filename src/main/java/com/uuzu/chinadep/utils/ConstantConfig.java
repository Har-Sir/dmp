package com.uuzu.chinadep.utils;

public class ConstantConfig {

//	public static final int MAN_GENDER = 0;

	public static final String APPPROPERTY_GENDER = "gender";// 性别
	public static final String APPPROPERTY_AGE = "age_bin"; // 年龄
	public static final String APPPROPERTY_AGE_ECOLOGY = "agebin"; // 年龄(生态地图用)
	public static final String APPPROPERTY_INCOME = "income";// 收入 
	public static final String APPPROPERTY_EDUCATION = "education";// 学历
	public static final String APPPROPERTY_EDUCATION_ECOLOGY = "edu";// 学历(生态地图用)
	public static final String APPPROPERTY_PEOPLE = "people_groups";// 人群
	public static final String APPPROPERTY_PEOPLE_ECOLOGY = "segment";// 人群(生态地图用)
	public static final String APPPROPERTY_CHILD = "child";//有无孩子
	public static final String APPPROPERTY_CHILD_ECOLOGY = "kids";// 有无孩子(生态地图用)
	public static final String APPPROPERTY_ZONE = "zone";//国家
	public static final String APPPROPERTY_PROVINCE = "province";//省份
	public static final String APPPROPERTY_CITY = "city";//城市
	public static final String APPPROPERTY_MOBILELEVEL = "mobile_level";//手机价位
	public static final String APPPROPERTY_MOBILELEVEL_ECOLOGY = "model_level";//手机价位(生态地图用)
	public static final String APPPROPERTY_MOBILEFACTORY = "mobile_factory";//品牌
	public static final String APPPROPERTY_MOBILEFACTORY_ECOLOGY = "cell_factory";//品牌(生态地图用)
	public static final String APPPROPERTY_MOBILEMODEL = "mobile_model";//手机型号
	public static final String APPPROPERTY_MOBILEMODEL_ECOLOGY = "model";//手机型号(生态地图用)
	public static final String APPPROPERTY_CARRIER_ECOLOGY = "carrier ";//运营商(生态地图用)
	public static final String APPPROPERTY_NETWORK_ECOLOGY = "network  ";//网络(生态地图用)
	public static final String APPPROPERTY_SCREENSIZE_ECOLOGY = "screensize   ";//分辨率(生态地图用)
	public static final String APPPROPERTY_SYSVER_ECOLOGY = "sysver  ";//操作系统(生态地图用)

	public static final String APPPROPERTY_COUNTRY = "country";//国家

	public static final String APPPROPERTY_SOURCE = "source";//来源
	public static final String APPPROPERTY_FLOW = "flow";//去向

	public static final String SHOW_OTHER = "其他";//其他

	public static final int REMAIN_TYPE_DAILY = 1;//日留存
	public static final int REMAIN_TYPE_WEEKLY = 2;//周留存
	public static final int REMAIN_TYPE_MONTHLY = 3;//月留存

	public static final String HBASE_TABLE_ANALYSIS = "app_user_analysis";//Habse中用户分析与用户参与表
	public static final String HBASE_TABLE_REMAIN_ACTIVE = "app_remain_actives";//Habse中活跃留存表
	public static final String HBASE_TABLE_REMAIN_INTALL = "app_remain_install";//Habse中安装留存表

	public static final String FAMILY_ANALYSIS = "analysis";//用户分析列族
	public static final String FAMILY_ACTION = "action";//用户参与列族
	public static final String FAMILY_REMAIN_DAILY = "daily";//日留存列族
	public static final String FAMILY_REMAIN_WEEKLY = "weekly";//周留存列族
	public static final String FAMILY_REMAIN_MONTHLY = "monthly";//月留存列族

	public static final String COUNTRY_CHINA = "cn";//城市：中国
	public static final String COUNTRY_ALL = "00";//城市：不限

	public static final int UPLOAD_STATE_FINISH = 3;//上传状态：已完成
	public static final int TASK_STATE_FINISH = 4;//任务执行状态：已完成
	public static final int TASK_STATE_PREPARE = 0;//任务执行状态：0:待执行 1:执行中
	public static final int TASK_STATE_PROCESS = 1;//任务执行状态：1:执行中

	public static final String PROJECT_NAME = "mobeye";// 项目名

	public static final String MODEL_NAME_UPLOAD = "ecologyupload";// 创建生态地图模块名

	public static final String MODEL_NAME_DOWNLOAD = "ecologydownload";// 生态地图下载模块名

	public static final String MODEL_NAME_DMP_DOWNLOAD = "dmpdownload";// 生态地图下载模块名

	public static final String MODEL_ECOLOGY = "Ecology";// 生态管理：一方ID

	public static final String MODEL_ECOLOGY_CLIENTDATA = "clientdata";// 生态管理：一方ID
	public static final String MODEL_ECOLOGY_LOOKLIKE = "looklike";// 生态管理：LookLike
	public static final String MODEL_ECOLOGY_DMP = "dmp";// 生态管理：DMP
	public static final String MODEL_ECOLOGY_CROSS = "cross";// 生态管理：自定义
	public static final String MODEL_ECOLOGY_DOWNLOAD = "download";// 生态管理：下载

	public static final String SHELL_PATH_UPLOAD = "/data/schedule/scoreData.sh";// 生态地图下载模块名
//
//	public static final String SHELL_PATH_DOWLOAD = "/data/schedule/featchData.sh";// 生态地图下载模块名
//	public static final String SHELL_PATH_UPLOAD = "/data/schedule/test.sh";// 生态地图下载模块名

	public static final String SHELL_PATH_DOWLOADA_IMEI = "/data/schedule/fetchData_imei.sh";// 生态地图下载模块名
	public static final String SHELL_PATH_DOWLOAD_MAC = "/data/schedule/fetchData_mac.sh";// 生态地图下载模块名

//	public static final String SHELL_PATH_CLIENTDATA_VIP = "/data/schedule/mobeye_ecology_clientdata_sp.sh";// 生态管理：一方ID
//	public static final String SHELL_PATH_CLIENTDATA = "/data/schedule/mobeye_ecology_clientdata.sh";// 生态管理：一方ID
//	public static final String SHELL_PATH_LOOKLIKE = "/data/schedule/mobeye_ecology_looklike.sh";// 生态管理：LookLike
//	public static final String SHELL_PATH_DOWLOAD = "/data/schedule/mobeye_ecology_fetchData.sh";// 生态管理：下载

	public static final int TASK_FLAG_UPLOAD = 0;//任务标志位：创建生态地图

	public static final int TASK_FLAG_DOWLOAD = 1;//任务标志位：生态地图明细下载
	public static final int TASK_FLAG_DMP_DOWLOAD = 2;//任务标志位：DMP下载

//	public static final String URL_DOWLOAD_SHELL = "http://10.6.98.124:20100/mobeye/download?user_id=admin&path=";//下载URL
//	public static final String URL_DOWLOAD_SHELL = "http://10.5.1.45:20100/mobeye/download?user_id=admin&path=";//下载URL

	public static final int TABLE_TYPE_CHANNEL = 1;//表类型：渠道
	public static final int TABLE_TYPE_OTHER = 2;//表类型：其他

	public static final int SQL_TYPE_TOTAL = 1;//类型：total
	public static final int SQL_TYPE_GROUP = 2;//类型：group

	public static final int SQL_TYPE_GROUP_CHANNEL = 1;//类型：group-渠道
	public static final int SQL_TYPE_GROUP_OTHER = 2;//类型：group-其他

	public static final Double APP_ACTIVE_RATIO_DEFAULT = 1d;//app默认活跃倍数
	public static final Double APP_ACTIVE_RATIO_OTHER = 4d;//app默认活跃倍数
	public static final Double APP_ACTIVE_RATIO_USERSCALE = 5d;//用户规模

	public static final Double APP_ACTIVE_RATIO_USERSCALE_OPEN = 5.5d;//用户规模(打开率)

	public static final String USER_UID = "id";// 用户Id

//	public static final String URL_UPLOAD = "http://10.6.98.125:20100/mobeye/upload2";// 文件上传URL
//	public static final String URL_UPLOAD = "http://webservice.fileload.mob.com/mobeye/upload2";// 文件上传URL
//	public static final String URL_UPLOAD = "http://192.168.43.115:20100/mobeye/upload2";// 文件上传URL

	public static final String CODE_UPLOAD_SUCCESS = "000000";// 文件上传成功的code

	public static final String URL_USER = "http://user.pmp.appgo.cn/openApi/mob-user";// 文件上传URL

	public static final String PROJECT_O2O = "o2o";// 项目:o2o

	public static final String URL_HIVE_API = "http://192.168.43.235:8080/api/query";
}
