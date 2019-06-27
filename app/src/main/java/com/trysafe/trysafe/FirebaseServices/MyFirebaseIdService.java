package com.trysafe.trysafe.FirebaseServices;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendNewTokenServer(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendNewTokenServer(String token) {
    }
}
