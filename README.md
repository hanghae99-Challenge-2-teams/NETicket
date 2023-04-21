##   💳 NETicket

📝 프로젝트 이름 : 티켓 예매 서비스

📄 프로젝트 설명 : No Error Ticket!  동시접속량이 많아져도 No Error! 
  *  10초안에 10000 트랜잭션 처리, 1000TPS 이상으로 개선, 오류율 1% 이내, 평균 응답속도 10000ms대로 목표 설정
  *  현재 티켓사이트는 예매가 복잡하여 예매를 다른사람에게 부탁을 하는 경우가 많다. 복잡한 예매과정을 간소화시켜 누구나 쉽고빠르게 티켓팅을 성공할 수 있게 하는 것이 목표입니다.

📅 프로젝트 기간 : 2023.03.31 ~ 2023.05

# [![Notion Badge](https://img.shields.io/badge/Notion-000000.svg?&style=flat-round&logo=notion&link=https://www.notion.so/NETicket-10c043b6526a4c1c9d892b46d77d229c?pvs=4)](https://www.notion.so/NETicket-10c043b6526a4c1c9d892b46d77d229c?pvs=4)

👨‍👩‍👧‍👦 Challenge 2조 : [장진혁](https://github.com/jangjh45) [김건율](https://github.com/ChoonB) [서성혁](https://github.com/dltngurxodud) [이유진](https://github.com/Yujin-17)

# 📣 티켓 서비스 요구사항

1. 회원에 대한 요구사항
    1. 아이디는 이메일형식으로 한다.
    2. 이메일 중복 방지, 2~30자 / 한글, 영어만 가능
    3. 닉네임 중복 방지, 2~10자 / 한글, 영어, 숫자만 가능
    4. 비밀번호는 8~20자 / 영문자 1개, 숫자 1개, 특수문자 1개 이상 입력 해야합니다.
    5. 로그인을 한 사용자만 예매를 진행할 수 있다.
    6. 모든 사용자는 공연정보를 조회가능하다.
2. 공연정보에 대한 요구사항
    1. 메인페이지는 페이지당 8개의 공연만 조회한다.
    2. 공연시간이 임박한 공연이 최상위로 조회된다.
    3. 현재시간으로 지나간 공연은 조회대상에서 제외된다.
3. 공연 상세정보에 대한 요구사항
    1. 상세페이지에서는 공연에 대한 제목, 장소, 가격, 공연날짜, 남은 좌석 수를 조회가능합니다.
    2. 로그인한 사용자는 매수와 공연회차를 선택하고 예매가 가능하다.
    3. 공연 예매에 대한 요구사항
        1. 사용자 요청이 몰릴시 서버가 수용할 수 있는 자원보다 초과하면 대기열을 생성하여 순서대로 처리한다.
        2. 모든 요청에 대해서도 일정시간내의 응답속도를 보장해야한다.(허용 응답속도는 정해야한다.) 
        3. 예매가 정상적으로 완료가 되면 마이페이지로 이동하고 로그인을 한 사용자의 예매현황을 확인 가능하다.

## 🔧 Technologies & Software Used

<img src="https://img.shields.io/badge/Java-007396?style=flat-round&logo=OpenJDK&logoColor=white"/>  <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-round&logo=spring&logoColor=white"/>  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-round&logo=SpringSecurity&logoColor=white"/>  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-round&logo=springboot&logoColor=white"/>

# ⚙ Service Architecture
![net drawio (4)](https://user-images.githubusercontent.com/74438259/230075897-d14696fc-bb55-4b89-aa92-37823d53891b.png)


