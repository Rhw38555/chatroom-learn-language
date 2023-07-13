
# chat-learn-language
chat gpt를 이용하여 언어 상황극 채팅을 진행하여 언어를 배울 수 있는 서비스 개발하였다.
이 프로젝트는 CHAT GPT API를 이용하여 사용자가 언어(한국어, 영어, 일본어, 중국어, 스페인어, 독일어, 러시아어)를 선택하여 
상황극 채팅을 진행하며 언어를 배울 수 있는 서비스를 개발하였습니다. 주요 기능은 아래와 같다.
1. 사용자 관리: 회원가입 및 로그인  
2. 채팅방 생성 및 채팅(웹소켓) : 사용자가 Chat GPT와 상황극을 할 수 있게 사용자 별 채팅방을 생성해주고 웹소켓 서버와 채팅을 진행한다.
3. Chat GPT API 사용하여 상황극 및 멘트 추천받기 : Chat GPT 에게 상황극 메세지를 전달받아 채팅하고 상황극에 맞는 메세지도 추천받는다.
   
### 기술 스택
개발 언어 : kotlin 1.6.21  
서비스 프레임 워크 : Spring Boot 2.7.14  
디비 : MongoDB, h2   
기타 라이브러리 : 템플릿엔진 thymeleaf, JPA 인터페이스(Spring Data JPA + Spring Data Mongodb), 웹소켓(sockjs-client), 테스트 코드(Kotest, Mockk)  
개발 툴 : IntelliJ IDEA 2022.2.3  


### 화면 

![채팅방](https://github.com/Rhw38555/chatroom-learn-language/assets/32809047/d04a204c-9878-4087-ad51-91ee774a5dcb)
![채팅상세](https://github.com/Rhw38555/chatroom-learn-language/assets/32809047/b4f95e8a-125e-4af1-8905-6d9db4784461)
