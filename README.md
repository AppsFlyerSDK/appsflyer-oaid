<img src="https://www.appsflyer.com/wp-content/uploads/2016/11/logo-1.svg"  width="450">

# OAID collection

[![Release Artifacts](https://img.shields.io/nexus/r/com.appsflyer/oaid.svg?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/releases/com/appsflyer/oaid/)

## Table of content
- [Adding to your project](#adding-to-your-project)
- [Standalone usage](#standalone-usage)

Supported devices:

Manufacturer    |   OS version
---             |   ---
Huawei          |   HMS 2.6.2
Xiaomi          |   MIUI 10.2
Vivo            |   Android 9
OPPO            |   Color OS 7.0
Lenovo          |   ZUI 11.4
Samsung, Meizu, Nubia, ZTE, ASUS, OnePlus, Black shark, Motorola, Freeme OS |   Android 10

## Adding to your project
module **build.gradle**

Download [AAR](oaid/libs/oaid_sdk_1.0.23.aar) provided by the [MSA](http://www.msa-alliance.cn/col.jsp?id=120) to your module libs folder
```groovy
implementation 'com.appsflyer:oaid:6.1.2'
implementation files('libs/oaid_sdk_1.0.23.aar')
```
## Standalone usage
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {
            val info = OaidClient(this, 1, TimeUnit.SECONDS).fetch()
            if (info != null) {
                println(info.id)
                val lat = info.lat
                if (lat != null) println(lat)
            }
        }.start()
    }
}
```

## Important
Time to fetch oaid is around 10 - 1000 ms

---
🛠 In order for us to provide optimal support, we would kindly ask you to submit any issues to support@appsflyer.com

*When submitting an issue please specify your AppsFlyer sign-up (account) email , your app ID , production steps, logs, code snippets and any additional relevant information.*

