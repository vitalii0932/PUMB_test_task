// Selecting form element and necessary dropdowns
const form = document.querySelector('form');
const filterSelect = document.getElementById("filter");
const sortSelect = document.getElementById("sort");

// Converting options of sortSelect into an array of values
const sortValues = Array.from(sortSelect.options).map(option => option.value);

// Adding event listener for form submit
form.addEventListener('submit', handleSubmit);

// Running updateSelects function when DOM content is loaded
document.addEventListener("DOMContentLoaded", function() {
    updateSelects();
});

// Adding event listener for filterSelect change
filterSelect.addEventListener("change", function() {
    updateSelects();
});

// Function to display file name in the label
function displayFileName(input) {
    const fileName = input.files[0].name;
    document.getElementById('file-label').innerText = fileName;
}

// Function to handle form submission
function handleSubmit(event) {
    event.preventDefault();
    uploadFiles();
}

// Function to upload files via fetch API
function uploadFiles() {
    const url = '/api/v1/test_task/files/uploads';
    const formData = new FormData();
    const file = document.getElementById("file").files[0];

    formData.append("file", file)

    const fetchOptions = {
        method: 'post',
        body: formData
    };

    fetch(url, fetchOptions).then(r => console.log(r.ok));
}

// Function to update options in sortSelect based on selected value in filterSelect
function updateSelects() {
    sortSelect.innerHTML = '';

    const selectedValue = filterSelect.value;

    sortValues.forEach(optionText => {
        console.log(optionText);
        if (optionText.includes(selectedValue.toLowerCase())) {
            const option = document.createElement("option");
            option.text = optionText.substring(optionText.indexOf(".") + 1);
            sortSelect.add(option);
        }
    });
}

// Function to search elements based on selected filter and sort options
function searchElements() {
    const filterType = document.getElementById("filter").value;
    const sortBy = document.getElementById("sort").value;
    const sortLike = document.getElementById("sortLike").value;

    const textArea = document.getElementById("textArea");

    const params = new URLSearchParams({
        filter: filterType,
        sort: sortBy,
        sortLike: sortLike
    });

    const url = "/api/v1/test_task/filter?" + params;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const jsonString = JSON.stringify(data, null, 2);

            textArea.textContent = jsonString;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}
