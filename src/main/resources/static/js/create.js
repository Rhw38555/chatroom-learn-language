const scenarioInput = document.getElementById("scenario");
scenarioInput.addEventListener("keyup", function() {
    translateMy("scenario", "scenarioEN");
});

const myRoleInput = document.getElementById("myRole");
myRoleInput.addEventListener("keyup", function() {
    translateMy("myRole", "myRoleEN");
});

const gptRoleInput = document.getElementById("gptRole");
gptRoleInput.addEventListener("keyup", function() {
    translateMy("gptRole", "gptRoleEN");
});

window.addEventListener('load', function() {
    var createForm = document.getElementById('createForm');
    createForm.addEventListener('submit', function(event) {
        event.preventDefault();
        validateForm();
    });
});

async function translateText(text) {
    try {
        // 외부 번역 작업을 수행하는 비동기 함수를 작성하세요
        // 예시에서는 외부 번역 API 호출을 가정하고 있습니다

        // 외부 번역 API 호출
        const response = await fetch('/translate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ koreanText: text })
        });

        if (!response.ok) {
            throw new Error('번역에 실패하였습니다.');
        }

        // 번역된 결과를 가져옴
        const translation = await response.text();
        return translation;
    } catch (error) {
        console.error('번역에 실패하였습니다:', error);
        throw new Error('번역에 실패하였습니다.');
        // throw error;
    }
}

// 내 역할 번역 함수
async function translateMy(role, roleEn) {
    // "내 역할 (한국어)" 입력란 요소 가져오기
    const myRoleInput = document.getElementById(role);

    // 입력된 텍스트 가져오기
    const text = myRoleInput.value;

    // 이전에 등록된 타이머가 있다면 취소
    if (myRoleInput.timerId) {
        clearTimeout(myRoleInput.timerId);
    }

    // 입력된 텍스트가 있는 경우에만 처리
    if (text) {
        // 지연 후 번역을 위한 타이머 설정
        myRoleInput.timerId = setTimeout(async function() {
            // 외부 번역 API 호출하여 번역 결과 가져오기 (예시에서는 임의의 결과 사용)
            const translation = await translateText(text);

            // "내 역할 (외국어)" 입력란 요소 가져오기
            const myRoleENInput = document.getElementById(roleEn);

            // 번역 결과를 "내 역할 (외국어)" 입력란에 설정
            myRoleENInput.value = translation;
        }, 1000); // 1.5초 지연
    }
}



function validateForm() {
    var language = document.getElementById('language').value;
    var level = document.getElementById('level').value;
    var scenario = document.getElementById('scenario').value;
    var scenarioEN = document.getElementById('scenarioEN').value;
    var myRole = document.getElementById('myRole').value;
    var myRoleEN = document.getElementById('myRoleEN').value;
    var gptRole = document.getElementById('gptRole').value;
    var gptRoleEN = document.getElementById('gptRoleEN').value;


    // If the form is valid, create the chat
    createChat(language, level, scenario, scenarioEN, myRole, myRoleEN, gptRole, gptRoleEN);
}

function createChat(language, level, scenario, scenarioEN, myRole, myRoleEN, gptRole, gptRoleEN) {
    var chatData = {
        language: language,
        level: level,
        scenarioKor: scenario,
        scenarioEng: scenarioEN,
        myRoleKor: myRole,
        myRoleEng: myRoleEN,
        gptRoleKor: gptRole,
        gptRoleEng: gptRoleEN
    };

    // Send chat data to the server
    fetch('/chatRooms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(chatData)
    })
        .then(function(response) {
            if (response.ok) {
                alert('채팅방 생성이 완료되었습니다.');
                window.location = '/list';
                // Redirect to chat list page or perform any other action
            } else {
                console.error('Failed to create chat.');
                // Handle error response
            }
        })
        .catch(function(error) {
            console.error('Failed to create chat.', error);
            // Handle error
        });
}