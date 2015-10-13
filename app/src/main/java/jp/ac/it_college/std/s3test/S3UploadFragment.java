package jp.ac.it_college.std.s3test;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class S3UploadFragment extends Fragment {
    private ImageView mSelectedPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s3_upload, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        mSelectedPic = (ImageView) view.findViewById(R.id.img_selected_pic);
    }

    
}
