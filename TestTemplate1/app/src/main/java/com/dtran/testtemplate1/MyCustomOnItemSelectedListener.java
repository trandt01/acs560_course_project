package com.dtran.testtemplate1;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyCustomOnItemSelectedListener extends ListActivity implements OnItemSelectedListener {
    private Spinner liftSpinner;
    String[] liftArray;
    String[] bodyPartArray;
    ArrayAdapter<String> arrayAdapter;


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(), "hi1" , Toast.LENGTH_SHORT).show();
        //String selectBodyPart = parent.getItemAtPosition(pos).toString();
        //liftSpinner = (Spinner)findViewById(R.id.spinnerLift);
        //liftArray =  getResources().getStringArray( R.array.chest_arrays);
        //bodyPartArray = getResources().getStringArray( R.array.body_part_arrays);

        //Toast.makeText(parent.getContext(), "hi2" , Toast.LENGTH_SHORT).show();
       // liftSpinner = (Spinner)parent.findViewById(R.id.spinnerLift);
       // String tt = liftSpinner.getSelectedItem().toString();
       // Toast.makeText(parent.getContext(),"pos" + bodyPartArray[pos].toString() , Toast.LENGTH_SHORT).show();
        Toast.makeText(parent.getContext(), "id" + id , Toast.LENGTH_SHORT).show();

        arrayAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.chest_arrays));
        //Toast.makeText(parent.getContext(), "hi" , Toast.LENGTH_SHORT).show();
       /*
        switch (selectBodyPart){
            case "Chest":
                arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.chest_arrays));
                //setListAdapter(arrayAdapter);
                //liftSpinner.setAdapter(arrayAdapter);
                break;
            case "Back":
                arrayAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.back_arrays));
                //setListAdapter(arrayAdapter);
                //liftSpinner.setAdapter(arrayAdapter);
                break;
            case "Legs":
                arrayAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.legs_arrays));
                //setListAdapter(arrayAdapter);
               // liftSpinner.setAdapter(arrayAdapter);
                break;
            case "Abs":
                arrayAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.abs_arrays));
                //setListAdapter(arrayAdapter);
                //liftSpinner.setAdapter(arrayAdapter);
                break;
            case "Arms":
                arrayAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_spinner_item, getResources().getStringArray ( R.array.arms_arrays));
               // setListAdapter(arrayAdapter);
                //liftSpinner.setAdapter(arrayAdapter);
                break;
        }
*/

/*
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
               */
  /*
        Toast.makeText(parent.getContext(),
                "Hello",
                Toast.LENGTH_SHORT).show();
                */
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}

/*
class MyList extends ListActivity{
    MyCustomOnItemSelectedListener service = new MyCustomOnItemSelectedListener();
}
*/