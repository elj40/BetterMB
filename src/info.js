// This file is just to keep track of what kind of json parameters is sent and
// received by the uni's web client

// Action
request_method =
request_link   =
request_body   =
response_body  =

// Click on date
request_method = GET
request_link   = /student-meal-booking/spring/api/get-meal-slots-dto/2025-09-17/en
request_body   =
response_body  = [
    {"code":"0","description":"Select"},
    {"code":"B","description":"Breakfast"},
    {"code":"L","description":"Lunch"},
    {"code":"D","description":"Dinner"}
]

// Select meal slot
request_method = GET
request_link   = /student-meal-booking/spring/api/get-meal-slot-facilities/en/2025-09-17/B
request_body   =
response_body  = [
    {"code":"0","description":"Select"},
    {"code":"8","description":"Minerva"},
    {"code":"9","description":"Dagbreek"},
    {"code":"11","description":"Huis Ten Bosch"},
    {"code":"20","description":"Lydia"},
    {"code":"22","description":"Majuba"},
    {"code":"27","description":"VictoriaHub"}
]

// Select meal facility
request_method = GET
request_link   = /student-meal-booking/spring/api/get-meal-slot-facility-options/en/2025-09-17/B/22
request_body   =
response_body  = [
    {"code":"0","description":"Select","cost":null,"sessionId":0,"sessionStart":"","sessionEnd":"","bookable":false,"reason":null},
    {"code":"159523","description":"Take Away Prepacked Meal","cost":"R44.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"160095","description":"Vegetarion","cost":"R21.79","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"160667","description":"Chicken Meal","cost":"R18.79","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"161239","description":"Halal Friendly Meal","cost":"R18.79","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"161811","description":"Lactose Free Meal","cost":"R18.79","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"195628","description":"Farmhouse Breakfast","cost":"R71.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"195968","description":"Breakfast Wrap Egg Bacon Tomat","cost":"R58.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"196308","description":"Omelet Option","cost":"R60.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"196648","description":"Cheesegriller Option","cost":"R67.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"196988","description":"Breakfast Waffle Option","cost":"R58.00","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""},
    {"code":"25159","description":"Standard Meal","cost":"R18.79","sessionId":39,"sessionStart":"07:15","sessionEnd":"08:30","bookable":true,"reason":""}
]

// Select meal
// Nothing happens
request_link   =
request_body   =
response_body  =

// Select book ahead days
// Nothing happens
request_link   =
request_body   =
response_body  =

// Submit
request_method = POST
request_link   = /student-jeal-booking/spring/api/store-meal-booking/en
request_body   = {"mealDate":"2025-09-17","mealSlot":"B","mealFacility":"22","mealOption":"25159","mealSession":39,"excludeWeekends":true}
{"mealDate":"2025-09-15","mealSlot":"B","mealFacility":"22","mealOption":"25009","mealSession":39,"advanceBookingDays":"4","excludeWeekends":true}
response_body  = [
    {"bookingDate":"2025-09-17","bookingMessage":"Booking successful"}
]
[{"bookingDate":"2025-09-15","bookingMessage":"Booking successful"},{"bookingDate":"2025-09-16","bookingMessage":"Booking successful"},{"bookingDate":"2025-09-17","bookingMessage":"Reservation already exists"},{"bookingDate":"2025-09-18","bookingMessage":"Booking successful"},{"bookingDate":"2025-09-19","bookingMessage":"Booking successful"}]
// Get meals booked this month
request_method = GET
request_link   = /student-meal-booking/spring/api/get-meal-bookings-dto/en/2025-08-31
request_body   =
response_body  = [
    {"canModify":false,"title":"Lunch","start":"2025-08-31T11:00","description":"Sunday Roast Beef","facility":"Majuba","mealTime":"11:00 - 13:00","mealCost":"R44.00","mealSlot":"L","backgroundColor":"#28793d","borderColor":"#28793d","id":8172130},
    {"canModify":false,"title":"Breakfast","start":"2025-09-01T07:15","description":"Fried Egg Cheese Tomato","facility":"Majuba","mealTime":"07:15 - 08:30","mealCost":"R18.79","mealSlot":"B","backgroundColor":"#e29e20","borderColor":"#e29e20","id":8172107},
    {"canModify":false,"title":"Lunch","start":"2025-09-01T12:30","description":"Chicken Pie","facility":"Majuba","mealTime":"12:30 - 13:30","mealCost":"R44.00","mealSlot":"L","backgroundColor":"#28793d","borderColor":"#28793d","id":8186151},
    {"canModify":false,"title":"Breakfast","start":"2025-09-02T07:15","description":"Standard Meal","facility":"Majuba","mealTime":"07:15 - 08:30","mealCost":"R18.79","mealSlot":"B","backgroundColor":"#e29e20","borderColor":"#e29e20","id":8172108},
    {"canModify":false,"title":"Lunch","start":"2025-09-02T12:30","description":"Standard Meal","facility":"Majuba","mealTime":"12:30 - 13:30","mealCost":"R44.00","mealSlot":"L","backgroundColor":"#28793d","borderColor":"#28793d","id":8186207},
    {"canModify":false,"title":"Breakfast","start":"2025-09-03T07:15","description":"Standard Meal","facility":"Majuba","mealTime":"07:15 - 08:30","mealCost":"R18.79","mealSlot":"B","backgroundColor":"#e29e20","borderColor":"#e29e20","id":8172109},
    ....
]

// Cancel meal
request_method = GET
request_link   = /student-meal-booking/spring/api/cancel-meal-booking/en/8201982
request_body   =
response_body  = {"success":true,"message":null}; // I guess there is a message on fail
