

 var removedindex='';
let compSet = new Set()
let chemicals=[];

$(document).ready(function() {
    // Initialize DataTable
    var messsage ="";
    getComponents()
    var addedComponents = new Set();
    var moleFrac =[];
      var valuesArray=[];
     var sum=0.0;
     var temp;
     var press;

    const table = $('#dataTable').DataTable();
      //
     autoComplete(compSet)
  var index =1;
$('#addButton').on('click', function() {
        const name = $("input[name='Search component']").val();
       var newRow ='';

        // Check if name is not empty



             if (compSet.has(name)){
            // Add a new row to the DataTable
            newRow = table.row.add([index,name, '', '<td><span class="removeRow"><button type="button" id ="removeButton" class="btn btn-danger btn-rounded btn-sm my-0" onblur=deleteElementAtIndexInAllArrays(valuesArray,this-1)> Remove </button></span></td>']).draw().node();
            compSet.delete(name)
            addedComponents.add(name);
            index++;
            console.log(addedComponents)
              autoComplete(compSet)
             }

              else if (addedComponents.has(name)){
              message ="Component already added"
              alert(message);
              }
             else  {
                       message ="Component not found in the database/not a valid component"
                        $("input[name='Search component']").val('')
                       alert(message)
                       }
                console.log("here")
                //  getDataFromTable(table)
            // Add a click event for the "Remove" button
            $(newRow).find('.removeRow').on('click', function() {
            $("input[name='Search component']").val('')

                // Get the corresponding row and remove it
                const rowToRemove = $(this).closest('tr');
               ;


                const nameToRemove = rowToRemove.find('td:eq(1)').text();
                removedindex=parseInt(rowToRemove.find('td:eq(0)').text())-1;
                console.log(rowToRemove.find('td:eq(0)'))
                console.log("index to emove: " +removedindex)
                removeRow(rowToRemove, nameToRemove);

            });

            // Add an input field for Mole frac (make it editable)
            const moleFracInput = $('<input type="number" data-prev-value="" class="moleFracInput" placeholder="Enter a value less than 1" onchange=updateValue(this)>');

            $(newRow).find('td:eq(2)').html(moleFracInput);

            // Clear the search input
            $("input[name='Search component']").val('')

            // Focus on the Mole frac input for editing
            moleFracInput.focus();

            // Handle Mole frac input changes
            moleFracInput.on('change',function() {

                const moleFracValue = $(this).val();
               // checkFracValue($(this))
               const closestName = $(this).closest('tr')
               const currentIndex = closestName.find('td:eq(0)').text();
               console.log("current index: " + currentIndex)
                const addedName = closestName.find('td:eq(1)').text();
                const addedMoleFrac = closestName.find('td:eq(2)').val();
                console.log("current name: " + addedName)

                moleFrac.push(moleFracValue)

                console.log("----------")
                 console.log(moleFrac)


            });



    });

    const dataArray = [];


    // Function to add values to the data array
    function addToDataArray(name, moleFracValue) {
      console.log(addedComponents)
        // if (!addedComponents.has(name)){
        dataArray.push({ name, moleFracValue });
        //}
        console.log(dataArray); // You can replace this with your desired action
    }

    // Function to remove a row
    function removeRow(rowToRemove, nameToRemove) {
        // Remove the row from the DataTable
        table.row(rowToRemove).remove().draw();
        compSet.add(nameToRemove)
        addedComponents.delete(nameToRemove);
         autoComplete(compSet)
       deleteElementAtIndexInAllArrays(valuesArray, rowToRemove-1)
        // Remove the corresponding data from the dataArray
        const indexToRemove = dataArray.findIndex(item => item.name === nameToRemove);
        if (indexToRemove !== -1) {
            dataArray.splice(indexToRemove, 1);
            console.log(dataArray); // Updated dataArray after removal
        }
         updateTableData()
    }

 function getFlashTPParams(){
            $('#calculate').on('click', function(){

            temp=$("input[name='temperature']").val()
            press=$("input[name='pressure']").val()
           console.log("Temperature: "+temp)
           console.log("Pressure: "+press)

           console.log(table.data())


            })

            }
            getFlashTPParams()
})

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

