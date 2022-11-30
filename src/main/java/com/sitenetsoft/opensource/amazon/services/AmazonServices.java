package com.sitenetsoft.opensource.amazon.services;

import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AmazonServices {

    public static final String module = AmazonServices.class.getName();

    static class ZoneSections {
        public static String orderHistory = "https://amazon.ca/gp/css/order-history";
    }

    static class ZoneAuth {
        public static String signin = "https://www.amazon.ca/ap/signin";
        public static String signinHttpMethod = "POST";
    }

    static class LoginFormInputs {
        public static String username = "username";
        public static String password = "password";
    }

    public static Map<String, Object> pullDataAmazon(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();

        CustomerCenterSections customerCenterSections = new CustomerCenterSections();
        LoginFormInputs loginFormInputs = new LoginFormInputs();

        try {

            try {
                // Get the Cookie that is initialized in the login form page
                Connection.Response loginForm = Jsoup.connect(customerCenterSections.dashboard)
                        .method(Connection.Method.GET)
                        .execute();

                Document document = Jsoup.connect(customerCenterSections.dashboard)
                        //.data("cookieexists", "false")
                        .data(LoginFormInputs.username, username)
                        .data(LoginFormInputs.password, password)
                        .cookies(loginForm.cookies())
                        .post();
                System.out.println(document);
            } catch (Exception exception) {
                System.out.println(exception);
            }

            /*try {
                Document doc = Jsoup.connect("http://example.com").get();
                doc.select("p").forEach(System.out::println);
                System.out.println(doc.select("p").toString());
            } catch (Exception exception) {
                System.out.println(exception);
            }*/

            GenericValue videotron = delegator.makeValue("AmazonInvoices");
            // Auto generating next sequence of ofbizDemoId primary key
            amazon.setNextSeqId();
            // Setting up all non primary key field values from context map
            amazon.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            amazon = delegator.create(amazon);
            result.put("amazonId", amazon.getString("amazonId"));
            Debug.log("==========This is my first Java Service implementation in Apache OFBiz. Amazon record created successfully with ofbizDemoId: " + amazon.getString("amazonId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in Amazon Invoice entity ........" +module);
        }
        return result;

        /*Connection.Response loginForm = Jsoup.connect("https://www.desco.org.bd/ebill/login.php")
        .method(Connection.Method.GET)
        .execute();

        Document document = Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
        .data("cookieexists", "false")
        .data("username", "32007702")
        .data("login", "Login")
        .cookies(loginForm.cookies())
        .post();
        System.out.println(document);*/
    }

}