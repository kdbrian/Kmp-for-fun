package com.farmconnect.admin.core.domain.dto

import com.farmconnect.admin.core.util.PaymentMethods
import com.farmconnect.admin.core.util.ResponseBody
import kotlinx.serialization.Serializable
import kotlin.time.Instant

object PaymentUtils {

    interface Payment {
        val method: String
        val amount: Double
        val subject: String
        val commodity: String

        val phoneNumber: String
        val addedOn: Long get() = Instant.now().epochSeconds
        val updatedOn: Long get() = Instant.now().epochSeconds
    }

    //    @Serializable
    @ConsistentCopyVisibility
    data class MPesaPaymentRecord internal constructor(
        val id: String = "",
        override val phoneNumber: String = "",
        override val method: String = PaymentMethods.Mpesa.name,
        val failureReason: String = "",
        override val amount: Double = 0.0,
        override val commodity: String = "",
        val status: String = "pending",
        val stkPush: Map<String, Any> = emptyMap(),
        val mpesaCallback: Map<String, Any> = emptyMap(),
        override val subject: String = ""
    ) : Payment {

        class Builder {
            var id: String = ""
            var method: PaymentMethods = PaymentMethods.Mpesa
            var amount: Double = 0.0
            var commodity: String = ""
            var failureReason: String = ""
            var status: String = "pending"

            var subject: String = ""
            var addedOn: Long = Instant.now().epochSeconds
            var updatedOn: Long = Instant.now().epochSeconds
            var phoneNumber: String = ""


            fun id(id: String) = apply { this.id = id }
            fun method(method: PaymentMethods) = apply { this.method = method }
            fun subject(subject: String) = apply { this.subject = subject }
            fun amount(amount: Double) = apply { this.amount = amount }
            fun commodity(commodity: String) = apply { this.commodity = commodity }
            fun addedOn(addedOn: Long) = apply { this.addedOn = addedOn }
            fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
            fun updatedOn(updatedOn: Long) = apply { this.updatedOn = updatedOn }
            fun failureReason(failureReason: String) = apply { this.failureReason = failureReason }



            fun build() = MPesaPaymentRecord(
                id = id,
                method = method.name,
                phoneNumber = phoneNumber,
                amount = amount,
                subject = subject,
                commodity = commodity
            )
        }
    }


    @Serializable
    data class MpesaPaymentResponse(
        override val message: String = "",
        override val success: Boolean = false,
        val checkoutRequestID: String = ""
    ) : ResponseBody


}