import 'package:flutter/material.dart';
import 'package:sms_plugin/model/ContactData.dart';
import 'package:sms_plugin/model/SmsDate.dart';
import 'dart:async';
import 'package:sms_plugin/smsplugin.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    List<ContactData> con = await SmsPlugin.getContacts();
    List<SmsDate> sms = await SmsPlugin.getSmsList();
    con.forEach((element) {
      debugPrint(element.name);
      debugPrint(element.phoneNumber);
      debugPrint("createTime:${element.createTime}");
      debugPrint("updateTime:${element.updateTime}");
    });
    sms.forEach((element) {
      debugPrint('11111${element.address}');
      debugPrint('11111${element.body}');
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: const Center(
          child: Text('Running on:222 }'),
        ),
      ),
    );
  }
}
