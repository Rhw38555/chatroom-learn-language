document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");
    loginForm.addEventListener("submit", function(event) {
        event.preventDefault();
        const usernameInput = document.getElementById("username");
        const passwordInput = document.getElementById("password");
        const username = usernameInput.value;
        const password = passwordInput.value;

        // 서버로 로그인 요청 보내기
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/login");
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onload = function() {
            if (xhr.status === 200) {
                // 로그인 성공 시 리스트 페이지로 이동
                window.location.href = "/list";
            } else {
                // 로그인 실패 시 처리
                alert("로그인에 실패했습니다.");
            }
        };
        xhr.send(JSON.stringify({ username: username, password: password }));
        // xhr.send({ username: username, password: password });
    });
});
