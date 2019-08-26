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

import android.app.ProgressDialog;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.content.Context;

public class activityComments extends AppCompatActivity {


    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> commentsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setAlpha(0.45f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createEditTextView();
                Toast.makeText(activityComments.this, "Button to add new Title and Content", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem2) {
                //return false;
                switch (menuItem2.getItemId())
                {
                    case R.id.action_home:
                        Intent intent = new Intent(activityComments.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_users:
                        Intent intent2 = new Intent(activityComments.this, activityUsers.class);
                        startActivity(intent2);
                        break;
                    case R.id.action_comment:
                        break;
                }
                return true;
            }
        });

        /*comments = (ListView)findViewById(R.id.list);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Downloading data....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Error connecting to server!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(activityComments.this);
        rQueue.add(request);*/

        commentsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new retrieveData().execute();
    }

    protected void createEditTextView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        lv.addView(rowView, lv.getChildCount()-1);
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

                        String userId = e.getString("userId");
                        String id = e.getString("id");
                        String title = e.getString("title");
                        String body = e.getString("body");

                        // store into hashmap
                        HashMap<String, String> comment = new HashMap<>();
                        comment.put("userId", userId);
                        comment.put("id", id);
                        comment.put("title", title);
                        comment.put("body", body);

                        //store in listview
                        commentsList.add(comment);
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
            ListAdapter adapter = new SimpleAdapter(activityComments.this, commentsList,
                    R.layout.item_list, new String[]{ "title","body"},
                    new int[]{R.id.title, R.id.body});
            lv.setAdapter(adapter);
        }
    }



    /*public void nav_activity_main()
    {
        Intent intent1 = new Intent(activityComments.this, MainActivity.class);
        startActivity(intent1);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(activityComments.this, "Button to tweak settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.Log_out) {
            Toast.makeText(activityComments.this, "Button to log out", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
