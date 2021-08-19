import 'package:flutter/material.dart';
import 'package:rpn/providers/settings_provider.dart';
import 'package:rpn/settings_page.dart';
import 'package:rpn/widget/results_screen.dart';
import 'package:provider/provider.dart';

import 'key_definition.dart';
import 'login_page.dart';
import 'widget/keypad.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _currentPageIndex;
  bool showAppBar;

  @override
  void initState() {
    super.initState();
    _currentPageIndex = 0;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: context.watch<SettingsProvider>().showAppBar
          ? AppBar(
              title: Text("RPN Calculator"),
              backgroundColor: Theme.of(context).accentColor,
              actions: [
                  IconButton(
                    icon: Icon(Icons.settings),
                    onPressed: () {
                      Navigator.push(context, MaterialPageRoute(
                        builder: (BuildContext context) {
                          if (context.watch<SettingsProvider>().token == "") {
                            return LoginPage();
                          }
                          print(context.watch<SettingsProvider>().token);
                          return SettingsPage();
                        },
                      ));
                    },
                  )
                ])
          : null,
      floatingActionButton: !context.watch<SettingsProvider>().showAppBar
          ? FloatingActionButton(
              child: Icon(Icons.settings),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (BuildContext context) {
                    if (context.watch<SettingsProvider>().token == "") {
                      return LoginPage();
                    }
                    print(context.watch<SettingsProvider>().token);
                    return SettingsPage();
                  }),
                );
              },
            )
          : null,
      floatingActionButtonLocation: FloatingActionButtonLocation.endTop,
      body: SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.max,
          children: [
            Expanded(
              child: ResultsScreen(),
            ),
            context.watch<SettingsProvider>().showTrigonometrics
                ? _currentPageIndex == 0
                    ? Keypad(
                        numRows: 5,
                        numCols: 5,
                        keyDefinitions: KEYPAD_HOME,
                      )
                    : Keypad(
                        numRows: 5,
                        numCols: 4,
                        keyDefinitions: KEYPAD_TRIGONOMETRICS,
                      )
                : Keypad(
                    numRows: 5,
                    numCols: 5,
                    keyDefinitions: KEYPAD_HOME,
                  )
          ],
        ),
      ),
      bottomNavigationBar: context.watch<SettingsProvider>().showTrigonometrics
          ? BottomNavigationBar(
              currentIndex: _currentPageIndex,
              onTap: (int index) => setState(() => _currentPageIndex = index),
              items: [
                BottomNavigationBarItem(
                  icon: Icon(Icons.home),
                  label: "Home",
                ),
                BottomNavigationBarItem(
                  icon: Icon(Icons.square_foot),
                  label: "Trigonometrics",
                ),
              ],
            )
          : null,
    );
  }

  static const List<KeyDefinition> KEYPAD_HOME = [
    KeyDefinition(
        text: "Clear", op: "clear", fontFactor: 0.5, color: Colors.red),
    KeyDefinition(
        iconData: Icons.swap_vert,
        op: "swap",
        fontFactor: 1.0,
        color: Colors.blue),
    KeyDefinition(
        text: "%", op: "percent", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(text: "±", op: "pm", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(
        iconData: Icons.backspace,
        op: "back",
        fontFactor: 0.9,
        color: Colors.red),
    KeyDefinition(
        text: "7", op: "7", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "8", op: "8", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "9", op: "9", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(text: "+", op: "+", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(text: "√", op: "sqrt", fontFactor: 1.0, color: Colors.black),
    KeyDefinition(
        text: "4", op: "4", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "5", op: "5", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "6", op: "6", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(text: "-", op: "-", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(
        text: "x²", op: "square", fontFactor: 1.0, color: Colors.black),
    KeyDefinition(
        text: "1", op: "1", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "2", op: "2", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: "3", op: "3", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        iconData: Icons.close, op: "*", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(text: "xʸ", op: "pow", fontFactor: 0.65, color: Colors.black),
    KeyDefinition(
        text: "0", op: "0", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        text: ".", op: ".", fontFactor: 1.0, color: Colors.deepPurple),
    KeyDefinition(
        iconData: Icons.subdirectory_arrow_left,
        op: "enter",
        fontFactor: 1.0,
        color: Colors.green),
    KeyDefinition(text: "/", op: "/", fontFactor: 1.0, color: Colors.brown),
    KeyDefinition(
        text: "1/x", op: "recip", fontFactor: 0.65, color: Colors.black),
  ];

  static const List<KeyDefinition> KEYPAD_TRIGONOMETRICS = [
    KeyDefinition(text: "π", op: "pi", fontFactor: 0.6, color: Colors.blue),
    KeyDefinition(text: "π²", op: "pi2", fontFactor: 0.6, color: Colors.blue),
    KeyDefinition(text: "2π", op: "2pi", fontFactor: 0.6, color: Colors.blue),
    KeyDefinition(
        text: "DEG\nto\nRAD",
        op: "d>r",
        fontFactor: 0.4,
        color: Colors.deepOrange),
    KeyDefinition(text: "sin", op: "sin", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(text: "cos", op: "cos", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(text: "tan", op: "tan", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "RAD\nto\nDEG",
        op: "r>d",
        fontFactor: 0.4,
        color: Colors.deepOrange),
    KeyDefinition(
        text: "sin⁻¹", op: "asin", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "cos⁻¹", op: "acos", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "tan⁻¹", op: "atan", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "tan'⁻¹", op: "atan2", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "sinh", op: "sinh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "cosh", op: "cosh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "tanh", op: "tanh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "RECT\nto\nPOL",
        op: "r>p",
        fontFactor: 0.4,
        color: Colors.deepOrange),
    KeyDefinition(
        text: "sinh⁻¹", op: "asinh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "cosh⁻¹", op: "acosh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "tanh⁻¹", op: "atanh", fontFactor: 0.6, color: Colors.black),
    KeyDefinition(
        text: "POL\nto\nRECT",
        op: "p>r",
        fontFactor: 0.4,
        color: Colors.deepOrange),
  ];
}
