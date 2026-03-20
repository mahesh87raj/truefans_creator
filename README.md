# TrueFans Creator App

<<<<<<< HEAD
## About the Project
This project was built as part of the TrueFans Hackathon.

The idea behind the app is simple — creators can showcase their profile and fans can support them by sending tips. The main focus of this project is to create a smooth and realistic payment experience along with a clean and user-friendly UI.

---

## What I Built

### 1. Creator Profile Screen
- Shows creator image, name, and bio
- Follow / Unfollow button
- Tip button with bottom sheet (₹50 / ₹100 / ₹500)
- Grid layout for past content (using mock data)
- Smooth scrolling and simple UI

---

### 2. Fan Payment Flow

#### Support Creator Screen
- Three tip options: ₹50, ₹100, ₹500
- Option to enter custom amount

#### Checkout Screen
- Shows selected amount
- 10% platform fee
- Final amount received by creator

#### Payment Screen
- Razorpay integration (Test Mode)
- (In case not working, a simulated payment flow is used)

#### Success / Failure Screen
- Shows payment result
- Displays transaction ID
- Option to try again or go back

---

## Tech Stack Used
- Kotlin / Java
- Android Studio
- MVVM Architecture
- Room Database
- Razorpay SDK (Test Mode)

---

## How to Run the Project

1. Clone this repository:

##### Razorpay test key 

1.val RAZORPAY_KEY_ID = "rzp_test_5S8f7mY9ZkL2qP"
=======
## Razorpay Integration Setup

To enable payments in this app, you need to set up your Razorpay Test Key.

### Steps to get Razorpay Test Key:
1. Sign up/Log in to the [Razorpay Dashboard](https://dashboard.razorpay.com/).
2. Select the **Test Mode** from the top right toggle.
3. Navigate to **Account & Settings** > **API Keys**.
4. Click on **Generate Test Key**.
5. You will get a **Key ID** (e.g., `rzp_test_xxxxxxxxxxxxxx`).

### Setup in Android Studio:
1. Open `app/src/main/java/com/example/truefans_creator/PaymentGatewayActivity.kt`.
2. Locate the `RAZORPAY_KEY_ID` variable.
3. Replace the placeholder with your actual **Key ID**.

```kotlin
private val RAZORPAY_KEY_ID = "YOUR_RZP_TEST_KEY_HERE"
```

### Razorpay Test Credentials:
Use these for testing successful payments:

| Field | Value |
|-------|-------|
| **Card Number** | `4111 1111 1111 1111` (Visa) |
| **Expiry** | `12 / 2030` (Any future date) |
| **CVV** | `123` |
| **OTP** | `123456` |

*For more test cards (Mastercard, Rupay, etc.), visit [Razorpay Test Cards](https://razorpay.com/docs/payments/payments/test-card-details/).*
>>>>>>> b25059f (Updated project files)
