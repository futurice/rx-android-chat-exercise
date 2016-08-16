package com.futurice.rxandroidchatexercise;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ttuo on 16/08/16.
 */
public class MessagesViewModel {

    private CompositeSubscription subscriptions;
    private final Observable<String> messagesObservable;
    private final BehaviorSubject<List<String>> messageList =
            BehaviorSubject.create(new ArrayList<>());

    public MessagesViewModel(Observable<String> messagesObservable) {
        this.messagesObservable = messagesObservable;
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
}
