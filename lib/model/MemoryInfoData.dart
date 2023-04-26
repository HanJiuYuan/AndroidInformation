class MemoryInfoData {
  late int totalMem;
  late int availMem;
  late int threshold;

  MemoryInfoData({
    required this.totalMem,
    required this.availMem,
    required this.threshold,});

  MemoryInfoData.fromJson(dynamic json){
    totalMem = json['totalMem'];
    availMem = json['availMem'];
    threshold = json['threshold'];
  }
  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['name'] = totalMem;
    map['availMem'] = availMem;
    map['threshold'] = threshold;

    return map;
  }
}