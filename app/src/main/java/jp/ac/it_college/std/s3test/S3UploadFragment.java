package jp.ac.it_college.std.s3test;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.net.URISyntaxException;


public class S3UploadFragment extends Fragment {

    private static final int REQUEST_GALLERY = 0;
    private TransferUtility mTransferUtility;
    private String mFilePath;
    private static final String TAG = "S3UploadFragment";
    private TextView mLabelSelectedFile;


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

        return view;
    }


    private void setUpUtility() {
        mTransferUtility =
                ((MainActivity) getActivity()).getClientManager().getTransferUtility(getActivity());
    }

    private void findViews(View view) {
        mLabelSelectedFile = (TextView) view.findViewById(R.id.lbl_selected_file);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_s3_upload, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_s3_upload_select_file:
                selectFile();
                break;
            case R.id.menu_s3_begin_upload:
                beginUpload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            setFile(data);
        }
    }

    private void beginUpload() {
        if (mFilePath == null) {
            Toast.makeText(getActivity(), "File has not been set.", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(mFilePath);
        TransferObserver observer = mTransferUtility.upload(
                Constants.BUCKET_NAME,
                file.getName(),
                file);

        observer.setTransferListener(new S3UploadListener());
    }

    private void selectFile() {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= 19) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.setType("*/*");
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
        }
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void setFile(Intent data) {
        try {
            mFilePath = getPath(data.getData());
        } catch (URISyntaxException e) {
            Toast.makeText(
                    getActivity(),
                    "Unable to get the file from the given URI.  See error log for details",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        mLabelSelectedFile.setText(getString(R.string.lbl_select_file) + getFileName(data.getData()));
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        Cursor cursor =
                getActivity().getContentResolver().query(uri, null, null, null, null);

        if (cursor.moveToFirst()) {
            int nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(nameIdx);
        }

        return fileName;
    }

    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;

        if (needToCheckUri && DocumentsContract.isDocumentUri(getActivity(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");

                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };

            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private class S3UploadListener implements TransferListener {

        private ProgressDialogFragment mDialogFragment;
        private static final String TAG = "UploadListener";

        public S3UploadListener() {
            setUpDialogFragment();
        }

        public void onStateChanged(int i, TransferState transferState) {
            Log.d(TAG, "onStateChanged: " + String.valueOf(i));

            switch (transferState) {
                case IN_PROGRESS:
                    // アップロード開始時にダイアログ表示
                    mDialogFragment.show(getFragmentManager(), "S3UploadListener");
                    break;
                case COMPLETED:
                    // アップロード終了時にダイアログ非表示
                    mDialogFragment.dismiss();

                    //Toast表示
                    Toast.makeText(getActivity(), "Upload completed.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Upload failed.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onProgressChanged(int i, long l, long l1) {
            Log.d(TAG, "onProgressChanged:");
        }

        @Override
        public void onError(int i, Exception e) {
            Log.d(TAG, "Error during upload: " + i, e);
        }

        private void setUpDialogFragment() {
            mDialogFragment = ProgressDialogFragment.newInstance("S3Upload", "Uploading...");
        }
    }
}
