document.getElementById('robotForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(this);
    fetch(this.action, {
        method: 'POST',
        body: formData
    }).then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('Data submitted successfully');
        fetchAllData(); 
    }).catch(error => {
        console.error('Error:', error);
    });
});



document.getElementById('fetchDataBtn').addEventListener('click', function() {
    fetch('http://172.20.10.7:8080/rest/team19/latestData')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const dataDiv = document.getElementById('data');
            dataDiv.innerHTML = `
                <p>Left Motor: ${data.leftMotor}</p>
                <p>Right Motor: ${data.rightMotor}</p>
                <p>Security Distance: ${data.securityDistance}</p>
                <p>Line Color: ${data.lineColor}</p>
            `;
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            document.getElementById('data').innerHTML = `
                <p>Error fetching data: ${error.message}</p>
            `;
        });
});