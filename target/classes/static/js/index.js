const form = document.querySelector('form');
const filterSelect = document.getElementById("filter");
const sortSelect = document.getElementById("sort");
const sortValues = Array.from(sortSelect.options).map(option => option.value);
form.addEventListener('submit', handleSubmit);

document.addEventListener("DOMContentLoaded", function() {
    updateSelects();
});

filterSelect.addEventListener("change", function() {
    updateSelects();
});

function displayFileName(input) {
    const fileName = input.files[0].name;
    document.getElementById('file-label').innerText = fileName;
}

function handleSubmit(event) {
    event.preventDefault();

    uploadFiles();
}

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