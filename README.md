## CVR Java Client

CVR Java Client is an easy-to-use tool that provides access to data from [Danish Central Business Register (CVR)](https://datacvr.virk.dk/ "Danish Central Business Register (CVR)").

❗**Currently with this tool you can only search in the company database (Virksomhedssøgning).**

### Why?
I needed to get information from CVR in some of my Java projects. I didn't find any fitting solutions so I built my own simple tool that handles all initialization and provides easy-to-use methods that query the CVR database and convert JSON responses to predefined Java model classes. 
This tool was built for my personal use. Please let me know if you need any new functionality or have some advice for me. 💖
### Description
CVR stores data in the **ElasticSearch 6.8** database and provides API endpoints to make search requests. Those endpoints accept ElasticSearch queries as a JSON body. Check their [documentation](https://datacvr.virk.dk/artikel/system-til-system-adgang-til-cvr-data "documentation").
This project uses ElasticSearch [JavaHighLevelRestClient](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high.html "JavaHighLevelRestClient") and provides an abstraction layer over it so you don't have to worry about initialization and configuration process. We don't need a lot of JavaHighLevelRestClient functionality (like data update/delete) because CVR allows only data reading.

## Quick Start
#### Maven Dependency
>Java 11+ is required
```xml
<dependency>
  <groupId>io.github.clivelewis</groupId>
  <artifactId>cvr-client</artifactId>
  <version>1.0.0</version>
</dependency>
```
#### Initialization
1. Get your credentials from CVR. You can do it [**here**](https://datacvr.virk.dk/artikel/system-til-system-adgang-til-cvr-data "here"). You cannot proceed until you have your own userId and password.

2. Initialize CvrClient with your credentials. 
>I recommend creating a single instance of CvrClient and using it throughout your application. If you create many instances don't forget to release resources with the .close() method.

```java
CvrClient cvrClient = new CvrClient("userId", "password");
```

3. Use it 👀😎

#### Usage Examples
1. Find a company with CVR number 32616739 and store information about it in CvrMainCompanyInfo model class.

```java
Optional<CvrMainCompanyInfo> companyInfo = cvrClient.findCompanyByCvrNumber(32616739L, CvrMainCompanyInfo.class);

```

2. Find all active companies from branch/industry 494100 (Vejgodstransport) and return their CVR numbers.

```java
List<CvrCompanyOnlyCvrNumber> cvrNumbers = cvrClient.findAllActiveCompaniesByBranchCode("494100", CvrCompanyOnlyCvrNumber.class);
```

3. Run a simple matchAll query and return the first 10 (default response size) elements as a JSON. Each element in the list is a separate JSON string. Check ElasticSearch [documentation](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high-search.html "documentation") to learn how to build queries.
```java
SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
SearchRequest request = new SearchRequest().source(sourceBuilder);
List<String> elementsAsJson = cvrClient.searchInCompanyIndex(request);
```
