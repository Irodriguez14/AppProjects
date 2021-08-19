import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:rpn/users_api.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:fluttertoast/fluttertoast.dart';

class SettingsProvider extends ChangeNotifier {
  Color _colorTheme;
  String _fontSize;
  bool _showAppBar;
  bool _showTrigonometrics;
  String _token;
  bool _futureTokenComplete;
  UsersApi usersApi;

  var appBarValue;

  Color get colorTheme => _colorTheme;
  String get fontSize => _fontSize;
  String get token => _token;
  bool get showAppBar => _showAppBar;
  bool get showTrigonometrics => _showTrigonometrics;
  bool get futureTokenComplete => _futureTokenComplete;

  SettingsProvider() {
    _showAppBar = false;
    _showTrigonometrics = true;
    _futureTokenComplete = false;
    _fontSize = "16";
    _colorTheme = Colors.deepOrange;
    usersApi = UsersApi();
    loadSettings();
  }

  void loadSettings() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    _token = prefs.getString("token");
    print(_token);
    if (token != "" && token != null) {
      Future<List<dynamic>> loadSettings = usersApi.getAllSettings(_token);
      List<dynamic> result;
      await loadSettings.then((value) => result = value);
      for (int i = 0; i < result.length; i++) {
        if (result[i]["name"] == "appBar") {
          _showAppBar = result[i]["value"] == "true";
        } else if (result[i]["name"] == "trigonometrics") {
          print(result[i]["value"] == true);
          _showTrigonometrics = result[i]["value"] == "true";
          print(_showTrigonometrics);
        } else if (result[i]["name"] == "fontSize") {
          _fontSize = result[i]["value"];
        } else if (result[i]["name"] == "colorTheme") {
          _colorTheme = new Color(int.parse(result[i]["value"]));
        }
      }
    }else{
      _showAppBar = false;
      _showTrigonometrics = true;
      _futureTokenComplete = false;
      _fontSize = "16";
      _colorTheme = Colors.deepOrange;
    }
      notifyListeners();
  }

  void toggleAppBar() async {
    _showAppBar = !_showAppBar;
    Future<Map<String, dynamic>> editSetting =
        usersApi.editSetting("appBar", _showAppBar.toString(), _token);
    Map<String, dynamic> result;
    await editSetting.then((value) => result = value);
    if (result == null) {
      Fluttertoast.showToast(
          msg: "Error saving at the database",
          toastLength: Toast.LENGTH_LONG,
          fontSize: 30.0);
    }
  }

  void toggleTrigonometrics() async {
    _showTrigonometrics = !_showTrigonometrics;
    notifyListeners();
    Future<Map<String, dynamic>> editSetting = usersApi.editSetting(
        "trigonometrics", _showTrigonometrics.toString(), _token);
    Map<String, dynamic> result;
    await editSetting.then((value) => result = value);
    if (result == null) {
      Fluttertoast.showToast(
          msg: "Error saving at the database",
          toastLength: Toast.LENGTH_LONG,
          fontSize: 30.0);
    }
  }

  void changeFontSize(String font) async {
    _fontSize = font;
    notifyListeners();
    Future<Map<String, dynamic>> editSetting =
        usersApi.editSetting("fontSize", _fontSize, _token);
    Map<String, dynamic> result;
    await editSetting.then((value) => result = value);
    if (result == null) {
      Fluttertoast.showToast(
          msg: "Error saving at the database",
          toastLength: Toast.LENGTH_LONG,
          fontSize: 30.0);
    }
  }

  void changeColor(Color color) async {
    _colorTheme = color;
    notifyListeners();
    Future<Map<String, dynamic>> editSetting = usersApi.editSetting(
        "colorTheme", _colorTheme.value.toString(), _token);
    Map<String, dynamic> result;
    await editSetting.then((value) => result = value);
    if (result == null) {
      Fluttertoast.showToast(
          msg: "Error saving at the database",
          toastLength: Toast.LENGTH_LONG,
          fontSize: 30.0);
    }
  }

  void saveToken(String token) async {
    _token = token;
    notifyListeners();
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString("token", _token);
  }

  void eraseToken() async {
    _token = "";
    notifyListeners();
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString("token", _token);
    loadSettings();
  }

  void isLogin() {
    _futureTokenComplete = true;
    notifyListeners();
  }

  void login(String username, String password) async {
    Future<String> tokenFuture = usersApi.getToken(username, password);
    String tokenActual;
    await tokenFuture.then((value) => tokenActual = value);
    tokenFuture.whenComplete(() => _futureTokenComplete = false);
    notifyListeners();
    print(tokenActual);
    if (tokenActual == null) {
      Fluttertoast.showToast(
          msg: "Error with credentials",
          toastLength: Toast.LENGTH_LONG,
          fontSize: 30.0);
    } else {
      saveToken(tokenActual);
      loadSettings();
    }
  }
}
