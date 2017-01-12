import urllib.request
import pifacecad
import time
import xml.etree.ElementTree as EL

cad = pifacecad.PiFaceCAD()


# fetch the data
xml = urllib.request.urlopen("http://192.168.0.100:8080/rest/guestbook/").read()
# parse the data and create a XML tree
root = EL.fromstring(xml)
# we can now iterate over the XML tree
# the greetings are the children of root
# the content tag is the 4th (so index 3)
# child of a greeting
for greeting in root:
    cad.lcd.clear()
    cad.lcd.set_cursor(15, 0)
    cad.lcd.write(str(greeting[3].text))
    for x in range (0,16+len(greeting)):
        cad.lcd.curser_off()
        cad.lcd.move_left()
        time.sleep(0.5)