package com.newscorp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.grid_spans)
    public AppCompatSpinner gridSpans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);
        gridSpans.setAdapter(new ArrayAdapter(MainActivity.this, R.layout.spinner_item, new String[]{"1 Span", "2 Spans", "3 Spans", "4 Spans"}));
        //Default to a span count of 3
        gridSpans.setSelection(2);
        showImages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void showImages() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ImagesFragment.newInstance(), ImagesFragment.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
