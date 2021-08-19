class Comic {
  final int id;
  final String title;
  final String description;
  final String thumbnail;
  // final List<ComicSimple> comics;   // Original JSON
  List<Comic> comicsList;

  Comic({
    this.id,
    this.title,
    this.description,
    this.thumbnail,
  });

  factory Comic.fromJson(Map<String, dynamic> data) {
    String url = "${data['thumbnail']['path']}.${data['thumbnail']['extension']}";
    url = url.replaceAll("http:", "https:");
    return Comic(
      id: data['id'] as int,
      title: data['title'],
      description: data['description'],
      thumbnail: url,
    );
  }
}