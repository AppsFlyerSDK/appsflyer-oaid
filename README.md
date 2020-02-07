
<img src="https://www.appsflyer.com/wp-content/uploads/2016/11/logo-1.svg"  width="450">

# Android SDK OAID Collection

This Card describes how OAID can be collected.


## Table of content

- [Adding the OAID SDK to your project](#installation)
- [Initializing the SDK](#init-sdk)
- [Stand Alone Usage](#standalone)
  


Supported devices:

Kit | Maven Artifact 
----|---------
Xiaomi|  [`xiaomi-oaid-sdk.jar`](https://github.com/AppsFlyerSDK/appsflyer-oaid/blob/master/oaid/libs/xiaomi-oaid-sdk.jar)
Huawei|  [`com.huawei.hms:hms-ads-identifier:3.4.26.303](https://github.com/AppsFlyerSDK/appsflyer-oaid/blob/master/oaid/build.gradle#L17)




## <a id="installation"> Adding the SDK to your project


For Appsflyer SDK use a gradle:

```
implementation 'com.appsflyer:oaid:5.1.0'
```


## <a id="standalone"> Stand Alone Usage
  
  
- Add Huawei SDK to their project.
In `build.gradle`:

 Add repository
  
```  
	maven { url 'http://developer.huawei.com/repo/' }
```  
	
 Add dependency
```
	implementation 'com.huawei.hms:hms-ads-identifier:3.4.26.303'
```  

The usage:

```java
 OaidClient.Info oaidInfo = OaidClient.fetch(context);
 if (oaidInfo != null) {
    oaid = oaidInfo.getId();
  }
```

---

# Important

Time to fetch oaid is around 10 - 5000 ms


🛠 In order for us to provide optimal support, we would kindly ask you to submit any issues to support@appsflyer.com

*When submitting an issue please specify your AppsFlyer sign-up (account) email , your app ID , production steps, logs, code snippets and any additional relevant information.*


[![Release Artifacts](https://img.shields.io/nexus/r/com.appsflyer/oaid.svg?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/releases/com/appsflyer/oaid/)
