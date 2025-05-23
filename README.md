
## ERD

![ERD](https://github.com/user-attachments/assets/62126bc1-46d5-40bd-8b67-53aa7c6a08b3)

## API 명세서

- [Postman API 명세서 바로가기](https://documenter.getpostman.com/view/44667399/2sB2qWGPpF)

## 환경 변수 및 설정

`.env` 파일 또는 환경 변수로 아래 값을 지정해야 합니다.

DB_USER=your_db_user</br>
DB_PASSWORD=your_db_password

`src/main/resources/application.properties` 

예시:</br>
`spring.datasource.username=${DB_USER}`</br>
`spring.datasource.password=${DB_PASSWORD}`</br>
`spring.datasource.url=jdbc:mysql://localhost:3306/schedule`</br>
`spring.jpa.hibernate.ddl-auto=create`</br>

## 실행 방법

1. MySQL 데이터베이스를 준비하고, `schedule` 데이터베이스를 생성하세요.
2. 환경 변수(`DB_USER`, `DB_PASSWORD`)를 설정하세요.
3. Gradle 빌드 및 실행:
   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```
4. API는 기본적으로 `http://localhost:8080`에서 동작합니다.
