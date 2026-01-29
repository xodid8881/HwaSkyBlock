<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=180&section=header&text=HwaSkyBlock&fontSize=40&fontAlignY=40"/>

<h3>📦 Kotlin 기반의 커스텀 스카이블럭 플러그인</h3>
<p><b>PaperMC 전용</b> • <b>모듈형 API 제공</b> • <b>커스터마이징 자유도 극대화</b></p>
### [Wiki](https://eight-emmental-b5a.notion.site/HwaSkyBlock-237397b18e59800ba89ffa43a3748c1b?pvs=74)

</div>

---

## 🌟 프로젝트 소개

> 당신만의 스카이블럭 서버를 더욱 강력하게!

**HwaSkyBlock**은 Kotlin으로 개발된 PaperMC 기반 스카이블럭 플러그인입니다.  
아직 개발중인 프로젝트임으로 베타버전을 사용해보실 수 있습니다.  
GUI 기반 인터페이스와 다양한 API 제공으로 개발자와 운영자 모두에게 강력한 유연성을 제공합니다.  
최적화된 성능과 깔끔한 구조로 대규모 서버에도 무리 없이 사용할 수 있습니다.

---

## ⚙️ 설치 방법

### `build.gradle.kts` 설정

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.xodid8881:HwaSkyBlock:main-SNAPSHOT")
}
```

---

## 🧩 API 예시

```kotlin
override fun onEnable() {
    val api = HwaSkyBlock.api
    // 초기화 시 API를 불러와 사용할 준비를 합니다.
}

// 예: 명령어 실행 시 사용
fun handleIslandCommand(player: Player) {
    val api = HwaSkyBlock.api

    if (api.hasIsland(player)) {
        player.sendMessage("당신은 이미 섬을 보유하고 있습니다.")
    } else {
        player.sendMessage("섬이 없습니다. 생성해주세요!")
    }
}

```

---

## 📘 제공 API 목록

| 함수                                                                  | 설명                   |
|---------------------------------------------------------------------|----------------------|
| `hasIsland(player: Player): Boolean`                                | 플레이어가 섬을 보유하고 있는지 확인 |
| `hasOwner(player: Player, island_number: Int): Boolean`             | 플레이어가 특정 섬의 주인인지 확인  |
| `upgradeIsland(player: Player, island_number: Int, plus_size: Int)` | 섬 사이즈 업그레이드 수행       |

---

## 🤝 기여 & 소통

이 프로젝트는 **오픈소스**입니다.  
Pull Request 및 Issue 등록은 언제든 환영합니다!  
기여 가이드 및 API 문서는 [Wiki](https://eight-emmental-b5a.notion.site/HwaSkyBlock-237397b18e59800ba89ffa43a3748c1b?pvs=73)에서 확인하실 수 있습니다.

- 👉 [이슈 등록하러 가기](https://github.com/xodid8881/HwaSkyBlock/issues)
- 👉 [PR 보내기](https://github.com/xodid8881/HwaSkyBlock/pulls)

---

<div align="center">

✨ 당신의 SkyBlock 서버에 날개를 달아보세요!  
🧊 <b>Thanks for using HwaSkyBlock</b> 🧊  
🌙 Made with love by <a href="https://github.com/xodid8881">xodid8881</a>

</div>
