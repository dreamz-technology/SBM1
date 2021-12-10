package com.dreamz.samtaBhratruMandal.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;


public class ForgotPassword extends AppCompatActivity {

    private ShowLoader showLoader;
    SwipeRefreshLayout swipe;
    WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        swipe= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        webView = (WebView) findViewById(R.id.website_view);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webView.setWebViewClient(new Client());
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl("https://www.samatabhratrumandal.com/Account/ForgotPassword?Length=0");
    }

    public class Client extends WebViewClient {
        ProgressDialog progressDialog;
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // If url contains mailto link then open Mail Intent
            if (url.contains("mailto:")) {
                // Could be cleverer and use a regex
                //Open links in new browser
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                // Here we can open new activity
                return true;
            }else {
                // Stay within this webview and load url
                view.loadUrl(url);
                return true;
            }
        }
        //Show loader on url load
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Then show progress  Dialog
            // in standard case YourActivity.this
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ForgotPassword.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }
        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            try {
                swipe.setRefreshing(false);
                // Close progressDialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }


   /* private void clickEvents() {
        btnForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    API_Call();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*

    private void API_Call() throws UnsupportedEncodingException, JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", UserForEmail.getText().toString());
        jsonParams.put("password", UserPassword.getText().toString());
        entity = new StringEntity(jsonParams.toString());

      //  params.put("Email",UserForEmail.getText().toString());

        showLoader.show();

        client.post(this,ForgotPass,entity, "application/json", new JsonHttpResponseHandler()
        {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        showLoader.hashCode();
                        try {

                            if (response.getString("result").equals("Success")) {
                                Toast.makeText(getApplicationContext(), response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginPage.class));


                            } else
                            {
                                Toast.makeText(getApplicationContext(), response.getString("result").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TAG","Error Response:-"+responseString);
                        showLoader.hashCode();
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please connect to Internet and try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void init()
    {
        showLoader = ShowLoader.getProgressDialog(ForgotPassword.this);
        UserForEmail = findViewById(R.id.forgot_email_txt);
        UserPassword = findViewById(R.id.txt_password);
        btnForPass = findViewById(R.id.btn_forgotPassword);


    }*/
}