
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
Vivo            |   Android 8
OPPO, Realme    |   Color OS 3
Lenovo          |   ZUI 11.4
Samsung, Meizu, Nubia, ZTE, ASUS, OnePlus, Black shark, Motorola, Freeme OS  |   Android 10

**Supported MSA library version:** 2.2.0

## Adding to your project
module **build.gradle**
#### Step 1
Download AAR provided by the [MSA](http://www.msa-alliance.cn/col.jsp?id=120) to your module libs folder  
**Note:** MSA AAR requires min API level 16
```groovy  
implementation 'com.appsflyer:oaid:6.12.3'  
implementation files('libs/oaid_sdk_2.2.0.aar')  
```  

#### Step 2
Copy your certificate file (<APPLICATION_PACKAGE_NAME>.cert.pem) to the project assets directory.

#### Step 3 (When using ProGaurd)
Add the following code to your `proguard-rules.pro` file:
```
# sdk
-keep class com.bun.miitmdid.** { *; }
-keep interface com.bun.supplier.** { *; }
# asus
-keep class com.asus.msa.SupplementaryDID.** { *; }
-keep class com.asus.msa.sdid.** { *; }
# freeme
-keep class com.android.creator.** { *; }
-keep class com.android.msasdk.** { *; }
# huawei
-keep class com.huawei.hms.ads.** { *; }
-keep interface com.huawei.hms.ads.** {*; }
# lenovo
-keep class com.zui.deviceidservice.** { *; }
-keep class com.zui.opendeviceidlibrary.** { *; }
# meizu
-keep class com.meizu.flyme.openidsdk.** { *; }
# nubia
-keep class com.bun.miitmdid.provider.nubia.NubiaIdentityImpl
{ *; }
# oppo
-keep class com.heytap.openid.** { *; }
# samsung
-keep class com.samsung.android.deviceidservice.** { *; }
# vivo
-keep class com.vivo.identifier.** { *; }
# xiaomi
-keep class com.bun.miitmdid.provider.xiaomi.IdentifierManager
{ *; }
# zte
-keep class com.bun.lib.** { *; }
# coolpad
-keep class com.coolpad.deviceidsupport.** { *; }
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
                if (lat != null) {
                    println(lat)
                }
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
