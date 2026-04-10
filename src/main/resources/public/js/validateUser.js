const password = document.getElementById("reg-password");
const repeatedPassword = document.getElementById("reg-password-repeat");

function validatePassword(){
    if(password.value !== repeatedPassword.value){
        repeatedPassword.setCustomValidity("Password dosen't match");
    }else {
        repeatedPassword.setCustomValidity('');
    }
}
password.onchange = validatePassword;
repeatedPassword.onkeyup = validatePassword;