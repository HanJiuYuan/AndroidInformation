class ContactData {
  late String name;
  late String phoneNumber;
  late int createTime;
  late int updateTime;

  ContactData({
    required this.name,
    required this.phoneNumber,
    required this.createTime,
    required this.updateTime});

  ContactData.fromJson(dynamic json){
    name = json['name'];
    phoneNumber = json['phoneNumber'].toString();
    createTime = json['creationDate'];
    updateTime = json['modificationDate'];
  }
  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['name'] = name;
    map['phoneNumber'] = phoneNumber;
    map['createTime'] = createTime;
    map['updateTime'] = updateTime;
    return map;
  }
}