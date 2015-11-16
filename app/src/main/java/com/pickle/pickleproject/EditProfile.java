package com.pickle.pickleproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    private Context context;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profiles);
        final Account account = (Account) getIntent().getSerializableExtra("object");

        final EditText username = (EditText) findViewById(R.id.editText8);
        final EditText password = (EditText) findViewById(R.id.editText9);
        final EditText email = (EditText) findViewById(R.id.editText10);
        EditText address = (EditText) findViewById(R.id.editText11);
        final EditText phone = (EditText) findViewById(R.id.editText12);

        MontserratButton saveButton = (MontserratButton) findViewById(R.id.saveButton);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/account/" + account.getId();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.setUsername(username.getText().toString());
                account.setEmail(password.getText().toString());
                account.setEmail(email.getText().toString());
                account.setPhone_number(phone.getText().toString());

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                String json = gson.toJson(account);
                Log.d("json", json);
                final CustomJSONObjectRequest jsonRequest;
                try {
                    jsonRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("result", response.getString("result"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error:", error.getMessage());
                        }
                    });
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(jsonRequest);
                    changeProfile();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void changeProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

}