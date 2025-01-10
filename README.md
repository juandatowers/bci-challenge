# BCI code challenge

Spring Boot Application BCI code challenge.

## Run

`./gradlew bootRun `

## signUp

curl --location 'http://localhost:8080/sign-up' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=0C0ACE78DA61FA14C27CD8964767014D' \
--data-raw '{
"name" : "Juan Torres",
"email": "juan.torres@gmail.com",
"password": "Xyzab34qr",
"phones": [
{
"number": 12345,
"cityCode": 3,
"countryCode":"+57"   
}
]
}'

## login

`curl --location 'http://localhost:8080/login' \
--header 'Authorization:  Bearer TOKEN' \
--header 'Cookie: JSESSIONID=0C0ACE78DA61FA14C27CD8964767014D'`
