## Running Locally

```sh
$ git clone https://github.com/fltr-reagent-re-frame-heroku
$ cd fltr-reagent-re-frame-heroku
$ lwrap lein figwheel
$ lein cljsbuild auto
```

Your app should now be running on [http://localhost:3449/#/](http://localhost:3449/#/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

For more information about using Clojure on Heroku, see these Dev Center articles:

- [Clojure on Heroku](https://devcenter.heroku.com/categories/clojure)
