import json
import requests
import time
from pprint import pprint


def startSession():
	payload = {"email": "pi@ase-in.tum.de", "password": "asepi123", "returnSecureToken": "true"}
	url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDW2PaX5m9MwqGDKK-mSLFFfCscbJfF5ek"
	r = requests.post(url, json=payload)
	data = json.loads(r.text)
	return data["idToken"]

piToken = startSession()

def addStudent(studentToken, week, presentation):


	paramToken = studentToken[3:-2]
	paramToken = studentToken.split('.')[1]
	pprint(paramToken)
	urlGetUserTokenForWeek = "https://ase-pi-project.firebaseio.com/user/"+paramToken+"/week/"+str(week)+".json?auth="+str(piToken)
	pprint(urlGetUserTokenForWeek)
	#urlGetUserTokenForWeek = "https://ase-pi-project.firebaseio.com/user/D9IVpZ2ldkU6tuTGXGL5BjIomdJ2/week/1.json?auth="+piToken
	userTokenRequest = requests.get(urlGetUserTokenForWeek)
	userTokenData = json.loads(userTokenRequest.text)
	pprint(userTokenData)
	pprint(userTokenData["token"])
	pprint(studentToken[3:-2])

	if str(userTokenData["token"]) == studentToken[3:-2]:
		if presentation==False:
			userPayload = {"verified_status":"true"}
			urlPatchUser = "https://ase-pi-project.firebaseio.com/user/"+paramToken+"/week/"+str(week)+".json?auth=" + str(piToken)
			userRequest = requests.patch(urlPatchUser, json=userPayload)
			userData = json.loads(userRequest.text)
			if userData["verified_status"]=="true":
				return "Attended"
		else:
			userPayload = {"presented":"true"}
			urlPatchUser = "https://ase-pi-project.firebaseio.com/user/"+paramToken+"/bonusStatus.json?auth=" + str(piToken)
			userRequest = requests.patch(urlPatchUser, json=userPayload)
			userData = json.loads(userRequest.text)
			if userData["presented"]=="true":
				return "Presented"
	else:
		return "Token not correct"
