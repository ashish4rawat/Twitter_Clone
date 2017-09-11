package com.technical.twitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersActive extends AppCompatActivity {

        ListView usersListView;
        ArrayList<String> myUsers;
        ArrayAdapter adapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_users_active);

                setTitle("Users Active");



                        List<String> emptyList = new ArrayList<>();
                        ParseUser.getCurrentUser().put("isFollowing",emptyList);



                usersListView = (ListView)findViewById(R.id.usersListView);
                usersListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                 myUsers = new ArrayList<String>();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());


                query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                                for (ParseUser object : objects){

                                        myUsers.add(object.getUsername());

                                }

                                 adapter = new ArrayAdapter(UsersActive.this,android.R.layout.simple_list_item_checked,myUsers);
                                usersListView.setAdapter(adapter);



                        }
                });




                usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                CheckedTextView checkedTextView = (CheckedTextView)view;
                                if(checkedTextView.isChecked()){

                                        Log.i("Checked","checked");
                                        ParseUser.getCurrentUser().getList("isFollowing").add(usersListView.getItemAtPosition(i).toString());
                                        Log.i("Checked Added",ParseUser.getCurrentUser().getList("isFollowing").toString());
                                        ParseUser.getCurrentUser().saveInBackground();


                                }
                                else{

                                        Log.i("Checked","Not Checked");
                                        ParseUser.getCurrentUser().getList("isFollowing").remove(myUsers.get(i));
                                        Log.i("Checked removed",ParseUser.getCurrentUser().getList("isFollowing").toString());
                                        ParseUser.getCurrentUser().saveInBackground();


                                }


                        }
                });






        }







        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.main_menu,menu);

                return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {



                if(item.getItemId()==R.id.tweet){

                        final EditText tweetText = new EditText(this);
                        LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        tweetText.setLayoutParams(layPar);

                        new AlertDialog.Builder(this)
                                .setTitle("Send a Tweet")
                                .setView(tweetText)
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                                ParseObject obj = new ParseObject("Tweets");
                                                obj.put("tweet",tweetText.getText().toString());
                                                obj.put("username",ParseUser.getCurrentUser().getUsername());
                                                obj.saveInBackground();

                                                Toast.makeText(UsersActive.this,"Tweeted",Toast.LENGTH_LONG).show();

                                        }
                                })
                                .setNegativeButton("Cancel",null)
                                .show();




                }

                if(item.getItemId()==R.id.yourfeed){


                        Intent intent = new Intent(this,YourFeed.class);
                        startActivity(intent);




                }

                if(item.getItemId()==R.id.logout){

                        ParseUser.getCurrentUser().logOut();
                        Intent inte = new Intent(this,MainActivity.class);
                        startActivity(inte);

                }

                return super.onOptionsItemSelected(item);
        }
}
