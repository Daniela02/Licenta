package com.example.easyappointment.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.GoogleApi.GooglePay.PaymentsUtil;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import io.objectbox.Box;

public class PayForAppointmentActivity extends AppCompatActivity {
    public static final String APPOINTMENT_ID = "APPOINTMENT_ID";
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private Appointments appointment;
    private TextView serviceName;
    private TextView providerOrClientName;
    private TextView startTime;
    private Button cancelButton;
    private Button acceptButton;
    private TextView status;
    private LinearLayout getDirections;
    private Box<Appointments> appointmentsBox;
    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    private View googlePayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_appointment);
        // Set up the mock information for our item in the UI.
        appointmentsBox = ObjectBox.get().boxFor(Appointments.class);

        appointment = appointmentsBox.get(getIntent().getLongExtra(APPOINTMENT_ID, 0));
        initItemUI();

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode  Result code returned by the Google Pay API.
     * @param data        Intent from the Google Pay API containing payment or error data.
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     * from an Activity</a>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }


    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * PaymentsClient.html#isReadyToPay(com.google.android.gms.wallet.
     * IsReadyToPayRequest)">PaymentsClient#IsReadyToPay</a>
     */
    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    /**
     * If isReadyToPay returned {@code true}, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns {@code
     * false}.
     *
     * @param available isReadyToPay API response.
     */
    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "GooglePay is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see <a href="https://developers.google.com/pay/api/android/reference/
     * object#PaymentData">PaymentData</a>
     */
    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String tokenizationType = tokenizationData.getString("type");
            final String token = tokenizationData.getString("token");

            if ("PAYMENT_GATEWAY".equals(tokenizationType) && "examplePaymentMethodToken".equals(token)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Do you agree to proceed with the payment?")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

            Toast.makeText(
                    this, "Payment to " + appointment.provider_service.getTarget().provider.getTarget().account.getTarget().name,
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     *                   WalletConstants.ERROR_CODE_* constants.
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * WalletConstants#constant-summary">Wallet Constants Library</a>
     */
    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        double garmentPrice = appointment.getPrice();
        long garmentPriceCents = Math.round(garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.longValue());
        long priceCents = garmentPriceCents;

        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }

    }


    private void initItemUI() {
        serviceName = findViewById(R.id.appointment_service_name);
        providerOrClientName = findViewById(R.id.appointment_provider);
        startTime = findViewById(R.id.appointment_start_time);
        status = findViewById(R.id.appointment_status);
        getDirections = findViewById(R.id.appointment_direction);
        cancelButton = findViewById(R.id.appointment_cancel_button);
        acceptButton = findViewById(R.id.appointment_accept_button);
        googlePayButton = findViewById(R.id.googlepay_button);

        status.setText(appointment.provider_service.getTarget().service.getTarget().price + "RON");
        cancelButton.setVisibility(View.GONE);
        acceptButton.setVisibility(View.GONE);
        getDirections.setVisibility(View.GONE);
        serviceName.setText(appointment.provider_service.getTarget().service.getTarget().name);
        providerOrClientName.setText(appointment.provider_service.getTarget().provider.getTarget().account.getTarget().name);
        startTime.setText(appointment.start_time);

        googlePayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPayment(view);

                        appointment.payed = true;
                        appointmentsBox.put(appointment);
                    }
                });
    }

}
