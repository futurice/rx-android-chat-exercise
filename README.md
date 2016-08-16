Rx Android Chat Exercise Part 2
===============================

Topics: View models, life cycles, rotation

Instructions
------------

This time the code does not go to Activity onCreate / onDestroy because we are using a Loader to manage the socket. Most code will go to the MainActivityViewModel or MainActivity onResume / onPause. The Loader has been already set up in the code - it is persisted across Activity instances that happen when rotating.

You can use the temporary chat server web page to test: https://lit-everglades-74863.herokuapp.com

0. You can see the custom observable implementation in SocketUtil in case you did not do it in Part 1. It already in use in the Loader, though.
1. The MainActivityViewModel is already receiving the arguments it needs to operate, it is just missing some wiring. Connect the output to the inputs so that subscribe starts pushing values to the messageList behavior. There is a utility in MessageUtil for the part we covered in last chapter.
2. In Activity onResume do as before and bind the view model output to the list adapter.
3. In Activity onPause release the subscriptions made in the onResume. Not doing this could cause strange behavior as the view model is persisted.
4. Also connect the send button to the view model sendMessage function (which is also missing a bit of implementation).
5. Test rotating the device - the received messages should stay on the screen since the view model is reconnected.

