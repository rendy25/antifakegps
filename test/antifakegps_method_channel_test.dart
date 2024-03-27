import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:antifakegps/antifakegps_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelAntifakegps platform = MethodChannelAntifakegps();
  const MethodChannel channel = MethodChannel('antifakegps');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return false;
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
    expect(await platform.detectFakeLocation(), false);
    expect(await platform.getMockLocationApps(),
        ['FakeGPSApp1', 'FakeGPSApp2', 'FakeGPSApp3']);
  });
}

// import 'package:flutter/services.dart';
// import 'package:flutter_test/flutter_test.dart';
// // import 'package:detect_fake_location/detect_fake_location_method_channel.dart';
// import 'package:antifakegps/antifakegps_method_channel.dart';

// void main() {
//   MethodChannelAntifakegps platform = MethodChannelAntifakegps();
//   const MethodChannel channel = MethodChannel('detect_fake_location');

//   TestWidgetsFlutterBinding.ensureInitialized();

//   setUp(() {
//     channel.setMockMethodCallHandler((MethodCall methodCall) async {
//       return false;
//     });
//   });

//   tearDown(() {
//     channel.setMockMethodCallHandler(null);
//   });

//   test('detectFakeLocation', () async {
//     expect(await platform.detectFakeLocation(), false);
//   });
// }
