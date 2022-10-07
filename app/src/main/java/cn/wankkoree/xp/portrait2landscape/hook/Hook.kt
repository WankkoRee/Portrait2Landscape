package cn.wankkoree.xp.portrait2landscape.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.StringType

class Hook: YukiBaseHooker() {
    override fun onHook() {
        "com.bilibili.pegasus.api.model.BasicIndexItem".hook {
            injectMember {
                allConstructors()
                afterHook {
                    val uri = field {
                        name = "uri"
                        type = StringType
                    }.get(instance)
                    uri.string().let {
                        if (it.isNotEmpty()) {
//                                loggerD(msg = "BasicIndexItem.<init>(url = $it)")
                            if (it.startsWith("bilibili://story/")) {
                                uri.set("bilibili://video/" + it.substringAfter("bilibili://story/"))
                                loggerD(msg = "初始化视频信息时，竖屏转横屏成功")
                            }
                        }
                    }
                }
            }
            injectMember {
                method {
                    name = "setUri"
                    param(StringType)
                }
                beforeHook {
                    (args[0]!! as String).let {
                        if (it.isNotEmpty()) {
//                                loggerD(msg = "BasicIndexItem.setUri(url = $it)")
                            if (it.startsWith("bilibili://story/")) {
                                args(0).set("bilibili://video/" + it.substringAfter("bilibili://story/"))
                                loggerD(msg = "设定视频链接时，竖屏转横屏成功")
                            }
                        }
                    }
                }
            }
        }
    }
}