package com.dtran.testtemplate1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.net.URL;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AccountDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //70bc50a703789579
       String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        try{
            Account account = null;
            datasource = new AccountDataSource(this);
            datasource.open();

            account = datasource.getLatestAccount();
            if(account != null)
            {
                ((EditText) findViewById(R.id.editTextHeight)).setText(Double.toString(account.getHeight()));
                ((EditText) findViewById(R.id.editTextWeight)).setText(Double.toString(account.getWeight()));
                ((EditText) findViewById(R.id.editTextBodyFat)).setText(Double.toString(account.getBodyFat()));
            }
        }catch (Exception e) {}
        finally{
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Context context = this;

        if (id == R.id.nav_calendar) {
            Intent intent = new Intent(context, CalendarActivity.class);
            startActivity(intent);
        }
        /*
        else {
            if (id == R.id.nav_sync) {
                //http://127.0.0.1:8080/set/lifthistory/10204343?Lift=aaa&&BodyPart=head&&Weight=100.44&&Reps=10&&Date=2015-12-30&&Active=true
                //10204343 = device id
                //Date = Date format
                //http://127.0.0.1:8080/set/account/102033?Weight=17.3&&Height=20.69&&BodyFat=100.44

                //String myUrl = "http://127.0.0.1:8080/set/lifthistory/10204343?Lift=aaa&&BodyPart=head&&Weight=100.44&&Reps=10&&Date=2015-12-30&&Active=true";
                //String myUrl = "http://www.microsoft.com/";


                try {

                    //URL url1 = new URL("http://username:password@host:8080/directory/file?query#ref");
                    //URL url = new URL("http", "127.0.0.1", 8080, "set/account/102033");

                    //URI test = new URI(myUrl);
                //
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(out);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readStream(in);
                //
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // urlConnection.disconnect();
                }
//
                File data = Environment.getDataDirectory();
                FileChannel source=null;
                FileChannel destination=null;
                String currentDBPath = "DATA/data/"+ "com.dtran.testtemplate1" +"/databases/workouttracker.db";
                String backupDBPath = "workouttracker.db";
                File currentDB = new File(data, currentDBPath);
                //File backupDB = new File(sd, backupDBPath);
//
                //sFile f = new File("/data/data/com.dtran.testtemplate1/databases/workoutTracker.db");
               // FileInputStream fis=null;
                //FileOutputStream fos=null;

                HttpURLConnection urlConnection = null;
                try {
                    Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show();
                    String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    //test 121.42.201.6
                    //prod
//40.76.13.137/set/sqlitefile/23423432)form:multipart/form-data name:sqlitefile
                    //String myUrl = "http://127.0.0.1:8080/set/lifthistory/10204343?Lift=aaa&&BodyPart=head&&Weight=100.44&&Reps=10&&Date=2015-12-30&&Active=true";
                    //URL url = new URL("http", "121.42.201.6", 8080, "set/account/"+ android_id + ", Lift=Test&&BodyPart=head&&Weight=100.44&&Reps=10&&Date=2015-12-6&&Active=true");
                    //URL url = new URL("http","40.76.13.137",8080, "set/account/"+ android_id + "?Weight=17.3&&Height=20.69&&BodyFat=100.44");
                    //URL url = new URL("http", "40.76.13.137", 8080, "set/account/1244?Weight=171.44&&Height=5.44&&BodyFat=12.44");
                    //URL url = new URL("http//40.76.13.137:8080/set/account/1212?Weight=17.3&&Height=20.69&&BodyFat=100.44");
                   // URL url = new URL("http://stackoverflow.com/questions/13775103/how-do-i-know-if-i-have-successfully-connected-to-the-url-i-opened-a-connection");
                   // HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                   // int  test = conn.getResponseCode();


                    URL url = new URL("http", "40.76.13.137", 8080, "set/sqlitefile/"+android_id);
                    //http://40.76.13.137:8080/set/sqlitefile/DeviceID
                    Sync mySync = new Sync();
                    mySync.execute(url);





//SQLiteDatabase: /data/user/0/com.dtran.testtemplate1/databases/workoutTracker.db

                    //System.out.println(conn.getResponseCode());

                    //URL url = new URL("http","40.76.13.137",8080,"set/lifthistory/1212?Lift=Test&&BodyPart=legs&&Weight=111.44&&Reps=11&&Date=2015-12-06&&Active=true");
                    //http://40.76.13.137:8080/set/account/111?Weight=17.3&&Height=20.69&&BodyFat=100.44
                    //http://40.76.13.137:8080/set/lifthistory/1111?Lift=Test&&BodyPart=legs&&Weight=111.44&&Reps=11&&Date=2015-12-6&&Active=true"

                    //URL url = new URL("http", "121.42.201.6", 8080, "set/sqlitefile/DeviceID(" + android_id + "), form:multipart/form-data, name:" + f);
                   // http://121.42.201.6:8080/set/sqlitefile/DeviceID(like 102323), form:multipart/form-data, name:sqlitefile
                    //fis = new FileInputStream(f);

                   // urlConnection = (HttpURLConnection) url.openConnection();
                    //String message = urlConnection.getResponseMessage();

                    String tt = "";

                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                }catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    //urlConnection.disconnect();
                }


//
                try {
                    source = new FileInputStream(currentDB).getChannel();
                    //destination = new FileOutputStream(backupDB).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    source.close();
                    destination.close();
                    Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
//
            }
        }
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void saveAccount(View v) {

        EditText heightEditText = (EditText) findViewById(R.id.editTextHeight);
        EditText weightEditText = (EditText) findViewById(R.id.editTextWeight);
        EditText bodyFatEditText = (EditText) findViewById(R.id.editTextBodyFat);

        String height = heightEditText.getText().toString().trim();
        String weight = weightEditText.getText().toString().trim();
        String bodyFat = bodyFatEditText.getText().toString().trim();

        if (TextUtils.isEmpty(height)) {
            Toast.makeText(this, "Please enter height", Toast.LENGTH_SHORT).show();
           // heightEditText.setError("Please input height");
        } else if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bodyFat)){
            Toast.makeText(this, "Please enter body fat", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Profile saving", Toast.LENGTH_SHORT).show();

            Account account = null;
            datasource = new AccountDataSource(this);
            datasource.open();

            Date todayDate = new Date();
            account = datasource.createAccount(Double.parseDouble(weight),Double.parseDouble(height),Double.parseDouble(bodyFat),todayDate.toString(),todayDate.toString() );
            if(account != null) {Toast.makeText(this, "Account saved", Toast.LENGTH_SHORT).show();}

            try {
                String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                //test 121.42.201.6

                URL url = new URL("http", "40.76.13.137", 8080, "set/account/"+android_id+"?Weight="+weight+"&&Height="+height+"&&BodyFat="+bodyFat);
                SetAccount setAccount = new SetAccount();
                setAccount.execute(url);
            }catch(Exception e) {
                e.printStackTrace();
            } finally {
                datasource.close();
            }
        }
    }
}
