# 🎟NETicket🎟
### 대규모 트랜잭션을 빠르고 안정적으로 처리하는 티켓 예매 사이트🤗

## 📚Team Notion 보러가기
[Notion](https://www.notion.so/NETicket-10c043b6526a4c1c9d892b46d77d229c)
## ✨Team Brochure 보러가기✨
[Brochure](https://www.notion.so/NETicket-2b06067c2b2448faa71951f43f225a0a)
<br>
# 💁🏻‍♂️프로젝트 소개

```jsx
🎫 NETicket 
    
No Error Ticket!
동시접속자가 많아져도 No Error!
남녀노소 누구나 내 티켓은 NETicket에서 쉽고 편리하게! 
   
저희 서비스는 간편하고 직관적인 레이아웃으로,
불편한 예매 과정을 최소화해 모두가 쉽게 이용할 수 있는 예매 사이트입니다.

다양한 화면 크기와 장치에서 일관된 사용자 경험을 제공할 수 있도록,
반응형 디자인으로 설계했습니다. 

대규모 트래픽 부하 테스트를 하며, 
최소 비용으로 시스템의 안정성과 성능 향상에 중점을 두고 프로젝트를 진행했습니다. 

한 번에 50K의 예매 요청이 발생했을 때,
1000 TPS의 처리량으로 평균 응답속도 약 1초 내외로 Error 없이 운영 중입니다. 
```

# 👨‍👨‍👧‍👦 Team

| 이름 | GITHUB |
|--|--|
| 장진혁🔰 | https://github.com/jangjh45 |
| 김건율 | https://github.com/ChoonB  |
| 서성혁 | https://github.com/dltngurxodud |
| 이유진 | https://github.com/Yujin-17 |
---

# 💻주요기능
<details>
<summary>주요기능🧐</summary>
<div markdown="1">
    
### 📌 Redis Cache를 사용한 빠른 예매

![예매2](https://user-images.githubusercontent.com/74438259/236969438-dac5e615-147e-40ee-84f2-176b12bd57cb.gif)
- Scheduling 기능을 이용하여 예매 오픈 시간에 공연의 남은 좌석 수를 Redis Cache에 데이터를 자동으로 업데이트하는 기능을 구현
- 고객이 선택한 티켓 수만큼 Write Back 방식으로 Redis Cache에 있는 공연의 남은 좌석 수를 차감해 빠른 응답 시간 도출
- 한 번에 50K 이상의 요청을 평균 응답시간 약 1초 내외로 예매 가능

### 📌 Admin 공연관리 페이지

![관리자](https://user-images.githubusercontent.com/74438259/236969535-012dc00e-287c-43f9-8be8-255db3a5ecac.gif)
- 관리자만 접근 가능한 Admin 공연 관리 페이지에서 공연 추가를 할 수 있으며, 추가되는 공연의 이미지 업로드는 AWS S3를 통해 안정적으로 처리
- Redis Cache에 자동 갱신 기능을 추가해, 데이터가 자동으로 업데이트되지만, 장애 상황을 대비해 관리자 제어 시스템 추가. 이를 통해 데이터의 무결성과 가용성을 보장 가능
- Redis Cache에 이상이 생기면 Redis CLI를 통하지 않고, 관리자 페이지에서 쉽고 직관적으로 관리할 수 있도록 구성

### 📌 QueryDSL을 사용한 검색

![검색2](https://user-images.githubusercontent.com/74438259/236969559-e1b77116-9f3c-4a2a-8465-0b11d04469f1.gif)
- 키워드 공백 기준으로 분할해 여러 단어로 구성된 키워드 처리
- title과 place에서 대소문자 구분 없이 단어를 포함하는 event를 찾는 검색 조건
- 예매 가능 여부와, 날짜를 고려한 정렬 순서
- 검색 조건에 일치하는 전체 이벤트 수 계산을 이용한 Pagination

### 📌 마이페이지 예매취소

![마이페이지](https://user-images.githubusercontent.com/74438259/236969580-4b69ced4-abfc-45f9-80bd-1ca06b8de84f.gif)
- 마이페이지에서 사용자가 가장 최근에 예매한 공연부터 정렬
- 아직 시작하지 않은 공연만 사용자가 쉽고 빠르게 예매 취소 가능

</div>
</details>
 
---

# 🎀프로젝트 챌린지 포인트

<details>
<summary>📈 사용자의 원활하고 끊김 없는 서비스 제공 위한 높은 TPS와 빠른 응답속도</summary>
<div markdown="1">
    
    

    저희 프로젝트의 주요 목표는 사용자가 항상 원활하고 끊김 없는 서비스를 이용할 수 있도록 하는 것입니다. 이를 위해, 대규모 트랜잭션 상황에서도 안정적이면서도 높은 TPS와 빠른 응답속도를 제공하기 위해 다양한 기술적 요소를 적절하게 활용하였습니다.
    
    우선, 분산 처리 아키텍처와 In-memory caching, Database Tuning 등의 기술을 조합하여 안정적이면서도 높은 TPS와 빠른 응답속도를 실현하였습니다. 
    특히, Redis Cache를 활용하여 In-memory Data Store를 구축하여 응답속도를 크게 향상했습니다.
    
    또한, 부하 분산을 위한 Load Balancing과 자원 확장 및 축소를 자동으로 처리하는 Auto Scaling을 도입하여, 서버 부하를 적절하게 분산하고 트래픽 변화에 따라 적절한 자원을 할당함으로써, 트래픽 급증 시에도 끊김 없는 서비스를 제공할 수 있도록 구성하였습니다. 
    
    마지막으로, HikariCP와 MySQL을 Tuning 하여 Database 연결을 최적화하고, 서버 분산을 통해 병목 현상을 예방하여 안정적인 서비스를 구현하였습니다. 
    
    이러한 다양한 기술적 요소들을 적절하게 조합하여, 저희 서비스는 높은 성능과 안정성을 동시에 유지할 수 있게 되었습니다. 이를 통해 사용자들은 언제나 원활하고 끊김 없는 서비스를 경험할 수 있게 되었습니다.

</div>
</details>    
<details>
<summary>🕸 데이터의 정확성과 일관성을 보장해, 데이터 무결성 확보</summary>
<div markdown="1">
    
    

    저희는 Redis를 도입하여 높은 TPS와 빠른 응답 속도를 확보하였으나, 중복 데이터로 인한 데이터의 일관성과 정확성 문제가 생겼습니다. 이에 대응하여 데이터 무결성을 확보하기 위해 아래와 같은 캐시 전략을 수립하였습니다.
    
    먼저 쓰기 전략으로 Write Back 방식을 도입하여, 티켓의 남은 좌석 수 데이터 수정 시 캐시에만 변경사항이 기록되고, 주기적으로 또는 특정 조건이 충족될 때 Database에 동기화합니다. 이를 통해 빠른 응답 시간과 Database의 부하를 줄일 수 있습니다. 
    
    특히, Redis의 Single Thread 특성과 원자적 연산을 사용해 락을 사용하지 않고도 동시성 제어를 하여 데이터 무결성을 확보할 수 있었습니다.
    
    읽기 전략은 Look Aside 방식을 도입하여 클라이언트가 특정 데이터를 읽을 때마다 캐시를 먼저 확인하고, Cache miss의 경우 Database에서 Data를 가져와 캐시에 저장한 후 클라이언트에 반환합니다. 이 방식을 통해 Database와 캐시 간의 일관성을 유지할 수 있습니다.
    
    종합적으로 Redis를 통해 대규모 트랜잭션 상황에서의 동시성 제어를 하면서, 위의 캐시 전략으로 데이터의 정확성과 일관성을 보장해 데이터 무결성을 확보할 수 있었습니다.

</div>
</details>    
<details>
<summary>📊 APM을 활용해서 시스템의 성능과 안정성을 지속적으로 관리</summary>
<div markdown="1">
    
    
    대규모 트랜잭션 상황에서 동시성 제어를 수행하며, 프로젝트의 챌린지 포인트 중 하나로 APM을 활용한 모니터링을 도입하였습니다. 
    이를 통해 시스템의 성능과 안정성을 지속적으로 관리하고 개선할 수 있었습니다. 
    
    저희 팀은 Grafana, Cloud Watch 및 Pinpoint와 같은 다양한 모니터링 도구를 사용하여 시스템의 전반적인 성능을 실시간으로 확인하였습니다.
     
    특히, Grafana와 Cloud Watch를 통해 EC2, Elastic Cache, ALB, RDS, Auto Scaling 등의 상태를 실시간으로 모니터링할 수 있었습니다. 
    
    또한, Pinpoint를 사용하여 병목 현상이 나타나는 지점을 확인하고 개선할 수 있었습니다. 이를 통해 서비스의 안정성과 성능을 지속적으로 유지하고 개선할 수 있었으며, 사용자들에게 최상의 서비스 경험을 제공할 수 있게 되었습니다. 
    
    종합적으로, 이러한 모니터링 도구들의 활용을 통해 시스템 내 문제가 발생했을 때 빠르게 진단하고 수정 및 개선 작업을 수행할 수 있었습니다. 결과적으로 프로젝트는 안정성과 높은 성능을 보장하는 성공적인 구현이 이루어졌습니다.  

</div>
</details>    
<details>
<summary>📉 비용을 최소화하면서도 높은 가용성과 성능을 유지하는 가성비 최적화 전략</summary>
<div markdown="1">
    
    
    저희는 최소 비용으로도 높은 가용성과 성능을 유지하는 것을 목표로 하며, 이를 위해 다양한 기술적인 방법과 전략적인 설계를 채택하였습니다. 
    
    EC2는 직전 버전보다 20% 저렴한 Graviton2 arm64 아키텍쳐 기반에, 무료로도 사용 가능한 t4g.small 서버를 사용했습니다. 서버 확장이 필요할 경우 비용이 더 발생하는 Scale Up 방식보다 Load Balancing을 이용해 Scale Out 방식의 수평적 확장으로 비용을 최소화 하였습니다.
    
    공연 예매 사이트의 특성상 예매 오픈 시간대에 트래픽이 집중되는 현상이 발생합니다. 그래서 Auto Scaling을 설정해 오픈 시간 직전과 트랜잭션이 몰리는 상황에서만 서버 인스턴스 확장을 하고, 서버 부하가 없는 대부분의 시간에는 서버 인스턴스가 최소로 유지됩니다. 서버 수를 동적으로 조절하여 자원 사용량을 최적화하고 비용 절감 효과를 극대화할 수 있었습니다.
    
    이를 통해 높은 가용성과 성능을 유지하면서도 비용을 최소화하는 것뿐만 아니라, 가성비 측면에서도 최적의 결과를 얻을 수 있도록 했습니다.

</div>
</details>    

---

# 📚 기술 스택

## ⚙ Architecture 구성도
![neticket3](https://user-images.githubusercontent.com/74438259/236971428-202c450b-330a-4cd1-a49f-99722605069a.png)


## ⚔ 프로젝트에서 사용한 기술

### *💻 Backend*

---

📚 **Tech Stack**

<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=springboot&logoColor=white"/>  <img src="https://img.shields.io/badge/Spring JPA-6DB33F?style=flat&logo=&logoColor=white"/>  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/JAVA-6DB33F?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/JWT-6DB33F?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis Cache-DC382D?style=flat&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/QueryDSL-7957D5?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Caffeine-0000FF?style=flat&logo=caffeine&logoColor=white"/>

🔩 **DB**

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>  <img src="https://img.shields.io/badge/Redis (AWS ElastiCache)-005571?style=flat&logo=&logoColor=white"/>

🗜 **DevOps**

<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/>  <img src="https://img.shields.io/badge/AWS S3-FF9900?style=flat&logo=amazons3&logoColor=white"/>  
<img src="https://img.shields.io/badge/AWS Application Load Balancer-6DB33F?style=flat&logo=&logoColor=white"/>  <img src="https://img.shields.io/badge/AWS Auto Scaling-FF9900?style=flat&logo=&logoColor=white"/>  
<img src="https://img.shields.io/badge/AWS Code Delploy-6DB33F?style=flat&logo=&logoColor=white"/>  <img src="https://img.shields.io/badge/GitHub Actions-F05032?style=flat&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white"/>  

⚖ **Test**

<img src="https://img.shields.io/badge/Junit5-25A162?style=flat&logo=junit5&logoColor=white"/>  <img src="https://img.shields.io/badge/Mockito-6DB33F?style=flat&logo=&logoColor=white"/>  <img src="https://img.shields.io/badge/Jmeter-D22128?style=flat&logo=apachejmeter&logoColor=white"/>  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white"/>  

🖥 **Monitoring**

<img src="https://img.shields.io/badge/AWS CloudWatch-FF4F8B?style=flat&logo=amazoncloudwatch&logoColor=white"/>  <img src="https://img.shields.io/badge/Grafana-F46800?style=flat&logo=grafana&logoColor=white"/> <img src="https://img.shields.io/badge/Pinpoint-03C75A?style=flat&logo=&logoColor=white"/>  

---

## 🏹 기술적 의사 결정

[기술적 의사 결정 과정 보러가기](https://www.notion.so/aef8266d7e5d42ff908fdce9bb438300)

## 🗺 API 명세서

[API 명세서 보러가기](https://www.notion.so/API-ec546f91e7b0472ea3ab3909ffc1b2ee)

## 💾 ERD

![Untitled (3)](https://user-images.githubusercontent.com/74438259/236996505-f900364e-84e6-4b9f-a65d-654b76aa5b59.png)

---

# 👾 Trouble Shooting

### 🌟 예매 서비스 로직에서 데이터 무결성을 지키며 응답속도와 TPS 개선 🌟

<aside>
💡 챌린지 포인트로 설정한 목표를 달성하기 위해<br>
예매 서비스 로직에서 ‘남은 좌석 수’의 데이터 무결성을 지켜야 하고,<br>
많은 트랜잭션을 처리하여 클라이언트에게 빠른 응답 속도로 예매 결과를 돌려줘야 합니다.
</aside>
<br><br>

[1. 트랜잭션 충돌 문제를 비관적 락으로 해결](https://www.notion.so/1-2ace0d67e5be4082a3fc3153de662e73)<br>
[2. 비관적 락 적용 시 속도 문제를 Redis Cache로 해결](https://www.notion.so/2-Redis-Cache-58069ce89a91466d94fe7d1d7ca0812b)<br>
[3. 더 높은 목표치를 Scale Out으로 해결](https://www.notion.so/3-Scale-Out-036894647e974fde914d91195eb83420)<br>
[4. 서버 확장으로 인한 비용 문제를 AutoScaling으로 해결](https://www.notion.so/4-AutoScaling-e8c4f28c5ea545a284400422ec865794)<br>
[5. APM으로 찾은 병목현상을 Hikari, MySQL 튜닝으로 해결](https://www.notion.so/5-APM-Hikari-MySQL-fd64de87205e4c59a4bdf31ed0b5627f)<br>

---

# 🚀 성능 개선

<aside>
🛠 저희는 트러블 슈팅의 각 단계에서 성능이 어느 정도까지 개선되었는지 알아보기 위해,<br>
프로젝트 내내 Apache Jmeter를 통해 부하테스트를 진행했습니다.<br>
평균 응답 속도(ms)와 처리량(TPS, Transacion Per Second)을 위주로 정리해 두었습니다.<br>

</aside>
<br>

[성능개선 항목 보러가기](https://www.notion.so/17f7b104d38e40aaa75f1bd8d6dc9619)

---

