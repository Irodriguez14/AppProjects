import 'package:flutter/material.dart';

class KeyDefinition {
  final String text;
  final IconData iconData;
  final String op;
  final double fontFactor;
  final Color color;

  const KeyDefinition({
    this.text,
    this.iconData,
    @required this.op,
    this.fontFactor = 1.0,
    this.color,
  }) : assert(text != null || iconData != null);
}
