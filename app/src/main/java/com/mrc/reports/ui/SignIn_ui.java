package com.mrc.reports.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.utils.AccountUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mrc.reports.utils.AccountUtils.hasActiveUserAccount;

public class SignIn_ui extends BaseActivity {

    Button mLoginBtn;
    EditText mPhone;
    EditText mPassword;
    ProgressBar mProgressBar;
    ImageView mBg;

    String regexStr = "[0-9]{10}";
    String Phone, Password;

    private static final String TAG = SignIn_ui.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoginBtn = findViewById(R.id.login_btn);
        mPhone = findViewById(R.id.phone_txt);
        mPassword = findViewById(R.id.pwd_txt);
        mProgressBar = findViewById(R.id.progress);
        mBg = findViewById(R.id.bg_img);

        Glide.with(this)
                .load(R.drawable.login_img)
                .into(mBg);

        if(hasActiveUserAccount(this)){
            startActivity(new Intent(this,Home_ui.class));
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Phone = mPhone.getText().toString();
                Password = mPassword.getText().toString();

                if((!TextUtils.isEmpty(Phone)) && (Phone.matches(regexStr)) && (!TextUtils.isEmpty(Password))){

                    //Authenticate user against the database
                    sendData(Phone,Password);

                }else {
                    Snackbar.make(view ,"Please Enter Correct Phone Number or Password",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }


    public void sendData(String Phone, String Pwd){
        mProgressBar.setVisibility(View.VISIBLE);

        retrofit2.Call<ResponseBody> SignCall = api.singInUser(Phone,Pwd);
        SignCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    try {
                        parseLoginData(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Snackbar.make(mPhone, "Could not reach server at this time please try again later", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(mPhone,"Something went wrong during login. Please try again",Snackbar.LENGTH_SHORT).show();
            }
        });


    }



    public void parseLoginData(String response) throws JSONException {

        //TODO: Finish this when the API's are ready
        JSONObject loginObject = new JSONObject(response);
        Log.d(TAG,"return String " + loginObject.toString());

        String mMessage = "";
        String mRole = "";
        String mAccount = "";
        String mToken = "";
        String mFullName = "";

        //Check if Registration was done successfully
        if(loginObject.getInt("code") == 400){
            JSONObject responseObject = loginObject.getJSONObject("content");
            if (!responseObject.isNull("error"))
                mMessage = responseObject.getString("error");
            Toast.makeText(this,mMessage,Toast.LENGTH_LONG).show();

        }else if(loginObject.getInt("code") == 202){
            JSONObject responseObject = loginObject.getJSONObject("content");
            if (!responseObject.isNull("error"))
                mMessage = responseObject.getString("error");
            Toast.makeText(this,mMessage,Toast.LENGTH_LONG).show();

        }else if(loginObject.getInt("code") == 200){
            JSONObject responseObject = loginObject.getJSONObject("content");
            if (!responseObject.isNull("staff_role"))
                mRole = responseObject.getString("staff_role");
            if (!responseObject.isNull("staff_name"))
                mFullName = responseObject.getString("staff_name");
            if (!responseObject.isNull("token"))
                mToken = responseObject.getString("token");
            if (!responseObject.isNull("username"))
                mAccount = responseObject.getString("username");

            AccountUtils.setupUser(this,mRole,mToken,mFullName,mAccount);

            mPhone.setText("");
            mPassword.setText("");
            startActivity(new Intent(this, Home_ui.class));
        }



    }

}
