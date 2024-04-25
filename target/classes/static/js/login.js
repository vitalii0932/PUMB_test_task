function submitLoginForm() {
    if (document.cookie.includes('bearer')) {
        deleteCookie('bearer');
    }

    var form = document.getElementById("login_form");

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        email: email,
        password: password
    };

    fetch("/api/v1/auth/authenticate", {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                document.getElementById('error-message').style.display = 'block';
            }
            return response.json();
        })
        .then(data => {
            var jwtToken = data.token;

            if (jwtToken == null) {
                document.getElementById('error-message').style.display = 'block';
            } else {
                setCookie("bearer", jwtToken, 1, function() {
                    window.location.href = "/h2-console";
                });
            }
        })
        .catch(() => {
            document.getElementById('error-message').style.display = 'block';
        });
}

function setCookie(cookieName, cookieValue, expirationDays, callback) {
    var d = new Date();
    d.setTime(d.getTime() + (expirationDays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
    callback();
}

function deleteCookie(cookieName) {
    var d = new Date();
    d.setTime(d.getTime() - (24 * 60 * 60 * 1000)); // Устанавливаем прошедший день
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=;" + expires + ";path=/"; // Устанавливаем куки с истекшим сроком годности
}


function logout() {
    if (document.cookie.includes('bearer')) {
        deleteCookie('bearer');
    }
    window.location.href = '/api/v1/test_task'
}