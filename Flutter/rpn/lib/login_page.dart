import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:rpn/settings_page.dart';
import 'package:rpn/users_api.dart';

import 'home_page.dart';
import 'providers/settings_provider.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final UsersApi usersApi = UsersApi();
  final userController = TextEditingController();
  final passController = TextEditingController();
  Future<String> futureToken;

  @override
  Widget build(BuildContext context) {
    print(context.watch<SettingsProvider>().futureTokenComplete);
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.backspace),
        onPressed: () {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (BuildContext context) {
              return HomePage();
            }),
          );
        },
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.startTop,
      resizeToAvoidBottomInset: false,
      body: SafeArea(
        child: Column(
          children: [
            Container(
              height: 200.0,
              width: MediaQuery.of(context).size.width,
              decoration: BoxDecoration(
                color: Colors.orangeAccent,
              ),
              child: Center(
                  child: Text(
                "RPN CALCULATOR",
                style: TextStyle(fontSize: 38, fontWeight: FontWeight.bold, color: Colors.white),
                textAlign: TextAlign.center,
              )),
            ),
            SizedBox(height: 50),
            Center(
              child: SizedBox(
                width: 300,
                child: TextField(
                  decoration: InputDecoration(labelText: "Username"),
                  controller: userController,
                ),
              ),
            ),
            SizedBox(height: 50),
            Center(
              child: SizedBox(
                width: 300,
                child: TextField(
                  obscureText: true,
                  enableSuggestions: false,
                  autocorrect: false,
                  controller: passController,
                  decoration: InputDecoration(labelText: "Password"),
                ),
              ),
            ),
            SizedBox(height: 50),
            Container(
              height: 50,
              width: 250,
              decoration: BoxDecoration(
                  color: Colors.blue, borderRadius: BorderRadius.circular(20)),
              child: TextButton(
                onPressed: () {
                  Provider.of<SettingsProvider>(context, listen: false)
                      .isLogin();
                  Provider.of<SettingsProvider>(context, listen: false)
                      .login(userController.text, passController.text);
                  if (Provider.of<SettingsProvider>(context, listen: false)
                          .token !=
                      "") {
                    Navigator.pushReplacement(context,
                        MaterialPageRoute(builder: (BuildContext context) {
                      return SettingsPage();
                    }));
                  }
                },
                child: Text(
                  'Login',
                  style: TextStyle(color: Colors.white, fontSize: 25),
                ),
              ),
            ),
            SizedBox(height: 50),
            if (context.watch<SettingsProvider>().futureTokenComplete)
              CircularProgressIndicator(),
          ],
        ),
      ),
    );
  }
}
