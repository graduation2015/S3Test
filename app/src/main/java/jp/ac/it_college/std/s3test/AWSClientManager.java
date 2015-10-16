package jp.ac.it_college.std.s3test;

import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AWSClientManager {

    private AWSCredentials mCredentials;
    private AmazonS3Client s3Client;
    private TransferUtility transferUtility;
    private Context context;


    public AWSClientManager() {
        this.mCredentials = new BasicAWSCredentials(Constants.ACCESS_KEY, Constants.SECRET_KEY);
    }

    public AWSClientManager(AWSCredentials credentials, Context context) {
        this.mCredentials = credentials;
        this.context = context;
    }

    private AmazonS3Client makeS3Client() {
        s3Client = new AmazonS3Client(mCredentials);
        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        return s3Client;
    }

    public AmazonS3Client getS3Client() {
        if (s3Client == null) {
            return makeS3Client();
        } else {
            return s3Client;
        }
    }

    private TransferUtility makeTransferUtility(Context context) {
        //Download and Upload用のUtilityセットアップ
        TransferUtility transferUtility = new TransferUtility(getS3Client(), context);
        return  transferUtility;
    }

    public TransferUtility getTransferUtility() {
        if (transferUtility == null) {
            return makeTransferUtility(context);
        } else {
            return transferUtility;
        }
    }

    public AWSCredentials getCredentials() {
        return mCredentials;
    }
}
