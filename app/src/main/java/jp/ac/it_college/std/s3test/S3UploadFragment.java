package jp.ac.it_college.std.s3test;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.InputStream;


public class S3UploadFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<TransferObserver> {

    private ImageView mSelectedPic;
    private static final String INTENT_TYPE = "image/*";
    private static final int REQUEST_GALLERY = 0;
    private static final String KEY_FILE_PATH = "file_path";
    private TransferUtility mTransferUtility;
    private String mFilePath;
    private ProgressDialogFragment mDialogFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s3_upload, container, false);

        //Viewを取得
        findViews(view);

        //メニューを表示
        setHasOptionsMenu(true);

        //TransferUtilityを設定
        setUpUtility();

        //ProgressDialogFragmentのセットアップ
        setUpDialogFragment();

        return view;
    }

    private void setUpDialogFragment() {
        mDialogFragment = ProgressDialogFragment.newInstance("S3Upload", "Uploading...");
    }

    private void setUpUtility() {
        AmazonS3Client s3Client = ((MainActivity) getActivity())
                .getClientManager().getS3Client();
        mTransferUtility = new TransferUtility(s3Client, getActivity());
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
            case R.id.menu_s3_upload:
                s3Upload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void s3Upload() {
        getLoaderManager().restartLoader(0, makeUploadBundle(), this);
    }

    private Bundle makeUploadBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FILE_PATH, mFilePath);
        return  bundle;
    }

    private void selectPic() {
        Intent intent = new Intent();
        intent.setType(INTENT_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void setPic(Intent data) {
        //画像のパスをセット
        mFilePath = data.getData().getPath();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            setPic(data);
        }
    }

    /* Implemented LoaderManager.LoaderCallbacks */
    @Override
    public Loader<TransferObserver> onCreateLoader(int i, Bundle bundle) {
        mDialogFragment.show(getFragmentManager(), "TAG_S3_Upload");
        String filePath = null;
        TransferUtility utility = ((MainActivity) getActivity())
                .getClientManager().getTransferUtility();
        if (bundle != null) {
            filePath = bundle.getString(KEY_FILE_PATH);
        }

        return new S3UploadObjectAsyncTaskLoader(getActivity(), filePath, utility);
    }

    @Override
    public void onLoadFinished(Loader<TransferObserver> loader, TransferObserver transferObserver) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mDialogFragment.dismiss();
            }
        });

        Toast.makeText(getActivity(), "Upload completed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<TransferObserver> loader) {

    }
}
