from PIL import Image
import zbarlight
import subprocess
import sys
import pifacecad
import time
import os

import camera as Cam
import database as Data
import qr_code as QR
import display as Dis

presentation=False
week=1

def checkQR(event):
	Dis.display("Taking Photo",cad)
	file_path = Cam.take_photo()
	print(file_path)
	Dis.display("checking QR code",cad)
	code = QR.checkQR(file_path)
	Dis.display(str(code),cad)
	if(code==None):
		Dis.display("try again !",cad)
	else:
		Dis.display(Data.addStudent(code, week, presentation),cad)
		
def changeWeek(event):
	global week
	if(week<14):
		week=week+1
	else:
		week=1
	showStatus()

def changeMode(event):
	global presentation
	if(presentation==True):
		presentation=False
	else:
		presentation=True
	showStatus()

def showStatus():
	if(presentation==True):
		Dis.display("Week: "+str(week)+"\nMode: Presentation",cad)
	else:
		Dis.display("Week: "+str(week)+"\nMode: Attendance",cad)
	

cad=pifacecad.PiFaceCAD()
showStatus()
Data.startSession
listener=pifacecad.SwitchEventListener(chip=cad)
listener.register(4, pifacecad.IODIR_FALLING_EDGE, checkQR)
listener.register(0, pifacecad.IODIR_FALLING_EDGE, changeWeek)
listener.register(1, pifacecad.IODIR_FALLING_EDGE, changeMode)
listener.activate()
