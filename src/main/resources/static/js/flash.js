$(document).ready(function(){

let components =[];
let selectedComponents=[];

getComponents()

table = $("#myTable");
console.log(components)

selectComponents()

autoComplete(compSet)
}
)



let selected = new Set()
let compSet = new Set()

function getComponents() {

        url = "http://localhost:8081/api/all_components";

       var data = $.ajax({
             type: "GET",
             url: url,
             async: false
             }).done(function (data){});
        let len = data.responseJSON.length;
        components = data.responseJSON.map(components => components);
        compSet = new Set(components)

    return data.responseJSON;
}






        function autoComplete(val){

          if (Object.prototype.toString.call(val) === '[object Set]'){
            $("#names").autocomplete({source: Array.from(val)});
            }

         else {
         $("#names").autocomplete({source: val});
         }
       }

       function selectComponents(){


       $("#addButton").on("click", function(){



        var name = $("input[name='Search component']").val();

          var array = Array.from(selected)
        const iterator = selected.entries();

        addDataToTable("myTable", name)
        selected.add(name)
        compSet.delete(name)

               autoComplete(compSet)
        console.log(selected)

          })
           }

        function getTableData(){

         var table = document.getElementById("myTable");
         console.log(table.length);
          for (var i = 0; i < table.length; i++) {

      console.log(table.rows.item(i).cells)
          }
        }

        function removeElementByName( array, name){
        var result=[];
        for (var i=0; i < array.length; i++){

                if (array[i] == name){
                 array.splice(i,1)
                }
            }
            result=[...array]
            return result
        }

               function addDataToTable(tableId, rowName){
              var counter = $('#' + tableId+ ' >tbody >tr').length;

                table.append('<tr id='+counter+'><td>'+rowName+'</td ><td contenteditable="true"></td><td><span class="table-remove"><button type="button" id ="removeButton'+counter+'" class="btn btn-danger btn-rounded btn-sm my-0"> Remove </button></span></td></tr>');
                $("#removeButton"+counter).on("click", function(){

                console.log("added "+ rowName)
                    document.getElementById(counter).remove();
                    //compSet.add(rowName)
                   // console.log(compSet)
                    autoComplete(compSet)
              selected.delete(rowName)

                         })

        }
