# store-management


DEMO
-
- Register for a new account and choose a role for it by sending a POST request at http://localhost:8080/register with this body as JSON
    - The body should be of type JSON and look like so
      - { 
        "username": "username", 
        "password": "password", 
        "authority": "USER|ADMIN|OWNER" 
        }
    - 'authority' supports lowercase
    - 'authority' field does not allow for any other input
- Send a POST request to http://localhost:8080/login
  - The body should be of type JSON and look like so
    - { 
      "username": "owner", 
      "password": "123456" 
      }
- In the reponse headers get value from 'Authorization' field, which is a JWT token
  - Value should look like so
    - 'Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJvd25lciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJSRUFEIn0seyJhdXRob3JpdHkiOiJST0xFX09XTkVSIn0seyJhdXRob3JpdHkiOiJXUklURSJ9XSwiaWF0IjoxNzEwOTY5NjA5LCJleHAiOjE3MTIxNzgwMDB9.VIJPxJsoBZn2uYNqzcyy1wDGOJwr6R9nbXvcWq8K-2tADJhKuu83LXY8odt29_YJ'
- In Authorization tab, choose 'Bearer Token' and in the 'Token' field put the JWT token
- You can now acces the API's corresponding to your user's roles
- Run all tests

You can also log in with one of the default accounts:
- username: user, password: 123456
  - no rights
- username: admin, password: 123456
  - read rights
- username: owner, password: 123456
  - read, write rights


API
-
- http://localhost:8080/
  - GET request
  - Index page - empty, but accessible by the security with no credentials
- http://localhost:8080/register
  - POST request
  - Register page
  - Input a JSON body like
    - {
      "username": "username",
      "password": "password",
      "authority": "USER|ADMIN|OWNER"
      }
- http://localhost:8080/login
  - POST request
  - Log in page
  - Input a JSON body like
    - {
      "username": "username",
      "password": "password"
      }
  - Supports registered and default accounts
- http://localhost:8080/api/product/{id}
  - GET request
  - Returns one product from the store
  - {id} must be an id from the database that exists
- http://localhost:8080/api/product
  - GET request
  - Returns all products from the store
- http://localhost:8080/api/product
  - POST request
  - Must be accompanied by a JSON body payload such as
    - {
      "name": "name",
      "description": "description",
      "price": 1,
      "msrp": 2,
      "stock": 3
      }
    - 'name' must not conflict with another entry's name in the table and must not be null or ''
    - 'description' must not be null or ''
    - 'price' must not be null or 0
    - 'msrp' can be null
    - 'stock' must not be null
- http://localhost:8080/api/product/{id}
  - PUT request
  - {id} must be an id from the database that exists
  - Must be accompanied by a JSON body payload such as
    - {
      "name": "name",
      "description": "description",
      "price": 1,
      "msrp": 2,
      "stock": 3
      }
    - fields can be missing from this payload, but not all of them
- http://localhost:8080/api/product/{id}
- DELETE request
- {id} must be an id from the database that exists


Requirements
-
Create an API that acts as a store management tool:
- Use git in a verbose manner, push even if you wrote only one class
- Create a Java, maven based project, Springboot for the web part
- No front-end, you can focus on backend, no need to overcomplicate the structure
- Implement basic functions, for example: add-product, find-product, change-price or others
- Optional: Implement a basic authentication mechanism and role based endpoint access
- Design error mechanism and handling plus logging
- Write unit tests, at least for one class
- Use Java 9+ features
- Add a small Readme to document the project
