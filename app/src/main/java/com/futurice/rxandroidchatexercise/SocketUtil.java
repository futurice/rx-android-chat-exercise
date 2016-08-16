package com.futurice.rxandroidchatexercise;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by ttuo on 16/08/16.
 */
public class SocketUtil {
    private static final String TAG = SocketUtil.class.getSimpleName();
    public static final String CHAT_MESSAGE_ID = "chat message";

    public static Socket createSocket() {
        Socket socket = null;
        try {
            socket = IO.socket("https://lit-everglades-74863.herokuapp.com");
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error creating socket", e);
        }
        return socket;
    }

    public static void sendMessage(Socket socket, String message) {
        socket.emit(CHAT_MESSAGE_ID, message);
    }
}
