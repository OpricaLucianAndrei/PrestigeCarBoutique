package it.capstone.prestigecarboutique.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/prestigecarboutique/pagamenti")
public class StripeController {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostMapping("/payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> requestData) {
        try {
            // Verifica e recupero dei valori richiesti
            Integer amount = requestData.get("amount") != null ? Integer.parseInt(requestData.get("amount").toString()) : null;
            String currency = requestData.get("currency") != null ? requestData.get("currency").toString() : null;

            // Verifica se i valori richiesti sono nulli o non validi
            if (amount == null || currency == null) {
                return ResponseEntity.badRequest().body("Missing or invalid amount/currency");
            }

            // Continua con la creazione del PaymentIntent
            Stripe.apiKey = stripeSecretKey;
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setAmount((long) amount)
                    .setCurrency(currency)
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", intent.getClientSecret());

            return ResponseEntity.ok(responseData);
        } catch (NumberFormatException | StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create payment intent");
        }
    }

}
