package com.example.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.CookieManager;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class WebView extends AppCompatActivity {
    private android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_web_view);

        webView = (android.webkit.WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

                IntentHandler handler = new IntentHandler(WebView.this);
                if (url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url,IntentHandler.type);

                        if (intent!=null){
                            handler.startIntent(view,intent);
                            return true;
                        }

                    } catch (URISyntaxException e) {
                        Toast.makeText(WebView.this, "Unable to Open", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

        });

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                try {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                    request.setMimeType(mimetype);
                    String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);

                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Download File...");
                    String fileName = contentDisposition.replace("inline; filename=", "");
                    fileName = fileName.replaceAll(".+UTF-8''", "");
                    fileName = fileName.replaceAll("\"", "");
                    fileName = URLDecoder.decode(fileName, "UTF-8");
                    request.setTitle(fileName);

//                request.setTitle(URLUtil.guessFileName(url, contentDisposition,mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    assert dm != null;
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Download files", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    ContextCompat.checkSelfPermission(WebView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Name of your downloadble file goes here, example: Mathematics II ");
//                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                dm.enqueue(request);
//                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
//                        Toast.LENGTH_LONG).show();


            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://pkmsawit.netlify.app/");
    }
}