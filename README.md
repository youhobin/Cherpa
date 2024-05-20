# Cherpa 프로젝트

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
erd 사진

<br>


## API 문서
api

<br>


## 아키텍처
아키텍처 사진

<br>


## 성능 개선
1. feign client -> 이벤트 처리 (kafka)ehdtl


<br>


## 트러블 슈팅 경험
1. 주문 실패 시 보상적 트랜잭션 적용
2. 이벤트 발행 보장 (transactional outbox 패턴)
3. msa 에서 동시성 문제 -> redis 분산 락 구현
4. 로그아웃 -> redis 블랙 리스트 사용

<br>



