import 'package:flutter/material.dart';
import 'package:sms_plugin/model/InstalledAppsDate.dart';
import 'package:sms_plugin/model/PhoneDate.dart';
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
    List<InstalledAppsDate> i = await SmsPlugin.getInstalledApps();
    i.forEach((element) {
      debugPrint("packageName:${element.packageName} versionName:${element.versionName}");
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on:222 }'),
        ),
      ),
    );
  }
}
