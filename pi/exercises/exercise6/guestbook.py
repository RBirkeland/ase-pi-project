import urllib.request
import xml.etree.ElementTree as EL
import pifacecad
import time
import os


def display_greeting(greeting):
    global cad
    print("Displaying")
    print(greeting)
    # truncate /too/ long greetings
    if len(greeting) > 32:
        greeting = greeting[:32]
    cad.lcd.clear()
    cad.lcd.write(greeting)
    for c in greeting:
        cad.lcd.move_left()
        time.sleep(0.5)

# Note: because display_greeting is called
# from the event handler it will block until display_greeting
# has returned. Thus it is not possible to "confuse" this script
# by pressing the button multiple times
def next_greeting(event):
    global root
    global cur_greeting
    display_greeting(root[cur_greeting][3].text)
    cur_greeting += 1
    if cur_greeting >= len(root):
        cad.lcd.clear()
        cad.lcd.write("End of Guestbook!")
        os._exit(0)

cad = pifacecad.PiFaceCAD()
cad.lcd.backlight_on()

# fetch the data
def get_data():
    print("get data")
    return urllib.request.urlopen("http://192.168.0.100:8080/rest/guestbook/").read()

def parse_data():
    print("parse data")
    # parse the data and create a XML tree
    return EL.fromstring(get_data())

cur_greeting = 0
root = parse_data()

listener = pifacecad.SwitchEventListener(chip=cad)
listener.register(4, pifacecad.IODIR_FALLING_EDGE, next_greeting)
listener.activate()