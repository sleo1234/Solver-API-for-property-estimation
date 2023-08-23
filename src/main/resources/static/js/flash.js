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
       var counter =0;
       $("#addButton").on("click", function(){
       table = $("#myTable");
        var name = $("input[name='Search component']").val();
        selected.push(name);
        counter++;
        table.append('<tr id="row"' +string.valueOf(counter)+'><td>'+name+'</td ><td contenteditable="true"></td><td><span class="table-remove"><button type="button" id ="removeButton" class="btn btn-danger btn-rounded btn-sm my-0"> Remove </button></span></td></tr>');


      $("#removeButton").on("click", function(){

                          document.getElementById("row"+string.valueOf(counter)).remove();
                              console.log(document.getElementById("myTable"));
                 })

       });
       selectedComponents = [...selected];
        //alert(selectedComponents);
        }

