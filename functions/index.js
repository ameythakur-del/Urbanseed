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


exports.onItemCreation = functions.firestore.document('Registering users/{userId}')
 .onCreate(async(snapshot, context) => {
     const itemDataSnap = await snapshot.ref.get()
     var fieldheader = "Dear " + itemDataSnap.data().Name + ", <br><br> Welcome to the Urbanseed Family!! We are glad to know that you are showing interest in ordering the fresh and healthy groceries with us. That's why, we have planned a special offer for you. Kindly, Find below attachment to know more about it!"
     return admin.firestore().collection('mail').add({
        to: [itemDataSnap.data().Email],
        message: {
          subject: itemDataSnap.data().Name + ", Here is a Surprise Offer for you.",
          html: fieldheader,
	  attachments: [{   
            filename: 'Surprise Offer.png',
            path: 'https://firebasestorage.googleapis.com/v0/b/saatwik-be896.appspot.com/o/FCMImages%2FSurprise%20Offer.png?alt=media&token=e0d9aa11-1999-4480-b3d0-0dd742fec39a'
        }]
        }
      }).then(() => console.log('Queued email for delivery!'))
	.catch(err => console.log(err));
 });

exports.onItemConfirm = functions.firestore.document('Confirmed Orders/{userId}')
 .onCreate(async(snapshot, context) => {
     const itemDataSnap = await snapshot.ref.get()
     var fieldheader = "Dear " + itemDataSnap.data().name + ", <br><br> Thank you for your shopping with us! Your order is confirmed by our team. Here are the further details.<br><br> Name : " + itemDataSnap.data().name + "<br><br> Delivery Address : " + itemDataSnap.data().address + "<br><br> Estimated Delivery Time : " + itemDataSnap.data().date + ", " + itemDataSnap.data().time  + "<br><br> Mobile Number : " + itemDataSnap.data().mobile
     return admin.firestore().collection('mail').add({
        to: [itemDataSnap.data().email],
        message: {
          subject: itemDataSnap.data().name + ", Your Order is Confirmed",
          html: fieldheader
        }
      }).then(() => console.log('Queued email for delivery!'))
	.catch(err => console.log(err));
 });

exports.onSendInvoice = functions.firestore.document('invoices/{userId}')
 .onCreate(async(snapshot, context) => {
     var fieldheader = "Dear "
     const itemDataSnap = await snapshot.ref.get()
     return admin.firestore().collection('mail').add({
        to: [itemDataSnap.data().email],
        subject: 'New Follower',
        templateId: 'd-33834422b8954619a487dd8ab14938b7'
      }).then(() => console.log('Queued email for delivery!'))
	.catch(err => console.log(err));
 });