# Ausführbar mit
# chmod777 install-requirements.sh
# ./install-requirements.sh

echo "Installing requirements"
sudo apt-get install fswebcam libzbar0 libzbar-dev
echo "pip3 python library"
wget http://bootstrap.pypa.io/get-pip.py
sudo pip3 install zbarlight --upgrade
sudo pip3 install requests
echo "removing not neccesarry stuff"
rm get-pip.py
