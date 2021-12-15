import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import datetime
import time
cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred)
db = firestore.client()
current=datetime.datetime.utcnow()
maxi_days=19
out_history_date = datetime.datetime.timestamp(current)-2592000.00/30*maxi_days

def check_exist(license):
    licensePlateNumber = license
    # db = firestore.client()
    doc_ref=db.collection(u'LicensePlateNumber').document(licensePlateNumber)
    doc=doc_ref.get()
    if doc.exists:
        get_id= doc_ref.get({u'id'})
        id= u'{}'.format(get_id.to_dict()['id'])
        print("id : "+str(id))
        docs = db.collection(u'TimeInOut').where(u'id', u'==', id).stream()
        for doc1 in docs:
            time= u'{}'.format(doc1.to_dict()['time'])
            # print(time)
            element = datetime.datetime.strptime(time,"%Y-%m-%d %H:%M:%S.%f%z")
            timestamp = datetime.datetime.timestamp(element)
            # print(timestamp)

            if(timestamp<=out_history_date):
                checkin =u'{}'.format(doc1.to_dict()['checkin'])
                print(element)
                if(eval(checkin)):
                    print('true')
                else:
                    print('false')
            else:
                checkin=str('True')
    return eval(checkin)
check_exist('43a2')