function pushValue(value){
   var arr=[];

   return arr.push([value])
}
            var indexes=[];
            var frac=[];

      function deleteElementAtIndexInAllArrays(arrOfArrays, indexToDelete) {
            if (indexToDelete < 0) {
              throw new Error('Index must be a non-negative number');
            }

            for (let i = 0; i < arrOfArrays.length; i++) {
              const innerArray = arrOfArrays[i];
              if (indexToDelete < innerArray.length) {
                innerArray.splice(indexToDelete, 1);
              }
            }

            console.log("Removed row with index: " + removedindex);
          }

      function updateValue(input){

            var sumOfMoleFrac=0.0;
              const rowIndex =  input.closest("tr").rowIndex - 1
                 let names=[];

                const newValue = input.value;
               // checkFracValue(newValue)
                //indexes.push(rowIndex)
                const prevValue = input.getAttribute("data-prev-value");
                 if (prevValue>1 || newValue>1){

                   message="mole fraction greater than 1.Enter a value less than 1"
                              alert(message)
                              input.setAttribute("data-prev-value",'')
                 }
                indexes.push(rowIndex)

                 console.log("Old Value at row: " + rowIndex +" is"+prevValue)
                 console.log("new Value at row: " + rowIndex +" is"+newValue)
                 //moleFrac.push(newValue)



             if (newValue !== prevValue) {
                              // Value has changed, update the array
                              input.setAttribute("data-prev-value", newValue);
                              //const rowIndex = input.closest("tr").rowIndex - 1;
                               const rowIndex =input.closest("tr").rowIndex - 1;


                              // Ensure the array has the same length as the number of rows
                              //while (valuesArray.length <= rowIndex) {

                             // }

                             // valuesArray[rowIndex] = newValue;
                          }

                console.log("----------01");
                       frac.push(input.value)
                       console.log("----from here: " + removedindex);
                       if (removedindex.length >0){
                       console.log("----from here: " + removedindex);
                       }
                       valuesArray=filterUniqueIndexes(indexes,frac);
                       for (var i=0; i<valuesArray[1].length; i++){
                         sumOfMoleFrac = sumOfMoleFrac + parseFloat(valuesArray[1][i])
                       }
                       console.log("Sum is: " +sumOfMoleFrac)
                      // names=Array.from(addedComponents)
                       var table = $('#dataTable').DataTable();

                      console.log( table.column(1).data())
                       //  getFlashTPParams()
                        var closestName = input.closest("tr")
                           console.log("----"+chemicals);
                       console.log(filterUniqueIndexes(indexes,frac));
                }

      function filterUniqueIndexes(indexes, values) {
        const uniqueElements = {}; // Object to store the last index of each unique element
        const filteredIndexes = [];
        const filteredValues = [];

        for (let i = 0; i < indexes.length; i++) {
          const idx = indexes[i];
          const val = values[i];

          // Check if the element is already in the object
          if (uniqueElements.hasOwnProperty(idx)) {
            // If it is, update the last index
            uniqueElements[idx] = i;
          } else {
            // If it's not, add it to the object
            uniqueElements[idx] = i;
            filteredIndexes.push(idx);
            filteredValues.push(val);
          }
        }

        // Use the filtered indexes to get the corresponding values
        const filteredValueArray = filteredIndexes.map((idx) => values[uniqueElements[idx]]);

        return [filteredIndexes, filteredValueArray];
      }


      function updateTableData(){

      //names
      var table = $('#dataTable').DataTable();
      console.log("names: " +table.column(1).data()[0])
      console.log("mole frac: " +table.column(2).data()[0])

      }



