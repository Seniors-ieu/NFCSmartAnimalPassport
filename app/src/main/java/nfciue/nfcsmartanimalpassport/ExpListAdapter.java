package nfciue.nfcsmartanimalpassport;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ASLI on 20.4.2018.
 */

public class ExpListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> basliklar;
    private HashMap<String, ArrayList<String>> icerik;
    //String data ="123456";
    ArrayList<ArrayList<String>> data;


    public ExpListAdapter(Context context, ArrayList<String> basliklar, HashMap<String, ArrayList<String>> icerik,ArrayList<ArrayList<String>> data) {
        this.context = context;
        this.basliklar = basliklar;
        this.icerik = icerik;
        this.data=data;
    }

    @Override
    public int getGroupCount() {
        return basliklar.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return icerik.get((basliklar).get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return basliklar.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return icerik.get(basliklar.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String baslik = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);

        }

        TextView tvBaslik = (TextView) convertView.findViewById(R.id.textView1);
        tvBaslik.setText(baslik);
        tvBaslik.setTypeface(null, Typeface.BOLD);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // TODO Auto-generated method stub
        String text = (String) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView tvText = (TextView) convertView.findViewById(R.id.textView1);
        tvText.setText(text);
        Log.e("childview","set");

        TextView tvData = (TextView) convertView.findViewById(R.id.textView2);
        tvData.setText(data.get(groupPosition).get(childPosition));
        tvData.setTypeface(null, Typeface.BOLD);
        Log.e("groupview","set");
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
