class Main
{
    String sun_mb_store_booking_link = "127.0.0.0:8080";
    public static void main(String[] args)
    {
        System.out.println("Hello World!");

        String mealDate = "2025-09-08";
        String mealSlot = "L";
        int    mealFacility = 22;
        int    mealOption = 24684;
        int    mealSession = 60;
        int    advanceBookingDays = 2;
        boolean excludeWeekends = true;
        result = book_meal(mealDate, mealSlot, mealFacility, mealOption, mealSession, advanceBookingDays, excludeWeekends);
    };

    boolean book_meal(
            String mealDate,
            String mealSlot,
            int    mealFacility,
            int    mealOption,
            int    mealSession,
            int    advanceBookingDays,
            boolean excludeWeekends
            )
    {
        String booking_json = create_booking_json( mealDate,
            mealSlot,
            mealFacility,
            mealOption,
            mealSession,
            advanceBookingDays,
            excludeWeekends);

        //response = send_post(sun_mb_store_booking_link, security_cookies, booking_json);
        URI uri = URI.create(sun_mb_store_booking_link);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .POST()
            .setHeader("Cookie", security_cookies)
        HttpResponse response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        return true;
    }
};
