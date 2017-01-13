import subprocess
import sys
import pyqrcode
#import qrcode
#import cv2
#import pifacecad

subprocess.call("fswebcam -r 640x480  image.jpg",shell=True)
#d=qrcode.Decoder()
reader = zxing.BarCodeReader("project/ZXing/zxing")
if d.decode('image.jpg'):
	#cad.lcd.write('result: '+d.result)
	print("result"+d.result)
	#d.result
else:
	#cad.lcd.write('error' + d.error)
	print('error: '+d.error)
