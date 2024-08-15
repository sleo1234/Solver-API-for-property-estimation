$(document).ready(function() {
   baseurl ="http://localhost:8081/";

       getBaseUrl()

       function getBaseUrl(){
            burl=window.location.href
          if (burl.includes("ngrok")){
           baseurl=burl
           console.log("Base URL: "+baseurl)
          }
          if (burl.includes("app.thermo.lol")){
          baseurl = burl
           console.log("Base URL 2nd if: "+baseurl)
          }
         return baseurl

       }
         })