# HierarchicalUserSystem
User system with three layers. Institution at top, Admin in middle and Regulars at bottom. UNDER DEVELOPMENT.

## Hierarchical User System

<b>Structure:</b>

Institution<br>
    |<br>
  Admin<br>
    |<br>
 Regular<br>


## How to run:
Run Main.java to get to login-page.

test.db contains an Institution with following credentials (can be used to login).<br>
<b>Email:</b> badhuset@sthlm.se<br>
<b>Password:</b> badhuset<br>
<b>USERTYPE:</b> INSTITUTION<br>

To add Users go to register page and register.
<b>You can currently only login as Institution.</b>



## More info

This is a system for handling registration requests. When an institution is registered it can accept or deny registration requests from Users (Admin and Regulars).
There are three types of Users. <br><br>

<b>Structure:</b>

Institution<br>
    |<br>
  Admin<br>
    |<br>
 Regular<br><br>
 
 In this way the top layer (Institution) can stop misuse and faulty registrations.
 <br>
 
 ## TODO
 
 Admin needs to be able to add Regulars. Admin and Regulars needs to have their own "logged-in-scene".<br>
 Test with JUnit.<br>
 Fix all small bugs.<br>
 Functionality issued with TODO within the code.
 
 