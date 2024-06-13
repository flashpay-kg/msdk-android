[![Download](https://maven-badges.herokuapp.com/maven-central/kg.flashpay/msdk-android/badge.svg) ](https://maven-badges.herokuapp.com/maven-central/kg.flashpay/msdk-android/badge.svg)


# FlashPay MSDK

To open the payment form:
1. Create the `FlashPayMSDK` object.

2. Create the `SDKPaymentOptions` object.

This object must contain the following required parameters:

- `projectId`  (String) — a project identifier 
- `orderId`  (String) — a payment identifier unique within the project
- `currency`  (String) — the payment currency code in the ISO 4217 alpha-3 format
- `amount`  (int) — the payment amount in the smallest currency units
- `currency`  (String) — a customer's identifier within the project

```
val paymentOptions = SDKPaymentOptions(
            orderId = UUID.randomUUID().toString(),
            projectId = "YOUR PROJECT ID",
            payment = SDKPayment(
                amount = 10000,
                currency = "KGS"
            ),
            customer = mapOf("id" to "1", "email" to "test@customer.me"),
            paymentData = null
        )
```
3. Call setup method of `FlashPayMSDK` object
```
val params = msdk.setup(paymentOptions)
```

4. Calculate `signature1` and `signature2`.
```
signature1 = HMAC.sha256(
    key = "YOUR SECRET KEY",
    message = params.paramForSignature1
),
signature2 = HMAC.sha256(
    key = "YOUR SECRET KEY",
    message = params.paramForSignature2
)
```

5. Open the payment form and handle result.

```
val intent = msdk.buildPaymentIntent(
    activity = this@MainActivity,
    sdkPaymentOptions = paymentOptions,
    signature1 = HMAC.sha256(
        key = "YOUR SECRET KEY",
        message = params.paramForSignature1
    ),
    signature2 = HMAC.sha256(
        key = "YOUR SECRET KEY",
        message = params.paramForSignature2
    )
)
startActivityForResult.launch(intent)
```
