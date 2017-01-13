echo "Installing requirements"
sudo apt-get install fswebcam libzbar0 libzbar-dev
echo "pip3 python library"
wget https://bootstrap.pypa.io/get-pip.py
sudo pip3 install zbarlight --upgrade
echo "removing not neccesarry stuff"
rm get-pip.py
