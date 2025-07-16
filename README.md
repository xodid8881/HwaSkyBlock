<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=180&section=header&text=HwaSkyBlock&fontSize=40&fontAlignY=40"/>

<h3>📦 Kotlin 기반의 커스텀 스카이블럭 플러그인</h3>
<p>PaperMC 전용 | 모듈형 API 제공 | 커스터마이징 자유도 극대화</p>

</div>

---

## 🚀 개요

`HwaSkyBlock`은 Kotlin으로 제작된 강력한 SkyBlock 플러그인입니다.  
PaperMC 서버에 최적화되어 있으며, 외부 프로젝트에서도 활용할 수 있는 API를 제공합니다.  
유저별 섬 관리, 업그레이드 기능, GUI 구성 등 다양한 확장성을 고려해 개발되었습니다.

---

## 🔧 설치 방법

### `build.gradle.kts` 설정

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.xodid8881:HwaSkyBlock:main-SNAPSHOT")
}

</div> ```


| 함수                                                                  | 설명                           |
| ------------------------------------------------------------------- | ---------------------------- |
| `hasIsland(player: Player): Boolean`                                | 해당 플레이어가 섬을 보유하고 있는지 확인합니다   |
| `hasOwner(player: Player, island_number: Int): Boolean`             | 특정 섬 번호에 대해 플레이어가 주인인지 확인합니다 |
| `upgradeIsland(player: Player, island_number: Int, plus_size: Int)` | 섬의 사이즈를 업그레이드합니다             |

💬 문의 및 기여
이 프로젝트는 오픈소스입니다. PR 및 이슈 환영합니다!

질문 및 협업 제안은 Issues를 통해 남겨주세요.

<div align="center">
🧊 Thank you for using HwaSkyBlock! 🧊
🌙 Made with passion by xodid8881
