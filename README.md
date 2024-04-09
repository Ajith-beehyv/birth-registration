# birth-registration-assignment

This service is used to issue birth certificate.
### Service Dependencies

- egov-idgen
- egov-user
- egov-workflow

## Service Details

The service is integrated with User service, IdGen service and
Workflow service to create, update and search a birth registration application.

### API Details

`BasePath` /birth-registration/birth/registration/v1/[API endpoint]

#### Method

a) `_create`
- Creates birth registration application and return the application number

b) `_update`
- Updates birth registration application based on application number

c) `_search`
- Searches birth registration application based on application number

### Kafka Producers

- Following are the Producer topics.
    - **save-bt-application**
    - **update-bt-application**
