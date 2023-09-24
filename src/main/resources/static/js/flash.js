$(document).ready(function(){

let components =[];
let selectedComponents=[];

getComponents()

//

console.log(components)

selectComponents()

autoComplete(compSet)
getTableData()
})

let selected = new Set()
let compSet = new Set()
jsTable =new DataTable("#myTable")
let moleFrac = new Set();


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
        var name = $("input[name='Search component']").text();
       addDataToTable("myTable", name)
       addNewRow(name)
       //getTableData()
       console.log(moleFrac)
       $("input[name='Search component']").val('')
        compSet.delete(name)
        autoComplete(compSet)
        })
       console.log(moleFrac)
       }



        function getTableData(){

          jsTable =new DataTable("#myTable")
             console.log(jsTable)
            jsTable.each(function(){
            moleFrac.add(self.find("td:eq(1)").text())
            console.log("-------")
            console.log(self.find("td:eq(1)").text())
             console.log("-------")
            })

}



               function addDataToTable(tableId, rowName){
              var counter = $('#' + tableId+ ' >tbody >tr').length;

                table.append('<tr id='+counter+'><td>'+rowName+'</td ><td id="moleFrac"'+counter+' contenteditable="true"></td><td><span class="table-remove"><button type="button" id ="removeButton'+counter+'" class="btn btn-danger btn-rounded btn-sm my-0"> Remove </button></span></td></tr>');
                console.log(table)


                //let jsTable = new DataTable("#myTable");
                 //moleFrac.add(table.DataTable().rows(counter))
                $("#removeButton"+counter).on("click", function(){
                    document.getElementById(counter).remove();
                    compSet.add(rowName)
                    autoComplete(compSet)

                    selected.delete(rowName)
                         })
              }



              function addNewRow(compName) {
              jsTable.row.add([
                  name
              ]).draw(false)
              }




