import 'package:flutter/material.dart';

import 'character_details.dart';
import 'marvel_api.dart';
import 'model/character.dart';

class CharacterListPage extends StatelessWidget {
  Future<List<Character>> _downloadCharacters() async {
    MarvelApi api = MarvelApi();
    return await api.downloadCharacterList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Marvel App"),
        actions: [],
      ),
      body: FutureBuilder(
          future: _downloadCharacters(),
          builder: (BuildContext context, AsyncSnapshot<List<Character>> snapshot) {
            if (snapshot.connectionState != ConnectionState.done) {
              return Center(child: CircularProgressIndicator());
            }
            List<Character> characters = snapshot.data;
            return ListView.builder(
              itemCount: characters.length,
              itemBuilder: (BuildContext context, int index) {
                Character character = characters[index];
                return GestureDetector(
                  onTap: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(
                        builder: (_) => CharacterDetails(character.id),
                      ),
                    );
                  },
                  child: Container(
                    height: 300,
                    child: Column(
                      children: [
                        SizedBox(
                          width: 250,
                          height: 250,
                          child: Image.network(character.thumbnail),
                        ),
                        Text(
                          character.name,
                          style: TextStyle(fontSize: 36.0),
                          overflow: TextOverflow.ellipsis,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                );
              },
            );
          }),
    );
  }
}
