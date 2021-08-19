import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:rpn/providers/settings_provider.dart';
import 'package:rpn/rpn_calculator.dart';

import 'home_page.dart';

void main() => runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => RPNCalculator()),
        ChangeNotifierProvider(create: (_) => SettingsProvider()),
      ],
      child: MyApp(),
    ),
  );

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'RPN Calculator',
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
        accentColor: context.watch<SettingsProvider>().colorTheme,
      ),
      home: HomePage(),
    );
  }
}
