package jp.ac.it_college.std.s3test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;


public class S3UploadFragment extends Fragment {
    private ImageView mSelectedPic;
    private static final String INTENT_TYPE = "image/*";
    private static final int REQUEST_GALLERY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s3_upload, container, false);
        findViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void findViews(View view) {
        mSelectedPic = (ImageView) view.findViewById(R.id.img_selected_pic);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_s3_upload, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_s3_upload_select_image:
                selectPic();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPic() {
        Intent intent = new Intent();
        intent.setType(INTENT_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = getActivity().getContentResolver()
                        .openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                // 選択した画像をセット
                mSelectedPic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
