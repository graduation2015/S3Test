package jp.ac.it_college.std.s3test;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener {
    public static final String TAG = "DrawerItemClickListener";
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        selectItem(position);
    }

    private void selectItem(int position) {
        Log.d(TAG, String.valueOf(position));
    }
}
