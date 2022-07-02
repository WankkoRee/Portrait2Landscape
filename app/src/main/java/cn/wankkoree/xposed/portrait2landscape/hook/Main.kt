package cn.wankkoree.xposed.portrait2landscape.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerW
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
        loadApp("tv.danmaku.bili") {
            val recyclerViewAdapterClass = "com.bilibili.pegasus.promo.index.i"
            val getItemMethod = "Y1"

            recyclerViewAdapterClass.hook {
                injectMember {
                    method {
                        name = "onBindViewHolder"
                        paramCount = 3
                    }
                    afterHook {
                        val holder = args[0]!!
                        when (holder.javaClass.name) {
                            "com.bilibili.pegasus.card.banner.BannerV8Card\$BannerV8Holder" -> { // 灯箱
                                loggerD(msg = "cover 类型: 灯箱[${holder.javaClass.name}]")
                            }

                            "com.bilibili.pegasus.card.SmallCoverV9Holder" -> { // 直播
                                val smallCoverV9Item = findClass("com.bilibili.bilifeed.card.BaseCardViewHolder").instance!!.method {
                                    name = getItemMethod
                                }.get(holder).call()!!

                                val title = findClass("com.bilibili.pegasus.api.model.BasicIndexItem").instance!!.field {
                                    name = "title"
                                }.get(smallCoverV9Item).self!! as String

                                loggerD(msg = "cover 类型: 直播[${holder.javaClass.name}] $title")
                            }

                            "com.bilibili.pegasus.card.SmallCoverV2Card\$SmallCoverV2Holder" -> { // 小号视频/文章
                                val smallCoverV2Item = findClass("com.bilibili.bilifeed.card.BaseCardViewHolder").instance!!.method {
                                    name = getItemMethod
                                }.get(holder).call()!!

                                val title = findClass("com.bilibili.pegasus.api.model.BasicIndexItem").instance!!.field {
                                    name = "title"
                                }.get(smallCoverV2Item).self!! as String

                                loggerD(msg = "cover 类型: 小号视频/文章[${holder.javaClass.name}] $title")

                                val uri = findClass("com.bilibili.pegasus.api.model.BasicIndexItem").instance!!.field {
                                    name = "uri"
                                }.get(smallCoverV2Item)
                                (uri.self!! as String).let {
                                    if (it.startsWith("bilibili://story/")){
                                        uri.set("bilibili://video/" + it.substringAfter("bilibili://story/"))
                                        loggerD(msg = "竖屏转横屏成功")
                                    }
                                }
                            }

                            "com.bilibili.pegasus.card.LargeCoverV9Card\$LargeCoverV9Holder" -> { // 可播视频
                                loggerD(msg = "cover 类型: 可播视频[${holder.javaClass.name}]")
                            }

                            "com.bilibili.pegasus.card.c0\$b" -> { // 可播视频的壳
                                loggerD(msg = "cover 类型: 可播视频的壳[${holder.javaClass.name}]")
                            }

                            "com.bilibili.pegasus.card.q3\$b" -> { // 纪录片/番剧
                                loggerD(msg = "cover 类型: 纪录片/番剧[${holder.javaClass.name}]")
                            }

                            "com.bilibili.pegasus.card.a\$c" -> { // 创作推广/直播推广/广告/游戏
                                loggerD(msg = "cover 类型: 创作推广/直播推广/广告/游戏[${holder.javaClass.name}]")
                            }

                            else -> {
                                loggerW(msg = "cover 类型: 未知[${holder.javaClass.name}]")
                            }
                        }
                    }
                }
            }
        }
    }
}