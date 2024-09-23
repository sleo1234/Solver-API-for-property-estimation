$(document).ready(function() {
   // alert($("input").val());
        baseurl ="http://localhost:8081";
           getBaseUrl()
        url =baseurl+"/api/v1/save_component"


    $("#refresh-btn").on('click',function() {
  var body={}


      $("#form-component").find("input[type='text']").each(function(){
      reqId = $(this).attr('id')
      reqVal = $(this).val()

      if (isNaN(reqVal)) {
         body[$(this).attr('id')] = reqVal
        }
        else{
        if (reqVal== ""){
           body[$(this).attr('id')] = 0.0
        }
        body[$(this).attr('id')] = parseFloat(reqVal)
        }

      })


        addComponent(url, body)

    });





});


function addComponent(url, body){
             $.ajax({
                     method:"POST",
                     url: url,
                     data: JSON.stringify(body),
                     contentType:"application/json",
                     async: false,
                    headers: {
                          Accept: 'application/json;charset=utf-8',
                          contentType: 'application/json;charset=utf-8',
                         'Access-Control-Allow-Origin': getBaseUrl(),
                        }
                    })
                    .done(function(data){
                    alert ("Component " + data["name"]+" added successfully")
                    })

}

 function getBaseUrl(){
            domain=window.location.href.split("/")
            burl=domain[0]+"//"+domain[1]+domain[2]
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

