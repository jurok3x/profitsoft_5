# Email notifications service
Application for sending emails to authors who posted new article in Article app.
For simplicity added jar of previous project.
## Set-up
1. First create an account in https://mailtrap.io/ and specify your credentials to docker-compose.yaml in order to enable mail sending.
2.  Run:
```bash 
    docker-compose up
   ```
3. Add new article with **POST** *http://localhost:8080/api/v1/articles*

## Example payload
```json
{
  "id": "2d7e3cbe-9547-49eb-a662-b06e5469e376",
  "title": "News in It",
  "field": "COMPUTER_SCIENCE",
  "year": 2020,
  "journal": "IT today",
  "author": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```