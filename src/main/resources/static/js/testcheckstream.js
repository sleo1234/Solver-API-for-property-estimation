const apiUrl = 'http://localhost:8081/api/v1/validate_componentlist'

data = {"ethane":0.5,"methane":0.5}
// Make a GET request

fetch(apiUrl, requestOptions)
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    outputElement.textContent = JSON.stringify(data, null, 2);
  })
  .catch(error => {
    console.error

('Error:', error);
  });