import pifacecad
import os
import signal

# you can ignore these three lines of code
# they are needed so that you can end the
# program by pressing Ctrl+C
def signal_handler(signal, frame):
	os._exit(0)
signal.signal(signal.SIGINT, signal_handler)

def text_of_pin(num):
	if num == 0:
		return "I am your father!"
	if num == 1:
		return "Do yo feel lucky, punk?"
	if num == 2:
		return "Engage"
	if num == 3:
		return "I'll be back!"
	if num == 4:
			return "The name is Bond James Bond!"

# event handler that is called after a button is
# pressed. The event handler is linked to the
# button press by listener.register(...) below
def update_pin_text(event):
	#event.chip.lcd.set_cursor(13, 0)
	event.chip.lcd.clear()
	outputStr = text_of_pin(event.pin_num)

	event.chip.lcd.set_cursor(0, 0)
	if(len(outputStr) <= 16):
		event.chip.lcd.write(str(outputStr))
	else:
		event.chip.lcd.write(str(outputStr[:15]))
		event.chip.lcd.set_cursor(0,1)
		event.chip.lcd.write(str(outputStr[15:]))

cad = pifacecad.PiFaceCAD()
#cad.lcd.write("You pressed: ")
listener = pifacecad.SwitchEventListener(chip=cad)
# the display has eight buttons: 
# five dip switchtes 
# left, right and push for the three-way dip dwitch
for i in range(8):
	listener.register(i, pifacecad.IODIR_FALLING_EDGE, update_pin_text)
listener.activate()


