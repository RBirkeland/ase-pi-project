import pifacecad

cad = pifacecad.PiFaceCAD()

def button_pushed(event):
	event.chip.lcd.clear()
	event.chip.lcd.set_cursor(0, 0)
	event.chip.lcd.write(str("I love you!"))

def button_released(event):
	event.chip.lcd.clear()
	event.chip.lcd.set_cursor(0, 0)
	event.chip.lcd.write(str("Hello World!"))

listener = pifacecad.SwitchEventListener(chip=cad)
listener.register(4, pifacecad.IODIR_FALLING_EDGE, button_pushed)
listener.register(4, pifacecad.IODIR_RISING_EDGE, button_released)
listener.activate()