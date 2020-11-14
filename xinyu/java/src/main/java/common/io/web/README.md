# Web Module in Common Layer

This might be the most complex and useful module in common layer so far, so it 
deserves a detailed documentation. 

### Goal of this Web Module
The goal of this web module is simple and clear: provides a unified way for fetching online
information via http/https, Get/Post request. 

### Sync or PoolingAsync

We provide two interfaces and their implementation for you, one is the SyncHttpClient, which
takes a request and return you a response. You can't send the next request while you are waiting
for the response. For PoolingAsyncHttpClient, you can put a lot of request into its pool and get
the response out as a Future object. it is not blocking.


### Request and Response

You might have been very familiar with a HttpUriRequest which is basically a root url+ 
parameters + request header. For the response part, however, we do not simply return you a 
plain text string. We used Java generics when design our HttpClient, and each HttpClient
needs a ResponseProcessor to initialize. ResponseProcessor is used to process the response, 
the most simple ResponseProcessor may return a plain string to represent the response's content
Some other frequently used Process may return you a Json Objection or Json Array. The return type
of the ResponseProcessor is just the generics type. We design it in this way because it allows you
to maximize the code reusability. As you see, we can provide you the JsonProcessor and JsonArrayProcess
in advanced for you so that you don't need to code it again. 

**Attension: Remember to close the Response in your own ResponseProcessor**

### ResponseProcessor

As you see, ResponseProcessor interface has only one method need to implement:

Optional<T> process(CloseableHttpResponse response, String url) throws Exception;

It takes in a response object and a url (the url here will contains the parameter is you are using GET method and provides additional parameter)
, and construct a result object (type is depended on you) 
in Optional. The httpclient will use this method to process the response for you.
When design this method, I also realized that users may also need other information when generate their
response object in additional to url. I was thinking about use a RequestContext object to take place
of the String url, but that may also not enough to meet all kinds of requirement. And I didn't actually 
need additional information in reality so I didn't implement that yet. If you need it, you can define an
interface named RequestContext and maybe you need to modifiy the implementation of those HttpClients. 

### More one PoolingAsyncHttpClient

As you see, for each Request you add into the pool, we ask you to tag it, the tag does not need
to be unique for each request, different request can share the same tag. That's why we return a 
List of Future when you ask for the request of a tag. Typically, you can use the url for the tag,
the same url with different parameter may be put into one list.



