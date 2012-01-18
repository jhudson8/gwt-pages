## Status
This project is not maintained now that that the GWT team has release an official application framework

It has been moved from google code to centralize projects: http://code.google.com/p/gwtpages

## Description
Do you find yourself re-inventing the wheel for every GWT-based project you take on? Do you feel like there should be an easy way of handling page navigation events? GWT Pages is a flexible and surprisingly simple framework which does just that - and more.

Although GWT Pages provides a lot of functionality, it isn't meant to be a "kitchen sink" framework. The core product is a focused page controller which lets you as the developer use best of breed technologies that are best suited to work with your unique requirements.

Here are a few examples of what GWT Pages can do for you:

- Automatic history token management with easy access to additional data parameters
- Different page request parameter types are automatically serialized and deserialized to and from the history token (Boolean, String, Integer, Long, Float, Double, Date and Date/Time)
- Easy Code splitting of your view and presenter classes (or just your page widgets if not using MVP pattern)
- Easy way to provide and view page and field scoped application messages
- Detailed page request lifecycle events to customize or add to the default behavior of GWT Pages
- Makes using UiBinder very easy - all the UiBinder code is written for you
