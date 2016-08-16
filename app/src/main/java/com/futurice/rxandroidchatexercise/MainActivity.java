package com.futurice.rxandroidchatexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jakewharton.rxbinding.view.RxView;

import java.net.URISyntaxException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompositeSubscription subscription;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View sendButton = findViewById(R.id.send_button);
        final EditText editText = (EditText) findViewById(R.id.edit_text);
        final ListView listView = (ListView) findViewById(R.id.list_view);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        subscription = new CompositeSubscription();

        try {
            socket = IO.socket("https://lit-everglades-74863.herokuapp.com");
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error creating socket", e);
        }

        if (socket != null) {
            Observable<String> messagesObservable = SocketUtil.createMessageListener(socket);
            Observable<List<String>> messageListObservable =  MessageUtil.accumulateMessages(messagesObservable);

            subscription.add(
                    messageListObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageList -> {
                                        arrayAdapter.clear();
                                        arrayAdapter.addAll(messageList);
                                    }));

            socket.connect();
        }

        RxView.clicks(sendButton)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ev -> {
                    final String text = editText.getText().toString();
                    if (text.length() > 0) {
                        socket.emit("chat message", text);
                        editText.setText("");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
        if (subscription != null) {
            subscription.clear();
            subscription = null;
        }
    }
}
