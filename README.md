<img src="https://www.appsflyer.com/wp-content/uploads/2016/11/logo-1.svg"  width="450">

# OAID collection
## Table of content
- [Adding the OAID SDK to your project](#installation)
- [Standalone usage](#standalone)

Supported devices:

Manufacturer    |   OS version
---             |   ---
Huawei          |   All
Xiaomi          |   MIUI 10.2
Vivo            |   FuntouchOS 9
OPPO            |   Color OS 7.0
Lenovo          |   ZUI 11.4
Samsung, Meizu, Nubia, ZTE, ASUS, OnePlus, Freeme OS, Ssui OS   |   Android 10

## <a id="installation"> Adding the SDK to your project
project **build.gradle**
```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://developer.huawei.com/repo/' }
    }
}
```
module **build.gradle**
```groovy
implementation 'com.appsflyer:oaid:5.2.0'
```
## <a id="standalone"> Standalone Usage
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {
            val info = OaidClient.fetch(this, 1, TimeUnit.SECONDS)
            if (info != null) {
                println(info.id)
                val lat = info.lat
                if (lat != null) println(lat)
            }
        }.start()
    }
}
```

# Important
Time to fetch oaid is around 10 - 1000 ms

---
🛠 In order for us to provide optimal support, we would kindly ask you to submit any issues to support@appsflyer.com

*When submitting an issue please specify your AppsFlyer sign-up (account) email , your app ID , production steps, logs, code snippets and any additional relevant information.*


[![Release Artifacts](https://img.shields.io/nexus/r/com.appsflyer/oaid.svg?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/releases/com/appsflyer/oaid/)
