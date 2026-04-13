const password = document.getElementById("reg-password");
const repeatedPassword = document.getElementById("reg-password-repeat");
const form = document.getElementById("registerForm");

console.log("validateUser.js loaded");
function validatePassword() {
    if (password.value !== repeatedPassword.value) {
        repeatedPassword.setCustomValidity("Password dosen't match");
    } else {
        repeatedPassword.setCustomValidity("");
    }

}

password.addEventListener("input",validatePassword);
repeatedPassword.addEventListener("input",validatePassword);

form.addEventListener("submit", function (e){
    validatePassword();

    if(!form.checkValidity()){
        e.preventDefault();
        form.reportValidity();
    }
})

