package com.futurice.rxandroidchatexercise;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ttuo on 16/08/16.
 */
public class MessagesViewModel {

    private CompositeSubscription subscriptions;
    private final Observable<String> messagesObservable;
    private final Action1<String> sendMessageAction;
    private final BehaviorSubject<List<String>> messageList =
            BehaviorSubject.create(new ArrayList<>());

    public MessagesViewModel(Observable<String> messagesObservable,
                             Action1<String> sendMessageAction) {
        this.messagesObservable = messagesObservable;
        this.sendMessageAction = sendMessageAction;
    }

    public void subscribe() {
        unsubscribe();
        subscriptions = new CompositeSubscription();
        Observable<List<String>> messageListObservable =
                MessageUtil.accumulateMessages(messagesObservable);
        subscriptions.add(messageListObservable.subscribe(messageList));
    }

    public void unsubscribe() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
            subscriptions = null;
        }
    }

    public Observable<List<String>> getMessageList() {
        return messageList.asObservable();
    }

    public void sendMessage(String message) {
        this.sendMessageAction.call(message);
    }
}
