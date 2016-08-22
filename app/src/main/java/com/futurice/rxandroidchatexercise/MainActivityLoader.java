package com.futurice.rxandroidchatexercise;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;

public class MainActivityLoader extends android.support.v4.content.Loader<MainActivityViewModel> {
    private static final String TAG = MainActivityLoader.class.getSimpleName();

    private Gson gson;
    private final ChatMessageRepository chatMessageRepository = new ChatMessageRepository();
    private final ChatMessageApi chatMessageApi;

    private Subscription messageSubscription;
    private Socket socket;
    private MainActivityViewModel mainActivityViewModel;

    public MainActivityLoader(Context context) {
        super(context);
        Log.d(TAG, "MainActivityLoader");

        gson = new Gson();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://blooming-brook-85633.herokuapp.com/")
                .build();
        chatMessageApi = retrofit.create(ChatMessageApi.class);
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

        socket = SocketUtil.createSocket();
        socket.connect();

        messageSubscription = SocketUtil.createMessageListener(socket)
                .subscribe(messageString -> {
                    Log.d(TAG, "chat message: " + messageString);
                    // TODO: Incoming message logic
                });

        mainActivityViewModel = new MainActivityViewModel(
                chatMessageRepository.getMessageListStream(),
                message -> {
                    // TODO: Outgoing message logic
                }
        );
        mainActivityViewModel.subscribe();
        deliverResult(mainActivityViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");
        messageSubscription.unsubscribe();
        mainActivityViewModel.unsubscribe();
        mainActivityViewModel = null;
        socket.disconnect();
        socket = null;
    }
}
