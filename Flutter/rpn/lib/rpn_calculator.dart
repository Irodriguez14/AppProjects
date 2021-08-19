import 'dart:math' as math;

import 'package:flutter/foundation.dart';

import 'hyperbolic.dart' as hyp;

class RPNCalculator extends ChangeNotifier {
  List<double> _stack;
  List<double> get stack => _stack;

  String _editing;
  String get editing => _editing;
  bool get isEditing => _editing != null;

  RPNCalculator() : _stack = [];

  // bool isDigit(String op) => "0123456789".split("").contains(op);

  void doCalc(String op) {
    _doCalc(op);
    notifyListeners();
  }

  void _doCalc(String op) {
    // Digit writing number
    bool isDigit = "0123456789".split("").contains(op);
    if (isDigit) {
      if (_editing == null) {
        _editing = op;
      } else {
        _editing = "$_editing$op";
      }
      return;
    }

    // double dot
    if (op == '.') {
      if (_editing == null) {
        _editing = "0.";
      } else if (_editing == "-") {
        _editing = "-0.";
      } else if (!_editing.contains('.')) {
        _editing = "$_editing$op";
      }
      return;
    }

    // Back del
    if (op == "back" && isEditing && _editing.length > 0) {
      _editing = _editing.substring(0, _editing.length - 1);
      if (_editing.isEmpty) _editing = null;
      return;
    }

    // Enter the number
    if ((op == "enter") && isEditing) {
      _stack.add(double.parse(_editing));
      _editing = null;
      return;
    }

    // CLEAR
    if (op == 'clear') {
      _stack = [];
    }

    // OPERATORS
    // First accept the currently writing number (as enter)
    if (isEditing && (_editing != "-")) {
      _stack.add(double.parse(_editing));
      _editing = null;
    }

    dynamic result;
    int length = _stack.length;

    // CONSTANT OPS (0 argument)
    if (constantOperations.containsKey(op)) {
      result = constantOperations[op]();
    }
    if (result != null) {
      return _addResults(result);
    }

    // UNARY OPS (1 argument)
    if (length < 1) return;
    double value1 = _stack.last;
    if (unaryOperations.containsKey(op)) {
      result = unaryOperations[op](value1);
    }
    if (result != null) {
      _stack.removeLast();
      return _addResults(result);
    }

    // BINARY OPS (2 argument)
    if (length < 2) return;
    double value2 = value1;
    value1 = _stack[length - 2];
    if (binaryOperations.containsKey(op)) {
      result = binaryOperations[op](value1, value2);
    }
    if (result != null) {
      _stack.removeRange(length - 2, length);
      return _addResults(result);
    }
  }

  void _addResults(dynamic results) {
    if (results is List) {
      _stack.addAll(results as List<double>);
    } else {
      _stack.add(results as double);
    }
  }
}

Map<String, Function()> constantOperations = {
  'pi': () => math.pi,
  '2pi': () => 2 * math.pi,
  'pi2': () => math.pi * math.pi,
};

// UNARY OPERATIONS
Map<String, Function(double)> unaryOperations = {
  'pm': (double value) => -value,
  'sqrt': (double value) => math.sqrt(value.toDouble()),
  'recip': (double value) => 1 / value,
  'square': (double value) => value * value,
  'sin': (double value) => math.sin(value.toDouble()),
  'cos': (double value) => math.cos(value.toDouble()),
  'tan': (double value) => math.tan(value.toDouble()),
  'asin': (double value) => math.asin(value.toDouble()),
  'acos': (double value) => math.acos(value.toDouble()),
  'atan': (double value) => math.atan(value.toDouble()),
  'sinh': (double value) => hyp.sinh(value.toDouble()),
  'cosh': (double value) => hyp.cosh(value.toDouble()),
  'tanh': (double value) => hyp.tanh(value.toDouble()),
  'asinh': (double value) => hyp.asinh(value.toDouble()),
  'acosh': (double value) => hyp.acosh(value.toDouble()),
  'atanh': (double value) => hyp.atanh(value.toDouble()),
  'd>r': (double value) => value * math.pi / 180.0,
  'r>d': (double value) => value * 180.0 / math.pi,
};

// BINARY OPERATIONS
Map<String, Function(double, double)> binaryOperations = {
  '+': (double value1, double value2) => value1 + value2,
  '-': (double value1, double value2) => value1 - value2,
  '*': (double value1, double value2) => value1 * value2,
  '/': (double value1, double value2) => (value2.toDouble() == 0) ? double.infinity : value1 / value2,
  'swap': (double value1, double value2) => [ value2, value1 ],
  'pow': (double value1, double value2) => math.pow(value1, value2),
  'percent': (double value1, double value2) => value1 * value2 / 100,
  'atan2': (double value1, double value2) => math.atan2(value1.toDouble(), value2.toDouble()),
  'r>p': (double value1, value2) => [
        math.sqrt(value1 * value1 + value2 * value2),
        math.atan2(value2, value1),
      ],
  'p>r': (double value1, value2) => [
        value1 * math.cos(value2),
        value1 * math.sin(value2),
      ],
};
