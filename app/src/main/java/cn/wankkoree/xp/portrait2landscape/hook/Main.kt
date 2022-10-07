package cn.wankkoree.xp.portrait2landscape.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed(entryClassName = "Entry", isUsingResourcesHook = false)
class Main : IYukiHookXposedInit {
    override fun onInit() = YukiHookAPI.configs {
        debugTag = "Portrait2Landscape"
        isDebug = false
        isAllowPrintingLogs = true
        isEnableModulePrefsCache = false
        isEnableModuleAppResourcesCache = false
        isEnableHookModuleStatus = true
        isEnableDataChannel = false
        isEnableMemberCache = true
    }

    override fun onHook() = YukiHookAPI.encase {
        val hooker = Hook()
        loadApp("tv.danmaku.bili", hooker)
        loadApp("com.bilibili.app.in", hooker)
    }
}