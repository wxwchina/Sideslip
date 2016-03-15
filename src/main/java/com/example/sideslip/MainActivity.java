package com.example.sideslip;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.change) Button change;
    @Bind(R.id.sideslipview)
    MySideslipView sideslipview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.change)
    public void onClick() {
        sideslipview.change();
    }
}
