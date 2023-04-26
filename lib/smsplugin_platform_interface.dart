import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'smsplugin_method_channel.dart';

abstract class SmspluginPlatform extends PlatformInterface {
  /// Constructs a SmspluginPlatform.
  SmspluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static SmspluginPlatform _instance = MethodChannelSmsplugin();

  /// The default instance of [SmspluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelSmsplugin].
  static SmspluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SmspluginPlatform] when
  /// they register themselves.
  static set instance(SmspluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
