$(document).ready(function(){

let components =[];
let selectedComponents=[];

getComponents()
autoComplete()
selectComponents()

}
)




function getComponents() {


        url = "http://localhost:8081/api/all_components";

       var data = $.ajax({
             type: "GET",
             url: url,
             async: false
             }).done(function (data){});
        let len = data.responseJSON.length;
        components = data.responseJSON.map(components => components);
    return data.responseJSON;

}






        function autoComplete(){

       var names = components;
         $("#names").autocomplete({source: names});

       }

       function selectComponents(){

       var selected=[];

       $("#addButton").on("click", function(){

        var name = $("input[name='Search component']").val();
        selected.push(name);

       });
       selectedComponents = [...selected];
        alert(selectedComponents);
        }