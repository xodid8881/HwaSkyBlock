<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=180&section=header&text=HwaSkyBlock&fontSize=40&fontAlignY=40"/>

<h3>📦 Kotlin 기반의 PaperMC SkyBlock 프레임워크</h3>
<p><b>코어 + 애드온 분리 구조</b> • <b>1.21.x 대응</b> • <b>운영/개발 확장성 중심</b></p>

</div>

---

## 🌟 프로젝트 개요

**HwaSkyBlock**은 Paper 서버에서 동작하는 Kotlin 기반 SkyBlock 플러그인입니다.  
단일 플러그인 형태를 넘어, **코어(HwaSkyBlock-Core)** 와 **애드온(JAR) 로딩 구조**를 중심으로 설계되어 기능을 유연하게 확장할 수 있습니다.

- 코어 기능: 섬 생성/관리, GUI, 데이터 저장, 명령어 처리
- 애드온 기능: 랭킹, 커스텀 포인트, 가축 시스템, 일일 미션 등
- 데이터베이스: `sqlite` / `mysql` 선택 가능

---

## ✅ 지원 환경

- Java: **21**
- 서버: **Paper 1.21.1** (또는 호환 1.21.x 계열)
- 필수 의존성:
  - **Vault** (코어 활성화에 필요)
- 선택 의존성(기능 확장):
  - PlaceholderAPI
  - Floodgate
  - ItemsAdder
  - Oraxen
  - Nexo
  - CustomCrops

---

## 🧱 모듈 구성

루트 Gradle 기준 모듈:

- `:v1_21:HwaSkyBlock-Core`
- `:v1_21:skyblock-ranking-addon`
- `:v1_21:skyblock-custompoint-addon`
- `:v1_21:skyblock-cattle-addon`
- `:v1_21:skyblock-dailymission-addon`

각 애드온은 `HwaSkyBlock-Core`를 `compileOnly`로 참조하며, 빌드 시 독립 JAR로 생성됩니다.

---

## 🚀 서버 설치 가이드

### 1) 코어 플러그인 설치

1. `HwaSkyBlock` 코어 JAR을 서버 `plugins/` 폴더에 넣습니다.
2. Vault 플러그인 및 경제 플러그인(예: EssentialsX Economy)을 함께 설치합니다.
3. 서버를 1회 실행해 기본 설정 파일을 생성합니다.

생성되는 핵심 파일:

- `plugins/HwaSkyBlock/config.yml`
- `plugins/HwaSkyBlock/message.yml`

### 2) 데이터베이스 설정

`plugins/HwaSkyBlock/config.yml`에서 DB 타입을 선택합니다.

```yml
database:
  type: sqlite # mysql 또는 sqlite
  mysql:
    host: localhost
    port: 3306
    user: root
    password: password
    database: minecraft
```

### 3) 애드온 설치

HwaSkyBlock는 애드온을 다음 폴더에서 자동 로드합니다.

- `plugins/HwaSkyBlock/Libs/*.jar`

절차:

1. 원하는 애드온 JAR 빌드/다운로드
2. `plugins/HwaSkyBlock/Libs/`에 복사
3. 서버 재시작

---

## 🧩 기본 명령어

코어 `plugin.yml` 기준:

- `/섬` (`/island`)
- `/섬설정` (`/isset`)

---

## 🧩 API 사용 (개발자)

### Gradle 의존성(JitPack)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.xodid8881:HwaSkyBlock:main-SNAPSHOT")
}
```

### API 인터페이스

`HwaSkyBlockAPI` 주요 메서드:

- `hasIsland(player: Player): Boolean`
- `hasOwner(player: Player, island_number: Int): Boolean`
- `upgradeIsland(player: Player, island_number: Int, plus_size: Int)`
- `addIslandPoint(player: Player, island_number: Int, point: Int)`

### 코드 예시

```kotlin
override fun onEnable() {
    val api = HwaSkyBlock.api

    // 섬 보유 여부 확인
    if (api.hasIsland(player)) {
        logger.info("이미 섬을 보유한 플레이어입니다.")
    }
}

fun addPointExample(player: Player, islandId: Int) {
    val api = HwaSkyBlock.api
    api.addIslandPoint(player, islandId, 10)
}
```

---

## 🔌 제공 애드온 요약

- `SkyblockRankingAddon`
  - 섬 랭킹 조회 기능 제공
- `SkyblockCustomPointAddon`
  - 블록 파괴/수확 기반 커스텀 포인트 시스템
  - ItemsAdder/Oraxen/Nexo/CustomCrops 연동 가능
- `SkyblockCattleAddon`
  - 섬 전용 가축/사육 관련 기능
- `SkyblockDailyMissionAddon`
  - 섬 단위 일일 미션 및 진행도 관리

---

## 🛠️ 로컬 빌드

루트에서:

```bash
./gradlew build -x test
```

Windows:

```powershell
.\gradlew.bat build -x test
```

빌드 결과물은 모듈별 `build/libs`와 루트 `build/libs`에 정리됩니다.

---

## 🧪 운영 체크리스트

- Vault + Economy 플러그인이 활성화되어 있는가?
- `database.type` 값이 `sqlite` 또는 `mysql`로 정확히 입력되었는가?
- 애드온 JAR을 `plugins/HwaSkyBlock/Libs/`에 넣었는가?
- 서버 Java 버전이 21인가?

---

## 🤝 기여

Issue / PR은 언제든 환영합니다.

- Wiki: https://github.com/xodid8881/HwaSkyBlock/wiki
- Issues: https://github.com/xodid8881/HwaSkyBlock/issues
- Pull Requests: https://github.com/xodid8881/HwaSkyBlock/pulls

---

<div align="center">

🧊 Thanks for using HwaSkyBlock

</div>
