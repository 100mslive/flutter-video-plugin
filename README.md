# 100ms Video Plugin

[![Pub Version](https://img.shields.io/pub/v/hms_video_plugin)](https://pub.dev/packages/hms_video_plugin)
[![Build](https://github.com/100mslive/100ms-flutter/actions/workflows/build.yml/badge.svg?branch=develop)](https://github.com/100mslive/100ms-flutter/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/100mslive/100ms-flutter)](https://www.100ms.live/)
[![Documentation](https://img.shields.io/badge/Read-Documentation-blue)](https://docs.100ms.live/flutter/v2/foundation/basics)
[![Discord](https://img.shields.io/discord/843749923060711464?label=Join%20on%20Discord)](https://100ms.live/discord)
[![Firebase](https://img.shields.io/badge/Download%20Android-Firebase-green)](https://appdistribution.firebase.dev/i/b623e5310929ab70)
[![TestFlight](https://img.shields.io/badge/Download%20iOS-TestFlight-blue)](https://testflight.apple.com/join/Uhzebmut)
[![Activity](https://img.shields.io/github/commit-activity/m/100mslive/100ms-flutter.svg)](https://github.com/100mslive/100ms-flutter/projects?type=classic)
[![Register](https://img.shields.io/badge/Contact-Know%20More-blue)](https://dashboard.100ms.live/register)

<p align="center" width="100%">
<img alt="Prebuilt - VB" src="https://github.com/100mslive/flutter-video-plugin/assets/93931528/b85b5f48-b06c-4b11-b3d6-8639f8cbd1c3">
</p>

Integrate virtual backgrounds, blur backgrounds effects in your Flutter app with the 100ms Video Plugin. 100ms video plugin enabled adding virtual backgrounds, blur backgrounds, and other video filters to your 100ms video conferencing app. The plugin is built on top of the 100ms Flutter SDK.

📖 Read the Complete Documentation here: https://www.100ms.live/docs/flutter/v2/guides/quickstart

📲 Download the Sample iOS app here: <https://testflight.apple.com/join/Uhzebmut>

🤖 Download the Sample Android app here: <https://appdistribution.firebase.dev/i/b623e5310929ab70>

100ms Flutter apps are also released on the App Stores, you can download them here:

📲 iOS app on Apple App Store: <https://apps.apple.com/app/100ms-live/id1576541989>

🤖 Android app on Google Play Store: <https://play.google.com/store/apps/details?id=live.hms.flutter>

<div align="center">
  <video src="https://github.com/100mslive/flutter-video-plugin/assets/93931528/c44c4501-dd70-4d92-a23d-06fee1872278" controls >
</div>


## 🚂 Setup Guide

1. Sign up on <https://dashboard.100ms.live/register> & visit the Developer tab to access your credentials.

2. Get familiarized with [Tokens & Security here](https://www.100ms.live/docs/flutter/v2/foundation/security-and-tokens)

3. Complete the steps in [Auth Token Quick Start Guide](https://www.100ms.live/docs/flutter/v2/guides/token)

4. Get the `hms_video_plugin` via [pub.dev](https://pub.dev/packages/hms_video_plugin). Add the `hms_video_plugin` to your pubspec.yaml.

📖 Do refer the Complete [Installation Guide here](https://www.100ms.live/docs/flutter/v2/features/integration).

## Minimum Configuration

- Support for Flutter 3.10.0 or above
- Support for Android API level 21 or above
- Support for iOS 15 or above

## Supported Versions/Resolutions

-   Minimum 100ms SDK version it can work with is `1.10.3`

## Limitations

- Has poor fps on older android phones
- Minimum iOS version required to support Virtual Background plugin is `iOS 15`
- Virtual background plugin is in beta stage and may have performance issues on iPhone X, 8, 7, 6 and other older devices. We recommend that you use this feature on a high performance device for smooth experience.

## Add dependency

To add virtual background to your application add `hms_video_plugin` to your application's `pubspec.yaml` file.

```yaml
hms_video_plugin:
```

## How to Integrate Virtual Background Plugin:

> 🔑 Note: `hms_video_plugin` cannot be used independently. Always call the virtual background APIs after `onJoin` or `onPreview`.

#### 1. Set the `isVirtualBackgroundEnabled` property in `HMSVideoTrackSetting` as `true`


```dart
var videoTrackSetting = HMSVideoTrackSetting(
            trackInitialState: joinWithMutedVideo
                ? HMSTrackInitState.MUTED
                : HMSTrackInitState.UNMUTED,
            isVirtualBackgroundEnabled: true);
```

#### 2. Pass the Track Settings to the HMSSDK constructor

```dart
/// Create Instance of `HMSTrackSetting`
var trackSettings = HMSTrackSetting(
        audioTrackSetting: HMSAudioTrackSetting(),
        videoTrackSetting: videoTrackSetting);
/// Set the track settings to HMSSDK
var hmsSDK = HMSSDK(
        hmsTrackSetting: trackSettings);
```

#### Step 3: Check for Virtual Background availability

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    ...
}
```

#### Step 4: If Virtual Background is available, enable it

To enable virtual background, call the `enable` method.

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    void enableVirtualBackground(Uint8List? image) async{
        ///[image] is the image to be set as background
        if(isVirtualBackgroundSupported){
            HMSException? isEnabled = await HMSVideoPlugin.enable(image: image);
            if(isEnabled == null){
                ///Virtual background started successfully
            }else{
                ///Error enabling virtual background
            }
        }
    }
    ...
}
```

To enabled background blur, call the `enableBlur` method.

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    void enableBackgroundBlur(int blurRadius) async{
        ///[blurRadius] is the radius of the blur effect
        if(isVirtualBackgroundSupported){
            HMSException? isEnabled = await HMSVideoPlugin.enableBlur(blurRadius: blurRadius);
            if(isEnabled == null){
                ///Background blur started successfully
            }else{
                ///Error enabling blur
            }
        }
    }
    ...
}
```

#### Step 5: To change virtual background image use changeVirtualBackground method

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    ///If virtual background is enabled, then we can change the virtual background image
    void changeVirtualBackground(Uint8List? image) {
        ///[image] is the image new image to be set as background
        ///[isVirtualBackgroundSupported] is the flag to check if virtual background is supported
        ///[isVirtualBackgroundEnabled] is the flag to check if virtual background is enabled
        if(isVirtualBackgroundSupported && isVirtualBackgroundEnabled){
            HMSVideoPlugin.changeVirtualBackground(image: image);
        }
    }
    ...
}
```

#### Step 6: To disable Virtual Background use disable methods

To disable virtual background, call the `disable` method.

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    void disableVirtualBackground() async{
        if(isVirtualBackgroundSupported){
            HMSException? isDisabled = await HMSVideoPlugin.disable();
            if(isDisabled == null){
                ///Virtual Background disabled successfully
            }else{
                ///Error disabling virtual background
            }
        }
    }
    ...
}
```

To disable background blur use `disableBlur` method

```dart
class Meeting implements HMSUpdateListener, HMSActionResultListener{
    ...
    bool isVirtualBackgroundSupported = false;
    /// This method checks the virtual background availability
    void checkIsVirtualBackgroundSupported() async {
        isVirtualBackgroundSupported = await HMSVideoPlugin.isSupported();
    }
    void disableBackgroundBlur() async{
        if(isVirtualBackgroundSupported){
            HMSException? isDisabled = await HMSVideoPlugin.disableBlur();
            if(isDisabled == null){
                ///Background blur disabled successfully
            }else{
                ///Error disabling blur
            }
        }
    }
    ...
}
```
