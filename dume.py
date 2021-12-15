# from time import sleep
# from picamera import PiCamera
# import requests 
import numpy as np
# import cv2
import FCMManager as fcm
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from sendToAdmin import sendToAdmin 
# import servo
import checkIn
import json
cnt=0
number=''
img_encoded=''
db=firestore.client()
while True:
    cnt+=1
    if cnt>10:
        print(cnt)
        typVehicle='car'
        license1=input()        
        if not checkIn.TrueResult(typVehicle,license1):
                ('ko du ky tu')
                continue
        if number!=license1:
            doc_ref=db.collection(u'LicensePlateNumber').document(license1)
            doc=doc_ref.get()
            if doc.exists:
                print('exist')
                if not checkIn.ParkingCheck(doc_ref,db):
                    print('xe ngoai')
                else:
                    print('di ra khoi lan')
            else:
                sendToAdmin(img_encoded,'kiemtravao')
                print('da gui admin')
                doc_ref=db.collection(u'LicensePlateNumber').document(license1)
                doc=doc_ref.get()
                if doc.exists:
                    print('exist')
                    if not checkIn.ParkingCheck(doc_ref,db):
                        print('xe ngoai')
                    else:
                        print('di ra khoi lan')
                else:
                    print('chua dk acc')
            number=license1
            print(number)
            continue
        else:
            print('lap bien so')
            continue
    else:
        print('ko co xe')


