# CQRS를 고려한 백엔드 시스템
* * *
<img width="571" alt="diagram" src="https://github.com/user-attachments/assets/a23c16f8-2f32-4c0c-9b80-8fd66f1bf181">

- Springboot v3.x 버전을 이용하였으며 비동기 처리를 위해 WebFlux를 이용하였습니다.
- CQRS 패턴을 위해 쓰기 디비는 MongoDB를 이용하였고 읽기는 Redis에서 읽도록 하였습니다.
- MongoDB 쓰기 성공 이후 Kafka 메시지를 이용하여 Redis에 읽기 정보를 기록하였습니다.
- 단, MongoDB와 Redis 데이터의 동기화 전략은 고려하지 않았습니다. 
- API spec 노출을 위해 Swagger를 이용하였으며 상세 설정은 하지 않았습니다.
- 단위 테스트는 Kotest를 이용하였고 Jacoco를 통해 코드 커버리지 리포트를 생성합니다.
- k6를 이용하여 부하 테스트 및 멀티 요청에 대한 테스트를 진행합니다.
- 모니터링은 Prometheus 와 Grafana를 이용하였습니다.

# 구성
* * *
- Springboot v3.4.0
- WebFlux
- Redis
- MongoDB
- Kotlin
- Swagger
- Kotest
- Prometheus
- Grafana
- Jacoco
- java 23 

# 프로젝트 시작
* * *
### docker-compose 실행

- docker 를 실행합니다.
- docker-compose.yml 을 실행시킵니다.
- docker-compose 는 docker 폴더 아래의 시스템을 실행시킵니다.
- prometheus 는 ./docker/prometheus/config 폴더 내의 두 설정 파일을 읽어서 실행합니다.

```shell
docker-compose -f ./docker-compose.yml up -d
```
- 프로젝트에서 사용하는 시스템은 아래와 같습니다.
<img width="567" alt="docker" src="https://github.com/user-attachments/assets/95a8f7c9-28e8-40c1-87e1-1a2965fadb65">

### 각 시스템 관리 페이지
- kafka : http://localhost:8989
- grafana : http://localhost:3000 admin/admin
- prometheus : http://localhost:9090
- mongoDB : http://localhost:8081
- swagger: http://localhost:8080/swagger-ui.html

### redis 동작 확인
- redis-cli 로 접속해서 ping 명령어를 실행하면 동작 확인이 가능합니다.

```shell
docker exec -it redis_main redis-cli

ping 명령어 입력
PONT 답변
```

### Application 실행
```bash
gradle build

java -jar ./build/libs/homework-0.0.1-SNAPSHOT.jar
```
* 웹 메인 페이지 : http://localhost:8080/

# 테스트 방법
* * *

## 단위 테스트
테스트 실행 및 커버리지 리포트 생성
```bash
./gradlew --console verbose test jacocoTestReport jacocoTestCoverageVerification
```
<img width="1130" alt="testcov" src="https://github.com/user-attachments/assets/d7778377-3d99-47c2-bb76-2e658e2c1c5a">

- 리포트 경로 : [./build/jacocoHtml/index.html](./build/jacocoHtml/index.html)

## 성능 테스트
실제 환경에서 읽기, 쓰기 성능 테스트 를 확인 할 수 있는 K6 테스트를 이용하였습니다.

- 본 프로젝트에서는 읽기 성능위주로 테스트를 진행하였습니다.
- 읽기 테스트를 위해 초기 데이터 생성이 필요합니다. 

1. K6 설치
```bash
brew install k6
```
2. 스크립트 실행
```bash
k6 run ./k6/employee-test.js 
```
<img width="868" alt="Screenshot 2024-12-05 at 4 20 04 PM" src="https://github.com/user-attachments/assets/462ea4b7-1982-47d5-b6c7-78aa47dde179">

# 구현 내용
* * *
## 직원 연락처 관리
### 직원들의 기본 연락정보 요청
GET /api/employee?page={page}&pageSize={pageSize}
- page, pageSize 정보를 통해 페이징이 가능해야 합니다.
```bash
curl --location --request GET 'localhost:8080/api/employee?page=0&pageSize=10' \
--header 'Content-Type: application/json' \
--data-raw ''
```
```json
{
    "totalCount": 8,
    "page": 0,
    "pageSize": 2,
    "employees":[
        {
            "name": "aaa@xxx.com",
            "email": "김철수",
            "tel": "01075312468",
            "joined":[2018, 3, 7]
        },
        {
            "name": "matilda@xxx.com",
            "email": "박영희",
            "tel": "01087654321",
            "joined":[2021, 4, 28]
        }
    ]
}
```

