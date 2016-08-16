package com.futurice.rxandroidchatexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayAdapter<String> arrayAdapter;
    private View sendButton;
    private EditText editText;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.send_button);
        editText = (EditText) findViewById(R.id.edit_text);

        // Create adapter for the ListView
        ListView listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        // Create a socket for the incoming message
        socket = SocketUtil.createSocket();

        // Attach a listener for message
        socket.on(SocketUtil.CHAT_MESSAGE_ID, this::onChatMessage);

        // Connect to the chat server
        socket.connect();
    }

    private void onChatMessage(Object... args) {
        Log.d(TAG, SocketUtil.CHAT_MESSAGE_ID + ": " + args[0]);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        // Disconnect from chat server
        socket.off(SocketUtil.CHAT_MESSAGE_ID, this::onChatMessage);
        socket.disconnect();
    }
}
