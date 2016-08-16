package com.futurice.rxandroidchatexercise;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;

import rx.Observable;

/**
 * Handles the creation and management of MainActivityViewModel. The same view model instance is
 * used across different Activity instances. The Android platform takes care of the life cycle of
 * the loader.
 */
public class MainActivityLoader extends android.support.v4.content.Loader<MainActivityViewModel> {
    private static final String TAG = MainActivityLoader.class.getSimpleName();

    private Socket socket;
    private MainActivityViewModel mainActivityViewModel;

    public MainActivityLoader(Context context) {
        super(context);
        Log.d(TAG, "MainActivityLoader");
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");

        if (mainActivityViewModel != null) {
            deliverResult(mainActivityViewModel);
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad");

        // Create and connect the socket
        socket = SocketUtil.createSocket();
        socket.connect();

        // Create the view model based on the socket. Notice that the view model does not have
        // a direct reference to the socket.
        Observable<String> messagesObservable = SocketUtil.createMessageListener(socket);
        mainActivityViewModel = new MainActivityViewModel(
                messagesObservable,
                message -> socket.emit("chat message", message)
        );
        mainActivityViewModel.subscribe();
        deliverResult(mainActivityViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");

        // Clean up the view model and the socket.
        mainActivityViewModel.unsubscribe();
        mainActivityViewModel = null;
        socket.disconnect();
        socket = null;
    }
}
