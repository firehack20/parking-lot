#from gpiozero import Servo
from time import sleep
#import pigpio
import RPi.GPIO as GPIO
#servo = Servo(18)
GPIO.cleanup()
GPIO.setmode(GPIO.BOARD)
GPIO.setup(12,GPIO.OUT)
servo=GPIO.PWM(12,50)
servo.start(0)
sleep(2)
def openBarrier():
	print('mo cong')
	servo.ChangeDutyCycle(7)
	sleep(0.5)
	servo.ChangeDutyCycle(0)
	#sleep(1.5)
def closeBarrier():
	print('dong cong')
	servo.ChangeDutyCycle(2)
	sleep(0.5)
	servo.ChangeDutyCycle(0)
	
def dongmocong():
	openBarrier()
	sleep(5)
	closeBarrier()
	sleep(5)		
	servo.stop()
#dongmocong()
