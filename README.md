Rx Android Chat Exercise Part 1
===============================

Topics: Subjects, .scan, Subscriptions

Instructions
------------

Most code goes to the Activity onCreate. Tear-down code is in onDestroy.

You can use the temporary chat server web page to test: https://lit-everglades-74863.herokuapp.com

0. Add a listener to the sendButton and use SocketUtil.sendMessage to send a message and see it in the logs.
1. Create a PublishSubject in the Activity and push all incoming messages to it.
2. Subscribe to the subject and show the last message on the ArrayAdapter.
3. Save the subscription and release it when the Activity is destroyed.
3. Use .scan to aggregate the messages so that you will have a complete list of messages.
4. You now have a working chat client!

Optional:

5. Instead of having a separate subject for the incoming messages create a custom observable that gives the messages. Considerations:

  - Use Observable.create
  - The observable needs a reference to socket to establish the .on(..) listener
  - Do not worry about the .off(..) for now
  - Pass everything from the listener to the subscriber
  - Use the new observable the same way you would use the subject
  - Use CompositeSubscription to collect all subscriptions that you create in onCreate and release them at once in onDestroy

6. Add clean up for the custom observable. Considerations:

  - You can attach more subscriptions to the subscriber that will be triggered on unsubscribe, use subscriber.add(Subscription)
  - To create a new subscription you can use BooleanSubscription.create(..)
  - Save the function reference passed to the .on(..) listener as an Action1
  - Call the .off(..) in the unsubscribe of the custom subscription
