package jp.ac.it_college.std.s3test;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;

public class S3GetBucketListAsyncTaskLoader extends AsyncTaskLoader<ObjectListing> {
    private Context mContext;
    private AmazonS3Client mS3Client;

    public S3GetBucketListAsyncTaskLoader(Context context, AmazonS3Client s3Client) {
        super(context);
        this.mS3Client = s3Client;
    }

    @Override
    public ObjectListing loadInBackground() {
        return mS3Client.listObjects(Constants.BUCKET_NAME);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
