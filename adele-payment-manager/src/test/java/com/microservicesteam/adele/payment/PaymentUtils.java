package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;
import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;
import static java.math.BigDecimal.TEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import org.assertj.core.util.Lists;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;

public class PaymentUtils {

    private static final String PAYER_ID = "payerId";
    private static final String PAYMENT_ID = "paymentId";

    public static PaymentRequest paymentRequest() {
        return PaymentRequest.builder()
                .addTickets(Ticket.builder()
                        .sector(1)
                        .priceAmount(TEN)
                        .build())
                .currency(Currency.getInstance("EUR"))
                .programName("Adele Concert 2018")
                .programDescription("Adele Concert 2018 London")
                .returnUrl("http://adeleproject.com/paymentAtRequest?status=success")
                .cancelUrl("http://adeleproject.com/paymentAtRequest?status=failure")
                .build();
    }

    public static PaymentResponse paymentResponse() {
        return PaymentResponse.builder()
                .paymentId("PAY-2N032050XT284644ULHXZJ7Q")
                .status(CREATED)
                .approveUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-4EY8952242123341P")
                .build();
    }

    public static Payment paymentAtRequest() {

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal("10.00");

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl("http://adeleproject.com/paymentAtRequest?status=success");
        redirectUrls.setCancelUrl("http://adeleproject.com/paymentAtRequest?status=failure");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);

        return payment;

    }

    public static Payment paymentAtResponse() {

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("1.00");

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        Links execute_url = new Links("https://api.sandbox.paypal.com/v1/payments/payment/PAY-2N032050XT284644ULHXZJ7Q/execute", "execute");
        Links approval_url = new Links("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-4EY8952242123341P", "approval_url");

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setId("PAY-2N032050XT284644ULHXZJ7Q");
        payment.setIntent("sale");
        payment.setState("created");
        payment.setPayer(payer);
        payment.setCreateTime("2017-10-24T19:31:10Z");
        payment.setTransactions(Lists.newArrayList(transaction));
        payment.setLinks(Arrays.asList(execute_url, approval_url));

        return payment;
    }

    public static Payment paymentAtExecution() {
        Payment payment = new Payment();
        payment.setId(PAYMENT_ID);
        payment.setState("approved");
        return payment;
    }

    public static ExecutePaymentRequest executePaymentRequest() {
        return ExecutePaymentRequest.builder()
                .payerId(PAYER_ID)
                .paymentId(PAYMENT_ID)
                .build();
    }

    public static PaymentExecution paymentExecution() {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PAYER_ID);
        return paymentExecution;
    }

    public static ExecutePaymentResponse executePaymentResponse() {
        return ExecutePaymentResponse.builder()
                .paymentId(PAYMENT_ID)
                .status(APPROVED)
                .build();
    }

    public static ExecutePaymentResponse failedExecutePaymentResponse() {
        return ExecutePaymentResponse.builder()
                .paymentId(PAYMENT_ID)
                .status(FAILED)
                .build();
    }
}
