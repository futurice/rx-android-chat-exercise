package com.futurice.rxandroidchatexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jakewharton.rxbinding.view.RxView;

import java.net.URISyntaxException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompositeSubscription subscription;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

        subscription = new CompositeSubscription();

        try {
            socket = IO.socket("https://lit-everglades-74863.herokuapp.com");
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error creating socket", e);
        }

        if (socket != null) {
            Observable<String> messages = createMessageListener(socket);
            subscription.add(
                    messages
                            .scan(new ArrayList<String>(),
                                    (list, value) -> {
                                        list.add(value);
                                        return list;
                                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageList -> {
                                        arrayAdapter.clear();
                                        arrayAdapter.addAll(messageList);
                                    }));
            socket.on(Socket.EVENT_CONNECT, args -> {
                Log.d(TAG, "connection1");
            });
            socket.connect();
        }

        RxView.clicks(findViewById(R.id.send_button))
                .subscribe(ev -> {
                    socket.emit("chat message", "hello world");
                });

    }

    private static Observable<String> createMessageListener(final Socket socket) {
        return Observable.create(subscriber -> {
            final Emitter.Listener listener =
                    args -> subscriber.onNext(args[0].toString());
            socket.on("chat message", listener);
            subscriber.add(BooleanSubscription.create(
                    () -> {
                        Log.d(TAG, "unsubscribe");
                        socket.off("chat message", listener);
                    }));
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
