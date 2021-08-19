import 'comic.dart';

class Character {
  final int id;
  final String name;
  final String description;
  final String thumbnail;
  // final List<ComicSimple> comics;   // Original JSON
  List<Comic> comicsList;

  Character({
    this.id,
    this.name,
    this.description,
    this.thumbnail,
  });

  factory Character.fromJson(Map<String, dynamic> data) {
    String url = "${data['thumbnail']['path']}.${data['thumbnail']['extension']}";
    url = url.replaceAll("http:", "https:");
    return Character(
      id: data['id'] as int,
      name: data['name'],
      description: data['description'],
      thumbnail: url,
    );
  }
}
