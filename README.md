# TripCloud

TripCloud는 사진 기반 클라우드 프로젝트입니다.

스마트폰으로 사진을 몇 년간 찍다 보면 용량이 부족해지고, 이로 인해 사진을 다른 물리 저장소나 클라우드 저장소로 옮기는 일이 생깁니다. 그러나 수천 장의 사진을 따로 분류하지 않고 업로드할 경우, 시간이 흐른 뒤 다시 찾아보는 데 어려움이 큽니다.

TripCloud는 사용자가 사진을 따로 분류하지 않더라도, **위치 기반 자동 분류**를 통해 오랜 시간이 지나도 빠르게 원하는 사진을 찾아볼 수 있도록 돕는 **클라우드 저장 플랫폼**입니다.

---

## 주요 기능

### ✅ 사용자 인증

- 간편한 회원가입 및 로그인 기능 제공
- JWT 기반 인증 체계를 도입하여 확장성과 보안성 확보
- 로그인한 사용자는 마이페이지를 통해 내 정보 확인 및 수정 가능

### ✅ 파일 업로드 및 관리

- 이미지뿐만 아니라 **일반 파일도 업로드 가능**
- **다중 파일 업로드**, **폴더 단위 업로드** 지원
- **파일/폴더 이름 수정** 기능
- **ZIP 다운로드** 지원

### ✅ 사진 전용 기능

- `contentType = image` 인 파일을 자동 분류하여 **사진 전용 뷰** 제공
- 사진에 대한 **설명글 추가** 가능
- **Gen AI 기반 키워드 자동 추출** 지원
- **키워드, 설명, 파일 이름 기반 검색** 기능 제공

### ✅ 지도 기반 분류 및 탐색

- 업로드된 사진의 메타데이터를 바탕으로 **시도/시군구별 자동 분류**
- **대표 이미지 자동 지정** (해당 지역 첫 사진 업로드 시 자동 지정)
- 사용자가 대표 이미지를 직접 선택 가능
- 지도를 채우며 사진을 탐색하는 **게이미피케이션 요소 제공**
- **방문 지역과 미방문 지역**에 대한 **관광지 추천 기능** 제공

### ✅ 정렬 및 삭제 관리

- 이름, 용량, 등록일 기준 **정렬 기능 제공**
- 삭제 시 **휴지통으로 이동** 후 **복원 또는 영구 삭제 가능**

### ✅ 이용자 커뮤니티 기능

- 사용자 간 소통을 위한 자유 게시판 제공
- 게시글에 대해 댓글 작성 및 좋아요 기능 제공
- 유저 참여를 유도하는 커뮤니티 중심의 소셜 기능 강화

---

## 시스템 구성도

아래는 TripCloud의 시스템 아키텍처를 나타낸 이미지입니다.

![TripCloud Architecture](./docs/architecture.png)

> 위 그림처럼 TripCloud는 `Nginx`, `Frontend`, `Backend`를 **각각 컨테이너로 분리**하여 구성되며, 각 요소는 다음과 같은 역할을 수행합니다:

### 1. **Nginx (Reverse Proxy)**

- HTTP 요청을 **Frontend** 및 **Backend**로 분기
- 정적 파일 직접 서빙 (Vue 빌드 결과)
- HTTPS, 보안 헤더 처리 담당

### 2. **Frontend (Vue.js)**

- 사용자 인터페이스를 담당
- 빌드된 정적 파일은 `npm run build`로 생성되어 Nginx가 서빙
- 이미지, 폴더 탐색, 지도 뷰, 검색 등 UX 처리

### 3. **Backend (Spring Boot)**

- REST API 서버
- 사용자 인증, 파일 메타데이터 관리, 위치 정보 처리, AI 키워드 추출 API 연결 등 담당

### 4. **MySQL**

- 사용자 정보, 파일 메타데이터, 키워드, 설명, 히스토리 등 영속 데이터 저장
- MyBatis를 활용하여 SQL 기반의 명확한 쿼리 처리

### 5. **MinIO (S3 호환 스토리지)**

- 실제 파일(이미지 및 일반 파일) 저장
- Presigned URL을 통해 프론트엔드에서 직접 접근 가능
- AWS S3 API와 호환되어 로컬 개발과 클라우드 전환이 용이함
- 오픈소스 기반의 객체 스토리지 솔루션으로, 자체 서버에 설치하여 온프레미스로 사용할 수 있어 퍼블릭 클라우드 없이도 독립적인 인프라 구축 가능

## 실행 및 배포

### 🛠️ 개발 시

```bash
# Frontend
cd frontend
npm install
npm run dev -- --port 5000

# Backend
cd backend
./gradlew bootRun
```

### 📦 배포 시

- Frontend를 빌드 후, Nginx로 정적 파일 서빙

```bash
cd frontend
npm run build
```

- 멀티스테이지 Dockerfile로 Vue 앱을 빌드하고 Nginx로 전달

### 📄 frontend/Dockerfile (배포용)

```Dockerfile
FROM node:18 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## 마무리

TripCloud는 사용자 중심의 클라우드 파일 저장소를 지향합니다. 단순한 저장소를 넘어, 사진을 즐기고, 관리하고, 탐험할 수 있는 플랫폼으로 나아가고 있습니다.
