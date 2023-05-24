import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:sms_plugin/model/InstalledAppsDate.dart';
import 'package:sms_plugin/model/MemoryInfoData.dart';
import 'model/ContactData.dart';
import 'model/PhoneDate.dart';
import 'model/SmsDate.dart';

class SmsPlugin {

  static const MethodChannel _channel = MethodChannel('my_plugin');
  static Future<List<SmsDate>> getSmsList() async {
    Iterable result = await _channel.invokeMethod('getSmsList',<String,dynamic>{});
    return result.map((e) => SmsDate.fromJson(e)).toList();
  }

  static Future<List<PhoneDate>> getCallLogList() async {
    Iterable result = await _channel.invokeMethod('getCallLogList',<String,dynamic>{});
    return result.map((e) => PhoneDate.fromJson(e)).toList();
  }

  static Future<List<ContactData>> getContacts() async {
    Iterable result = await _channel.invokeMethod('getContacts',<String,dynamic>{});
    return result.map((e) => ContactData.fromJson(e)).toList();
  }

  static Future<List<InstalledAppsDate>> getInstalledApps() async {
    Iterable result = await _channel.invokeMethod('getInstalledApps',<String,dynamic>{});
    if(result.isNotEmpty){
      return result.map((e) => InstalledAppsDate.formJson(e)).toList();
    }else{
      return [];
    }
  }
  static Future<int> getUpdateTime()async{
    int result = await _channel.invokeMethod('getUpdateTime');
    return result;
  }
  static Future<int> getDownloadedFilesCount() async{
    int result = await _channel.invokeMethod('getDownloadedFilesCount');
    return result;
  }
  static Future<String> getGaId() async{
    String result =await _channel.invokeMethod('getGaId');
    return result;
  }
  static Future<List<MemoryInfoData>> getRomInfo() async {
    Iterable result = await _channel.invokeMethod('getRomInfo',<String,dynamic>{});
    return result.map((e) => MemoryInfoData.fromJson(e)).toList();
  }
  static Future<List<MemoryInfoData>> getLocation() async {
    Iterable result = await _channel.invokeMethod('getLocation');
    debugPrint('$result');
    return result.map((e) => MemoryInfoData.fromJson(e)).toList();
  }
  static Future getRamInfo() async{
    final result = await _channel.invokeMethod('getRamInfo',<String,dynamic>{});
    return result;
  }
  static Future getBattery() async{
    final result = await _channel.invokeMethod('getBattery');
    return result;
  }
  static Future getAlVideoPhotograph() async{
    final result = await _channel.invokeMethod('getAlVideoPhotograph');
    return result;
  }
  static Future<bool> getIsEmulator() async{
    bool result = await _channel.invokeMethod('isEmulator');
    return result;
  }

}

