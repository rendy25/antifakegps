import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'antifakegps_platform_interface.dart';

/// An implementation of [AntifakegpsPlatform] that uses method channels.
class MethodChannelAntifakegps extends AntifakegpsPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('antifakegps');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> detectFakeLocation() async {
    final version =
        await methodChannel.invokeMethod<bool>('detectFakeLocation');
    return version;
  }

  @override
  Future<List<String>?> getMockLocationApps() async {
    final version =
        await methodChannel.invokeMethod<List<String>>('getMockLocationApps');
    return version;
  }

  @override
  Future<bool?> isFakeGpsAppRunning() async {
    final version =
        await methodChannel.invokeMethod<bool>('isFakeGpsAppRunning');
    return version;
  }
}
