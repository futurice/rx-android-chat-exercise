Rx Android Chat Exercise Part 3
===============================

In this part we will add a confirmation from the server that the message has arrived. All messages that are sent stay in "pending" state until they are received from the server.

Instructions
------------

The message handling code goes to the MainActivityLoader. We could move this to the MainActivityViewModel, but so far the Loader is not too big so there is no dire need.

For messages sent to and received from the server we will start to serialise them in JSON. A Gson parser is given and the ChatMessage class is serialisable with it.

1. Write the outgoing message as a serialised ChatMessage and then parse it in the incoming handler. You will only see log at this point.
2. Push the incoming message to the chatMessageRepository - the View Model will process it and pass to rendering.
3. Push the message to the repository also just after *sending* it. This way we see it immediately. However, when sending set the isPending flag to true.
4. When the message comes from the backend, set the isPending status to false before pushing it to the repository.

Message history:
5. Use the given ChatMessageApi to retrieve previous chat messages from the backend when the loader is created.
6. The messages are in the form of String, so you will need to use Gson to parse them from the List returned.
7. Push the messages to the ChatRepository as its initial data.

Advanced: Start moving more logic from the MainActivityLoader to the MainActivityViewModel and write unit tests for relevant parts.
