import 'package:flutter/material.dart';
import 'package:hms_room_kit/hms_room_kit.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      themeMode: ThemeMode.dark,
      darkTheme: ThemeData.dark(),
      home: const StartScreen(),
    );
  }
}

class StartScreen extends StatefulWidget {
  const StartScreen({super.key});

  @override
  State<StartScreen> createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  bool _isLoading = false;

  Future<void> _requestPermissions() async {
    print('Start button pressed!');
    setState(() {
      _isLoading = true;
    });

    try {
      print('Requesting camera permission...');
      final cameraStatus = await Permission.camera.request();
      print('Camera permission status: $cameraStatus');

      print('Requesting microphone permission...');
      final microphoneStatus = await Permission.microphone.request();
      print('Microphone permission status: $microphoneStatus');

      setState(() {
        _isLoading = false;
      });

      if (cameraStatus.isGranted && microphoneStatus.isGranted) {
        // Navigate to HMSPrebuilt
        if (mounted) {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (context) => HMSPrebuilt(
                roomCode: "cte-xyht-skd",
              ),
            ),
          );
        }
      } else {
        // Show error message with details
        if (mounted) {
          String message = 'Permissions needed:\n';
          if (!cameraStatus.isGranted) message += '- Camera\n';
          if (!microphoneStatus.isGranted) message += '- Microphone\n';

          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(message),
              duration: const Duration(seconds: 3),
              action: SnackBarAction(
                label: 'Settings',
                onPressed: () => openAppSettings(),
              ),
            ),
          );
        }
      }
    } catch (e) {
      setState(() {
        _isLoading = false;
      });

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error requesting permissions: $e'),
            duration: const Duration(seconds: 3),
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: _isLoading
            ? const CircularProgressIndicator()
            : ElevatedButton(
                onPressed: _requestPermissions,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 48,
                    vertical: 16,
                  ),
                  textStyle: const TextStyle(fontSize: 18),
                ),
                child: const Text('Start'),
              ),
      ),
    );
  }
}
