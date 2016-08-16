package com.futurice.rxandroidchatexercise;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ttuo on 16/08/16.
 */
public class MessageUtil {
    public static Observable<List<String>> accumulateMessages(
            Observable<String> messageObservable) {
        return messageObservable.scan(new ArrayList<String>(),
                (list, value) -> {
                    list.add(value);
                    return list;
                });
    }
}
