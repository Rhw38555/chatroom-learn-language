// signup.js

document.addEventListener("DOMContentLoaded", function() {
    const signupForm = document.getElementById("signupForm");
    signupForm.addEventListener("submit", function(event) {
        event.preventDefault();
        const usernameInput = document.getElementById("username");
        const passwordInput = document.getElementById("password");
        const username = usernameInput.value;
        const password = passwordInput.value;

        // 서버로 회원가입 요청 보내기
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/signup");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = function() {
            if (xhr.status === 201) {
                // 회원가입 성공 시 로그인 페이지로 이동
                window.location.href = "/loginPage";
            } else {
                // 회원가입 실패 시 처리
                alert("회원가입에 실패했습니다.");
            }
        };
        xhr.send(JSON.stringify({ username: username, password: password }));
    });
});