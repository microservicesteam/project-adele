# Order Manager
### POST `/orders`
Input parameters
- JSON body
```
{
    name: "MZo",
    email: "user@domain.com",
    reservationId: "b315d65a-37b8-4ba2-8417-031592afd673"
}
```

Output
- orderId: `a316465a-37b8-4ba2-8417-031592afzklf`


### GET `/orders/{orderId}/approval`
Input parameters
- path param, `orderId`

Output
- redirect URL
```
{
  "href": "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C",
  "rel": "approval_url",
  "method": "REDIRECT"
}
```

### GET `/orders/{orderId}/approvals?status=success&PayerID=abcd1234`
Input parameters
- path param `orderId`:
- query param `PayerID`: id from PayPal to execute the payment

Output
- redirect response (HTTP 302) to the success page

### GET `/orders/{orderId}`
Input parameters

Output
- public representation of the order (TBD)