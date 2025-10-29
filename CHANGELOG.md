# Latest Versions

| Package          | Version                                                                                                    |
| ---------------- | ---------------------------------------------------------------------------------------------------------- |
| hms_room_kit     | [![Pub Version](https://img.shields.io/pub/v/hms_room_kit)](https://pub.dev/packages/hms_room_kit)         |
| hmssdk_flutter   | [![Pub Version](https://img.shields.io/pub/v/hmssdk_flutter)](https://pub.dev/packages/hmssdk_flutter)     |
| hms_video_plugin | [![Pub Version](https://img.shields.io/pub/v/hms_video_plugin)](https://pub.dev/packages/hms_video_plugin) |

# 0.1.0 - 2025-10-29

| Package          | Version |
| ---------------- | ------- |
| hms_room_kit     | 1.2.0   |
| hmssdk_flutter   | 1.11.0  |
| hms_video_plugin | 0.1.0   |

### Added

- **Pre-initialization support for Virtual Background**: Added `preInitializeVirtualBackground()` method to improve reliability when enabling virtual background or blur effects
  - Ensures the plugin is properly added to the SDK pipeline before use
  - Reduces initialization failures and improves user experience
  - Should be called after `onJoin` or `onPreview` callbacks for optimal performance

### Fixed

- **Virtual Background Plugin Reliability**:
  - Fixed issues where virtual background/blur would fail to enable on first attempt
  - Improved error handling for plugin addition and removal operations
  - Added proper state tracking to prevent stale plugin references
  - Enhanced logging for debugging virtual background operations (Android)

### Changed

- **SDK Version Updates**:
  - Updated Android SDK to version `2.9.78`
  - Updated iOS SDK to version `1.17.0`

- **Android Platform Updates**:
  - Upgraded Gradle to `8.10.2`
  - Upgraded Android Gradle Plugin to `8.9.0`
  - Updated `compileSdkVersion` to `36`
  - Updated `minSdkVersion` to `24` (from `21`)
  - Updated Kotlin version to `2.+`
  - Updated Java target to version `17`
  - Added NDK version `27.2.12479018`

- **iOS Platform Updates**:
  - Updated minimum iOS version to `16.0` (from `11.0`)
  - Added CocoaPods integration with proper version locking
  - Enhanced permission handling configuration

- **Example App Improvements**:
  - Added required Android permissions (Camera, Microphone, Bluetooth, etc.)
  - Added Picture-in-Picture support for Android
  - Improved Android manifest configuration
  - Updated iOS info.plist with required permissions

- **Code Quality**:
  - Switched linter from `flutter_lints` to `lints/core.yaml`
  - Improved error handling in virtual background operations
  - Added comprehensive logging for debugging

### Technical Requirements

- **Android**: Requires API 24+ (Android 7.0+)
- **iOS**: Requires iOS 16.0+
- **Gradle**: 8.10.2
- **Kotlin**: 2.+
- **Java**: 17

Uses `hmssdk_flutter` package version 1.11.0

# 0.0.2 - 2024-07-01

| Package          | Version |
| ---------------- | ------- |
| hmssdk_flutter   | 1.10.4  |
| hms_room_kit     | 1.1.4   |
| hms_video_plugin | 0.0.2   |

- Updated package description

Uses `hmssdk_flutter` package version 1.10.3

# 0.0.1 - 2024-06-12

| Package          | Version |
| ---------------- | ------- |
| hmssdk_flutter   | 1.10.3  |
| hms_room_kit     | 1.1.3   |
| hms_video_plugin | 0.0.1   |

- Introducing support for Virtual Background and Blur

  Users can now use virtual background and blur features in their video calls using the `hms_video_plugin`.
  Learn more about the feature [here](https://www.100ms.live/docs/flutter/v2/how-to-guides/extend-capabilities/virtual-background)

Uses `hmssdk_flutter` package version 1.10.3
