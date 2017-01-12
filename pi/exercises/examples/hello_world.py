#
# Example based on http://piface.github.io/pifacecad/example.html
# It will display "Hello World". 
#
import pifacecad

cad = pifacecad.PiFaceCAD()
cad.lcd.backlight_on()
cad.lcd.write("Hello, world!")
