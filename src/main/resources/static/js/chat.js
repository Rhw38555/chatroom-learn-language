// chat.js
var stompClient = null;
var roomId = window.location.pathname.split("/")[2];
var sock = new SockJS('http://localhost:8080/chatLanguageGpt');

sock.onopen = function () {
    console.log('Connected to WebSocket');
};

sock.onmessage = function (event) {
    handleMessage(event.data);
};

sock.onclose = function () {
    console.log('WebSocket connection closed');
};

// 스크롤을 아래로 이동하는 함수
function scrollToBottom() {
    const messagesDiv = document.getElementById("messages");
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// 페이지 로드 시 스크롤을 아래로 이동
window.onload = function() {
    scrollToBottom();
};


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}


function handleMessage(message) {
    // 채팅 메시지 화면에 추가하기
    const messagesUl = document.querySelector("#messages ul");

    // 채팅 메시지 요소 생성
    const messageLi = document.createElement("li");
    const messageWrapperDiv = document.createElement("div");
    const gptChatDiv = document.createElement("div");
    const ttsButton = document.createElement("button");

    // 채팅 메시지 내용 및 클래스 설정
    gptChatDiv.innerText = message;
    gptChatDiv.classList.add("gpt-chat");
    ttsButton.innerText = "Read aloud";
    ttsButton.onclick = function(){
        speakText(message)
    };
    // "Me" 또는 "GPT" 표시를 위한 클래스 추가

    gptChatDiv.classList.add("gpt");


    // 채팅 메시지 요소 구조 설정
    messageWrapperDiv.classList.add("message-wrapper-gpt");
    messageWrapperDiv.appendChild(gptChatDiv);
    messageWrapperDiv.appendChild(ttsButton);
    messageLi.appendChild(messageWrapperDiv);
    messagesUl.appendChild(messageLi);
}

// 메시지 전송
function sendMessage(messageSender = "me") {
    // 채팅 입력창에서 메시지 가져오기

    const messageInput = document.getElementById("messageInput");
    const message = messageInput.value;

    // 채팅 입력창 초기화
    messageInput.value = "";


    // 채팅 메시지 화면에 추가하기
    const messagesUl = document.querySelector("#messages ul");

    // 채팅 메시지 요소 생성
    const messageLi = document.createElement("li");
    const messageWrapperDiv = document.createElement("div");
    const userChatDiv = document.createElement("div");
    const ttsButton = document.createElement("button");

    // 채팅 메시지 내용 및 클래스 설정
    userChatDiv.innerText = message;
    userChatDiv.classList.add("user-chat");
    ttsButton.innerText = "Read aloud";
    ttsButton.onclick = function(){
        speakText(message)
    };
    // "Me" 또는 "GPT" 표시를 위한 클래스 추가
    if (messageSender === "me") {
        userChatDiv.classList.add("me");
    } else if (messageSender === "gpt") {
        userChatDiv.classList.add("gpt");
    }

    // 채팅 메시지 요소 구조 설정
    messageWrapperDiv.classList.add("message-wrapper");
    messageWrapperDiv.appendChild(userChatDiv);
    messageWrapperDiv.appendChild(ttsButton);
    messageLi.appendChild(messageWrapperDiv);
    messagesUl.appendChild(messageLi);

    // 스크롤을 아래로 이동
    scrollToBottom();


    // socket 메세지 전송
    if (message.trim() !== "") {
        var ms = {
            roomId: roomId,
            textMessage: message
        };

        var jsonMessage = JSON.stringify(ms);
        sock.send(jsonMessage);
        messageInput.value = "";
    }
}

window.addEventListener("load", function () {

    // connect();

    var messageInput = document.getElementById("messageInput");
    messageInput.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    var micButton = document.getElementById("micButton");
    micButton.addEventListener("click", function () {
        startSpeechRecognition();
    });
});


// 음성 인식 시작 함수
function startSpeechRecognition() {
    // 음성 인식 객체 생성
    const recognition = new window.webkitSpeechRecognition() || new window.SpeechRecognition();

    // 음성 인식 설정
    var title = document.getElementById("h1Title").getInnerHTML();
    var titleLanguage = title.split(" ")[0].substring(1, title.length)
    // Set language code based on titleLanguage
    switch (titleLanguage) {
        case "English":
            langCode = "en-US";
            break;
        case "Japanese":
            langCode = "ja-JP";
            pitch = 0.8;
            break;
        case "Chinese":
            langCode = "zh";
            pitch = 1.2;
            break;
        case "Spanish":
            langCode = "es";
            break;
        case "German":
            langCode = "de";
            pitch = 1.2;
            break;
        case "Russian":
            langCode = "ru";
            pitch = 0.8;
            break;
        default:
            langCode = "ko-KR";
            pitch = 0.8;
            break;
    }

    recognition.lang = langCode; // 음성 인식 언어 설정 (예: 영어)
    // recognition.continuous = false; // 연속된 음성 인식 사용 여부

    // 음성 인식 이벤트 처리
    recognition.onresult = function (event) {
        const speechResult = event.results[0][0].transcript;
        console.log("음성 입력 결과:", speechResult);

        // 음성 결과를 텍스트 문장으로 변환하여 채팅 입력창에 표시
        const messageInput = document.getElementById("messageInput");
        messageInput.value = speechResult;
    };

    // 음성 인식 시작
    recognition.start();
}

