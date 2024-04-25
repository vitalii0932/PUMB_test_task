function submitLoginForm() {
    // Check if 'bearer' cookie exists and delete it
    if (document.cookie.includes('bearer')) {
        deleteCookie('bearer');
    }

    // Get form element by ID
    var form = document.getElementById("login_form");

    // Get values of email and password fields
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Create user object with email and password
    const user = {
        email: email,
        password: password
    };

    // Send POST request to authenticate endpoint
    fetch("/api/v1/auth/authenticate", {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            // If response is not ok, display error message
            if (!response.ok) {
                document.getElementById('error-message').style.display = 'block';
            }
            return response.json();
        })
        .then(data => {
            // Get JWT token from response data
            var jwtToken = data.token;

            // If token is null, display error message, else set cookie and redirect
            if (jwtToken == null) {
                document.getElementById('error-message').style.display = 'block';
            } else {
                setCookie("bearer", jwtToken, 1, function() {
                    window.location.href = "/h2-console";
                });
            }
        })
        .catch(() => {
            // If there's an error, display error message
            document.getElementById('error-message').style.display = 'block';
        });
}

// Function to set cookie with given expiration date
function setCookie(cookieName, cookieValue, expirationDays, callback) {
    var d = new Date();
    d.setTime(d.getTime() + (expirationDays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
    callback();
}

// Function to delete cookie
function deleteCookie(cookieName) {
    var d = new Date();
    d.setTime(d.getTime() - (24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=;" + expires + ";path=/";
}

// Function to handle logout
function logout() {
    // Check if 'bearer' cookie exists and delete it
    if (document.cookie.includes('bearer')) {
        deleteCookie('bearer');
    }
    // Redirect to test_task endpoint
    window.location.href = '/api/v1/test_task';
}
