package com.inc.miki.filozofski_falutet_pale;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyAppWebViewClient extends WebViewClient {

    private MainActivity m = new MainActivity();


    @Override
    public  boolean shouldOverrideUrlLoading (WebView view, String url){
       if(Uri.parse(url).getHost().endsWith(m.HOST_STRING)){return false;}

        Intent intent = new Intent (Intent.ACTION_VIEW,Uri.parse(url));
        view.getContext().startActivity(intent);
        return  true;


    }

}
