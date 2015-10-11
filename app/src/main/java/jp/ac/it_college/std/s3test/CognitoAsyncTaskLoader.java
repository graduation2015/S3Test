package jp.ac.it_college.std.s3test;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.regions.Regions;

public class CognitoAsyncTaskLoader extends AsyncTaskLoader<AWSCredentials>{
    private Context mContext;

    public CognitoAsyncTaskLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public AWSCredentials loadInBackground() {
        CognitoCredentialsProvider cognitoProvider = new CognitoCredentialsProvider(
                Constants.IDENTITY_POOL_ID,
                Regions.AP_NORTHEAST_1);

        return cognitoProvider.getCredentials();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
