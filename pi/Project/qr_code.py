from PIL import Image
import zbarlight
import subprocess
import sys
import pifacecad
import time
import os

def checkQR(file_path):
	with open('./image.jpg','rb') as image_file:
		image=Image.open(image_file)
		image.load()
	return zbarlight.scan_codes('qrcode',image)
