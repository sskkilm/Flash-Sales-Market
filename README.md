# Flash Sales Market
## 프로젝트 소개
**Flash Sales Market**은 일반 상품 판매와 지정된 시간에 오픈되는 선착순 이벤트 상품 판매 기능을 제공하는 이커머스 플랫폼입니다.

선착순 이벤트 상품 판매 시 발생하는 높은 트래픽을 효율적으로 처리하고 성능을 개선하는 방법을 학습한 개인 프로젝트로, MSA(Microservices Architecture) 환경을 가정하여 설계되었으며, 멀티모듈로 구성된 모노레포(monorepo) 구조를 채택하고 있습니다.
## 프로젝트 기간
2024.10.18 ~

## 기술 스택
- Languages: Java 21
- Frameworks & Libraries: Spring Boot 3, Spring Cloud, Spring Data Jpa, QueryDSL
- Database: MySQL, Redis
- Infrastructure: Docker
## 시스템 아키텍처
## 주요 기능
### 장바구니
- 상품 추가
- 상품 내용 수정
- 상품 삭제
- 장바구니 목록 조회
### 상품
- 상품 목록 조회
- 상품 상세 조회
### 주문
- 주문 생성
- 주문 내역 조회
- 주문 취소
- 환불
### 결제
- 결제 진입
- 결제 승인
## 기술적 의사결정
- 서비스 디스커버리 패턴 적용과 로드밸런싱
- 선착순 구매 상황을 고려한 재고 선점 방식 도입
- MSA 환경에서의 동기 통신과 이벤트 기반 통신
- 분산 락을 적용한 동시성 제어 및 데드락 방지
## 트러블 슈팅
- 트랜잭션 종료 전 분산 락 해제로 인한 동시성 문제


