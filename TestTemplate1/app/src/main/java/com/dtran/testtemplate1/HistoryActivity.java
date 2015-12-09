package com.dtran.testtemplate1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LiftDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    try {
                        long selectedDate = extras.getLong("selectedDate");
                        Intent intent = new Intent(HistoryActivity.this, InsertActivity.class);
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try
        {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                long selectedDate = extras.getLong("selectedDate");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                //Date date = sdf.parse(selectedDate);
                //long longCurrentDate = date.getTime();

                datasource = new LiftDataSource(HistoryActivity.this);
                datasource.open();
                //List<Lift> values = datasource.getAllLifts();

                List<Lift> mylist = datasource.getLifts(selectedDate);
                if(mylist != null)
                {
                    List<HistoryRowItem> historyRowList = new ArrayList<HistoryRowItem>();

                    for(Lift myLift : mylist) {
                        String title = myLift.getBodyPart() + ": " + myLift.getLift();
                        String desc = myLift.getWeight() + " lbs X " + myLift.getReps() + " reps";
                        HistoryRowItem newitem = new HistoryRowItem(myLift.getId(),title, desc, selectedDate);
                        historyRowList.add(newitem);
                    }

                    ListView listView = (ListView) findViewById(R.id.listview);
                    CustomListViewAdapter customAdapter = new CustomListViewAdapter(this, R.layout.listview_history_item_row, historyRowList);
                    listView.setAdapter(customAdapter);
                }
            }
        }
        catch(Exception e){}
        finally {
            datasource.close();
        }
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }
*/
   /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Context context = this;

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(context, CalendarActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}