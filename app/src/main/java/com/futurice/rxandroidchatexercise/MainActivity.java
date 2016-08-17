package com.futurice.rxandroidchatexercise;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MainActivityViewModel> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 101;

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        presenter.attach();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        presenter.detach();
    }

    // Loader callbacks. Boilerplate code to get a reference to our view model.

    @Override
    public Loader<MainActivityViewModel> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader(" + id + ")");
        return new MainActivityLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, MainActivityViewModel data) {
        Log.d(TAG, "onLoadFinished");
        this.presenter = new MainActivityPresenter(this, data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset");
    }
}
