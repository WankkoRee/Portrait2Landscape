package cn.wankkoree.xp.portrait2landscape.hook

import cn.wankkoree.xp.portrait2landscape.BuildConfig
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed(entryClassName = "Entry", isUsingResourcesHook = false)
class Main : IYukiHookXposedInit {
    override fun onInit() = YukiHookAPI.configs {
        debugLog {
            isEnable = BuildConfig.DEBUG
            isRecord = false
            elements(TAG, PRIORITY, PACKAGE_NAME, USER_ID)
            tag = "Portrait2Landscape"
        }
        isDebug = BuildConfig.DEBUG
        isEnableModulePrefsCache = true
        isEnableModuleAppResourcesCache = true
        isEnableHookModuleStatus = true
        isEnableHookSharedPreferences = false
        isEnableDataChannel = false
        isEnableMemberCache = true
    }

    override fun onHook() = YukiHookAPI.encase {
        val hooker = Hook()
        loadApp("tv.danmaku.bili", hooker)
        loadApp("com.bilibili.app.in", hooker)
    }
}