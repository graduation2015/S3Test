package jp.ac.it_college.std.s3test;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;

public class S3UploadObjectAsyncTaskLoader extends AsyncTaskLoader<TransferObserver> {

    private Context mContext;
    private String mFilePath;
    private TransferUtility utility;
    private static final String BUCKET_KEY = "s3_test_file";

    public S3UploadObjectAsyncTaskLoader(Context context,
                                         String filePath, TransferUtility utility) {
        super(context);
        this.mContext = context;
        this.mFilePath = filePath;
        this.utility = utility;
    }

    @Override
    public TransferObserver loadInBackground() {
        return utility.upload(
                Constants.BUCKET_NAME,
                BUCKET_KEY,
                new File(mFilePath));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
