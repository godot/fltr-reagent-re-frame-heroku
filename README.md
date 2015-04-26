## About

https://fathomless-fjord-3701.herokuapp.com/
- web version of [Foreign Language Text Reader](http://sourceforge.net/projects/fltr/)
- oxford 3000 - check which words in your text are part of [Oxford3000](http://www.oxfordlearnersdictionaries.com/about/oxford3000)
[more to come soon...]

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

## Roadmap

- database - persistence layer
- users accounts & authentication
- form validations
- articles management
- user's dictionaries, word list [anki]
