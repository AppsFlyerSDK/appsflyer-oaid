<img src="https://www.appsflyer.com/wp-content/uploads/2016/11/logo-1.svg"  width="450">

# OAID collection

[![Release Artifacts](https://img.shields.io/nexus/r/com.appsflyer/oaid.svg?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/releases/com/appsflyer/oaid/)

## Table of content
- [Adding to your project](#adding-to-your-project)
- [Standalone usage](#standalone-usage)

Supported devices:

Manufacturer    |   OS version
---             |   ---
Huawei          |   All
Xiaomi          |   MIUI 10.2
Vivo            |   FuntouchOS 9
OPPO            |   Color OS 7.0
Lenovo          |   ZUI 11.4
Samsung, Meizu, Nubia, ZTE, ASUS, OnePlus, Freeme OS, Ssui OS   |   Android 10

## Adding to your project
project **build.gradle**
```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://developer.huawei.com/repo/' }
    }
}
```
Download [aar](oaid/libs/msa_mdid_1.0.13.aar) from [msa alliance](http://www.msa-alliance.cn/col.jsp?id=120) to your module libs folder

module **build.gradle**
```groovy
implementation files('libs/msa_mdid_1.0.13.aar')
implementation 'com.appsflyer:oaid:5.2.0'
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

