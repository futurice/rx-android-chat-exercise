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

import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MessagesViewModel> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 101;

    private CompositeSubscription bindingSubscriptions;
    private MessagesViewModel messagesViewModel;

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
        bindingSubscriptions = new CompositeSubscription();
        bindingSubscriptions.add(
                messagesViewModel
                        .getMessageList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageList -> {
                                    arrayAdapter.clear();
                                    arrayAdapter.addAll(messageList);
                                })
        );
        bindingSubscriptions.add(
            RxView.clicks(sendButton)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ev -> {
                        final String text = editText.getText().toString();
                        if (text.length() > 0) {
                            messagesViewModel.sendMessage(text);
                            editText.setText("");
                        }
                    })
        );
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        bindingSubscriptions.unsubscribe();
        bindingSubscriptions = null;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public Loader<MessagesViewModel> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader(" + id + ")");
        return new MainActivityLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, MessagesViewModel data) {
        Log.d(TAG, "onLoadFinished");
        this.messagesViewModel = data;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset");
        this.messagesViewModel = null;
    }
}
