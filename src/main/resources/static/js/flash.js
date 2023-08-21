$(document).ready(function(){

let components =[];

getComponents()
autoComplete()
}
)




function getComponents() {


        url = "http://localhost:8081/api/all_components";

       var data = $.ajax({
             type: "GET",
             url: url,
             async: false
             }).done(function (data){});

//  var names = data.responseJSON;
    //alert(data.responseJSON)

       console.log(data);
        let len = data.responseJSON.length;
        components = data.responseJSON.map(components => components);
      //  alert(components);
    for (i=0; i < len; i++){

    }
    return data.responseJSON;

}






        function autoComplete(){

       var names = components;
         $("#names").autocomplete({source: names});

       }
