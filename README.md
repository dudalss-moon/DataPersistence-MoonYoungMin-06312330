# DataPersistence

H2 embedded DB와 순수 JDBC를 사용한 심플한 CRUD 데이터 영속성 예제 프로젝트입니다.

## 기술 스택

- Java 17
- Gradle 9
- H2 Database 2.3 (파일 기반 embedded)
- JUnit 5

## 프로젝트 구조

```
src/
├── main/java/org/example/
│   ├── db/
│   │   └── DatabaseManager.java       # DB 연결 및 테이블 초기화
│   ├── model/
│   │   └── User.java                  # 도메인 모델
│   ├── repository/
│   │   ├── UserRepository.java        # CRUD 인터페이스
│   │   └── UserRepositoryImpl.java    # JDBC 구현체
│   └── Main.java                      # 실행 예시
└── test/java/org/example/repository/
    └── UserRepositoryTest.java        # 단위 테스트 (8개)
```

## CRUD 구성

| 메서드 | SQL | 설명 |
|--------|-----|------|
| `save(User)` | INSERT | 새 사용자 저장 후 생성된 ID 반환 |
| `findById(Long)` | SELECT | ID로 단건 조회, `Optional<User>` 반환 |
| `findAll()` | SELECT | 전체 목록 조회 |
| `update(User)` | UPDATE | 이름·이메일·나이 수정 |
| `deleteById(Long)` | DELETE | ID로 삭제 |

## 실행 방법

### 사전 요구사항

- JDK 17 이상

### 테스트 실행

```bash
./gradlew test
```

### 메인 실행

```bash
./gradlew run
```

> 데이터는 프로젝트 루트의 `data/appdb` 파일에 저장됩니다.

## 테스트 결과

```
UserRepositoryTest
  ✔ save_새로운유저_저장되고_ID부여
  ✔ findById_존재하는ID_유저반환
  ✔ findById_없는ID_빈Optional반환
  ✔ findAll_저장된유저목록_전체반환
  ✔ update_유저정보_수정됨
  ✔ update_없는ID_예외발생
  ✔ deleteById_유저삭제_이후조회안됨
  ✔ deleteById_없는ID_예외발생

8 tests — 0 failures — 100% successful
```

## 아키텍처

```
Main
 └── UserRepository (interface)
      └── UserRepositoryImpl (JDBC)
           └── DatabaseManager (H2 connection)
```

인터페이스(`UserRepository`)를 통해 구현체를 분리했기 때문에, 추후 JPA나 MyBatis 등으로 교체할 때 호출 코드 변경 없이 구현체만 바꾸면 됩니다.
