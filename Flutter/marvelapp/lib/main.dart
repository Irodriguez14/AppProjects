import 'package:flutter/material.dart';

import 'character_list.dart';

void main() async {
  runApp(MarvelApp());
}

class MarvelApp extends StatelessWidget {
//   // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: CharacterListPage(),
    );
  }
}
