# A crawler program problem

We have a lot of information online which can be obtained by a http request

There is a kind of urls:

1. Very expensive to request
2. Returns the same response within a short time period

We hope we can send request only when necessary.

So each time we want the result, we query it at local database first, find the latest 
record about the url, check if we need to resend request to update.
then send request and store the response to the database. In this model, the database
is in front of URL.

Stock history price API is a good example, these kinds of url generally have daily request
limit and update only once every day. We would like to check in the database to see if 
the stock we are looking for is up to date before we send out a url query request.

stock symbol + last_update_time can be an indicator of update

symbol,
date,
start price,
close price,
low price,
high price