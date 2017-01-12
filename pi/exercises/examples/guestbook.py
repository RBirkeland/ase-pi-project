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
    cad.lcd.set_cursor(0, 0)
    cad.lcd.write(str(greeting[3].text))
    time.sleep(1)