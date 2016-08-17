package com.futurice.rxandroidchatexercise;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivityPresenter {

    private final MainActivityViewModel mainActivityViewModel;
    private final ArrayAdapter<String> arrayAdapter;
    private final View sendButton;
    private final EditText editText;

    public MainActivityPresenter(MainActivity mainActivity, MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
        sendButton = mainActivity.findViewById(R.id.send_button);
        editText = (EditText) mainActivity.findViewById(R.id.edit_text);

        final ListView listView = (ListView) mainActivity.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    public void attach() {
        // Make all subscriptions to connect the view model to the UI
    }

    public void detach() {
        // Clean all subscriptions that we made in the attach
    }
}
