document.getElementById('robotForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission
    const formData = new FormData(this);
    fetch(this.action, {
        method: 'POST',
        body: formData
    }).then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('Data submitted successfully');
        fetchAllData(); // Optionally refresh data statistics after submission
    }).catch(error => {
        console.error('Error:', error);
    });
});

function fetchAllData() {
    console.log('Fetching data...');
    fetch('/rest/team19/readall')
    .then(response => response.json())
    .then(data => {
        const html = data.map(item => `<div>${item.leftMotor}, ${item.rightMotor}, ${item.securityDistance}, ${item.lineColor}</div>`).join('');
        document.getElementById('dataDisplay').innerHTML = html;
    }).catch(error => {
        console.error('Error fetching data:', error);
    });
}

function sendCommand(command) {
    console.log('Sending command:', command);
    // Implement command sending logic here
}
