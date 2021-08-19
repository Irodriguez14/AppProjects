import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:crypto/crypto.dart' as crypto;

import 'model/character.dart';
import 'model/comic.dart';

const MARVEL_PUBLIC_KEY = "030512a8a6554ea408f52581e1d91728";
const MARVEL_PRIVATE_KEY = "8956391a69c611f3bc9534ccecfa0de2226b5103";

class MarvelApi {
  
  Future<Map<String, dynamic>> download(String url) async {
    int ts = DateTime.now().millisecondsSinceEpoch;
    String hashData = "$ts$MARVEL_PRIVATE_KEY$MARVEL_PUBLIC_KEY";
    String hash = crypto.md5.convert(utf8.encode(hashData)).toString();
    http.Response response = await http.get(
      Uri.parse(
          "https://gateway.marvel.com/v1/public$url?ts=$ts&apikey=$MARVEL_PUBLIC_KEY&hash=$hash"),
    );
    if (response.statusCode != 200) {
      print("Error ${response.statusCode}");
      return null;
    }
    Map<String, dynamic> json = jsonDecode(response.body);
    if (json['code'] != 200) {
      print("Error from response ${json['code']}");
      return null;
    }
    return json;
  }

  Future<List<Character>> downloadCharacterList() async {
    Map<String, dynamic> json = await download("/characters");
    if (json == null) return null;
    List<Character> characters =
        (json['data']['results'] as List).map((dynamic item) => Character.fromJson(item)).toList();
    return characters;
  }

  Future<Character> downloadCharacter(int id, {downloadDetails = false}) async {
    Map<String, dynamic> json = await download("/characters/$id");
    if (json == null) return null;
    List jsonResults = json['data']['results'] as List;
    if (jsonResults.length != 1) {
      return null;
    }
    Character character = Character.fromJson(jsonResults[0]);
    if (downloadDetails) {
      Map<String, dynamic> json2 = await download("/characters/$id/comics");
      if (json2 == null) return null;
      List jsonResults2 = json2['data']['results'] as List;
      character.comicsList = jsonResults2.map((item) => Comic.fromJson(item)).toList();
    }
    return character;
  }
}