from pyfcm import FCMNotification
import firebase_admin
from firebase_admin import credentials, messaging

cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred)
API_KEY="AAAA1ukjq5M:APA91bFlvrUlX1M2NEGY-PTpfQ6BTDzZcTWyb5nFahkqkJL3hCKM0jlgkbkiobgtCLCcgExM7dmxyxWcMJZ8sApRUzjAygVlUPUABr8doGvwzAH0Nm9s7jTCuC-rsiOQE1f7rtuooxzx"

def sendPush(title, msg, registration_token, dataObject=None):
    # See documentation on defining a message payload.
    message = messaging.MulticastMessage(
        notification=messaging.Notification(
            title=title,
            body=msg
        ),
        data=dataObject,
        tokens=registration_token,
    )

    # Send a message to the device corresponding to the provided
    # registration token.
    response = messaging.send_multicast(message)
    # Response is a message ID string.
    print('Successfully sent message:', response)
def sendNotification(title, msg, registration_token, dataObject=None):
    push_service=FCMNotification(api_key=API_KEY)
    result = push_service.notify_single_device(registration_id=registration_token, message_title=title, message_body=msg)
