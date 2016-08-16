package com.futurice.rxandroidchatexercise;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MainActivityViewModel> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 101;

    private MainActivityViewModel mainActivityViewModel;

    private ArrayAdapter<String> arrayAdapter;
    private View sendButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        sendButton = findViewById(R.id.send_button);
        editText = (EditText) findViewById(R.id.edit_text);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        // Make all subscriptions to connect the view model to the UI
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        // Clean all subscriptions to connect the view model to the UI
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
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
        this.mainActivityViewModel = data;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset");
        this.mainActivityViewModel = null;
    }
}
