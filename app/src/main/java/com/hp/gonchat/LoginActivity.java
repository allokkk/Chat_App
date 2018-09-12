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

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button btn;
    TextView newuser;
    String user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btn=findViewById(R.id.btn);
        newuser=findViewById(R.id.newuser);
        Firebase.setAndroidContext(this);

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        btn.setText("LOGIN");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convert input to string
                user=username.getText().toString().trim();
                pass=password.getText().toString().trim();

                String url="https://gonchat-b3d8d.firebaseio.com/user.json";


                // progress dialog for user Authentication
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Loading...");
                pd.show();
                // Get user data from server
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals("null")) {
                            Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);
                                if (!obj.has(user)) {
                                    Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                }
                                /*checking user and pass from server
                                 * put user and pass into userDetails class
                                 * */
                                else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                    UserDetails.username = user;
                                    UserDetails.password = pass;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // dismiss progress dialog
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(request);
            }

    });
}
}

