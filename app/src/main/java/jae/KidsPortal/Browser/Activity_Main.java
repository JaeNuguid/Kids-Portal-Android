package jae.KidsPortal.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import jae.KidsPortal.Browser.fragments.Fragment_Reports;
import jae.KidsPortal.Browser.helper.class_CustomViewPager;
import jae.KidsPortal.Browser.helper.helper_browser;
import jae.KidsPortal.Browser.helper.helper_editText;
import jae.KidsPortal.Browser.helper.helper_main;
import jae.KidsPortal.Browser.fragments.Fragment_Bookmarks;
import jae.KidsPortal.Browser.fragments.Fragment_Browser;
import jae.KidsPortal.Browser.fragments.Fragment_Files;
import jae.KidsPortal.Browser.fragments.Fragment_History;
import jae.KidsPortal.Browser.fragments.Fragment_Pass;
import jae.KidsPortal.Browser.fragments.Fragment_ReadLater;
import jae.KidsPortal.Browser.helper.helper_toolbar;

public class Activity_Main extends AppCompatActivity{


    // Views

    private class_CustomViewPager viewPager;
    private TextView urlBar;
    private TextView listBar;
    private EditText editText;
    private Toolbar toolbar;
    // Others
    private SharedPreferences sharedPref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Messages");

    public String user;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   // sig.onCreate();
        super.onCreate(savedInstanceState);

        final Activity activity = Activity_Main.this;

        helper_main.setTheme(activity);

        setContentView(R.layout.activity_main);
        WebView.enableSlowWholeDocumentDraw();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().putBoolean("isOpened", true).apply();

        if( getIntent().getExtras() !=null) {
            Bundle b = getIntent().getExtras();

            if (b.containsKey("user")) {
                if (b.getString("user").length() > 2) {
                    Toast.makeText(this,"Logged in: "+b.getString("user"), Toast.LENGTH_LONG).show();
                    sharedPref.edit().putString("user", b.getString("user")).apply();
                    sharedPref.edit().putBoolean("loggedIn", true).apply();
                   helper_main.grantPermissionsStorage(activity);
                }
            }

        }
        helper_main.onStart(activity);

        //helper_main.grantPermissionsStorage(activity);
        helper_browser.resetTabs(activity);



        // find Views

