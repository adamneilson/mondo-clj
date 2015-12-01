# mondo-clj

<img src="https://getmondo.co.uk/docs/images/logo-6cef8dc1.svg" style="width:200px">

This clojure lib gives a simple DSL for the [Mondo API](https://getmondo.co.uk/docs/). It's pre-alpha as I'm a couple of days away from getting my grubby paws on my shiny new Mondo account.

## Installing 
Current [semantic](http://semver.org/) version:

[![Clojars Project](http://clojars.org/mondo-clj/latest-version.svg)](http://clojars.org/mondo-clj)

To use with Leiningen, add

```
:dependencies [[mondo-clj 0.0.1]]
```
to your project.clj.

You can use it in a source file like this:

```
(:require [mondo-clj.core :as mondo])
```
or by REPL:

```
(require '[mondo-clj.core :as mondo])
```


## Usage

### Authentication
The Mondo API implements OAuth 2.0 so the first step is to get a token:

```clojure
(mondo/get-access-token client-id
                        client-secret
                        username
                        password)
```

This should return a map similar to this:

```
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :access-token "access_token"
 :client-id "client_id"
 :expires-in 21600
 :refresh-token "refresh_token"
 :token-type "Bearer"
 :user-id "user_id"}
```
As you can see I'm adding the `:status` to the returned map to give a bit of context to any errors.

At any time you can get information about an access token, using:

```
(mondo/whoami access-token)
```

returns:

```
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :authenticated true,
 :client-id "client_id",
 :user-id "user_id"}
```

To get a list of accounts:
```
(mondo/list-accounts access-token)
```

### Transactions
Get details of a single transaction:
```
(mondo/get-transaction access-token transaction-id)
```
Or list transactions
```
(mondo/list-transactions access-token account-id)

;or 

(mondo/list-transactions access-token account-id {:since #inst "2015-12-01T00:00:00.000-00:00"})

;or 

(mondo/list-transactions access-token account-id {:before #inst "2015-12-01T00:00:00.000-00:00"})

;or 

(mondo/list-transactions access-token account-id {:since #inst "2015-12-01T00:00:00.000-00:00" :limit 48})

```

You can also add meta-data to a transaction

```
(mondo/annotate-transaction access-token transaction-id metadata-map)
```
where `metadata-map` is a simple map of key vals.

### Feed

You can inject items into an accounts feed
```
(mondo/create-feed-item {:access-token "0000000000000000"
                         :account-id "account_id"
                         :type "basic"
                         :params {:title "My custom item"
                                  :image-url "www.example.com/image.png"
                                  :background-color "#FCF1EE"
                                  :body-color "#FCF1EE"
                                  :title-color "#333"
                                  :body "Some body text to display"}
                         :url "http://aan.io"}
```

## To Dos
The following endpoints are yet to be completed:

  - `register-webhook`
  - `delete-webhook`
  - `list-webhooks`
  - `upload-attachment`
  - `register-attachment`
  - `deregister-attachment`



## Questions & Shortcomings?
I don't have my Mondo account/card/app yet so this is pre-pre-alpha. That being said...

1. Would be really good to have an API version made available. Adding the request header `X-Api-Version` to make sure of  compatibility going forward.
2. It's unclear at this time whether Pagination allows for `before` to be either an instant or a transaction-id.

## License

Copyright © 2015 [Adam Neilson](http://aan.io)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0.
