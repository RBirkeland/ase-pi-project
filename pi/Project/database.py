import json
import requests
from pprint import pprint

payload = {"email":"pi@ase-in.tum.de","password":"asepi123","returnSecureToken": "true"}
piToken = NULL

def: startSession():
	url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDW2PaX5m9MwqGDKK-mSLFFfCscbJfF5ek"
	r = requests.post(url, json=payload)
	data = json.loads(r.text)
	piToken = data["idToken"]
	
def: addStudent(studentToken, week, presentation):
	
	urlGetUserTokenForWeek = "https://ase-pi-project.firebaseio.com/user/"+studentToken+"/week/"+week+".json?auth="+piToken
	userTokenRequest = requests.get(urlGetUserTokenForWeek, json=payload)
	userTokenData = json.loads(userTokenRequest.text)

	pprint(userTokenData["token"])

	if str(userTokenData["token"]) == week+"."+studentToken+".randomToken":
		if presentation=false:
			userPayload = {"verified_status":"true"}
			urlPatchUser = "https://ase-pi-project.firebaseio.com/user/"+studentToken+"/week/"+week+".json?auth=" + piToken
			userRequest = requests.patch(urlPatchUser, json=userPayload)
			userData = json.loads(userRequest.text)
			if(userData["verified_status"]=="true":
				return "Attended"
		else:
			userPayload = {"presented":"true"}
			urlPatchUser = "https://ase-pi-project.firebaseio.com/user/"+studentToken+"/bonusStatus.json?auth=" + piToken
			userRequest = requests.patch(urlPatchUser, json=userPayload)
			userData = json.loads(userRequest.text)
			if(userData["presented"]=="true"
				return "Presented"
	else:
		return "Token not correct"
