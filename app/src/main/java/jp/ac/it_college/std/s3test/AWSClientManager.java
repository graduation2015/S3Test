package jp.ac.it_college.std.s3test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

public class AWSClientManager {

    public AmazonS3Client makeS3Client() {
        AWSCredentials credentials = makeCredentials();

        AmazonS3Client client = new AmazonS3Client(credentials);
        client.setEndpoint(Constants.END_POINT);
        return client;
    }

    public AWSSessionCredentials makeCredentials() {
        CognitoCredentialsProvider cognitoProvider = new CognitoCredentialsProvider(
                Constants.ACCOUNT_ID,
                Constants.IDENTITY_POOL_ID,
                Constants.UNAUTH_ROLE_NAME,
                Constants.AUTH_ROLE_NAME,
                Regions.AP_NORTHEAST_1);

        return cognitoProvider.getCredentials();
    }
}
