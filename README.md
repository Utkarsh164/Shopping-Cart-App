# Shopping App

## Overview

The Shopping App is an Android-based e-commerce application that allows users to browse products, add them to their cart, and make changes to the cart quantities in real-time. The app integrates Firebase for user authentication and real-time data storage, and Retrofit for fetching product data from a remote API. Users can also view their cart, remove items, and see the current quantity of each product.

## Features

- **User Authentication**: Users can sign in and sign out using Firebase Authentication.
- **Product Listing**: Fetch products from a remote API (dummyjson.com) and display them in a list using Retrofit.
- **Real-time Cart Count**: Track and display the number of items in the cart in real-time using Firebase Realtime Database.
- **Cart Management**: Users can view, add, and remove products from their cart. Quantity adjustments are synchronized with Firebase.

## Technologies Used

- **Firebase**: For user authentication and real-time database storage.
- **Retrofit**: For making network requests and fetching product data from an API.
- **Picasso**: For loading images from URLs into `ImageView`.
- **RecyclerView**: For displaying product lists and cart items.

## Setup Instructions

### Prerequisites

1. **Android Studio**: Make sure you have Android Studio installed with the necessary SDKs.
2. **Firebase Project**: Set up a Firebase project in the Firebase Console and configure Firebase Authentication and Firebase Realtime Database.
3. **API Key**: The app fetches product data from the API at `https://dummyjson.com/product`. No special API key is required for this.

### Steps to Run the Project

1. Clone the repository to your local machine.
   ```bash
   git clone https://github.com/Utkarsh164/Shopping-Cart-App
