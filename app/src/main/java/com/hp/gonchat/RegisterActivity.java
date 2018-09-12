package com.hp.gonchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText username,password;
    Button btn;
    TextView newuser;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btn=findViewById(R.id.btn);
        newuser=findViewById(R.id.newuser);
        Firebase.setAndroidContext(this);

        newuser.setVisibility(View.GONE);


        btn.setText("Regidter");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=username.getText().toString().trim();
                pass=password.getText().toString().trim();

                final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Loading...");
                pd.show();
//                    Volley initialization
                String url = "https://gonchat-b3d8d.firebaseio.com/users.json";
//                    Stringrequest method to send data to the api
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
//                        Response handling method in volley
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://gonchat-b3d8d.firebaseio.com/users");
                        /*
                         * checks if
                         * */
                        if (s.equals("null")) {
                            reference.child(user).child("password").setValue(pass);
                            Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);
//
//                                    if user is not present then create new user
                                if (!obj.has(user)) {
                                    reference.child(user).child("password").setValue(pass);
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
//                                        check if user is already present then make toast
                                } else {
                                    Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pd.dismiss();
                    }
//                        Error handling method in volley
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("some error occured" + error);
                        pd.dismiss();
                    }
                });
//                    creating request queue for execution
                RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                rQueue.add(stringRequest);
            }

    });
}
}

