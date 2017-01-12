import urllib.request
import sys

# fetch the data
sys.stdout.buffer.write(urllib.request.urlopen("http://192.168.0.100:8080/rest/guestbook/").read())