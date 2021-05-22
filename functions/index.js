const functions = require('firebase-functions');const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.pushNotification = functions.database.ref('/Order for admin').onCreate((change, context) => {
    console.log('Push notification event triggered');

    const payload = {
        notification: {
            title: "New Order Arrived",
            body: "C'mmon be fast",
            sound: "ringtone.mp3"
        }
    };

    //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    return admin.messaging().sendToTopic("pushNotifications", payload, options);
});