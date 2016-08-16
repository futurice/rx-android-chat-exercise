package com.futurice.rxandroidchatexercise;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.UUID;

import rx.Observable;
import rx.Subscription;

public class MainActivityLoader extends android.support.v4.content.Loader<MessagesViewModel> {
    private static final String TAG = MainActivityLoader.class.getSimpleName();

    private Gson gson;
    private final ChatMessageRepository chatMessageRepository = new ChatMessageRepository();

    private Subscription messageSubscription;
    private Socket socket;
    private MessagesViewModel messagesViewModel;

    public MainActivityLoader(Context context) {
        super(context);
        Log.d(TAG, "MainActivityLoader");

        gson = new Gson();
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

        messageSubscription = SocketUtil.createMessageListener(socket)
                .subscribe(messageString -> {
                    Log.d(TAG, "chat message: " + messageString);
                    ChatMessage message = gson.fromJson(messageString, ChatMessage.class);
                    chatMessageRepository.put(new ChatMessage(message, false));
                });

        messagesViewModel = new MessagesViewModel(
                chatMessageRepository.getMessageListStream(),
                message -> {
                    ChatMessage chatMessage = new ChatMessage(
                            UUID.randomUUID().toString(),
                            message,
                            new Date().getTime(),
                            true
                    );
                    chatMessageRepository.put(chatMessage);
                    String json = gson.toJson(chatMessage);
                    Log.d(TAG, "sending message: " + json);
                    socket.emit("chat message", json);
                }
        );
        messagesViewModel.subscribe();
        deliverResult(messagesViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");
        messageSubscription.unsubscribe();
        messagesViewModel.unsubscribe();
        messagesViewModel = null;
        socket.disconnect();
        socket = null;
    }
}
