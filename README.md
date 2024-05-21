# Cherpa 프로젝트

<br>

## 프로젝트 소개
특정 상품에 대해 대규모로 들어오는 주문을 정확하게 처리해 구매할 수 있는 서비스입니다.

<br>

## 프로젝트 기간
2024.04 ~ 2024.05

<br>


## 기술 스택

BackEnd Skill
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Cloud Gateway

DevOps Skill
- Redis
- MariaDB
- Apache Kafka

<br>

## 주요 기능

<br>


## 실행 방법


<br>


## ERD
![image](https://github.com/youhobin/Cherpa/assets/111469930/83a3e180-546e-4ddd-b504-d105115b5c1b)


<br>


## API 문서
[API 노션 페이지](https://melon-periodical-048.notion.site/API-6a92035cc0ee413f8237552b5816b378?pvs=4)

<br>



## 아키텍처
아키텍처 사진

<br>

## 트러블 슈팅 경험
1. 주문 이후 다른 마이크로서비스에서 실패 시 보상적 트랜잭션 적용 [[분산 트랜잭션 제어 velog]](https://velog.io/@ghdb132/%EB%B6%84%EC%82%B0-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%9C%EC%96%B4-%EB%B3%B4%EC%83%81%EC%A0%81-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98)
2. 이벤트 발행 보장 (transactional outbox 패턴)
3. MSA 환경에서 동시성 문제 해결을 위해 Redisson을 이용한 분산 락 적용 [[Redisson 분산 락 velog]](https://velog.io/@ghdb132/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-%EB%B6%84%EC%82%B0-%EB%9D%BD-%EC%A0%81%EC%9A%A9-feat.-AOP)

<br>

## 성능 개선
1. 이벤트를 통해 서비스간 결합도 최소화 및 속도 개선
- 기존 다른 마이크로서비스간 feign client 통신
- kafka를 이용한 이벤트 통신으로 변경 후 400개의 응답에 대해 3744ms -> 1152ms로 속도 개선


<br>

