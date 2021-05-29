package com.maxprof90.redditarticles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.maxprof90.redditarticles.adapters.RedditPostAdapter;
import com.maxprof90.redditarticles.api.ApiClient;
import com.maxprof90.redditarticles.models.Children;
import com.maxprof90.redditarticles.models.RootReddit;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final int LIMIT = 4;
    private static final String SAVED_RECYCLER_VIEW_DATASET_ID = "RV_DATA";
    private static final String SAVED_RECYCLER_VIEW_STATUS_ID = "RV_STATE";

    RecyclerView recyclerView;
    List<Children> children;
    RedditPostAdapter adapter;
    ApiClient apiClient;
    ProgressBar progressBar;
    String after = "";
    String before = null;
    Button prev;
    String firstPageFlag = null; // Saves the first post id to determine the first page
    MutableLiveData<String> listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_load_photo);
        prev = findViewById(R.id.prevBt);
        apiClient = ApiClient.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager gd = new GridLayoutManager(MainActivity.this, 1);
            gd.onSaveInstanceState();
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        }
        if (savedInstanceState != null) {
            after = savedInstanceState.getString("after");
            before = savedInstanceState.getString("before");
            firstPageFlag = savedInstanceState.getString("firstPageFlag");

            children = savedInstanceState.getParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID);

            adapter = new RedditPostAdapter(this, children);
            Parcelable listState = savedInstanceState.getParcelable(SAVED_RECYCLER_VIEW_STATUS_ID);
            RecyclerView.LayoutManager lll = recyclerView.getLayoutManager();
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(listState);
            recyclerView.setAdapter(adapter);

        } else if (savedInstanceState == null) {

            children = new ArrayList<>();
            adapter = new RedditPostAdapter(this, children);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager gd = new GridLayoutManager(MainActivity.this, 1);
                gd.onSaveInstanceState();
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            }
            recyclerView.setAdapter(adapter);
            getFirstPage();

        }
        listen = new MutableLiveData<>();
        listen.setValue(before);
        listen.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                if (changedValue == null || after.equals(firstPageFlag)) {
                    prev.setVisibility(View.GONE);
                } else {
                    prev.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void getFirstPage() {
       progressBar.setVisibility(View.VISIBLE);
        apiClient.getApiInterface().getData().enqueue(new Callback<RootReddit>() {
            @Override
            public void onResponse(Call<RootReddit> call, Response<RootReddit> response) {
                RootReddit redditResponse = response.body();
                if (redditResponse != null && redditResponse.getData() != null) {
                    after = redditResponse.getData().getAfter();
                    firstPageFlag = after;
                    children.addAll(redditResponse.getData().getChildren());
                    adapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<RootReddit> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                checkInternetConnection(t);
            }
        });
    }

    public void checkInternetConnection(Throwable t) {
        Log.i(TAG, "onFailure: " + t.fillInStackTrace());
        if (t instanceof UnknownHostException) {
            new AlertDialog.Builder(MainActivity.this).setMessage(getString(R.string.check_internet))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
        } else
            new AlertDialog.Builder(MainActivity.this).setMessage(t.getLocalizedMessage()).show();


    }


    public void getAfter(View view) {
        if (!after.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            apiClient.getApiInterface().getDataAfter(after, LIMIT).enqueue(new Callback<RootReddit>() {
                @Override
                public void onResponse(Call<RootReddit> call, Response<RootReddit> response) {
                    RootReddit redditResponse = response.body();
                    if (redditResponse != null && redditResponse.getData() != null) {
                        after = redditResponse.getData().getAfter();
                        children.clear();
                        children.addAll(redditResponse.getData().getChildren());
                        adapter.notifyDataSetChanged();
                        if (validateData(redditResponse.getData().getChildren())) {
                            before = redditResponse.getData().getChildren().get(0).getData().getName();
                            listen.setValue(before);
                        }
                    }

                   progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<RootReddit> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    checkInternetConnection(t);
                }
            });


        } else {
            getFirstPage();
        }


    }

    public void getBefore(View view) {
        getPreviousPage();

    }

    private void getPreviousPage() {
        if (firstPageFlag != null && !firstPageFlag.equals(after)) { // check if  first page
            progressBar = findViewById(R.id.progress_load_photo);
            progressBar.setVisibility(View.VISIBLE);
            apiClient.getApiInterface().getDataBefore(before, LIMIT).enqueue(new Callback<RootReddit>() {
                @Override
                public void onResponse(Call<RootReddit> call, Response<RootReddit> response) {
                    RootReddit redditResponse = response.body();
                    if (redditResponse != null && redditResponse.getData() != null) {

                        if (redditResponse.getData().getAfter() != null)
                            after = redditResponse.getData().getAfter();
                        else { // if 'after' in response is null  get the id of the last post in the response
                            if (validateData(redditResponse.getData().getChildren())) {
                                after = redditResponse.getData().getChildren().get(redditResponse.getData().getChildren().size() - 1).getData().getName();

                            }
                        }
                        if (validateData(redditResponse.getData().getChildren())) {
                            before = redditResponse.getData().getChildren().get(0).getData().getName();
                            listen.setValue(before);
                        }
                        children.clear();
                        children.addAll(redditResponse.getData().getChildren());
                        adapter.notifyDataSetChanged();
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<RootReddit> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    checkInternetConnection(t);
                }
            });


        } else {
            before = null;
            listen.setValue(null);

        }
    }


    public boolean validateData(List<Children> childrenList) {
        return childrenList != null && !childrenList.isEmpty();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Parcelable listState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        // putting recyclerview position
        outState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, listState);
        // putting recyclerview items
        ArrayList<Children> childrenArrayList = new ArrayList<>(children);
        outState.putParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID, childrenArrayList);
        outState.putString("after", after);
        outState.putString("before", before);
        outState.putString("firstPageFlag", firstPageFlag);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // use back button to go back to previous posts page
        if (prev.getVisibility() == View.VISIBLE)
            getPreviousPage();
        else // if user is already in first page exit app
            super.onBackPressed();
    }
}