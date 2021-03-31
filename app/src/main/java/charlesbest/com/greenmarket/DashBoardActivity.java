package charlesbest.com.greenmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Fruits.OnFragmentInteractionListener{

    FragmentManager fm = getSupportFragmentManager();


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

         layout = findViewById(R.id.tabLayout);

        mSectionsPagerAdapter.addFragment(new Fruits(),"Fruits" );
        mSectionsPagerAdapter.addFragment(new Vegetables(),"Vegetables" );
        mSectionsPagerAdapter.addFragment(new Root(),"Root & Tubers" );
        mSectionsPagerAdapter.addFragment(new Grains(),"Grains" );

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        layout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            FirebaseAuth auth  = FirebaseAuth.getInstance();
            auth.signOut();
            Intent i = new Intent(DashBoardActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_profile) {
            Intent i = new Intent(DashBoardActivity.this,ProfileActivity.class);
            startActivity(i);
        }  else if (id == R.id.navigation_home) {
            Intent i = new Intent(DashBoardActivity.this,DashBoardActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.navigation_post) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String  seller = prefs.getString("acc",null);
            if(seller.equals("buyer")){
                Intent i = new Intent(DashBoardActivity.this,PostDemandActivity.class);
                startActivity(i);
            }else{
            Intent i = new Intent(DashBoardActivity.this,PostActivity.class);
            startActivity(i);
            }

        } else if (id == R.id.navigation_demand) {
            Intent i = new Intent(DashBoardActivity.this,ViewDemandActivity.class);
            startActivity(i);

        }
        else if (id == R.id.navigation_message) {
            Intent i = new Intent(DashBoardActivity.this,MessageActivity.class);
            startActivity(i);

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            Intent i = new Intent(DashBoardActivity.this,AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> tabTitle = new ArrayList<>();



        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            tabTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);
        }
    }
}
