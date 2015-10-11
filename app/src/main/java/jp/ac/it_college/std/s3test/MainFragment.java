package jp.ac.it_college.std.s3test;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.auth.AWSCredentials;


public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cognito_sync:
                cognitoSync();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cognitoSync() {
        AWSCredentials session = ((MainActivity) getActivity()).getClientManager().getCredentials();
        Log.d("Credentials", "AccessKey = " + session.getAWSAccessKeyId());
        Log.d("Credentials", "SecretKey = " + session.getAWSSecretKey());
    }
}
