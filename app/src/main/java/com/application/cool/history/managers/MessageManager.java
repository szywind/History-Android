package com.application.cool.history.managers;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.App;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenzhenyuan on 6/21/16.
 */

public class MessageManager {

    public enum MessageType { TextMessage, PersonMessage};

    private Context context;

    private AVIMClient client;

    private AVIMConversation conversation;

    private static MessageManager sharedInstance = null;

    private MessageManager(Context context){
        this.context = context;
    }

    public void initClient() {
        this.client = AVIMClient.getInstance(UserManager.getSharedInstance(context).getUserId());
        this.client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {

            }
        });
    }

    public static MessageManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new MessageManager(context);
        }
        return sharedInstance;
    }

    public void stopClient() {
        this.client = AVIMClient.getInstance(UserManager.getSharedInstance(context).getUserId());
        this.client.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {

            }
        });
    }

//    /**
//     *  Send a text message to event's friend.
//     *  If the current conversation's name is not eventId, create a new conversation.
//     *  Otherwise use the current conversation.
//     */
//    public void sendMessage(final String text, final String toUser, final Event event, final Boolean pushFlag) {
//        String eventId = event.getEventID();
//        if (!eventId.isEmpty()) { // AVObject.class init() { this.objectId = ""; ... }
//
//            // chat on event
//            String conversationName = eventId;
//            if (conversation == null || conversation.getName() == null ||
//                    !conversation.getName().equals(conversationName)) {
//                // create new conversation if conversationName doesn't equal to eventId
//                client.createConversation(Arrays.asList(toUser), conversationName, null, new AVIMConversationCreatedCallback() {
//                    @Override
//                    public void done(AVIMConversation avimConversation, AVIMException e) {
//                        conversation = avimConversation;
//                        MessageSender messageSender = new MessageSender(conversation, createTextMessage(text, event), event, pushFlag);
//                        messageSender.send();
//                    }
//                });
//            } else {
//                MessageSender messageSender = new MessageSender(conversation, createTextMessage(text, event), event, pushFlag);
//                messageSender.send();
//            }
//        } else {
//            // general chat
//            String conversationName = toUser;
//            if (conversation == null || conversation.getName() == null ||
//                    !this.conversation.getName().equals(conversationName)) {
//                client.createConversation(Arrays.asList(toUser), conversationName, null, new AVIMConversationCreatedCallback() {
//                    @Override
//                    public void done(AVIMConversation avimConversation, AVIMException e) {
//                        conversation = avimConversation;
//                        MessageSender messageSender = new MessageSender(conversation, createTextMessage(text, toUser), event, pushFlag);
//                        messageSender.send();
//                    }
//                });
//            } else {
//                MessageSender messageSender = new MessageSender(conversation, createTextMessage(text, toUser), event, pushFlag);
//                messageSender.send();
//            }
//        }
//    }
//
//    /**
//     *  A MessageSender class used to send a message and boradcast a notification after the message is sent.
//     *
//     *  - note: Use MessageSender(conversation, message, event).send()
//     */
//    public class MessageSender {
//        AVIMTextMessage message;
//        AVIMConversation conversation;
//        Event event;
//        Boolean pushFlag;
//
//        public MessageSender(AVIMConversation conversation, AVIMTextMessage message, Event event, Boolean pushFlag) {
//            this.message = message;
//            this.conversation = conversation;
//            this.event = event;
//            this.pushFlag = pushFlag;
//        }
//
//        public void send() {
//            this.conversation.sendMessage(this.message, new AVIMConversationCallback() {
//
//                @Override
//                public void done(AVIMException e) {
//                    if (e == null) {
//
//                        Map<String, Object> attrs = message.getAttrs();
//                        int msgTypeVal = (int) attrs.get(LCConstants.MSG_TYPE);
//
////                        MessageManager.MessageType msgType = MessageManager.MessageType.values()[msgTypeVal];
//
//                        MessageLC myMessage = new MessageLC(message);
//                        myMessage.setUnread(false);
//                        // post-process event chat messages
//                        if (msgTypeVal == MessageType.TextMessage.ordinal()) {
//                            Boolean declineFlag = (Boolean) attrs.get(LCConstants.MSG_DECLINE);
//                            if (!declineFlag) {
//                                MessageStore.getSharedInstance(context).storeMessage(myMessage.toMessageEntity());
//
//                                Intent intent = new Intent("postSendChatMsg");
//                                context.sendBroadcast(intent);
//
//                                if (pushFlag) {
//                                    PushManager.getSharedInstance(context).pushMessage(message.getText(), event);
//                                }
//                            }
//                            if (pushFlag) {
//                                PushManager.getSharedInstance(context).pushMessage("Sorry that I can't come as expected.", event);
//                            }
//                        }
//                        // post-process general chat messages
//                        else if (msgTypeVal == MessageType.PersonMessage.ordinal()) {
//
//                            MessageStore.getSharedInstance(context).storeMessage(myMessage.toMessageEntity());
//
//                            Intent intent = new Intent("postSendChatMsg");
//                            context.sendBroadcast(intent);
//
//                            String toUser = (String) attrs.get(LCConstants.MSG_RECEIVERID);
//                            if (pushFlag) {
//                                PushManager.getSharedInstance(context).pushMessage(message.getText(), toUser);
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//
//    /**
//     *  Create a text message.
//     *
//     *  - returns: A AVIMTextMessage with text: inputext, attributes: [type: MessageType.TextMessage.rawValue,
//     *             receiverId: friendId, eventId: eventId, pushNotification: push notification alert text].
//     */
//    private AVIMTextMessage createTextMessage(String text, Event event) {
//        Boolean declineFlag = event.getIsDeclined();
//        String pushNotification = SessionManager.getSharedInstance(context).getNickname() + ": " + text;
//
//        Map<String, Object> attrs = new HashMap<String, Object>();
//        attrs.put(LCConstants.MSG_TYPE, MessageManager.MessageType.TextMessage.ordinal());
//        attrs.put(LCConstants.MSG_RECEIVERID, event.getFriendID());
//        attrs.put(LCConstants.MSG_EVENTID, event.getEventID());
//        attrs.put(LCConstants.MSG_DECLINE, declineFlag);
//        attrs.put(LCConstants.MSG_PUSHNOTIFICATION, pushNotification);
//
//        AVIMTextMessage msg = new AVIMTextMessage();
//        msg.setText(text);
//        msg.setAttrs(attrs);
//        return msg;
//    }
//
//    /**
//     *  Create a text message for general chat.
//     *
//     *  - returns: A AVIMTextMessage with text: inputext, attributes: [type: MessageType.PersonMessage.rawValue,
//     *             receiverId: friendId, pushNotification: push notification alert text].
//     */
//    private AVIMTextMessage createTextMessage(String text, String toUser) {
//        String pushNotification = SessionManager.getSharedInstance(context).getNickname() + ": " + text;
//
//        Map<String, Object> attrs = new HashMap<String, Object>();
//        attrs.put(LCConstants.MSG_TYPE, MessageManager.MessageType.PersonMessage.ordinal());
//        attrs.put(LCConstants.MSG_RECEIVERID, toUser);
//        attrs.put(LCConstants.MSG_PUSHNOTIFICATION, pushNotification);
//
//        AVIMTextMessage msg = new AVIMTextMessage();
//        msg.setText(text);
//        msg.setAttrs(attrs);
//        return msg;
//    }
}
