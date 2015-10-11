package jp.ac.it_college.std.s3test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

public class AWSClientManager {

    private AWSCredentials mCredentials;
    private AmazonS3Client s3Client;

    public AWSClientManager() {
        this.mCredentials = new BasicAWSCredentials(Constants.ACCESS_KEY, Constants.SECRET_KEY);
        makeS3Client();
    }

    public AWSClientManager(AWSCredentials credentials) {
        this.mCredentials = credentials;
    }

    private void makeS3Client() {
        s3Client = new AmazonS3Client(mCredentials);
        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    }

    public AmazonS3Client getS3Client() {
        return s3Client;
    }

    public AWSCredentials getCredentials() {
        return mCredentials;
    }
}
