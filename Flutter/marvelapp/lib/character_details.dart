import 'package:flutter/material.dart';
import 'package:marvelapp/marvel_api.dart';

import 'model/character.dart';
import 'model/comic.dart';

class CharacterDetails extends StatelessWidget {
  final int id;

  CharacterDetails(this.id);

  Future<Character> _downloadCharacter() async {
    MarvelApi api = MarvelApi();
    return await api.downloadCharacter(id, downloadDetails: true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Details"),
      ),
      body: FutureBuilder(
        future: _downloadCharacter(),
        builder: (BuildContext context, AsyncSnapshot<Character> snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return Center(
              child: CircularProgressIndicator(),
            );
          }
          Character character = snapshot.data;
          return SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                children: [
                  Text(
                    "${character.name}",
                    style: TextStyle(fontSize: 42.0),
                  ),
                  Image.network(character.thumbnail),
                  SizedBox(
                    height: 12.0,
                  ),
                  Text(
                    character.description,
                  ),
                  SizedBox(
                    height: 12.0,
                  ),
                  Text(
                    "Comics",
                    style: TextStyle(fontSize: 22.0),
                  ),
                  SizedBox(
                    height: 12.0,
                  ),
                  Container(
                    height: 220,
                    child: ListView.builder(
                      scrollDirection: Axis.horizontal,
                      itemCount: character.comicsList.length,
                      itemBuilder: (BuildContext context, int index) {
                        Comic comic = character.comicsList[index];
                        return SizedBox(
                          width: 200,
                          height: 200,
                          child: Image.network(comic.thumbnail),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
