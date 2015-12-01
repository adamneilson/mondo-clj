# mondo-clj

<img src="https://cdn.rawgit.com/adamneilson/mondo-clj/master/resources/mondo-logo.svg" style="width:60%;display:block;margin:auto;">

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
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :accounts [{:id "acc_00009237aqC8c5umZmrRdh"
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
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :balance 50.0
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
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :transaction {:account-balance 130.13
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
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :file-url "https://s3-eu-west-1.amazonaws.com/mondo-image-uploads/user_00009237hliZellUicKuG1/LcCu4ogv1xW28OCcvOTL-foo.png"
 :upload-url "https://mondo-image-uploads.s3.amazonaws.com/user_00009237hliZellUicKuG1/LcCu4ogv1xW28OCcvOTL-foo.png?AWSAccessKeyId=AKIAIR3IFH6UCTCXB5PQ\u0026Expires=1447353431\u0026Signature=k2QeDCCQQHaZeynzYKckejqXRGU%!D(MISSING)"}
```

Once you have obtained a URL for an attachment, either by uploading to the `:upload-url` obtained from the `upload-attachment` endpoint above or by hosting a remote image, this URL can then be registered against a transaction. Once an attachment is registered against a transaction this will be displayed on the detail page of a transaction within the Mondo app.
 ```clojure
(mondo/register-attachment access-token external-id file-url file-type)
```
Returns:
```clojure
{:status {:success? true 
          :error-code 200 
          :error-name "OK" 
          :error-body "All is well."}
 :attachment {:id "attach_00009238aOAIvVqfb9LrZh"
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


> Just need to get my hands on one of these now....
![](https://raw.githubusercontent.com/adamneilson/mondo-clj/master/resources/mondo-alpha-card.png)

## License

Copyright © 2015 [Adam Neilson](http://aan.io)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0.
