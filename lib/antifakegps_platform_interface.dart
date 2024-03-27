import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'antifakegps_method_channel.dart';

abstract class AntifakegpsPlatform extends PlatformInterface {
  /// Constructs a AntifakegpsPlatform.
  AntifakegpsPlatform() : super(token: _token);

  static final Object _token = Object();

  static AntifakegpsPlatform _instance = MethodChannelAntifakegps();

  /// The default instance of [AntifakegpsPlatform] to use.
  ///
  /// Defaults to [MethodChannelAntifakegps].
  static AntifakegpsPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AntifakegpsPlatform] when
  /// they register themselves.
  static set instance(AntifakegpsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<bool?> detectFakeLocation() {
    throw UnimplementedError('detectFakeLocation() has not been implemented.');
  }

  Future<List<String>?> getMockLocationApps() {
    throw UnimplementedError('getMockLocationApps() has not been implemented.');
  }
}
