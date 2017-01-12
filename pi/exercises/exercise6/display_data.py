import pifacecad
import time
import sys

for greeting in sys.stdin:
    global cad
    if len(greeting) > 32:
        greeting = greeting[:32]
    cad.lcd.clear()
    cad.lcd.write(greeting)
    for c in greeting:
        cad.lcd.move_left()
        time.sleep(0.5)

cad = pifacecad.PiFaceCAD()
cad.lcd.backlight_on()