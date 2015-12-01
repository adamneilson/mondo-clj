# mondo-clj

<img style="width:100%" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4NCjwhLS0gR2VuZXJhdG9yOiBBZG9iZSBJbGx1c3RyYXRvciAxNC4wLjAsIFNWRyBFeHBvcnQgUGx1Zy1JbiAuIFNWRyBWZXJzaW9uOiA2LjAwIEJ1aWxkIDQzMzYzKSAgLS0+DQo8IURPQ1RZUEUgc3ZnIFBVQkxJQyAiLS8vVzNDLy9EVEQgU1ZHIDEuMS8vRU4iICJodHRwOi8vd3d3LnczLm9yZy9HcmFwaGljcy9TVkcvMS4xL0RURC9zdmcxMS5kdGQiPg0KPHN2ZyB2ZXJzaW9uPSIxLjEiIGlkPSJMYXllcl8xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB4PSIwcHgiIHk9IjBweCINCgkgd2lkdGg9IjI0OC41MzlweCIgaGVpZ2h0PSI5Ny40ODFweCIgdmlld0JveD0iLTkxLjUgMjQuNSAyNDguNTM5IDk3LjQ4MSIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAtOTEuNSAyNC41IDI0OC41MzkgOTcuNDgxIg0KCSB4bWw6c3BhY2U9InByZXNlcnZlIj4NCjxnPg0KCTxnPg0KCQk8cGF0aCBmaWxsPSIjNTg4MUQ4IiBkPSJNNjYuMTk1LDg1LjYxMWMwLDAuODEyLTAuMzIzLDEuNTkyLTAuODk3LDIuMTY2bC0xMy43MjcsMTMuNzI3Yy0wLjc3MiwwLjc3Mi0yLjA5MywwLjIyNi0yLjA5My0wLjg2Nw0KCQkJVjY5Ljg4bDE2LjUyNi0xNi40MzRoMC4xOVY4NS42MTF6Ii8+DQoJCTxwYXRoIGZpbGw9IiM5MEI0RkUiIGQ9Ik01NS4yNDIsNDIuNDkzYy0wLjQ3OS0wLjQ3OS0xLjI1NS0wLjQ3OS0xLjczNCwwbC0yMC40MSwyMC40MWgtMC4yODR2MjMuMDM2bDAuMjg0LDAuNjA0bDE2LjM4MS0xNi4zODENCgkJCWwxNi43MTctMTYuNzE3TDU1LjI0Miw0Mi40OTN6Ii8+DQoJCTxwYXRoIGZpbGw9IiM2M0IxMzIiIGQ9Ik0wLDg1LjYxMWMwLDAuODEyLDAuMzIzLDEuNTkyLDAuODk4LDIuMTY2bDEzLjcyNiwxMy43MjdjMC43NzIsMC43NzIsMi4wOTMsMC4yMjYsMi4wOTMtMC44NjdWNjkuODgNCgkJCUwwLjE5LDUzLjQ0N0gwVjg1LjYxMXoiLz4NCgkJPHBhdGggZmlsbD0iIzkxREM0NyIgZD0iTTEyLjY4OCw0Mi40OTNjLTAuNDc5LTAuNDc5LTEuMjU1LTAuNDc5LTEuNzM0LDBMMCw1My40NDdsMTYuNzE3LDE2LjcxN2wxNi4zODEsMTYuMzgxVjYyLjkwMw0KCQkJTDEyLjY4OCw0Mi40OTN6Ii8+DQoJPC9nPg0KPC9nPg0KPGc+DQoJPGc+DQoJPC9nPg0KPC9nPg0KPGc+DQoJPHBhdGggZmlsbD0iIzE0MjMzQyIgZD0iTTQxLjU5LDE4MS4yODN2LTI2Ljc2OGg3LjEyOHYzLjU4OGMxLjY0NS0yLjc0MSw0LjAzOC00LjA4Nyw3LjI3OC00LjA4N2MzLjI0MSwwLDUuNjgzLDEuMzk2LDcuNDI4LDQuMjg3DQoJCWMyLjc5Mi0yLjk0MSw0LjkzNS00LjI4Nyw4LjI3NS00LjI4N2M2LjAzMiwwLDkuNDIxLDMuMzksOS40MjEsOS42MnYxNy42NDZoLTcuNjc3di0xNC41MDZjMC0zLjczOC0wLjc5Ny01LjQzNC0zLjczOS01LjQzNA0KCQljLTIuOTkxLDAtNC41ODYsMS44NDUtNC41ODYsNS40MzR2MTQuNTA2aC03LjcyN3YtMTQuNTA2YzAtMy43MzgtMC43OTgtNS40MzQtMy43MzktNS40MzRjLTIuOTkxLDAtNC41ODYsMS44NDUtNC41ODYsNS40MzQNCgkJdjE0LjUwNkg0MS41OXoiLz4NCgk8cGF0aCBmaWxsPSIjMTQyMzNDIiBkPSJNODkuNjExLDE1OC4wMDRjMi42OTEtMi42OTEsNi4yMy00LjE4OCw5Ljg2OS00LjE4OGMzLjg4OSwwLDcuMjI5LDEuMzQ3LDkuOTIsMy44ODkNCgkJYzIuODQyLDIuNjkxLDQuMzg3LDYuMzgxLDQuMzg3LDEwLjAyYzAsNC4wMzctMS4zNDYsNy4zNzctNC4wMzcsMTAuMDY5Yy0yLjc0MiwyLjc0MS02LjEzMSw0LjE4Ny0xMC4xNyw0LjE4Nw0KCQljLTQuMDg3LDAtNy4zNzctMS4zOTUtMTAuMTE5LTQuMjg2Yy0yLjU5Mi0yLjY5Mi0zLjkzOC02LjEzMi0zLjkzOC05Ljg3Uzg2Ljk2OSwxNjAuNjQ2LDg5LjYxMSwxNTguMDA0eiBNOTkuNjMxLDE3NC41MDQNCgkJYzMuNDg4LDAsNi4zMy0yLjk0MSw2LjMzLTYuNThjMC0zLjY4OC0yLjg0Mi02LjYyOS02LjMzLTYuNjI5Yy0zLjQ5LDAtNi4zMzIsMi45NC02LjMzMiw2LjYyOQ0KCQlDOTMuMywxNzEuNTYyLDk2LjE5MSwxNzQuNTA0LDk5LjYzMSwxNzQuNTA0eiIvPg0KCTxwYXRoIGZpbGw9IiMxNDIzM0MiIGQ9Ik0xMTguMTg5LDE4MS4yODN2LTI2Ljc2OGg3LjQ3OWwtMC4xMDEsMy41ODhjMS42NDYtMi42OTEsNC4xMzgtNC4wODcsNy42NzctNC4wODcNCgkJYzUuNTMzLDAsOS4xMjMsMy40ODksOS4xMjMsOS42MnYxNy42NDZoLTcuODc3di0xNC41MDZjMC0zLjczOC0wLjc5Ny01LjQzNC0zLjgzOC01LjQzNGMtMy4wOTIsMC00LjczNiwxLjg0NS00LjczNiw1LjQzNHYxNC41MDYNCgkJSDExOC4xODl6Ii8+DQoJPHBhdGggZmlsbD0iIzE0MjMzQyIgZD0iTTE2Ny43MDcsMTc4LjI5M2MtMi4zNDQsMi4yOTMtNC43MzYsMy4xNDEtNy45MjcsMy4xNDFjLTMuMjM5LDAtNi4wMzEtMS4wNDctOC4zNzQtMy4xNDENCgkJYy0zLjA5Mi0yLjc0Mi00LjY4Ni02LjMzMS00LjY4Ni0xMC41MTljMC0zLjgzOCwxLjQ5NC03LjI3OCw0LjI4Ni05Ljk3YzIuNDkzLTIuNDQyLDUuNTM0LTMuNjg4LDguOTIzLTMuNjg4DQoJCWMzLjI0LDAsNS43MzIsMS4wOTcsNy41MjcsMy4zOXYtMTEuNjY0aDcuODc2djM1LjQ0MmgtNy42MjZWMTc4LjI5M0wxNjcuNzA3LDE3OC4yOTN6IE0xNjEuNTI1LDE3NC4zMDUNCgkJYzMuMzksMCw2LjIzLTIuNzkxLDYuMjMtNi40OGMwLTMuODM4LTIuODQyLTYuNzI5LTYuNTI5LTYuNzI5Yy0zLjczOSwwLTYuNjMxLDMuMDQtNi42MzEsNi42MjkNCgkJQzE1NC41OTYsMTcxLjQ2MywxNTcuNDg3LDE3NC4zMDUsMTYxLjUyNSwxNzQuMzA1eiIvPg0KCTxwYXRoIGZpbGw9IiMxNDIzM0MiIGQ9Ik0xODMuODIzLDE1OC4wMDRjMi42OTItMi42OTEsNi4yMzEtNC4xODgsOS44Ny00LjE4OGMzLjg4OSwwLDcuMjI5LDEuMzQ3LDkuOTIsMy44ODkNCgkJYzIuODQyLDIuNjkxLDQuMzg3LDYuMzgxLDQuMzg3LDEwLjAyYzAsNC4wMzgtMS4zNDYsNy4zNzgtNC4wMzcsMTAuMDY5Yy0yLjc0MiwyLjc0MS02LjEzMiw0LjE4OC0xMC4xNyw0LjE4OA0KCQljLTQuMDg4LDAtNy4zNzctMS4zOTYtMTAuMTE5LTQuMjg3Yy0yLjU5Mi0yLjY5Mi0zLjkzOC02LjEzMi0zLjkzOC05Ljg3UzE4MS4xODIsMTYwLjY0NiwxODMuODIzLDE1OC4wMDR6IE0xOTMuODQzLDE3NC41MDQNCgkJYzMuNDg5LDAsNi4zMzEtMi45NDEsNi4zMzEtNi41OGMwLTMuNjg4LTIuODQyLTYuNjI5LTYuMzMxLTYuNjI5cy02LjMzMSwyLjk0LTYuMzMxLDYuNjI5DQoJCUMxODcuNTEyLDE3MS41NjIsMTkwLjQwMywxNzQuNTA0LDE5My44NDMsMTc0LjUwNHoiLz4NCjwvZz4NCjwvc3ZnPg0K">

