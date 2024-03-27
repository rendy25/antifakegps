package com.lanchen.antifakegps;
import com.lanchen.antifakegps.mocklocation.MockLocationCheck;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.app.AppOpsManager;
import android.os.Process;
import android.location.Location;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.List;
import java.util.ArrayList;


/** AntifakegpsPlugin */
public class AntifakegpsPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "antifakegps");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("detectFakeLocation")) {
      boolean isMock = isMockLocationEnabled(context);
      result.success(isMock);
    } else if (call.method.equals("getMockLocationApps")) {
       List<String> mockLocationApps = getMockLocationApps(context);
       result.success(mockLocationApps);
    } else if (call.method.equals("isFakeGpsAppRunning")) {
      boolean isFakeGpsRunning = isFakeGpsAppRunning(context);
      result.success(isFakeGpsRunning);
    } else if (call.method.equals("canMockLocation")) {
      MockLocationCheck.LocationResult locationResult = new MockLocationCheck.LocationResult(){
        @Override
        public void gotLocation(Location location){
          //Got the location!
          if(location != null){
            result.success(location.isFromMockProvider());
          }else {
            result.success(false);
          }
        }
      };
      MockLocationCheck mockLocationCheck = new MockLocationCheck();
      mockLocationCheck.getLocation(context, locationResult);
    } else {
      result.notImplemented();
    }
  }

  // public boolean isMockLocationEnabled(Context context) {
  //     boolean isMockLocation = false;
      
  //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
  //         AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
  //         try {
  //             isMockLocation = appOpsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED;
  //         } catch (Exception e) {
  //             isMockLocation = false;
  //         }
  //     } else {
  //         try {
  //             isMockLocation = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).isEmpty();
  //         } catch (Exception e) {
  //             isMockLocation = false;
  //         }
  //     }
  //     return isMockLocation;
  // }

  /*public boolean isMockLocationEnabled(Context context) {
    boolean isMockLocation = false;
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            isMockLocation = appOpsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED;
        } catch (SecurityException e) {
            // Handle SecurityException
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Handle NullPointerException
            e.printStackTrace();
        }
    } else {
        try {
            isMockLocation = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).isEmpty();
        } catch (SecurityException e) {
            // Handle SecurityException
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Handle NullPointerException
            e.printStackTrace();
        }
    }
    return isMockLocation;
  }*/


  public boolean isMockLocationAppInstalled(Context context) {
    PackageManager packageManager = context.getPackageManager();
    List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ApplicationInfo packageInfo : packages) {
        try {
            // Check if the application has the "android.permission.ACCESS_MOCK_LOCATION" permission
            if ((packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA).flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // Non-system apps
                if (packageManager.getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions != null) {
                    for (String permission : packageManager.getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions) {
                        if (permission.equals("android.permission.ACCESS_MOCK_LOCATION")) {
                            // The app has permission to mock locations
                            return true;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            // Ignore NameNotFoundException
        }
    }

    return false;
  }

  public boolean isMockLocationEnabled(Context context) {
    boolean isMockLocation = false;

    // Check if mock location app is installed
    if (isMockLocationAppInstalled(context)) {
        isMockLocation = true;
    } else {
        // Your existing code to check if mock location is enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                isMockLocation = appOpsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                isMockLocation = false;
            }
        } else {
            try {
                isMockLocation = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).isEmpty();
            } catch (Exception e) {
                isMockLocation = false;
            }
        }
    }

    return isMockLocation;
  }

    public List<String> getMockLocationApps(Context context) {
    List<String> mockLocationApps = new ArrayList<>();

    PackageManager packageManager = context.getPackageManager();
    List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ApplicationInfo packageInfo : packages) {
        try {
            if ((packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA).flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String[] requestedPermissions = packageManager.getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (requestedPermissions != null) {
                    for (String permission : requestedPermissions) {
                        if ("android.permission.ACCESS_MOCK_LOCATION".equals(permission)) {
                            mockLocationApps.add(packageInfo.loadLabel(packageManager).toString());
                            break;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            // Ignore NameNotFoundException
        }
    }

        return mockLocationApps;
    }

    /*public boolean isFakeGpsAppRunning(Context context) {
        // Get package manager
        PackageManager packageManager = context.getPackageManager();
        // Get activity manager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Get the list of running services
        List<ActivityManager.RunningServiceInfo> runningServices;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        } else {
            // This method is deprecated in Android Oreo and above
            // Fallback to old method
            runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        }

        // Iterate through the list of running services
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            // Check if the service is related to a fake GPS app
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(service.service.getPackageName(), PackageManager.GET_META_DATA);
                if (appInfo != null && appInfo.metaData != null && appInfo.metaData.containsKey("com.lexa.fakegps")) {
                    // Fake GPS app detected
                    return true;
                }
            } catch (PackageManager.NameNotFoundException ignored) {
                // Ignore NameNotFoundException
            }
        }

        // No fake GPS app detected
        return false;
    }*/

    /*public boolean isFakeGpsAppRunning(Context context) {
        // Get package manager
        PackageManager packageManager = context.getPackageManager();
        // Get activity manager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Get the list of running app processes
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        // Iterate through the list of running app processes
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                if (processInfo.processName.equals("com.lexa.fakegps")) {
                    // Fake GPS app detected
                    return true;
                }
            }
        }
        // No fake GPS app detected
        return false;
    }*/


    public boolean isFakeGpsAppRunning(Context context) {
        // Get activity manager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Get the list of running app processes
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        // Iterate through the list of running app processes
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                // Check if the process name matches the package name of the fake GPS app
                if (processInfo.processName.equals("com.lexa.fakegps")) {
                    // Fake GPS app detected
                    return true;
                }
            }
        }

        // No fake GPS app detected
        return false;
    }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
