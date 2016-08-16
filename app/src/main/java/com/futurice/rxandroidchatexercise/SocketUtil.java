package com.futurice.rxandroidchatexercise;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import rx.Observable;
import rx.subscriptions.BooleanSubscription;

/**
 * Created by ttuo on 16/08/16.
 */
public class SocketUtil {
    private static final String TAG = SocketUtil.class.getSimpleName();

    public static Observable<String> createMessageListener(final Socket socket) {
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
}
