import 'package:flutter/material.dart';

import '../key_definition.dart';
import 'keypad_button.dart';

class Keypad extends StatelessWidget {
  final int numRows;
  final int numCols;
  final List<KeyDefinition> keyDefinitions;

  Keypad({
    @required this.numRows,
    @required this.numCols,
    @required this.keyDefinitions,
  });

  @override
  Widget build(BuildContext context) {
    double fontSize = MediaQuery.of(context).size.width / numCols * 0.5;
    return SizedBox(
      width: MediaQuery.of(context).size.width - 20,
      height: MediaQuery.of(context).size.width / numCols * numRows,
      child: GridView.count(
        physics: NeverScrollableScrollPhysics(),
        crossAxisCount: numCols,
        children: keyDefinitions
            .map((KeyDefinition keyDef) => KeypadButton(
                  text: keyDef.text,
                  iconData: keyDef.iconData,
                  op: keyDef.op,
                  color: keyDef.color,
                  size: fontSize * keyDef.fontFactor,
                ))
            .toList(),
      ),
    );
  }
}
