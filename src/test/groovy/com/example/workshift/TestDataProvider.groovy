package com.example.workshift

import com.example.workshift.business.shift.CreateShiftRequest
import com.example.workshift.business.shift.Shift
import com.example.workshift.business.shop.CreateShopRequest
import com.example.workshift.business.user.CreateUserRequest

import java.time.Duration
import java.time.Instant
import java.time.Period

class TestDataProvider {

        public static String USER_ADDRESS = "Street 1";
        public static String USER_EMAIL = "abc@gmail.com";
        public static String USER_FIRST_NAME = "John";
        public static String USER_LAST_NAME = "Doe";
        public static String USER_PHONE_NUMBER = "12345678";

        public static CreateUserRequest createUserRequest () {
                return new CreateUserRequest(USER_FIRST_NAME, USER_LAST_NAME, USER_ADDRESS, USER_PHONE_NUMBER, USER_EMAIL);
        }

        public static String CVR1 = "12345678";
        public static String SHOP1_ADDRESS = "Street 2";
        public static String SHOP1_EMAIL = "shop@gmail.com";
        public static String SHOP1_NAME = "Shop 1";
        public static String SHOP1_PHONE_NUMBER = "87654321";

        public static CreateShopRequest createShopRequest1 () {
          return new CreateShopRequest(SHOP1_NAME, CVR1, SHOP1_ADDRESS, SHOP1_PHONE_NUMBER, SHOP1_EMAIL);
        }

        public static String CVR2 = "23456789";
        public static String SHOP2_ADDRESS = "Street 3";
        public static String SHOP2_EMAIL = "shop2@gmail.com";
        public static String SHOP2_NAME = "Shop 2";
        public static String SHOP2_PHONE_NUMBER = "87654321";

        public static CreateShopRequest createShopRequest2 () {
          return new CreateShopRequest(SHOP2_NAME, CVR2, SHOP2_ADDRESS, SHOP2_PHONE_NUMBER, SHOP2_EMAIL);
        }

        public static Long SHOP_ID = 10L;

        public static Instant NOW = Instant.now();
        public static Instant TOMORROW = NOW + Period.ofDays(1);
        public static Long USER_ID = 1L;


        public static Shift createNewShift () {
                CreateShiftRequest createShiftRequest = new CreateShiftRequest(NOW, NOW + Duration.ofHours(2), USER_ID, SHOP_ID);
                return Shift.of(createShiftRequest);
        }


        public static createShiftRequest = { Instant activeFrom, Instant activeTo ->
                return new CreateShiftRequest(activeFrom, activeTo, USER_ID, SHOP_ID);
        }

}



