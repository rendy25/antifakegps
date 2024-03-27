import 'package:flutter_test/flutter_test.dart';
import 'package:antifakegps/antifakegps.dart';
import 'package:antifakegps/antifakegps_platform_interface.dart';
import 'package:antifakegps/antifakegps_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAntifakegpsPlatform
    with MockPlatformInterfaceMixin
    implements AntifakegpsPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
  Future<bool?> detectFakeLocation() => Future.value(false);
  Future<List<String>?> getMockLocationApps() => Future.value([
        "FakeGPSApp1",
        "FakeGPSApp2",
        "FakeGPSApp3",
      ]);
  Future<bool?> isFakeGpsAppRunning() => Future.value(false);
}

void main() {
  final AntifakegpsPlatform initialPlatform = AntifakegpsPlatform.instance;

  test('$MethodChannelAntifakegps is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAntifakegps>());
  });

  test('getPlatformVersion', () async {
    Antifakegps antifakegpsPlugin = Antifakegps();
    MockAntifakegpsPlatform fakePlatform = MockAntifakegpsPlatform();
    AntifakegpsPlatform.instance = fakePlatform;

    expect(await antifakegpsPlugin.getPlatformVersion(), '42');
    expect(await antifakegpsPlugin.detectFakeLocation(), false);
    expect(await antifakegpsPlugin.getMockLocationApps(),
        ['FakeGPSApp1', 'FakeGPSApp2', 'FakeGPSApp3']);
    expect(await antifakegpsPlugin.isFakeGpsAppRunning(), false);
  });
}