### 직원의 기본 연락정보 요청
GET /api/employee/{name}
```bash
curl --location --request GET 'localhost:8080/api/employee/김철수' \
--header 'Content-Type: application/json' \
--data-raw ''
```
```json
{
    "name": "홍길동",
    "email": "aaa@xxx.com",
    "tel": "010-7531-2468",
    "joined":[2018, 3, 7]
}
```

### 직원 정보 추가
POST /api/employee
- CSV 형식 (cURL을 통해서 실패하여 다른 툴을 통해 테스트 하였습니다.)

  <img width="819" alt="Screenshot 2024-12-05 at 5 27 41 PM" src="https://github.com/user-attachments/assets/c9291bf7-32e3-4476-86a2-98ff1f694f14">

```json
[
    {
        "name": "김철수",
        "email": "aaa@xxx.com",
        "tel": "01075312468",
        "joined":[2018, 3, 7]
    },
    {
        "name": "박영희",
        "email": "matilda@xxx.com",
        "tel": "01087654321",
        "joined":[2021, 4, 28]
    },
    {
        "name": "홍길동",
        "email": "kildong.hong@xxx.com",
        "tel": "01012345678",
        "joined":[2015, 8, 15]
    }
]
```

- Json 형식
```bash
curl --location --request POST 'localhost:8080/api/employee' \
--header 'Content-Type: application/json' \
--data-raw '
  [
      {
        "name": "박철수",
        "email": "clo@xxx.com",
        "tel": "010-7531-2468",
        "joined": "2018-03-07"
      },
      {
        "name": "김이삭",
        "email": "md@xxx.com",
        "tel": "010-3535-7979",
        "joined": "2013-07-01"
      },
      {
        "name": "사무시",
        "email": "connect@xxx.com",
        "tel": "010-8531-2468",
        "joined": "2019-12-05"
      }
]'
```
```json
[
    {
        "name": "박철수",
        "email": "clo@xxx.com",
        "tel": "010-7531-2468",
        "joined":[2018, 3, 7]
    },
    {
        "name": "김이삭",
        "email": "md@xxx.com",
        "tel": "010-3535-7979",
        "joined":[2013, 7, 1]
    },
    {
        "name": "사무시",
        "email": "connect@xxx.com",
        "tel": "010-8531-2468",
        "joined":[2019, 12, 5]
    }
]
```

### 로그 생성
POST /api/test/log/{level}
- 로그를 인위적으로 발생시키기 위한 테스트 API
- level은 ERROR, WARN, INFO, DEBUG, TRACE
```bash
curl --location --request POST 'localhost:8080/api/test/log/ERROR' \
--header 'Content-Type: application/json' \
--data-raw ''
```

### 에러 응답

- 직원 전체 또는 직원 정보 요청시 저장된 직원이 없을 경우
```json
{
    "code": 1000,
    "message": "직원이 존재하지 않습니다."
}
```
- 읽기 서버에 직원 정보가 존재하지 않는경우
```json
{
    "code": 1001,
    "message": "직원 정보가 존재하지 않습니다."
}
```
- 직원 저장시 요청한 직원 중 한명이라도 이미 저장된 이메일이 있는 경우
```json
{
    "code": 1002,
    "message": "중복된 직원이 존재 합니다."
}
```
- 직원 저장시 서버 문제로 저장이 실패한 경우
```json
{
    "code": 1003,
    "message": "직원 저장이 실패했습니다."
}
```
- 읽기 서버에 직원 정보 등록이 실패한 경우
```json
{
    "code": 1004,
    "message": "직원 정보 등록이 실패했습니다."
}
```
- 읽기 서버에서 직원 정보 읽기가 실패한 경우
```json
{
    "code": 1005,
    "message": "직원 정보 로딩이 실패했습니다."
}
```
- 직원 저장시 CVS 정보가 옳지 않은 경우
```json
{
    "code": 2000,
    "message": "올바르지 않은 CVS 포멧입니다."
}
```
