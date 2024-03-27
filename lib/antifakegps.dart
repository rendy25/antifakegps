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
      final List<Object?>? result =
          await mothodChannel.invokeMethod<List<Object?>>(
              'getMockLocationApps'); // Menggunakan List<Object?>
      if (result != null) {
        // Konversi List<Object?> menjadi List<String> jika diperlukan
        List<String> resultList = result.cast<
            String>(); // Konversi secara aman karena kita tahu result adalah List<String>
        return resultList;
      } else {
        return null;
      }
    } catch (e) {
      // Tangani kesalahan jika terjadi
      print('Error calling getMockLocationApps: $e');
      return null;
    }
  }

  Future<bool?> isFakeGpsAppRunning() async {
    try {
      bool result = false;
      if (await Permission.location.isGranted) {
        result = await mothodChannel.invokeMethod('isFakeGpsAppRunning');
        print("OI");
        print(result);

        return result;
      } else {
        PermissionStatus status = await Permission.locationWhenInUse.request();
        if (status == PermissionStatus.granted) {
          result = await mothodChannel.invokeMethod('isFakeGpsAppRunning');
          return result;
        } else if (status == PermissionStatus.permanentlyDenied) {
          return false;
        } else {
          return false;
        }
      }
    } catch (e) {
      print('Error calling isFakeGpsAppRunning: $e');
      return null;
    }
  }

  Future<bool?> checkMockLocation() async {
    final bool canMockLocation =
        await mothodChannel.invokeMethod('canMockLocation');
    return canMockLocation;
  }
}
