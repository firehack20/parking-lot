import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
# import FCMManager as fcm
import datetime
import time

# license='43a1'
def check_exist(license):
    licensePlateNumber = license
    cred = credentials.Certificate("./serviceAccountKey.json")
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    doc_ref=db.collection(u'LicensePlateNumber').document(licensePlateNumber)
    doc=doc_ref.get()
    if doc.exists:
        print('tontai')
check_exist('43K81940')
