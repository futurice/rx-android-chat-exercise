package com.futurice.rxandroidchatexercise;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import rx.Observable;

/**
 * Created by ttuo on 16/08/16.
 */
public class MainActivityLoader extends android.support.v4.content.Loader<MessagesViewModel> {
    private static final String TAG = MainActivityLoader.class.getSimpleName();

    private Socket socket;
    private MessagesViewModel messagesViewModel;

    public MainActivityLoader(Context context) {
        super(context);
        Log.d(TAG, "MainActivityLoader");
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");

        if (messagesViewModel != null) {
            deliverResult(messagesViewModel);
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad");

        socket = SocketUtil.createSocket();
        socket.connect();
        Observable<String> messagesObservable = SocketUtil.createMessageListener(socket);
        messagesViewModel = new MessagesViewModel(
                messagesObservable,
                message -> socket.emit("chat message", message)
        );
        messagesViewModel.subscribe();
        deliverResult(messagesViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");

        messagesViewModel.unsubscribe();
        messagesViewModel = null;
        socket.disconnect();
        socket = null;
    }
}
