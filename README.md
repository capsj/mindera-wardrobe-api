# Mindera exercise - Wardrobe API

Start up a Postgres instance by running
```
$ docker run -d -p 5432:5432 -e POSTGRES_HOST_AUTH_METHOD=trust -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD="" -e POSTGRES_DB=default_schema postgres:10
```

Start up service
```
$ sbt run
```

## API:  

### CSV upload
```
$ curl 'http://localhost:9000/clothing/csv' --form 'file=@./public/clothing.csv'
```

### Search clothing items by name
```
$ curl 'http://localhost:9000/clothing/search?term=iWalk'
```

### Tag clothing item as part of an outfit
```
$ curl --request POST 'http://localhost:9000/clothing/1/tag?outfitName=summer'
```
### List clothing items
```
$ curl 'http://localhost:9000/clothing/view'
```
