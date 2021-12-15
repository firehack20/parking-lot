import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import FCMManager as fcm
import datetime
licensePlateNumber='433S90031'#'435S45678'
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
            checkin = checkin == 'True'
            print(doc.get('time')) 
            if checkin:
                # print('trong bai')
                return True
                # diff= datetime.datetime.timestamp(datetime.datetime.utcnow())-datetime.datetime.timestamp(doc.get('time'))
                # if diff>=322746:#thoi gian chenh lech lon, co the luu dc
                #     print('luu dc ')
                #     return False
                # else:#thoi gian chenh lech thap, xe van con o loi vao
                #     print('xe con o lan vao')
                #     return True
                
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
# if TrueResult('motorcycle',licensePlateNumber):
#     print('oke')
# db = firestore.client()
# doc_ref=db.collection(u'LicensePlateNumber').document(licensePlateNumber)
# doc=doc_ref.get()
# if doc.exists:
#     if ParkingCheck(doc_ref,db):
#         print('trong bai')
#     else:
#         print('ngoai bai')
