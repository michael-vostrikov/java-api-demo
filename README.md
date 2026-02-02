### Product content API

This application shows demo API on Java for a service which is used by suppliers in internet shop to edit content of their products.
This is a simplified version of a real service which was used in production.


### Features

\- Spring Boot 4\
\- PostgreSQL\
\- Docker\
\- 2 applications - user API and internal API\
\- Input validation\
\- Database migrations with Flyway\
\- Authentication with Spring Security\
\- User sessions in database\
\- CSRF for user API\
\- CORS and API key for internal API\
\- HTML for main page with Thymeleaf\
\- Locks to prevent race conditions\
\- Unit tests\
\- Swagger\
\- JSON in entity fields and responses


### Business requirements

Users (suppliers) can edit data of their products.\
Data is not saved to product immediately.\
Instead, it is written to a separate table.\
User sees in UI the product data with applied changes.\
When user wants to deploy a product with new information to internet shop, he sends product with changes for review.\
Review is performed by managers in another system.\
This application sends data to that system via API.\
Managers can accept or decline the changes.\
Another system sends corresponding API requests to this application.\
If review is accepted, changes are written to a product.\
If review is declined, changes are deleted.\
There are validation rules, which product data must follow.\
Product cannot be send for review if new data does not match the rules.\
Product cannot be edited while it is on review.\
Product cannot be send for review while it is already on review.


### Technical details

Run:
```
docker compose up
```

Application uses ports 8080 for user API and 8090 for internal API.

Open http://127.0.0.1:8080 for main page.\
Username: user\
Password: 123

It contains UI with predefined data to run test requests. You can edit GET-parameters and JSON data.

There are 6 API actions.

User API:
```
/product/view
/product/create
/product/save
/product/send-for-review
```

Internal API:
```
/review/accept
/review/decline
```

It is supposed that frontend application calls user API and another system calls internal API.

Swagger for user API:\
http://127.0.0.1:8080/swagger-ui/index.html

Swagger for internal API:\
http://127.0.0.1:8090/swagger-ui/index.html
