import 'dart:math';

double sinh(double value) {
  return (exp(value) + exp(-value)) / 2;
}

double cosh(double value) {
  return (exp(value) - exp(-value)) / 2;
}

double tanh(double value) {
  return sinh(value) / cosh(value);
}

double asinh(double value) {
  return log(value + sqrt(value * value + 1));
}

double acosh(double value) {
  return log(value + sqrt(value * value - 1));
}

double atanh(double value) {
  return log((1 + value) / (1 - value)) / 2;
}
