import 'antifakegps_platform_interface.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class Antifakegps {
  static const mothodChannel = MethodChannel('antifakegps');

  Future<String?> getPlatformVersion() {
    return AntifakegpsPlatform.instance.getPlatformVersion();
  }

  Future<bool?> detectFakeLocation() async {
    bool result = false;
    // return await mothodChannel.invokeMethod('detectFakeLocation');
    if (await Permission.location.isGranted) {
      result = await mothodChannel.invokeMethod('detectFakeLocation');
      print("OIOI");
      print(result);
      return result;
    } else {
      PermissionStatus status = await Permission.locationWhenInUse.request();
      if (status == PermissionStatus.granted) {
        result = await mothodChannel.invokeMethod('detectFakeLocation');
        return result;
      } else if (status == PermissionStatus.permanentlyDenied) {
        return false;
      } else {
        return false;
      }
      // print(" 1111");
      // return false;
    }
  }

  Future<List<String>?> getMockLocationApps() async {
    try {
      final List<String>? result =
          await mothodChannel.invokeMethod('getMockLocationApps');
      return result;
    } catch (e) {
      // Tangani kesalahan jika terjadi
      print('Error calling getMockLocationApps: $e');
      return null;
    }
  }
}
