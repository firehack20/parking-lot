import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import FCMManager as fcm
import datetime
def ParkingCheck(doc_ref,db):
    get_id= doc_ref.get({u'id'})
    id= u'{}'.format(get_id.to_dict()['id'])
    print("id : "+str(id))
    col_ref=db.collection(u'TimeInOut').where(u'id',u'==',id).order_by(u'time',direction=firestore.Query.DESCENDING).limit(1)
    doc_list=col_ref.get()
    if len(doc_list)>0:
        for doc in doc_list:
            #checkin=doc.get('checkin')
            checkin= u'{}'.format(doc.to_dict()['checkin'])
            #print('checkin',type(checkin),checkin)
            checkin = checkin == 'True'
            #print(doc.get('time')) #False
            if checkin:
                #print('trong bai')
                return True
            else:
                # print('ngoai bai')
                return False
    else:
        # print('chua do bao h')
        return False
    
def TrueResult(typeVehicle,License1):
    if typeVehicle=='car' and len(License1)==8:
        return True
    if typeVehicle=='motorcycle' and 8<=len(License1)<=9:
        return True
    else:
        return False

