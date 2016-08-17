package com.futurice.rxandroidchatexercise;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class MainActivityViewModel {

    private final BehaviorSubject<List<String>> messageList =
            BehaviorSubject.create(new ArrayList<>());

    public MainActivityViewModel(Observable<String> messagesObservable,
                                 Action1<String> sendMessageAction) {
    }

    public void subscribe() {
        // Connect the messagesObservable to the messageList. Since it was already covered in part 1
        // you can use MessageUtil.accumulateMessages to do this.
    }

    public void unsubscribe() {
        // Clear all subscriptions created in subscribe
    }

    // Expose the messageList as an output from this view model
    public Observable<List<String>> getMessageList() {
        return messageList.asObservable();
    }

    public void sendMessage(String message) {
        // Send message using the given action
    }
}
