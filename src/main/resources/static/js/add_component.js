$(document).ready(function() {
   // alert($("input").val());

        url ="http://localhost:8081/api/v1/save_component"


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
                          contentType: 'application/json;charset=utf-8'
                        }
                    })
                    .done(function(data){
                    alert ("Component " + data["name"]+" added successfully")
                    })

}

