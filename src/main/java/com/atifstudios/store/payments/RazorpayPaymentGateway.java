package com.atifstudios.store.payments;

import com.atifstudios.store.orders.Order;
import com.atifstudios.store.orders.OrderItem;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentGateway implements PaymentGateway {
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            // Multiply by 100 to convert Rupees to Paise
            BigDecimal amountInPaise = order.getTotalPrice().multiply(BigDecimal.valueOf(100));

            // Round to the nearest whole number to remove decimal fractions safely
            // (e.g., 2.70 Rupees becomes 270 Paise)
            int totalAmountInPaise = amountInPaise.setScale(0, RoundingMode.HALF_UP).intValue();

            // Initialize the main Razorpay payload
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalAmountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rec_id_" + order.getId());

            // Create the notes payload for order items metadata
            JSONObject notes = new JSONObject();
            StringBuilder itemSummary = new StringBuilder();

            for (OrderItem item: order.getItems()) {
                itemSummary.append(item.getProduct().getName())
                        .append(" (x")
                        .append(item.getQuantity())
                        .append("), ");
            } // loop ends

            // remove last two character
            if (!itemSummary.isEmpty()) {
                itemSummary.setLength(itemSummary.length() - 2);
            }

            notes.put("items_summary", itemSummary.toString());
            orderRequest.put("notes", notes);

            // Create the Razorpay Order
            // THIS IS THE MOMENT: Your server travels across the web,
            // books the transaction on Razorpay's database, and waits for a reply.
            var razorpayOrder = razorpayClient.orders.create(orderRequest);
            return new CheckoutSession(razorpayOrder.get("id"));
        } catch (RazorpayException e) {
            throw new PaymentException(e.getMessage());
        }

    } // method ends

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        String signature = request.getHeaders().get("X-Razorpay-Signature");

        try {
            boolean isValidSignature = Utils.verifyWebhookSignature(request.getPayload(), signature, webhookSecretKey);

            if (!isValidSignature) {
                throw new InvalidWebhookSignatureException();
            }

            // Parse the payload text into JSON
            JSONObject jsonPayload = new JSONObject(request.getPayload());

            String eventType = jsonPayload.getString("event");

            JSONObject paymentEntity = getPaymentEntity(jsonPayload);
            String razorpayOrderId = paymentEntity.getString("order_id");
            String razorpayPaymentId = paymentEntity.getString("id");

            return switch (eventType) {
                case "payment.captured", "order.paid" -> 
                     Optional.of(new PaymentResult(razorpayOrderId, razorpayPaymentId, PaymentStatus.PAID));
                case "payment.failed" ->
                     Optional.of(new PaymentResult(razorpayOrderId, razorpayPaymentId, PaymentStatus.FAILED));
                default -> Optional.empty();
            };

        } catch (Exception e) {
            throw new WebhookProcessingException(e.getMessage());
        }
    } // method ends

    private JSONObject getPaymentEntity(JSONObject jsonPayload) {
        return jsonPayload.getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");
    } // end

} // class ends
