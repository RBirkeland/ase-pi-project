import json
import requests
from pprint import pprint

payload = {"email":"pi@ase-in.tum.de","password":"asepi123","returnSecureToken": "true"}
url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDW2PaX5m9MwqGDKK-mSLFFfCscbJfF5ek"

r = requests.post(url, json=payload)
data = json.loads(r.text)

pprint(data["idToken"])

urlGetUserTokenForWeek = "https://ase-pi-project.firebaseio.com/user/oXlgffDsWrZREwaKmKpxzBw2aOm2/week/1.json?auth="+data["idToken"]
userTokenRequest = requests.get(urlGetUserTokenForWeek, json=payload)
userTokenData = json.loads(userTokenRequest.text)

pprint(userTokenData["token"])

if str(userTokenData["token"]) == "1.oXlgffDsWrZREwaKmKpxzBw2aOm2.randomToken":
    userAttendedPayload = {"verified_status":"true"}
    urlPatchUser = "https://ase-pi-project.firebaseio.com/user/oXlgffDsWrZREwaKmKpxzBw2aOm2/week/1.json?auth=" + data["idToken"]
    userAttendedRequest = requests.patch(urlPatchUser, json=userAttendedPayload)
    userAttendedData = json.loads(userAttendedRequest.text)
    pprint(userAttendedData["verified_status"])
else:
    pprint("Damn, userToken not correct")

# for presentation it should be a patch request on userid/bonusStatus.json...
# payload = {"presented":"true}