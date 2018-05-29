package com.microservicesteam.adele.payment.paypal;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.microservicesteam.adele.payment.PaymentRequest;
import com.microservicesteam.adele.payment.Ticket;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;

@Component
public class PaymentRequestMapper {
    public Payment mapTo(PaymentRequest paymentRequest) {

        BigDecimal totalAmount = paymentRequest.tickets().stream()
                .map(Ticket::priceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Amount amount = new Amount();
        amount.setCurrency(paymentRequest.currency().getCurrencyCode());
        amount.setTotal(totalAmount.setScale(2, ROUND_CEILING).toEngineeringString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("This is the payment of your tickets!");

        //TODO make these details dynamic
        Item item = new Item();
        item.setName("Adele 2018");
        item.setDescription("Adele concert ticket");
        item.setCurrency(paymentRequest.currency().getCurrencyCode());
        item.setQuantity("1");
        item.setPrice(totalAmount.setScale(2, ROUND_CEILING).toEngineeringString());

        ItemList itemList = new ItemList();
        itemList.setItems(singletonList(item));

        transaction.setItemList(itemList);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(paymentRequest.returnUrl());
        redirectUrls.setCancelUrl(paymentRequest.cancelUrl());

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(singletonList(transaction));

        return payment;
    }
}
