$(document).ready(function() {
   // alert($("input").val());




    $("#refresh-btn").on('click',function() {
  var body={}


  $("#form-component").find("input[type='text']").each(function(){
      console.log("****** "+$(this).val()+" "+$(this).attr('id'));
  })






    });
});


