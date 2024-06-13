package kg.flashpay.msdk.testapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kg.flashpay.msdk.Constants
import kg.flashpay.msdk.FlashPayMSDK
import kg.flashpay.msdk.HMAC
import kg.flashpay.msdk.publicApi.SDKPayment
import kg.flashpay.msdk.publicApi.SDKPaymentOptions
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val msdk = FlashPayMSDK()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val params = msdk.setup(paymentOptions)

        setContent {
            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
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
                        },
                        content = {
                            Text(text = "openPaymentScreen")
                        }
                    )
                }
            }
        }
    }

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            when (result.resultCode) {
                Constants.RESULT_CANCELLED -> {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show()
                }

                Constants.RESULT_COMPLETED -> {
                    Toast.makeText(this, "Completed", Toast.LENGTH_LONG).show()
                }

                Constants.RESULT_ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
}
