#### 一、answer all questions specified above

1. Simple Client： Think about how to collect statistics from multiple threads.

```
在管理多个 thread 的进程中建立 HashMap 用于存储，在进程开始时将 HashMap 作为形式参数传入在线程内修改来收集多线程的数据。
```

2. Part 1b:  how to handle multiple threads reading and adding to the Map.

```
在管理多个 thread 的进程中建立 HashMap 用于存储，层层传递，在创建 request 对象时初始化类中的 HashMap 进行管理。
```

```java
WebRequestHandler wrh = 
		        new WebRequestHandler( connectionSocket, cfgMap, fileCache );

public WebRequestHandler(Socket connectionSocket, 
			     Map<String, String> cfgMap, Map<String, String> fileCache) throws Exception
    {
        reqCount ++;
		this.fileCache = fileCache;
		...
```

Please describe a particular design and implement it.



3. 