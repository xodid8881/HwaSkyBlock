# HwaSkyBlock

# API

## build.gradle

**repositories {**

    maven("https://jitpack.io")
    
**}**

**dependencies {**

    compileOnly("com.github.xodid8881:HwaSkyBlock:main-SNAPSHOT")
    
**}**


## Main

**override fun onEnable() {**

    val api = HwaSkyBlock.api
    
**}**

## API List

    hasIsland(player: Player): Boolean # 섬 보유 확인
    hasOwner(player: Player, number: Int): Boolean # 섬 주인 확인
    upgradeIsland(player: Player, number: Int, plusSize: Int) # 사이즈 업그레이드
