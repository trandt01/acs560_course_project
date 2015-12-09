package com.dtran.testtemplate1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomListViewAdapter  extends ArrayAdapter<HistoryRowItem> {
    Context context;

    public CustomListViewAdapter(Context context, int resourceId, List<HistoryRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final HistoryRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_history_item_row, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //long t = rowItem.getID();
                    LiftDataSource datasource = new LiftDataSource(v.getContext());
                    datasource.open();
                    datasource.deleteLiftById(rowItem.getID());


                    datasource.close();

                    Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                    intent.putExtra("selectedDate", rowItem.getSelectDate());
                    v.getContext().startActivity(intent);

                    /*
                    Intent intent = new Intent(v.getContext(), InsertActivity.class);
                    intent.putExtra("selectedLiftID",rowItem.getID());
                    v.getContext().startActivity(intent);
                    */
                }
            });

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());

        return convertView;
    }
}
