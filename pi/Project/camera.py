from PIL import Image
import zbarlight
import subprocess
import sys
import pifacecad
import time
import os

def take_photo():
	subprocess.call("fswebcam -r 640x480  image.jpg",shell=True)
	return = './image.jpg'