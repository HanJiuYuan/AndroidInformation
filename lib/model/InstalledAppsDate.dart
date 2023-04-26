class InstalledAppsDate{
  late int appType;
  late String appName;
  late String packageName;
  late String versionName;
  late int versionCode;
  late int updateTime;
  late int installTime;
  late String appSize;
  InstalledAppsDate({
    required this.appType,
    required this.appName,
    required this.packageName,
    required this.versionName,
    required this.versionCode,
    required this.updateTime,
    required this.installTime,
    required this.appSize
  });
  InstalledAppsDate.formJson(dynamic json){
    appType = json['appType'];
    appName = json['name'];
    packageName = json['packageName'];
    versionName = json['versionName'];
    versionCode = json['versionCode'];
    updateTime = json['updateTime'];
    installTime = json['installTime'] ;
    appSize = json['appSize'].toString();
  }
  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['appType'] = appType;
    map['appName'] = appName;
    map['packageName'] = packageName;
    map['versionName'] = versionName;
    map['versionCode'] = versionCode;
    map['createTime'] = installTime;
    map['updateTime'] = updateTime;
    map['appSize'] =appSize;
    return map;
  }
}