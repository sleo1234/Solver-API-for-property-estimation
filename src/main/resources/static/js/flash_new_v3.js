let compSet = new Set()

$(document).ready(function() {
    // Initialize DataTable
    getComponents()
    var addedComponents = new Set();
    const table = $('#dataTable').DataTable();
        var mapData=[]
        autoComplete(compSet)
        getFlashTPParams()
    // Handle search and add row
    $('#addButton').on('click', function() {
        const name = $("input[name='Search component']").val();

        // Check if name is not empty
        if (name.trim() !== '' && compSet.has(name)) {
            // Add a new row to the DataTable
            const newRow = table.row.add([name, '', '<button class="removeRow">Remove</button>']).draw().node();
            compSet.delete(name)
            autoComplete(compSet)
             $("input[name='Search component']").val('')
            addedComponents.add(name)
            // Add a click event for the "Remove" button
            $(newRow).find('.removeRow').on('click', function() {
                // Get the corresponding row and remove it
                const rowToRemove = $(this).closest('tr');
                const nameToRemove = rowToRemove.find('td:eq(0)').text();
                removeRow(rowToRemove, nameToRemove);
            });

            // Add an input field for Mole frac (make it editable)
            const moleFracInput = $('<input type="text" class="moleFracInput" placeholder="Mole frac">');
            $(newRow).find('td:eq(1)').html(moleFracInput);

            // Clear the search input
            $('#searchInput').val('');

            // Focus on the Mole frac input for editing
            moleFracInput.focus();

            // Handle Mole frac input changes
            moleFracInput.on('blur', function() {
            var indexes=[]

                const moleFracValue = $(this).val();
                // Add the Name and Mole frac values to an array only if the value has changed
                if (moleFracValue !== '') {
                     var chemicals=Array.from(addedComponents)
                      console.log(chemicals)
                    for (var i=1; i<= compSet.size; i++){

                   const rowIndex =this.closest("tr").rowIndex - 1;
                   indexes.push(rowIndex);

                    }

                    addToDataArray(name, moleFracValue);
                    addData(name, moleFracValue)
                }
            });
        }
    });

    const dataArray = [];
    const dataMap = new Map()

    // Data array to store Name and Mole frac values


    // Function to add values to the data array
    function addToDataArray(name, moleFracValue) {


        dataArray.push({ name, moleFracValue });


    }


    function addData(name, moleFracValue){

    dataMap.set(name,moleFracValue)
    }

    // Function to remove a row
    function removeRow(rowToRemove, nameToRemove) {
        // Remove the row from the DataTable
        table.row(rowToRemove).remove().draw();
         compSet.add(nameToRemove)
       //  autoComplete(compSet)
         addedComponents.delete(name)
         autoComplete(compSet)
                // Remove the corresponding data from the dataArray
        const indexToRemove = dataArray.findIndex(item => item.name === nameToRemove);
        filteredArray = dataArray.filter(obj => obj.name !== nameToRemove);
        dataMap.delete(nameToRemove)
        console.log("heree: " + dataArray)
        if (indexToRemove !== -1) {
            //filteredArray.splice(indexToRemove, 1);
             dataArray.splice(indexToRemove, 1);
             console.log("after removal");
             mapData=filterLastMoleFracForUniqueNames(dataArray)
            console.log(filterLastMoleFracForUniqueNames(dataArray)); // Updated dataArray after removal
        }
    }




    function filterLastMoleFracForUniqueNames(dataArray) {
        const uniqueNamesMap = new Map();

        // Iterate through the array to keep track of the last mole fraction value for each name
        for (let i = 0; i < dataArray.length; i++) {
            const obj = dataArray[i];
            const name = obj.name;
            const moleFracValue = obj.moleFracValue;

            // Update the map with the latest mole fraction value for each name
            uniqueNamesMap.set(name, moleFracValue);
           // dataArray = uniqueNamesMap
        }
        return uniqueNamesMap
}





   function getFlashTPParams(){
              $('#calculate').on('click', function(){
               var chemicals=[]
               var compValues=[]
              temp=$("input[name='temperature']").val()
              press=$("input[name='pressure']").val()
             console.log("Temperature: "+temp)
             console.log("Pressure: "+press)

                 filterLastMoleFracForUniqueNames(dataArray)
                 console.log("-0-0 "+table.columns().data()[0]);
                 console.log("-0-0 "+table.columns().data()[1]);

             const iterator1 = mapData[Symbol.iterator]();

              console.log(dataMap)
              dataMap.forEach(function (item, key, mapObj) {
                 chemicals.push(key)
                 compValues.push(item)
              });
             floatValues = compValues.map(c=>parseFloat(c))
               console.log("---------- "+chemicals)
               console.log("---------- "+compValues)
               const sum = floatValues.reduce((c,d)=> c+d)
               console.log("-----Sum "+sum)
               if (sum > 1){
               alert("Sum is greater than 1")
               }
              })

              }

});



















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
