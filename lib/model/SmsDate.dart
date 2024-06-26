class SmsDate{
  SmsDate({
    required this.id,
    required this.address,
    required this.body,
    required this.type,});
  late int id;
  late String address;
  late String body;
  late int type;
  late int date;
  late String read;
  late String person;
  SmsDate.fromJson(dynamic json) {
    id = json['id'];
    address = json['address'].toString();
    body = json['body'];
    type = json['type'];
    date = json['date'];
    read = json['read'] ?? '';
    person = json['person'];
  }

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['id'] = id;
    map['address'] = address;
    map['body'] = body;
    map['type'] = type;
    map['date'] =date;
    map['read'] = read;
    map['person'] = person;
    return map;
  }

}