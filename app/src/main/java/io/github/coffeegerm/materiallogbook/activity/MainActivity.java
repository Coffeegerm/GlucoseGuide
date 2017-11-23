package io.github.coffeegerm.materiallogbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instabug.library.Instabug;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.list.ListFragment;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.rss.NewsFragment;
import io.github.coffeegerm.materiallogbook.statistics.StatisticsFragment;
import io.github.coffeegerm.materiallogbook.utils.CustomTypeFaceSpan;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;

/**
 * Activity for controlling which fragment should be presented and containing
 * the main activity for holding Fragments
 **/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences sharedPreferences;
    public static boolean isResumed = false;
    public int lastSelectedTab;
    Fragment listFragment = new ListFragment();
    Fragment newsFragment = new NewsFragment();
    Fragment statsFragment = new StatisticsFragment();
    FragmentManager fragmentManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.invoke_instabug)
    TextView instabug;
    private Realm realm;
    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isCreated = true;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        realm = Realm.getDefaultInstance();
        setDrawerLayout();
        fragmentManager = getSupportFragmentManager();
        if (isCreated && !isResumed)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, listFragment).commit();

        int textColor;
        if (sharedPreferences.getBoolean(PREF_DARK_MODE, false)) {
            // DARK MODE
            navigationView.getHeaderView(0).setBackground(getResources()
                    .getDrawable(R.drawable.header_dark));
            navigationView.setBackgroundColor(getResources().getColor(R.color.darkThemeBackground));
            textColor = R.color.textColorPrimaryInverse;
        } else {
            // LIGHT MODE
            navigationView.getHeaderView(0).setBackground(getResources()
                    .getDrawable(R.drawable.header_light));
            textColor = R.color.textColorPrimary;
        }

        ColorStateList csl = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{-android.R.attr.state_checked}},
                new int[]{getResources().getColor(R.color.colorPrimary), getResources().getColor(textColor)});
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);

        instabug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                instabug.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Instabug.invoke();
                    }
                }, 300);
            }
        });
        setGlucoseGrade();
    }

    @Override
    protected void onResume() {
        isResumed = true;
        if (sharedPreferences.getBoolean(PREF_DARK_MODE, false))
            setTheme(R.style.AppTheme_Dark);
        else setTheme(R.style.AppTheme);

        if (isCreated) isCreated = false;
        else {
            recreate();
            navigationView.setCheckedItem(lastSelectedTab);
            switch (lastSelectedTab) {
                case R.id.nav_list:
                    setFragment(listFragment);
                    break;
                case R.id.nav_stats:
                    setFragment(statsFragment);
                    break;
                case R.id.nav_news:
                    setFragment(newsFragment);
                    break;
            }
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            new AlertDialog.Builder(this).setTitle(R.string.close_app)
                    .setMessage(R.string.close_confirmation)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton(R.string.no, null).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_list:
                // Swaps fragment to list fragment
                setFragment(listFragment);
                lastSelectedTab = R.id.nav_list;
                break;

            case R.id.nav_stats:
                // Swaps fragment to statistics fragment
                setFragment(statsFragment);
                lastSelectedTab = R.id.nav_stats;
                break;

            case R.id.nav_news:
                //Swaps fragment to news fragment
                setFragment(newsFragment);
                lastSelectedTab = R.id.nav_news;
                break;

            case R.id.nav_settings:
                // Opens up settings activity
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                navigationView.setCheckedItem(lastSelectedTab);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // Navigation Drawer should be 80% of screen width when open
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        int fullWidth = displayMetrics.widthPixels;
        int navViewWidth = Math.round(0.8f * fullWidth);
        ViewGroup.LayoutParams sidebarParameters = navigationView.getLayoutParams();
        sidebarParameters.width = navViewWidth;

        // Changes fonts within drawer layout
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    setMenuTypeface(subMenuItem);
                }
            }
            setMenuTypeface(mi);
        }
    }

    // Method used to set the typeface of items within menu
    private void setMenuTypeface(MenuItem menuItem) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNext-Regular.otf");
        SpannableString newTitle = new SpannableString(menuItem.getTitle());
        newTitle.setSpan(new CustomTypeFaceSpan("", font), 0, newTitle.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(newTitle);
    }

    private void setFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // Set grade in Navigation Menu
    private void setGlucoseGrade() {
        View header = navigationView.getHeaderView(0);
        TextView glucoseGrade = header.findViewById(R.id.navigation_view_grade);
        glucoseGrade.setText(getGlucoseGrade());
    }

    // Calculates the glucose grade based on user
    // sugar from last three days
    private String getGlucoseGrade() {
        String grade;
        int hyperglycemicCount = 0;
        int hyperglycemicIndex = sharedPreferences.getInt("hyperglycemicIndex", 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        Date threeDaysAgo = calendar.getTime();
        RealmResults<EntryItem> entriesFromLastThreeDays = realm.where(EntryItem.class).greaterThan("date", threeDaysAgo).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastThreeDays.size(); position++) {
            /*
            * Less than 3 hyperglycemic sugars and A+
            * 4 = A
            * 5 = A-
            * 6 = B+
            * 7 = B
            * etc...
            * */
            EntryItem currentItem = entriesFromLastThreeDays.get(position);
            assert currentItem != null;
            if (currentItem.getBloodGlucose() > hyperglycemicIndex) {
                hyperglycemicCount++;
            }
        }
        if (hyperglycemicCount == 0) {
            grade = "-";
        } else if (hyperglycemicCount <= 3) {
            grade = "A+";
        } else if (hyperglycemicCount == 4) {
            grade = "A";
        } else if (hyperglycemicCount == 5) {
            grade = "A-";
        } else if (hyperglycemicCount == 6) {
            grade = "B+";
        } else if (hyperglycemicCount == 7) {
            grade = "B";
        } else if (hyperglycemicCount == 8) {
            grade = "B-";
        } else if (hyperglycemicCount == 9) {
            grade = "C+";
        } else if (hyperglycemicCount == 10) {
            grade = "C";
        } else if (hyperglycemicCount == 11) {
            grade = "C-";
        } else if (hyperglycemicCount == 12) {
            grade = "D+";
        } else if (hyperglycemicCount == 13) {
            grade = "D";
        } else if (hyperglycemicCount == 14) {
            grade = "D-";
        } else {
            grade = "F";
        }
        return grade;
    }
}
