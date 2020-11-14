# handy
handy server

一个简单灵活的java服务，可以通过它快速打架服务项目，支持HTTP和WebSocket。


### 使用方式

推荐xml配置：
在项目根目录创建application.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<handy
		xmlns="http://schema.hutrace.info/handy"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://schema.hutrace.info/handy http://schema.hutrace.info/handy.xsd">
	<!-- 根据提示配置 -->
</handy>
```

Launch.java
```java
public static void main(String[] args) {
	System.out.println("Welcome Useing server");
	try {
		Handyserve.instance().run();
	}catch (Exception e) {
		e.printStackTrace();
		System.exit(0);
	}
}
```