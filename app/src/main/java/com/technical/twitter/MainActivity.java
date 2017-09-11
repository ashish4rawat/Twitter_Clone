package com.technical.twitter;

import android.content.Intent;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import javax.security.auth.login.LoginException;


public class MainActivity extends AppCompatActivity {
        EditText passwordText,usernameText;
        ParseUser user ;
        Intent intent ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                setTitle("Twitter : Login");


                 usernameText  = (EditText)findViewById(R.id.usernameText);
                 passwordText = (EditText)findViewById(R.id.passwordText);

                Log.i("ashihs","rawat");



                 if(ParseUser.getCurrentUser()!=null){

                         intent  = new Intent(MainActivity.this,UsersActive.class);
                         startActivity(intent);
                 }



                ParseAnalytics.trackAppOpenedInBackground(getIntent());

        }

        public void loginOrSignUp(View view){
                Log.i("InfoAbout","button presseed");

                if(usernameText.getText().toString().matches("") || passwordText.getText().toString().matches(""))
                {

                        Toast.makeText(this,"Enter some text",Toast.LENGTH_LONG).show();

                }
                else{


                        ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser usera, ParseException e) {

                                        if(e==null){
                                                Log.i("InfoAbout","Login Successfull");
                                                intent  = new Intent(MainActivity.this,UsersActive.class);
                                                startActivity(intent);

                                        }
                                        else{



                                                ParseUser cur = ParseUser.getCurrentUser();
                                                cur.logOut();

                                                user = new ParseUser();
                                                String str="";
                                                Log.i("InfoAbout","Login Failed");
                                                Log.i("InfoAbout",e.toString());


                                                Log.i("InfoAbout","Signing Up the user");

                                                        user.setUsername(usernameText.getText().toString());
                                                        user.setPassword(passwordText.getText().toString());


                                                        user.signUpInBackground(new SignUpCallback() {
                                                                @Override
                                                                public void done(ParseException e) {

                                                                        if(e==null){

                                                                              Log.i("InfoAbout","SignUp Success");
                                                                                intent  = new Intent(MainActivity.this,UsersActive.class);
                                                                                startActivity(intent);

                                                                        }
                                                                        else{
                                                                                Log.i("InfoAbout","Sign Up Failed");
                                                                                 Log.i("InfoAbout",e.getMessage());
                                                                        }
                                                                }
                                                        });




                                        }

                                }
                        });


                }


        }






}
