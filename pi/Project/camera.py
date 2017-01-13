from PIL import Image
import zbarlight
import subprocess
import sys
#import pyqrcode
#import cv2
import pifacecad

cad=pifacecad.PiFaceCAD()
listener=pifacecad.SwitchEventListener(chip=cad)

if cad.switches[4].value == 1:
	subprocess.call("fswebcam -r 640x480  image.jpg",shell=True)
	cad.lcd.set_cursor(0,0)
	cad.lcd.write("Taking Photo")
else: 
	cad.lcd.set_cursor(0,0)
	cad.lcd.write("Fertig")
	
file_path = './image.jpg'
with open(file_path,'rb') as image_file:
	image=Image.open(image_file)
	image.load()
codes = zbarlight.scan_codes('qrcode',image)
print('QR codes: %s'%codes)
#if d.decode('image.jpg'):
	#cad.lcd.write('result: '+d.result)
#	print("result"+d.result)
	#d.result
#else:
	#cad.lcd.write('error' + d.error)
#	print('error: '+d.error)
