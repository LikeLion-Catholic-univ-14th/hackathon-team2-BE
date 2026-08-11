# 🤖 MCM TIME PORTAL 2076 - AI 생성 모듈 연동 가이드

## 1. 개요
`GenerationController` 및 `GenerationService` 연동 가이드 문서입니다.

---

## 2. API Endpoint Specification

- **Method & Endpoint**: `POST /generations/{generationId}/result`
- **PathVariable**: `generationId` (Long)
- **Content-Type**: `application/json`

---

## 3. DTO 명세 (Data Transfer Objects)

### 📥 Request DTO (`GenerationRequestDto`)
```json
{
  "product": {
    "name": "MCM AERO STARK",
    "description": "MCM의 아이코닉 백팩으로 이동성과 수납력을 자랑하는 클래식 아카이브"
  },
  "lockedDna": [
    {
      "name": "Visetos",
      "description": "MCM의 시그니처 모노그램 패턴"
    },
    {
      "name": "Mobility",
      "description": "자유로운 이동과 현대적 라이프스타일을 위한 기능성"
    }
  ],
  "futureContext": {
    "name": "Space Travel",
    "description": "무중력 이동과 행성 간 여행을 위한 미래 환경"
  }
}
```

### 📤 Response DTO (`GenerationResponseDto`)
```json
{
  "productName": "MCM AERO STARK 2076",
  "category": "Adaptive Space Travel Gear",
  "imageUrl": "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop",
  "description": "2076년 Space Travel 환경에 맞춰 재탄생한 비세토스 패턴의 스마트 백팩. 선택하신 [Visetos, Mobility] DNA가 결합되어 미래 라이프스타일에서도 완벽한 수납과 MCM 브랜드 헤리티지를 유지합니다."
}
```

---

## 4. 예외 처리 & Fallback (안전성 보장)
- AI API 연결 문제, 타임아웃, 예외 발생 시에도 서버가 멈추지 않고 예비(Fallback) `GenerationResponseDto`를 **200 OK**로 안전하게 반환합니다.
