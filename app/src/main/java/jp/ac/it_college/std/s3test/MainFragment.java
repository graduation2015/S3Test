package jp.ac.it_college.std.s3test;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentials;


public class MainFragment extends Fragment {
    private TextView mLabelAccessKey;
    private TextView mLabelSecretKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        mLabelAccessKey = (TextView) view.findViewById(R.id.lbl_access_key);
        mLabelSecretKey = (TextView) view.findViewById(R.id.lbl_secret_key);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showCredentials:
                showCredentials();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCredentials() {
        AWSCredentials credentials = ((MainActivity) getActivity()).getClientManager().getCredentials();
        mLabelAccessKey.setText(String.format("%s\n%s", getString(R.string.lbl_access_key), credentials.getAWSAccessKeyId()));
        mLabelSecretKey.setText(String.format("%s\n%s", getString(R.string.lbl_secret_key), credentials.getAWSSecretKey()));
    }
}
