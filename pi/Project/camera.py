from PIL import Image
import zbarlight
import subprocess
import sys
#import pyqrcode
#import cv2
import pifacecad
import time
import os

def checkQR(event):
	subprocess.call("fswebcam -r 640x480  image.jpg",shell=True)
	cad.lcd.set_cursor(0,0)
	cad.lcd.write("Taking Photo")
	file_path = './image.jpg'
	with open(file_path,'rb') as image_file:
		image=Image.open(image_file)
		image.load()
	
	codes = zbarlight.scan_codes('qrcode',image)
	# remove double quotes
	print('QR codes: %s'%codes)
	cad.lcd.clear()
	cad.lcd.set_cursor(0,0)
	cad.lcd.write(str(codes))

cad=pifacecad.PiFaceCAD()
listener=pifacecad.SwitchEventListener(chip=cad)
listener.register(4, pifacecad.IODIR_FALLING_EDGE, checkQR)
listener.activate()


#if d.decode('image.jpg'):
	#cad.lcd.write('result: '+d.result)
#	print("result"+d.result)
	#d.result
#else:
	#cad.lcd.write('error' + d.error)
#	print('error: '+d.error)
