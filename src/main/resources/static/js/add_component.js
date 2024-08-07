$(document).ready(function() {
   // alert($("input").val());




    $("#refresh-btn").on('click',function() {
  var body={}


      $("#form-component").find("input[type='text']").each(function(){
      reqId = $(this).attr('id')
      reqVal = $(this).val()

      if (isNaN($(this).val())) {
         body[$(this).attr('id')] = $(this).val()
        }
        else{
        if ($(this).val() == ""){
           body[$(this).attr('id')] = 0.0
        }
        body[$(this).attr('id')] = parseFloat($(this).val())
        }

      })


       for (c in body){
          console.log(c+" -> "+body[c])
       }


    });
});


