#include "include/antifakegps/antifakegps_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "antifakegps_plugin.h"

void AntifakegpsPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  antifakegps::AntifakegpsPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
