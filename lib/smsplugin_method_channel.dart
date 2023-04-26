import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'smsplugin_platform_interface.dart';

/// An implementation of [SmspluginPlatform] that uses method channels.
class MethodChannelSmsplugin extends SmspluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('smsplugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
