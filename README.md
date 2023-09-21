# Role Based Access Control - User Management Microservice

#### Exposed REST apis
Here below the most relevant features exposed using REST Apis:

#### User management features

* Register a new user account
* Login with username & password
* Retrieve a single user account
* Retrieve the list of all the existing user accounts
* Update user account data (basic user data, contacts, address)
* Add or remove a role on an user account
* Delete a user account
* Define secured accounts that cannot be deleted but only modified
* Standard validation for email, phone, password

#### RBAC features: manages roles and permissions

* Retrieve all the permissions
* Retrieve the list of the existing roles
* Create a new role
* Retrieve a single role
* Delete a role
* Add a permission on a role
* Remove a permission on a role
* Create a new permission
* Update an existing permission (also enabled or disable it)
* Retrieve single permission
* Delete not used permission
* Api to generate a salt random value to encrypt password (configuration)

## REST apis exposed
Using a browser it's possible to interact with the REST apis with Swagger:

http://localhost:8090/swagger-ui.html

![Swagger](https://github.com/andreagiassi/microservice-rbac-user-management/blob/master/src/main/resources/images/swagger.png "Swagger interface")

Another alternative is to use an external tool, for example Postman (https://www.postman.com/).

This project contains also the Postman export file with all the configured test calls:

![Swagger](https://github.com/andreagiassi/microservice-rbac-user-management/blob/master/src/main/resources/images/postman.png "Swagger interface")

#### Author
This project has originally been created in February 2020 by Andrea Giassi.