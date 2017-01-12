import xml.etree.ElementTree as EL
import sys

document = ""
for line in sys.stdin:
    document += line

root = EL.fromstring(document)

for greeting in root:
    print(greeting[3].text)