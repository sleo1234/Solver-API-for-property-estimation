#!/bin/bash
#/snap/bin/ngrok http $1
baseurl=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels'[0] | jq -r '.public_url')
if [[ $baseurl == "" ]]; then
 read -p "Nothing runs on ngrok. Do you want to run (Y/N) ? " userinput
   if [[ $userinput == "Y" ]]; then
     read -p "Enter url: " localurl
     /snap/bin/ngrok http $localurl
     baseurl=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels'[0] | jq -r '.public_url')
       
   fi
fi
echo "running on"
echo $baseurl

 


