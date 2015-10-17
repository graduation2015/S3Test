package jp.ac.it_college.std.s3test;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;


public class S3ListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ObjectListing>{

    private List<S3ObjectSummary> mObjectSummaries = new ArrayList<>();
    private ProgressDialogFragment dialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        setListAdapter(new S3ObjectsListAdapter(getActivity(), R.layout.row_s3_objects, mObjectSummaries));
        dialogFragment = ProgressDialogFragment.newInstance(
                getString(R.string.progress_dialog_title), getString(R.string.progress_dialog_message));

        // オブジェクトリスト更新
        reloadList();

        return inflater.inflate(R.layout.fragment_s3_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_s3_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_s3_list_reload:
                reloadList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadList() {
        getLoaderManager().restartLoader(0, null, this);
   }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        S3ObjectSummary summary = (S3ObjectSummary) getListAdapter().getItem(position);
//        TransferUtility utility = ((MainActivity) getActivity()).getClientManager().getTransferUtility();
//
//        TransferObserver observer = utility.download(
//                Constants.BUCKET_NAME,     /* The bucket to download from */
//                summary.getKey(),    /* The key for the object to download */
//                MY_FILE        /* The file to download the object to */
//        );
    }

    @Override
    public Loader<ObjectListing> onCreateLoader(int i, Bundle bundle) {
        dialogFragment.show(getFragmentManager(), "Progress");

        AmazonS3Client client = ((MainActivity) getActivity()).getClientManager().getS3Client();
        return new S3GetObjectListAsyncTaskLoader(getActivity(), client);
    }

    @Override
    public void onLoadFinished(Loader<ObjectListing> loader, ObjectListing objectListing) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                dialogFragment.dismiss();
            }
        });

        mObjectSummaries.clear();
        mObjectSummaries.addAll(objectListing.getObjectSummaries());
        ((S3ObjectsListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ObjectListing> loader) {

    }
}
