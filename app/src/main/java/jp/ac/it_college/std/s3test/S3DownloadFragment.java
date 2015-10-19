package jp.ac.it_college.std.s3test;


import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class S3DownloadFragment extends ListFragment {

    private List<Bitmap> items = new ArrayList<>();
    private static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory()
            + "/SampleDirectory/";
    private String[] list = new File(DIRECTORY_PATH).list();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setListAdapter(new S3DownloadsListAdapter(getActivity(), R.layout.row_s3_downloads, items));
        setDownLoads();
        return inflater.inflate(R.layout.fragment_s3_download, container, false);
    }

    private void setDownLoads() {
        items.clear();

        for (String name : list) {
            Bitmap bitmap = BitmapFactory.decodeFile(DIRECTORY_PATH + name);
            items.add(bitmap);
        }

        ((S3DownloadsListAdapter) getListAdapter()).notifyDataSetChanged();
    }


}
