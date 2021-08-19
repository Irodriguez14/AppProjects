import 'dart:convert';

import 'package:http/http.dart' as http;

class UsersApi {
  Future<String> getToken(String username, String password) async {
    http.Response response = await http.post(
        Uri.parse("https://api.flx.cat/users/user/token/refresh"),
        body: {"username": username, "password": password});
    if (response.statusCode != 200) {
      print("Error ${response.statusCode}");
      return null;
    }
    Map<String, dynamic> json = jsonDecode(response.body);
    return json["token"];
  }

  Future<Map<String, dynamic>> addSetting(
      String nameSetting, String settingValue, String token) async {
    http.Response response = await http
        .post(Uri.parse("https://api.flx.cat/users/setting"), headers: {
      "Authorization": "Bearer $token",
    }, body: {
      "name": nameSetting,
      "value": settingValue,
    });
    if (response.statusCode != 200) {
      print("Error ${response.statusCode}");
      return null;
    }
    Map<String, dynamic> json = jsonDecode(response.body);
    return json;
  }

  Future<Map<String, dynamic>> editSetting(String nameSetting, String settingValue, String token) async {
    http.Response response = await http.patch(
        Uri.parse("https://api.flx.cat/users/setting/$nameSetting"),
        headers: {
          "Authorization": "Bearer $token",
        },
        body: {
          "value": settingValue,
        });
    if (response.statusCode != 200) {
      if (response.statusCode == 404) {
        return addSetting(nameSetting, settingValue, token);
      } else {
        print("Error ${response.statusCode}");
        return null;
      }
    }
    Map<String, dynamic> json = jsonDecode(response.body);
    return json;
  }

  Future<List<dynamic>> getAllSettings(String token) async {
    http.Response response = await http.get(
        Uri.parse("https://api.flx.cat/users/setting"),
        headers: {"Authorization": "Bearer $token"});
    if (response.statusCode != 200) {
      print("Error ${response.statusCode}");
      return null;
    }
    List<dynamic> json = jsonDecode(response.body);
    return json;
  }
}
