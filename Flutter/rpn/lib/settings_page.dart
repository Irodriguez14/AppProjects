import 'package:flutter/material.dart';
import 'package:flutter_colorpicker/flutter_colorpicker.dart';
import 'package:provider/provider.dart';
import 'package:rpn/home_page.dart';
import 'package:rpn/login_page.dart';
import 'package:rpn/providers/settings_provider.dart';

class SettingsPage extends StatefulWidget {
  @override
  _SettingsPageState createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  bool switchAppBar;
  bool switchTrigonometrics = true;
  Color currentColor;
  Color changeColor;
  String fontSize;

  @override
  Widget build(BuildContext context) {
    currentColor = Theme.of(context).accentColor;
    switchAppBar = context.watch<SettingsProvider>().showAppBar;
    switchTrigonometrics = context.watch<SettingsProvider>().showTrigonometrics;
    fontSize = context.watch<SettingsProvider>().fontSize;
    return Scaffold(
      appBar: context.watch<SettingsProvider>().showAppBar
          ? AppBar(
              title: Text("Settings"),
              backgroundColor: Theme.of(context).accentColor,
            )
          : null,
      floatingActionButton: !context.watch<SettingsProvider>().showAppBar
          ? FloatingActionButton(
              child: Icon(Icons.backspace),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (BuildContext context) {
                    return HomePage();
                  }),
                );
              },
            )
          : null,
      floatingActionButtonLocation: FloatingActionButtonLocation.startTop,
      body: SafeArea(
        child: Container(
          child: ListView(
            padding: const EdgeInsets.all(20.0),
            children: [
              SizedBox(
                height: 50,
              ),
              ListTile(
                  title: Text(
                    "Change color",
                    style: TextStyle(fontSize: 20),
                  ),
                  trailing: Icon(Icons.color_lens,
                      color: Theme.of(context).accentColor),
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return AlertDialog(
                          titlePadding: const EdgeInsets.all(0.0),
                          contentPadding: const EdgeInsets.all(0.0),
                          content: SingleChildScrollView(
                            child: ColorPicker(
                              pickerColor: currentColor,
                              onColorChanged: (Color color) {
                                currentColor = color;
                              },
                              colorPickerWidth: 300.0,
                              pickerAreaHeightPercent: 0.7,
                              enableAlpha: true,
                              displayThumbColor: true,
                              showLabel: true,
                              paletteType: PaletteType.hsv,
                              pickerAreaBorderRadius: const BorderRadius.only(
                                topLeft: const Radius.circular(2.0),
                                topRight: const Radius.circular(2.0),
                              ),
                            ),
                          ),
                          actions: [
                            TextButton(
                              child: Text("OK"),
                              onPressed: () {
                                Navigator.of(context).pop();
                                Provider.of<SettingsProvider>(context,
                                        listen: false)
                                    .changeColor(currentColor);
                              },
                            )
                          ],
                        );
                      },
                    );
                  }),
              ListTile(
                title: Text(
                  "Change keypad font size",
                  style: TextStyle(fontSize: 20),
                ),
                trailing: DropdownButton<String>(
                  value: fontSize,
                  icon: const Icon(Icons.arrow_drop_down),
                  iconSize: 24,
                  elevation: 16,
                  style: TextStyle(color: currentColor, fontSize: 18),
                  underline: Container(
                    height: 2,
                    color: Colors.deepPurpleAccent,
                  ),
                  onChanged: (String newValue) {
                    setState(() {
                      fontSize = newValue;
                      Provider.of<SettingsProvider>(context, listen: false)
                          .changeFontSize(fontSize);
                    });
                  },
                  items: <String>['12', '14', '16', '18', '20']
                      .map<DropdownMenuItem<String>>((String value) {
                    return DropdownMenuItem<String>(
                      value: value,
                      child: Text(value),
                    );
                  }).toList(),
                ),
              ),
              ListTile(
                title: Text(
                  "Show AppBar",
                  style: TextStyle(fontSize: 20),
                ),
                trailing: Switch(
                    value: switchAppBar,
                    onChanged: (value) {
                      setState(() {
                        switchAppBar = value;
                        Provider.of<SettingsProvider>(context, listen: false)
                            .toggleAppBar();
                      });
                    }),
              ),
              ListTile(
                title: Text(
                  "Show Trigonometrics",
                  style: TextStyle(fontSize: 20),
                ),
                trailing: Switch(
                    value: switchTrigonometrics,
                    onChanged: (value) {
                      setState(() {
                        switchTrigonometrics = value;
                        Provider.of<SettingsProvider>(context, listen: false)
                            .toggleTrigonometrics();
                      });
                    }),
              ),SizedBox(
                height: 50,
              ),
              Container(
                  height: 50,
                  width: 200,
                  decoration: BoxDecoration(
                      color: Colors.blue,
                      borderRadius: BorderRadius.circular(20)),
                  child: TextButton(
                    onPressed: () {
                      Provider.of<SettingsProvider>(context, listen: false).eraseToken();
                      Navigator.pushReplacement(context,
                        MaterialPageRoute(builder: (BuildContext context) {
                          return LoginPage();
                        }));
                    },
                    child: Text(
                      'Logout',
                      style: TextStyle(color: Colors.white, fontSize: 25),
                    ),
                  )),
            ],
            physics: NeverScrollableScrollPhysics(),
          ),
        ),
      ),
    );
  }

  void saveSwitchAppBar(value) {}
}
