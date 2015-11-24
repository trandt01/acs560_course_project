package com.dtran.testtemplate1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class InsertActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner bodyPartSpinner;
    private CommentsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
        bodyPartSpinner = (Spinner)findViewById(R.id.spinnerBodyPart);
        int selectionCurrent = bodyPartSpinner.getSelectedItemPosition();

        bodyPartSpinner.setOnItemSelectedListener(new MyCustomOnItemSelectedListener());
        */
/*
        bodyPartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String  mselection = bodyPartSpinner.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), "selected "+ mselection, toast).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        */
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
        getMenuInflater().inflate(R.menu.insert, menu);
        return true;
    }
*/
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Context context = this;

        if (id == R.id.nav_calendar) {
            Intent intent = new Intent(context, CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            //Intent intent = new Intent(context, ResultActivity.class);
            Intent intent = new Intent(context, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_test) {
            Intent intent = new Intent(context, TestDatabaseActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void insertLift(View v)
    {
        Toast.makeText(this, "Insert?", Toast.LENGTH_SHORT).show();
       // @SuppressWarnings("unchecked")
       // ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;

        //String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
        //int nextInt = new Random().nextInt(3);
        // save the new comment to the database
        comment = datasource.createComment("test1");
       // adapter.add(comment);
       // adapter.notifyDataSetChanged();
    }
    public void insertCancel(View v)
    {
        Toast.makeText(this, "Cancel?", Toast.LENGTH_SHORT).show();
       // final int id = v.getId();

        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }
}
