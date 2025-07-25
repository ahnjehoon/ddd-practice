# DDD 음식 주문 서비스

## 📋 프로젝트 소개
Domain-Driven Design(DDD) 원칙과 Clean Architecture를 적용한 음식 주문 서비스 프로젝트입니다.
마이크로서비스 아키텍처와 이벤트 기반 비동기 통신을 통해 확장 가능하고 유지보수성이 높은 시스템을 구현하려고 노력중입니다. 현재 드는 생각은 너무 욕심을 부려서 다이어트가 좀 필요할 것 같긴합니다.

## 🏗️ 아키텍처 개요

### DDD Layered Architecture
```
┌─────────────────────────────────────────────┐
│              Bootstrap Layer                │  
│      (API, Message sub, Batch Etc..)        │
├─────────────────────────────────────────────┤
│              Application Layer              │
│         (Use Cases, DTOs, Ports)            │
├─────────────────────────────────────────────┤
│                Domain Layer                 │
│    (Entities, Value Objects, Services)      │
├─────────────────────────────────────────────┤
│             Infrastructure Layer            │
│    (Persistence, Messaging, External)       │
└─────────────────────────────────────────────┘
```

### 모듈 구조
```
ddd-practice/
├── common/                          # 공통 모듈
│   ├── common-application/          # 공통 애플리케이션 유틸리티
│   ├── common-domain/               # 공통 도메인 객체 및 인터페이스
│   └── common-messaging/            # 메시징 인프라 (Kafka)
│       ├── core/                    # 메시징 핵심 기능
│       └── kafka/                   # Kafka 구현체
└── domain-service/                  # domain 서비스(order, restaurant, payment, customer)
    ├── application/                 # 애플리케이션 레이어
    ├── domain/                      # 도메인 레이어
    ├── infrastructure/              # 인프라스트럭처 레이어
    │   ├── api/                     # REST API
    │   ├── messaging-publisher/     # 메시지 발행
    │   ├── messaging-subscriber/    # 메시지 구독
    │   └── persistence/             # 데이터 영속성
    └── bootstrap/                   # 애플리케이션 부트스트랩 (일단 통합이긴한데 추후 분리될 수 있음)
```

## 🎯 주요 특징

### Domain-Driven Design
- **도메인 모델**: 비즈니스 로직이 중심이 되는 풍부한 도메인 모델
- **애그리게이트**: 일관성 경계를 명확히 정의한 애그리게이트 패턴
- **도메인 이벤트**: 도메인 상태 변화를 이벤트로 표현
- **헥사고날 아키텍처**: 포트와 어댑터 패턴으로 외부 의존성 격리

### 기술 스택
- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA
- **Database**: PostgreSQL
- **Messaging**: Apache Kafka
- **Build**: Gradle (멀티 프로젝트)
- **Container**: Docker & Docker Compose

## 🚀 시작하기

### 사전 요구사항
- Java 17 이상
- Docker & Docker Compose
- Gradle 8.x

### 환경 설정

1. **저장소 클론**
   ```bash
   git clone https://github.com/ahnjehoon/ddd-practice.git
   cd ddd-practice
   ```

2. **인프라 서비스 시작**
   ```bash
   # PostgreSQL 시작
   docker-compose -f docker/docker-compose.postgresql.yml up -d
   
   # Kafka 시작
   docker-compose -f docker/docker-compose.kafka.yml up -d
   ```

3. **애플리케이션 빌드 및 실행**
   ```bash
   # 전체 프로젝트 빌드
   ./gradlew build
   
   # Order Service 실행
   ./gradlew :order-service:bootstrap:bootRun
   ```