This clojure lib gives simple hooks for the [Mondo API](https://getmondo.co.uk/docs/). It's pre-pre-alpha as I'm a couple of days away from getting my grubby paws on my shiny new Mondo account. So as of yet it's not been tested in the slightest.

## Installing 
Current [semantic](http://semver.org/) version:

[![Clojars Project](http://clojars.org/mondo-clj/latest-version.svg)](http://clojars.org/mondo-clj)

To use with Leiningen, add

```clojure
:dependencies [[mondo-clj 0.0.5]]
```
to your project.clj.

You can use it in a source file like this:

```clojure
(:require [mondo-clj.core :as mondo])
```
or by REPL:

```clojure
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

```clojure
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

```clojure
(mondo/whoami access-token)
```

returns:

```clojure
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :authenticated true,
 :client-id "client_id",
 :user-id "user_id"}
```

### Accounts

To get a list of accounts:

```clojure
(mondo/list-accounts access-token)
```
Returns something like:

```clojure
{:accounts [{:id "acc_00009237aqC8c5umZmrRdh"
             :description "Peter Pan's Account"
             :created #inst "2015-12-01T00:00:00.000-00:00"}]}

```

### Balance
Get the balance of a specific account

```clojure
(mondo/read-balance access-token account-id)
```
Returns:

```clojure
{:balance 50.0
 :currency "GBP"
 :spend-today 0.0}
```
NB: The [Mondo docs](https://getmondo.co.uk/docs/#balance) currently say that the `balance` and `spend-today` return types are 64bit integers in minor units of the currency. Eg. GBP `5000` is £50.00. This seems counter-intuitive to me so I'll be coercing the `balance` and `spend-today` to *double* type so `50.0` is £50.00.

### Transactions
Get details of a single transaction. NB: full merchant details are returned:

```clojure
(mondo/get-transaction access-token transaction-id)
```

Returns 
```clojure
{:transaction {:account-balance 130.13
               :amount -5.10
               :created #inst "2015-12-01T00:00:00.000-00:00"
               :currency "GBP"
               :description "THE DE BEAUVOIR DELI C LONDON GBR"
               :id "tx_00008zIcpb1TB4yeIFXMzx"
               :merchant {:address {:address "98 Southgate Road"
                                    :city "London"
                                    :country "GB",
                                    :latitude 51.54151
                                    :longitude -0.08482400000002599
                                    :postcode "N1 3JD"
                                    :region "Greater London"}
                          :created #inst "2015-12-01T00:00:00.000-00:00"
                          :group_id "grp_00008zIcpbBOaAr7TTP3sv"
                          :id "merch_00008zIcpbAKe8shBxXUtl"
                          :logo "https://pbs.twimg.com/profile_images/527043602623389696/68_SgUWJ.jpeg"
                          :emoji "🍞"
                          :name "The De Beauvoir Deli Co."
                          :category "eating_out"}
               :metadata {}
               :notes "Salmon sandwich 🍞"
               :is_load false
               :settled true}}
```

Or list transactions

```clojure
(mondo/list-transactions access-token account-id)

;or 

(mondo/list-transactions access-token account-id {:since #inst "2015-12-01T00:00:00.000-00:00"})

;or 

(mondo/list-transactions access-token account-id {:before #inst "2015-12-01T00:00:00.000-00:00"})

;or 

(mondo/list-transactions access-token account-id {:since #inst "2015-12-01T00:00:00.000-00:00" :limit 48})
```

You can also add meta-data to a transaction

```clojure
(mondo/annotate-transaction access-token transaction-id {:foo "bar" :baz "quux"})
```


### Feed

You can inject items into an accounts feed

```clojure
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

### Webhooks
Web hooks allow your application to receive real-time, push notification of events in an account. 

You can register a webhook:

```clojure
(mondo/register-webhook access-token account-id webhook-url)
```

List the web hooks registered on an account:

```clojure
(mondo/list-webhooks access-token account-id)
```

Or delete a webhook to stop receiving transaction events:

```clojure
(mondo/delete-webhook access-token webhook-id)
```

### Attachments

The first step when uploading an attachment is to obtain a temporary URL to which the file can be uploaded. The response will include a `:file-url` which will be the URL of the resulting file, and an `:upload-url` to which the file should be uploaded to.
```clojure
(mondo/upload-attachment access-token file-name file-type)
```
Returns:
```clojure
{:file-url "https://s3-eu-west-1.amazonaws.com/mondo-image-uploads/user_00009237hliZellUicKuG1/LcCu4ogv1xW28OCcvOTL-foo.png"
 :upload-url "https://mondo-image-uploads.s3.amazonaws.com/user_00009237hliZellUicKuG1/LcCu4ogv1xW28OCcvOTL-foo.png?AWSAccessKeyId=AKIAIR3IFH6UCTCXB5PQ\u0026Expires=1447353431\u0026Signature=k2QeDCCQQHaZeynzYKckejqXRGU%!D(MISSING)"}
```

Once you have obtained a URL for an attachment, either by uploading to the `:upload-url` obtained from the `upload-attachment` endpoint above or by hosting a remote image, this URL can then be registered against a transaction. Once an attachment is registered against a transaction this will be displayed on the detail page of a transaction within the Mondo app.
 ```clojure
(mondo/register-attachment access-token external-id file-url file-type)
```
Returns:
```clojure
{:attachment {:id "attach_00009238aOAIvVqfb9LrZh"
              :user_id "user_00009238aMBIIrS5Rdncq9"
              :external-id "tx_00008zIcpb1TB4yeIFXMzx"
              :file-url "https://s3-eu-west-1.amazonaws.com/mondo-image-uploads/user_00009237hliZellUicKuG1/LcCu4ogv1xW28OCcvOTL-foo.png"
              :file-type "image/png"
              :created #inst "2015-12-01T00:00:00.000-00:00"}}

```

To remove an attachment, simply deregister this using its `id`
 ```clojure
(mondo/deregister-attachment access-token id)
```


## Questions & Shortcomings?
I don't have my Mondo account/card/app yet so this is pre-pre-alpha. That being said...

1. Would be really good to have an API version made available. Adding the request header `X-Api-Version` to make sure of  compatibility going forward.
2. It's unclear at this time whether Pagination allows for `before` to be either an instant or a transaction-id.


## License

Copyright © 2015 [Adam Neilson](http://aan.io)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0.
