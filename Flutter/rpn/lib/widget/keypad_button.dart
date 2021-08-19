import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../rpn_calculator.dart';

class KeypadButton extends StatelessWidget {
  final String text;
  final IconData iconData;
  final String op;
  final double size;
  final Color color;

  KeypadButton({
    this.text,
    this.iconData,
    @required this.op,
    @required this.size,
    this.color,
  }) : assert(text != null || iconData != null);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        // Accedim només per executar. Ometrà els notifyListeners i no es
        // repintarà gràcies al listen: false
        RPNCalculator calc = Provider.of<RPNCalculator>(context, listen: false);
        calc.doCalc(op);
      },
      child: Container(
        margin: EdgeInsets.all(1.0),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: (color != null) ? color : Theme.of(context).primaryColor,
          border: Border.all(
            color: Theme.of(context).secondaryHeaderColor,
            width: 2.0,
          ),
        ),
        child: text != null
            ? Text(
                text,
                textAlign: TextAlign.center,
                style: TextStyle(color: Colors.white, fontSize: size),
              )
            : Icon(iconData, size: size, color: Colors.white),
      ),
    );
  }
}
