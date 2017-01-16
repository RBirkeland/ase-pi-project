
import pifacecad

def display(text,cad):
	cad.lcd.clear()
	cad.lcd.set_cursor(0,0)
	cad.lcd.write(text)