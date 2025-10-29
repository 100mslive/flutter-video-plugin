require 'json'

sdkVersions = JSON.parse(File.read(File.join("../lib/assets/sdk-versions.json")))

Pod::Spec.new do |s|
  s.name             = 'hms_video_plugin'
  s.version          = sdkVersions["flutter"]
  s.summary          = 'The Flutter plugin for 100ms Video Plugin'
  s.description      = <<-DESC
  A Flutter plugin for integrating virtual backgrounds and blur background effects with 100ms SDK.
                      DESC
  s.homepage         = 'https://www.100ms.live/'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Yogesh Singh' => 'yogesh@100ms.live' }
  s.source           = { :git => 'https://github.com/100mslive/flutter-video-plugin.git' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.dependency "HMSSDK", sdkVersions["ios"]
  s.platform = :ios, '16.0'
  s.ios.deployment_target  = '16.0'
  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
