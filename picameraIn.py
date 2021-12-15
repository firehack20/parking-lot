from time import sleep
from picamera import PiCamera
import requests 
import numpy as np
import cv2
import FCMManager as fcm
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from sendToAdmin import sendToAdmin 
import servo
import checkIn
import json

camera = PiCamera()
camera.resolution = (640, 480)
content_type = 'image/jpeg'
headers = {'content-type': content_type}
db = firestore.client()
licenseNumber=''
# typeVehicle='car'

def saveIn(doc_ref):
	get_id= doc_ref.get({u'id'})
	id= u'{}'.format(get_id.to_dict()['id'])
	users_ref=db.collection(u'Users').document(id)
	get_name=users_ref.get({u'name'})
	name=u'{}'.format(get_name.to_dict()['name'])
	get_admin=users_ref.get({u'admin'})
	admin=u'{}'.format(get_admin.to_dict()['admin'])
	admin = admin == 'True'
	checkin=True #vao
	data={
		u'id':id,
		u'time':firestore.SERVER_TIMESTAMP,
		u'name':name,
		u'admin':admin,
		u'checkin':checkin
	}
	db.collection(u'TimeInOut').add(data)
	print('saved!')

while True:

	output = np.empty((480, 640, 3), dtype=np.uint8)
	camera.capture(output,'rgb')
	output = cv2.cvtColor(output , cv2.COLOR_BGR2RGB)
	_, img_encoded= cv2.imencode('.jpg',output)
	response=requests.post('http://detectparking.ddns.net/predict/',headers=headers ,data=img_encoded.tobytes())
	print(response.text)
	if len(response.text) > 1:#neu co hoi dap tu server thi thuc hien, k thi bo qua va gui anh tiep	
		typeVehicle, license1 = response.text.split('-')
		license1=input()
		if not checkIn.TrueResult(typeVehicle,license1):
			print('ko du ky tu')
			continue
		elif licenseNumber != license1:#neu nhan kq khac lan gui truoc
			print(license1)
			doc_ref=db.collection(u'LicensePlateNumber').document(license1)
			doc=doc_ref.get()
			if doc.exists:
				print('exist')
				if not checkIn.ParkingCheck(doc_ref,db):
					print('xe ngoai bai')
					saveIn(doc_ref)
					servo.dongmocong()
				else:
					print('luu roi di ra khoi lan')
			else:#nhan dien sai, de admin nhan dien 
				sendToAdmin(img_encoded,'Kiểm tra vào xe')
				license1=requests.get('http://detectparking.ddns.net/selfPredict/').text
				while license1 == 'None':
					license1=requests.get('http://detectparking.ddns.net/selfPredict/').text
					continue
				print(license1)
				dict=license1
				res = json.loads(dict)
				license1=str(res[u'command'])
				print(license1)
				# print('nhap')
				# license1=input()
				doc_ref=db.collection(u'LicensePlateNumber').document(license1)
				doc=doc_ref.get()
				if doc.exists:#true
					if not checkIn.ParkingCheck(doc_ref,db):
						print('xe ngoai bai')
						saveIn(doc_ref)
						servo.dongmocong()
					else:
						print('luu roi di ra khoi lan')
				else:#chua dang ky
					print('Chua dang ky tai khoan')
			licenseNumber=license1
		else:
			print('Trung lap bien so')
	else:
		print('khong co xe')

