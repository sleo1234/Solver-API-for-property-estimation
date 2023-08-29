$(document).ready(function(){

let components =[];
let selectedComponents=[];


//getComponents()
//autoComplete()

$("#names").on("click", function(){
getComponents()
autoComplete()

})
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
       table = $("#myTable");
        var name = $("input[name='Search component']").val();

        selected.push(name);




       var counter = $('#myTable >tbody >tr').length;
        table.append('<tr id='+counter+'><td>'+name+'</td ><td contenteditable="true"></td><td><span class="table-remove"><button type="button" id ="removeButton'+counter+'" class="btn btn-danger btn-rounded btn-sm my-0"> Remove </button></span></td></tr>');


          console.log(counter);

      $("#removeButton"+counter).on("click", function(){

                          document.getElementById(counter).remove();

                 })

       });
       selectedComponents = [...selected];
        //alert(selectedComponents);
        }