        urlBar = (TextView) findViewById(R.id.urlBar);
        listBar = (TextView) findViewById(R.id.listBar);
        editText = (EditText) findViewById(R.id.editText);
        viewPager = (class_CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(10);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (sharedPref.getBoolean ("swipe", false)){
            sharedPref.edit().putString("swipe_string", activity.getString(R.string.app_yes)).apply();
            viewPager.setPagingEnabled(true);
        } else {
            sharedPref.edit().putString("swipe_string", activity.getString(R.string.app_no)).apply();
            viewPager.setPagingEnabled(false);
        }

        viewPager.addOnPageChangeListener(new class_CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                helper_editText.hideKeyboard(activity, editText, 0, "", getString(R.string.app_search_hint));
                helper_toolbar.toolbarGestures(activity, toolbar, viewPager);

                assert getSupportActionBar() != null;

                if (position < 5) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    urlBar.setVisibility(View.VISIBLE);
                    listBar.setVisibility(View.GONE);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    urlBar.setVisibility(View.GONE);
                    listBar.setVisibility(View.VISIBLE);
                }

                if (position < 5) {
                    Fragment_Browser fragment = (Fragment_Browser) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else if (position == 5) {
                    Fragment_Bookmarks fragment = (Fragment_Bookmarks) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else if (position == 6) {
                    Fragment_ReadLater fragment = (Fragment_ReadLater) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else if (position == 7) {
                    Fragment_History fragment = (Fragment_History) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else if (position == 8) {
                    Fragment_Pass fragment = (Fragment_Pass) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else if (position == 10) {
                    Fragment_Reports fragment = (Fragment_Reports) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                } else {
                    Fragment_Files fragment = (Fragment_Files) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        helper_toolbar.toolbarGestures(activity, toolbar, viewPager);
        onNewIntent(getIntent());



        myRef.child(sharedPref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Messages MSG = dataSnapshot.getValue(Messages.class);


                    if (!MSG.getMessage().equals("jae35")) {
                       msg(MSG.getMessage());
                       myRef.child(sharedPref.getString("user", "")).setValue(new Messages());
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

    }
            public void msg(String msg){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Your parent/guardian has sent you a message:\n\n"+msg);
        builder1.setTitle("Kids Portal - Message");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    @Override
    public void onResume(){
        super.onResume();

        final int position = viewPager.getCurrentItem();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (position < 5) {
                    Fragment_Browser fragment = (Fragment_Browser) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.fragmentAction();
                }
            }
        }, 100);
    }

    protected void onNewIntent(final Intent intent) {

        String action = intent.getAction();
        Handler handler = new Handler();

        if (Intent.ACTION_SEND.equals(action)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            String searchEngine = sharedPref.getString("searchEngine", "http://www.kiddle.co/s.php?q=");
            sharedPref.edit().putString("openURL", searchEngine + sharedText).apply();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(0);
                }
            }, 250);
        } else if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            String link = data.toString();
            sharedPref.edit().putString("openURL", link).apply();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(0);
                }
            }, 250);
        } else if ("readLater".equals(action)) {
            sharedPref.edit().putInt("appShortcut", 1).apply();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(6);
                }
            }, 250);
        } else if ("bookmarks".equals(action)) {
            sharedPref.edit().putInt("appShortcut", 1).apply();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(5);
                }
            }, 250);
        } else if ("history".equals(action)) {
            sharedPref.edit().putInt("appShortcut", 1).apply();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(7);
                }
            }, 250);
        } else if ("pass".equals(action)) {
            sharedPref.edit().putInt("appShortcut", 1).apply();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(8);
                }
            }, 250);
        }else if ("Reports".equals(action)) {
            sharedPref.edit().putInt("appShortcut", 1).apply();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(10);
                }
            }, 250);
        }
    }

    private void setupViewPager(final class_CustomViewPager viewPager) {

        final String startTab = sharedPref.getString("tabMain", "0");
        final int startTabInt = Integer.parseInt(startTab);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Fragment_Browser(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Browser(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Browser(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Browser(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Browser(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Bookmarks(), String.valueOf(getString(R.string.app_title_bookmarks)));
        adapter.addFragment(new Fragment_ReadLater(), String.valueOf(getString(R.string.app_title_readLater)));
        adapter.addFragment(new Fragment_History(), String.valueOf(getString(R.string.app_title_history)));
        adapter.addFragment(new Fragment_Pass(), String.valueOf(getString(R.string.app_title_passStorage)));
        adapter.addFragment(new Fragment_Files(), String.valueOf(getString(R.string.app_name)));
        adapter.addFragment(new Fragment_Reports(), String.valueOf(getString(R.string.app_title_Report)));

        viewPager.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(startTabInt,true);
            }
        }, 250);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);// add return null; to display only icons
        }
    }

    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem() < 5) {
            Fragment_Browser fragment = (Fragment_Browser) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        } else if(viewPager.getCurrentItem() == 5) {
            Fragment_Bookmarks fragment = (Fragment_Bookmarks) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        } else if(viewPager.getCurrentItem() == 6) {
            Fragment_ReadLater fragment = (Fragment_ReadLater) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        } else if(viewPager.getCurrentItem() == 7) {
            Fragment_History fragment = (Fragment_History) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        } else if(viewPager.getCurrentItem() == 8) {
            Fragment_Pass fragment = (Fragment_Pass) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        } else if(viewPager.getCurrentItem() == 9) {
            Fragment_Files fragment = (Fragment_Files) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        }
        else if(viewPager.getCurrentItem() == 10) {
            Fragment_Reports fragment = (Fragment_Reports) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            fragment.doBack();
        }
    }
}