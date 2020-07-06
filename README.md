# Proxy-List-Provider
While using internet services, the accessibility and usability of some content is restricted on the basis of your location. To overcome this situation proxies are used. The Servers which provide proxy feature are called proxy servers. Proxy servers act like a gateway between user and internet. It acts like an intermediate layer which separates the user and website. A proxy server intercepts the connection between sender and receiver which means all data entering via one port is forwarded to rest of the network via different port. This prevention from direct access between two networks provides extra protection from hackers.

## Environment Setup
### Prerequisites
  - Java8 or later
  - Maven
  - IDE (any)
  - MySQL
  
### Steps to setup
 1. Download Source code or clone it using command line
 2. Import source code in any IDE using **maven import**
 3. Update username, password and schema name in **application.properties** file location: src\main\resources\application.properties
 4. Run using command line:
 
 #### Windows:
 ```
 mvnw spring-boot:run
 ```
 
 #### MacOS/Linux:
 ```
 ./mvnw spring-boot:run
 ```

## Dependencies
- Spring Web
- Data JPA
- MySQL connector driver
- thymeleaf
- devtools
- okhttp3
- json
- jsoup
