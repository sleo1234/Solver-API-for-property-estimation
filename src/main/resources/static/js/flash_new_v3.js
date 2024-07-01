let compSet = new Set()

$(document).ready(function() {
    // Initialize DataTable
    baseurl ="http://localhost:8081/";

    getBaseUrl()
    getComponents()

    var addedComponents = new Set();
    const table = $('#dataTable').DataTable();

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
                const moleFracValue = $(this).val();
                // Add the Name and Mole frac values to an array only if the value has changed
                if (moleFracValue !== '') {

                    addData(name, moleFracValue)
                }
            });
        }
    });

    const dataMap = new Map()


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
       // const indexToRemove = dataArray.findIndex(item => item.name === nameToRemove);


            dataMap.delete(nameToRemove)

    }

                          var x=[]
                          var y=[]
                          var vaporFrac
                          var bubblePointPressure
   function getFlashTPParams(){
                 temp_value = document.getElementById("temp").value;
                 mole_frac_value = document.getElementById("vapMolefFrac").value;
                 press_value = document.getElementById("press").value;

              $('#calculate').on('click', function(){
               var chemicals=[]
               var compValues=[]

              temp=$("input[name='temperature']").val()
           //   alert("Type of: temp" +(Number(temp))!==NaN)
              press=$("input[name='pressure']").val()
              x=$("input[name='Vapor mole fraction']").val()
             console.log("Temperature: "+temp)
             console.log("Pressure: "+press)
              dataMap.forEach(function (item, key, mapObj) {
                 chemicals.push(key)
                 compValues.push(item)
              });
             floatValues = compValues.map(c=>parseFloat(c))
               console.log("---------- "+Array.from(chemicals))
               console.log("---------- "+Array.from(compValues)+ " "+ compValues instanceof Array)
               const sum = floatValues.reduce((c,d)=> c+d)
               console.log("-----Sum "+sum)
               if (sum > 1){
               alert("Sum of mole fractions is greater than 1")
               }

               if (sum <0.999){
                              alert("Sum of mole fractions is less than 1")
                }
              if ( !isNaN(temp) && !isNaN(press) && x=="" ){
               flashTP(temp,press,Array.from(chemicals),Array.from(floatValues))
              // alert("vapour fraction: "+vaporFrac)
                $("#vapMolefFrac").val(truncateDecimals(vaporFrac,2))
              }

             if ( !isNaN(temp) && !isNaN(x) && press==""){
                        console.log("x values: "+ x);
                       flashTX(temp,x,Array.from(chemicals),Array.from(floatValues))
                        $("#press").val(truncateDecimals(bubblePointPressure,2))

             }
                    if ((temp_value !=="") && (press_value !=="") && (mole_frac_value !== "")){
                    console.log("temp: "+temp_value)
                    console.log("press: "+press_value)
                    console.log("mole frac: "+mole_frac_value)

                    alert("Enter only two values out of three")

                    }

              }

              )}


              function flashTP(t,p,chemicalList, moleFractionValue){


               postData = {
                 "t": t,
                 "p": p,
                 "names": chemicalList,
                 "xmol": moleFractionValue
                      }
                       console.log("post data: "+JSON.stringify(postData))
               url=baseurl+"api/flash_tp"

                $.ajax({
                method:"POST",
                url: url,
                data: JSON.stringify(postData),
                contentType:"application/json",
                async: false,
               headers: {
                     Accept: 'application/json;charset=utf-8',
                     contentType: 'application/json;charset=utf-8'
                   }
               }).done(function(data){
               console.log(data.x)


                 vaporFrac=(data.vapFrac)
                 x=data.x
                 y=data.y

               })
               }


               function flashTX(t,x,chemicalList,moleFractionValue){
               //x - the desired mole fraction of the vapour
               //moleFractionValue - initial mole fraction composition of the mixture

                postData = {
                                "t": t,
                                "x": x,
                                "names": chemicalList,
                                "xmol": moleFractionValue
                                     }
                 url=baseurl+"api/flash_tx"
                 $.ajax({
                                method:"POST",
                                url: url,
                                data: JSON.stringify(postData),
                                contentType:"application/json",
                                async: false,
                               headers: {
                                     Accept: 'application/json;charset=utf-8',
                                     contentType: 'application/json;charset=utf-8'
                                   }
                               }).done(function(data){
                               bubblePointPressure=data.bubblePoint
                               console.log(data.bubblePoint)

               })

               }

});

function getComponents() {

        url = baseurl+"api/all_components";

       var data = $.ajax({
             type: "GET",
             url: url,
             async: false,
             headers: {
             Accept: 'application/json;charset=utf-8',
             contentType: 'application/json;charset=utf-8',
             'Access-Control-Allow-Origin': getBaseUrl(),
             'Access-Control-Request-Headers': 'x-requested-with'

             }
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

function getBaseUrl(){
     burl=window.location.href
   if (burl.includes("ngrok")){
    baseurl=burl
    console.log("Base URL: "+baseurl)
   }
  return baseurl

}

function truncateDecimals(number,digits) {
  var multiplier = Math.pow(10, digits),
         adjustedNum = number * multiplier,
         truncatedNum = Math[adjustedNum < 0 ? 'ceil' : 'floor'](adjustedNum);

     return truncatedNum / multiplier;
}
