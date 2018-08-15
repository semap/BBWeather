package ca.aequilibrium.bbweather.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ca.aequilibrium.bbweather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    public static final String TAG = HelpFragment.class.getSimpleName();
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_help, container, false);

        webView = view.findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/help.html");
        return view;
    }

}
