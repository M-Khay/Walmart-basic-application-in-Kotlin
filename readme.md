# Take home assignment - Debugging Exercise

This repository contains an implementation of the Take Home Assignment with some known bugs for you to track down and fix.

## Issues
- Images are failing to load in live flavor
- App unit tests are failing, can you take a look?
- On initial load no loading spinner shown
- Navigating back from Product details exits app rather than returning to list fragment
- Rotating Details fragment switches to display the first item in the list.

## Spec
The next step is a take-home assignment, which consists of developing an Android app –

Create an app that uses WalmartLabs take-home assignment API.

The application should have two screens:

Screen 1:
First screen should contain a List of all the products returned by the service call.
The list should support Lazy Loading. When scrolled to the bottom of the list, start lazy loading next page of products and append it to the list.
When a product is clicked, it should go to the second screen.

Screen 2:
Second screen should display details of the product.
We should be able to swipe to next/previous items on this screen.
API Endpoint: (https://mobile-tha-server.firebaseapp.com/)[https://mobile-tha-server.firebaseapp.com/]

We'll be looking at your coding style, use of data structures, Collections, and overall Android SDK knowledge.
Handling orientation changes efficiently will be a plus.
It's up to you to impress us with this assignment. The list of products and the details screen can be a simple or as "fancy" as you'd like it to be. Include product image for each product. Add some animations. Try to have some fun!