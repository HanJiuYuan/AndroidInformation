class PhoneDate{
  late int id;
  late String number;
  late int date;
  late int type;
  late int duration;
  late String name;
  PhoneDate({
    required this.id,
    required this.number,
    required this.date,
    required this.type,
    required this.duration
  });
  PhoneDate.fromJson(dynamic json){
    id = json['id'];
    duration = json['duration'];
    number = json['number'];
    type = json['type'];
    date = json['date'];
    name = json['name'];
  }
  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['id'] = id;
    map['duration'] = duration;
    map['number'] = number;
    map['type'] = type;
    map['date'] =date;
    map['name'] = name;
    return map;
  }
}