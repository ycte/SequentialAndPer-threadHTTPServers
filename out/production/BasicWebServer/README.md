# SequentialAndPer-threadHTTPServers
part1b
#### Part 1b: Sequential and Per-thread HTTP Servers
In class we will cover multiple approaches to implementing network servers. In Part 1, you will need to implement only the sequential server an the per-thread server.

You can feel free to reuse the example code provided in class.
When your server executes, it must support the following:

* Configuration: Your server must support a configuration file, which we follow the Apache configuration style (http://httpd.apache.org/docs/2.4/vhosts/examples.html. Note that we implement a single server name, not multiple, as the Apache example configuration shows. We start a server by reading a configuration file:
%java <servername> -config <config_file_name>
The basic configuration parameter is listening port:

Listen <port such as 6789>
A configuration file should also contain one or more virtual hosts shown below. We use the same format as the Apache, but your server ignores the *:6789 part.

<VirtualHost *:6789>
  DocumentRoot <root dir>
  ServerName <server name>
<VirtualHost>  
We recommend that you consider a hash map in your program to implement configurations.

An example configuration file is httpd.conf.

* HTTP Methods: Your server must support HTTP 1.0 (http://www.w3.org/Protocols/HTTP/1.0/spec.html) GET method.

* Headers: Your server must send the Last-Modified header and understand the If-Modified-Since header from client. This means that you will need to parse date format. For this assignment, we use the rfc1123-date format. Your server also needs to understand the User-Agent header. For other headers, your server can skip.

* URL Mapping: If the URL ends with / without specifying a file name, your server should return index.html if it exists; otherwise it will return Not Found. If the request is for DocumentRoot without specifying a file name and the User-Agent header indicates that the request is from a mobile handset (e.g., it should at least detect iphone by detecting iPhone in the User-Agent string),  it should return index_m.html, if it exists; index.html next, and then Not Found.

* Caching: Your server needs to include a basic caching mechanism to speedup handling of requests for static files. The cache is a simple Java Map, with key being the file and content the whole file in an array. Before reading a file from disk, the server checks whether it is already cached. Think: how to handle multiple threads reading and adding to the Map.
The cache size can be specified in the configuration file:

CacheSize <cache size in KBytes>
To simplify your server, there is no cache replacement; i.e., when the cache is full, no addition to the cache.

* Dynamic content using CGI: Your server needs to check if a mapped file is executable. If so, it should execute the file and relay the results back to clients. Our assignment only handles the case that the input to the external program is from GET. Please see Java ProcessBuilder on how to start set environment variables and start a dynamic process. The example of the doc can be helpful. You will need to read RFC 3875 to set the right environment variables. You will need to write a dynamic CGI program to test your invocation.

* Heartbeat monitoring: Your server needs to implement a heartbeat monitoring URL service to integrate with a load balancer (e.g., Amazon Load Balancer we covered in class). In particular, a load balancer may query a virtual URL (i.e., no mapped file) named load (i.e., with request GET /load HTTP/1.0). If the server is willing to accept new connections, it should return status code 200; otherwise, it returns code 503 to indicate overloading. Your software design should follow a "plugin" design, at run time, of different algorithms to compute overloading conditions. In particular, the monitor class file name can be specified in the configuration file.
Monitor <MyCoolMonitorClassName>
Please describe a particular design and implement it.
