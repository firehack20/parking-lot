import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import FCMManager as fcm
import base64
tokens=[]
db = firestore.client()
# licensePlateNumber=input()
docs = db.collection(u'Admin').stream()
encoded_string=''
def sendToAdmin(img_encoded,texta):
    encoded_string = base64.b64encode(img_encoded)
    encoded_string=encoded_string.decode('utf-8')
    for doc in docs:
        tokens.clear()
        id=doc.id
        users_ref=db.collection(u'Users').document(id)
        users_ref.update({u'haveNotification':True})
        img_ref=db.collection(u'CurrentImage').document(u'current_image')
        # print('str : '+str(encoded_string))
        img_ref.update({u'value':str(encoded_string)})
        get_token=users_ref.get({u'token'})
        token=u'{}'.format(get_token.to_dict()['token'])
        tokens.append(token)
        fcm.sendPush("Parking Lot", texta, tokens)
    # fcm