function speakOnText(node) {
    var msg = node.previousSibling.previousSibling.textContent
    var title = document.getElementById("h1Title").getInnerHTML();
    var titleLanguage = title.split(" ")[0].substring(1, title.length)
    const utterance = new SpeechSynthesisUtterance(msg);

    var langCode;
    var rate = 1;
    var pitch = 1;

    // Set language code based on titleLanguage
    switch (titleLanguage) {
        case "English":
            langCode = "en-US";
            break;
        case "Japanese":
            langCode = "ja-JP";
            pitch = 0.8;
            break;
        case "Chinese":
            langCode = "zh";
            pitch = 1.2;
            break;
        case "Spanish":
            langCode = "es";
            break;
        case "German":
            langCode = "de";
            pitch = 1.2;
            break;
        case "Russian":
            langCode = "ru";
            pitch = 0.8;
            break;
        default:
            langCode = "ko-KR";
            pitch = 0.8;
            break;
    }

    utterance.rate = rate
    utterance.pitch = pitch

    // SpeechSynthesisUtterance events
    utterance.onstart = function () {
        console.log("Speech started");
    };

    utterance.onend = function () {
        console.log("Speech ended");
    };

    utterance.onerror = function (event) {
        console.error("Speech error:", event.error);
    };

    // Speak the text
    speechSynthesis.speak(utterance);
}

// TTS (Text-to-Speech) function
function speakText(text) {
    var title = document.getElementById("h1Title").getInnerHTML();
    var titleLanguage = title.split(" ")[0].substring(1, title.length)
    const utterance = new SpeechSynthesisUtterance(text);

    var langCode;
    var rate = 1;
    var pitch = 1;
    var voice = speechSynthesis.getVoices()[0]
    // Set language code based on titleLanguage
    switch (titleLanguage) {
        case "English":
            langCode = "en-US";
            voice = speechSynthesis.getVoices()[144]
            break;
        case "Japanese":
            langCode = "ja-JP";
            voice = speechSynthesis.getVoices()[153]
            pitch = 0.8;
            break;
        case "Chinese":
            langCode = "zh";
            voice = speechSynthesis.getVoices()[159]
            pitch = 1.2;
            break;
        case "Spanish":
            langCode = "es";
            voice = speechSynthesis.getVoices()[147]
            break;
        case "German":
            langCode = "de";
            voice = speechSynthesis.getVoices()[143]
            pitch = 1.2;
            break;
        case "Russian":
            langCode = "ru";
            voice = speechSynthesis.getVoices()[158]
            pitch = 0.8;
            break;
        default:
            langCode = "ko-KR";
            pitch = 0.8;
            break;
    }

    utterance.rate = rate
    utterance.pitch = pitch
    utterance.voice = voice

    // SpeechSynthesisUtterance events
    utterance.onstart = function () {
        console.log("Speech started");
    };

    utterance.onend = function () {
        console.log("Speech ended");
    };

    utterance.onerror = function (event) {
        console.error("Speech error:", event.error);
    };

    // Speak the text
    speechSynthesis.speak(utterance);
}

function recommendMsg(){
    // 로딩 이미지 표시
    const loadingImg = document.getElementById('loadingImg');
    const loadingOverlay = document.getElementById('loadingOverlay');
    loadingImg.style.display = 'inline';
    loadingOverlay.style.display = 'block';
    // 클릭 이벤트 막기
    document.body.style.pointerEvents = 'none';

    fetch('/commentRecommend', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({chatRoomId:roomId})
    })
        .then(function (response) {
            if (response.ok) {
                return response.text();
            } else {
                console.error('Failed to recommendMsg chat.');
                throw new Error('Failed to recommendMsg chat.');
            }
        })
        .then(function (text) {
            // 로딩 중 메시지 및 오버레이 숨기기
            loadingImg.style.display = 'none';
            loadingOverlay.style.display = 'none';

            // 클릭 이벤트 다시 허용하기
            document.body.style.pointerEvents = 'auto';
            const messageInput = document.getElementById("messageInput");
            messageInput.value = text;
        })
        .catch(function(error) {
            // 로딩 중 메시지 및 오버레이 숨기기
            loadingImg.style.display = 'none';
            loadingOverlay.style.display = 'none';

            // 클릭 이벤트 다시 허용하기
            document.body.style.pointerEvents = 'auto';
            console.error('Failed to recommendMsg chat.', error);
        });
}


