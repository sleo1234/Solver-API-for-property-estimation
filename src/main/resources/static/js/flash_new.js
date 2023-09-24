
let compSet = new Set()


$(document).ready(function() {
    // Initialize DataTable

    getComponents()


    const table = $('#dataTable').DataTable();

     autoComplete(compSet)

$('#addButton').on('click', function() {
        const name = $("input[name='Search component']").val();

        // Check if name is not empty


            // Add a new row to the DataTable
            const newRow = table.row.add([name, '', '<td><span class="removeRow"><button type="button" id ="removeButton" class="btn btn-danger btn-rounded btn-sm my-0"> Remove </button></span></td>']).draw().node();
            compSet.delete(name)
             console.log(compSet)
              autoComplete(compSet)

            // Add a click event for the "Remove" button
            $(newRow).find('.removeRow').on('click', function() {
            $("input[name='Search component']").val('')
                // Get the corresponding row and remove it
                const rowToRemove = $(this).closest('tr');
                const nameToRemove = rowToRemove.find('td:eq(0)').text();
                removeRow(rowToRemove, nameToRemove);
            });

            // Add an input field for Mole frac (make it editable)
            const moleFracInput = $('<input type="text" class="moleFracInput" placeholder="Mole frac">');
            $(newRow).find('td:eq(1)').html(moleFracInput);

            // Clear the search input
            $("input[name='Search component']").val('')

            // Focus on the Mole frac input for editing
            moleFracInput.focus();

            // Handle Mole frac input changes
            moleFracInput.on('blur', function() {
                const moleFracValue = $(this).val();

                if (moleFracValue != 0){
                // Add the Name and Mole frac values to an array
                addToDataArray(name, moleFracValue);
                }
            });

    });
    // Data array to store Name and Mole frac values
    const dataArray = [];

    // Function to add values to the data array
    function addToDataArray(name, moleFracValue) {
        dataArray.push({ name, moleFracValue });
        console.log(dataArray); // You can replace this with your desired action
    }

    // Function to remove a row
    function removeRow(rowToRemove, nameToRemove) {
        // Remove the row from the DataTable
        table.row(rowToRemove).remove().draw();
        compSet.add(nameToRemove)
         autoComplete(compSet)
        // Remove the corresponding data from the dataArray
        const indexToRemove = dataArray.findIndex(item => item.name === nameToRemove);
        if (indexToRemove !== -1) {
            dataArray.splice(indexToRemove, 1);
            console.log(dataArray); // Updated dataArray after removal
        }
    }
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


