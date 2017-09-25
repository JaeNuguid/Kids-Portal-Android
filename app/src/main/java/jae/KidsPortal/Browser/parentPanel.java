package jae.KidsPortal.Browser;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import jae.KidsPortal.Browser.fragments.Fragment_Browser;
import jae.KidsPortal.Browser.fragments.Fragment_Reports;

public class parentPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_panel);

        Intent intent = new Intent(this, Fragment_Reports.class);
        startActivity(intent);
  }

}
