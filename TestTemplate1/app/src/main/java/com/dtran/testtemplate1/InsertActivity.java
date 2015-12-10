package com.dtran.testtemplate1;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Spinner bodyPartSpinner;
    private Spinner liftSpinner;
    private LiftDataSource datasource;
    private ArrayAdapter<String> liftAdapter = null;
    private Bundle extras;
    private long selectedDate;
    private long selectedLiftID;
    private boolean editMode = false;
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bodyPartSpinner = (Spinner)findViewById(R.id.spinnerBodyPart);
        String[] bodyPartArray = getResources().getStringArray(R.array.body_part_arrays);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bodyPartArray);
        bodyPartSpinner.setAdapter(adapter);

        liftSpinner = (Spinner)findViewById(R.id.spinnerLift);
        String[] liftArray = getResources().getStringArray(R.array.chest_arrays);
        liftAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liftArray);
        liftSpinner.setAdapter(liftAdapter);

        bodyPartSpinner.setOnItemSelectedListener(new MyCustomOnItemSelectedListener());

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        extras = getIntent().getExtras();
        if(extras != null)
        {
            selectedDate = extras.getLong("selectedDate");
            selectedLiftID = extras.getLong("selectedLiftID");
            if(selectedLiftID > 0)
            {
                Button myAddButton = (Button)findViewById(R.id.buttonInsertDelete);
                myAddButton.setVisibility(View.VISIBLE);

                Button myAddButton1 = (Button)findViewById(R.id.buttonInsertAdd);
                myAddButton1.setText("Save");
                editMode = true;

                datasource = new LiftDataSource(InsertActivity.this);
                datasource.open();

                Lift currentLift = datasource.getLiftById(selectedLiftID);
                bodyPartSpinner.setSelection(adapter.getPosition(currentLift.getBodyPart()));
                liftSpinner.setSelection(liftAdapter.getPosition(currentLift.getLift()));

                ((EditText) findViewById(R.id.editTextWeight)).setText(String.valueOf(currentLift.getWeight()));
                Spinner mySpinner = (Spinner) findViewById(R.id.spinnerReps);

                //String t1 = datasource.getDBBPath();
               // /data/user/0/com.dtran.testtemplate1/databases/workoutTracker.db
                mySpinner.setSelection(((ArrayAdapter) mySpinner.getAdapter()).getPosition(String.valueOf(currentLift.getReps())));
            }
        }
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Button myAddButton = (Button)findViewById(R.id.buttonInsertAdd);
        myAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bodyPart = bodyPartSpinner.getSelectedItem().toString();
                String liftText = liftSpinner.getSelectedItem().toString();
                EditText weightEditText = (EditText) findViewById(R.id.editTextWeight);
                String reps = ((Spinner)findViewById(R.id.spinnerReps)).getSelectedItem().toString();

                String weightText = weightEditText.getText().toString().trim();
                if (TextUtils.isEmpty(weightText)) {
                    Toast.makeText(InsertActivity.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    datasource = new LiftDataSource(InsertActivity.this);
                    datasource.open();

                    Date todayDate = new Date();
                    Long date = todayDate.getTime();

                    Lift lift = datasource.createLift(liftText,bodyPart,Double.parseDouble(weightText),Integer.parseInt(reps), date,selectedDate,1);

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = DateFormat.format("yyyy-MM-dd", date).toString();
                        String liftDateString = DateFormat.format("yyyy-MM-dd", selectedDate).toString();

                        String t1 = liftText.replace(" ", "_");

                        URL url = new URL("http", "40.76.13.137", 8080, "insert/lifthistory/"+android_id+"/"+lift.getId()+"?sLift="+t1+"&&BodyPart="+bodyPart+"&&Weight="+weightText+"&&Reps="+reps+"&&InsertDate="+dateString+"&&LiftDate="+liftDateString+"&&Active=true");

                        SetLift mySetLift = new SetLift();
                        mySetLift.execute(url);

                    }catch(Exception e){}

                    datasource.close();
                    Intent intent = new Intent(InsertActivity.this, HistoryActivity.class);
                    intent.putExtra("selectedDate",selectedDate);
                    startActivity(intent);
                }
            }
        });
    }
    public class MyCustomOnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selectedItem = parent.getItemAtPosition(pos).toString();

            switch (selectedItem) {
                case "Chest":
                    String[] chestArray = getResources().getStringArray(R.array.chest_arrays);
                    liftAdapter = new ArrayAdapter<String>(InsertActivity.this, android.R.layout.simple_list_item_1, chestArray);
                    liftSpinner.setAdapter(liftAdapter);
                    break;
                case "Back":
                    String[] backArray = getResources().getStringArray(R.array.back_arrays);
                    liftAdapter = new ArrayAdapter<String>(InsertActivity.this, android.R.layout.simple_list_item_1, backArray);
                    liftSpinner.setAdapter(liftAdapter);
                    break;
                case "Legs":
                    String[] legArray = getResources().getStringArray(R.array.legs_arrays);
                    liftAdapter = new ArrayAdapter<String>(InsertActivity.this, android.R.layout.simple_list_item_1, legArray);
                    liftSpinner.setAdapter(liftAdapter);
                    break;
                case "Abs":
                    String[] abArray = getResources().getStringArray(R.array.abs_arrays);
                    liftAdapter = new ArrayAdapter<String>(InsertActivity.this, android.R.layout.simple_list_item_1, abArray);
                    liftSpinner.setAdapter(liftAdapter);
                    break;
                case "Arms":
                    String[] armArray = getResources().getStringArray(R.array.arms_arrays);
                    liftAdapter = new ArrayAdapter<String>(InsertActivity.this, android.R.layout.simple_list_item_1, armArray);
                    liftSpinner.setAdapter(liftAdapter);
                    break;
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
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

        if (id == R.id.nav_calendar) {
            Intent intent = new Intent(InsertActivity.this, CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(InsertActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void insertCancel(View v)
    {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("selectedDate",selectedDate);
        startActivity(intent);
    }
    public void deleteLift(View v){
        datasource = new LiftDataSource(this);
        datasource.open();
        datasource.deleteLiftById(selectedLiftID);
        datasource.close();
    }
}