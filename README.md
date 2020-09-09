# mindera-wardrobe-api


Start up a Postgres instance by running
```docker run -d -p 5432:5432 -e POSTGRES_PASSWORD="" -e POSTGRES_USER=postgres -e POSTGRES_DB=default_schema -e POSTGRES_HOST_AUTH_METHOD=trust circleci/postgres:10```


## API:  

### CSV upload
```
curl --location --request POST 'http://localhost:9000/clothing/csv' --form 'file=${CSV_PATH}'
```

### Search by name
```
curl --location --request GET 'http://localhost:9000/clothing/search?term=iWalk'
```

### List clothing items
