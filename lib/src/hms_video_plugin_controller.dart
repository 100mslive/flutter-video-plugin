library;

///Dart imports
import 'dart:io';

///Package imports
import 'package:flutter/foundation.dart';
import 'package:hmssdk_flutter/hmssdk_flutter.dart';

///Project imports
import 'package:hms_video_plugin/src/enum/plugin_method.dart';
import 'package:hms_video_plugin/src/platform_service.dart';

///[HMSVideoPlugin] is the entry point for the plugin.
///It provides methods to enable, disable, change virtual background and blur background.
abstract class HMSVideoPlugin {
  ///[enable] enables virtual background with the given image
  ///
  ///**parameters
  ///
  ///**image** - is the image to be used as virtual background
  ///
  ///Refer [enable] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-4-if-virtual-background-is-available-enable-it)
  static Future<HMSException?> enable({required Uint8List? image}) async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.enableVirtualBackground,
          arguments: {"image": image});
      if (result != null) {
        return HMSException.fromMap(result["error"]);
      } else {
        return null;
      }
    } else {
      return HMSVideoFilter.enable(image: image);
    }
  }

  ///[changeVirtualBackground] changes the virtual background with the given image
  ///
  ///**parameters**
  ///
  ///**image** - is the new image to be used as virtual background
  ///
  ///Note: This method can be used only if virtual background is already enabled
  ///Refer [changeVirtualBackground] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-5-to-change-virtual-background-image-use-changevirtualbackground-method)
  static Future<void> changeVirtualBackground(
      {required Uint8List? image}) async {
    if (Platform.isAndroid) {
      return PlatformService.invokeMethod(PluginMethod.changeVirtualBackground,
          arguments: {"image": image});
    } else {
      return HMSVideoFilter.changeVirtualBackground(image: image);
    }
  }

  ///[isSupported] returns whether virtual background is supported or not
  ///
  ///Refer [isSupported] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-3-check-for-virtual-background-availability)
  static Future<bool> isSupported() async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.isVirtualBackgroundSupported);
      if (result["success"]) {
        return result["data"];
      } else {
        return false;
      }
    } else {
      return HMSVideoFilter.isSupported();
    }
  }

  ///[preInitialize] pre-initializes the virtual background plugin
  ///by creating it and adding it to the SDK video processing pipeline.
  ///
  ///This method should be called after onJoin or onPreview callbacks
  ///to ensure the plugin is ready before the user attempts to enable
  ///blur or virtual background effects.
  ///
  ///Returns [HMSException] if initialization fails, null on success.
  static Future<HMSException?> preInitialize() async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.preInitializeVirtualBackground);
      if (result != null) {
        return HMSException.fromMap(result["error"]);
      } else {
        return null;
      }
    } else {
      // For iOS, initialization happens automatically
      // Just check support to initialize the plugin
      await HMSVideoFilter.isSupported();
      return null;
    }
  }

  ///[disable] disables virtual background
  ///
  ///Refer [disable] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-6-to-disable-virtual-background-use-disable-methods)
  static Future<HMSException?> disable() async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.disableVirtualBackground);
      if (result != null) {
        return HMSException.fromMap(result["error"]);
      } else {
        return null;
      }
    } else {
      return HMSVideoFilter.disable();
    }
  }

  ///[enableBlur] enables blur with the given blur radius
  ///
  ///**parameters**
  ///
  ///**blurRadius** - is the radius of the blur effect
  ///
  ///Refer [enableBlur] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-4-if-virtual-background-is-available-enable-it)
  static Future<HMSException?> enableBlur({required int blurRadius}) async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.enableBlurBackground,
          arguments: {"blur_radius": blurRadius});
      if (result != null) {
        return HMSException.fromMap(result["error"]);
      } else {
        return null;
      }
    } else {
      return HMSVideoFilter.enableBlur(blurRadius: blurRadius);
    }
  }

  ///[disableBlur] disables blur
  ///
  ///Refer [disableBlur] Docs [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background#step-6-to-disable-virtual-background-use-disable-methods)
  static Future<HMSException?> disableBlur() async {
    if (Platform.isAndroid) {
      var result = await PlatformService.invokeMethod(
          PluginMethod.disableBlurBackground);
      if (result != null) {
        return HMSException.fromMap(result["error"]);
      } else {
        return null;
      }
    } else {
      return HMSVideoFilter.disableBlur();
    }
  }
}
