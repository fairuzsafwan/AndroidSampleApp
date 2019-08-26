package com.example.sampletest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class activityUsers extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> usersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem2) {
                //return false;
                switch (menuItem2.getItemId())
                {
                    case R.id.action_home:
                        Intent intent = new Intent(activityUsers.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_users:
                        break;
                    case R.id.action_comment:
                        Intent intent2 = new Intent(activityUsers.this, activityComments.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });

        usersList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_users);

        new activityUsers.retrieveData().execute();
    }

    private class retrieveData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Process request
            String url = "https://jsonplaceholder.typicode.com/posts";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Status: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonA = new JSONArray(jsonStr);

                    ArrayList<String> uniqueUserID = new ArrayList<String>();

                    for(int i=0;i<jsonA.length();i++){
                        JSONObject e = jsonA.getJSONObject(i);

                        String userId = "User: " + e.getString("userId");
                        String id = e.getString("id");
                        String title = e.getString("title");
                        String body = e.getString("body");

                        if (uniqueUserID.contains(userId) == false)
                        {
                            // store into hashmap
                            HashMap<String, String> user = new HashMap<>();
                            user.put("userId", userId);
                            user.put("id", id);
                            user.put("title", title);
                            user.put("body", body);

                            uniqueUserID.add(userId);

                            //store in listview
                            usersList.add(user);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Error fetching data from server!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Error fetching data from server!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(activityUsers.this, usersList,
                    R.layout.users_item_list, new String[]{"userId"},
                    new int[]{R.id.userID});
            lv.setAdapter(adapter);
        }
    }
}
