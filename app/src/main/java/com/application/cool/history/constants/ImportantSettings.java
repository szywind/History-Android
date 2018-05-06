package com.application.cool.history.constants;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public class ImportantSettings {

    // Develop backend. This could be any Lean Cloud account.
    private static String personal_leancloud_id_us = "FCHudlonDJ4mzppWt6xSuRH7-gzGzoHsz";
    private static String personal_leancloud_key_us = "Wf1tUQlil0vn7FPeleN1KCyi";

    private static String personal_leancloud_id_cn = "FCHudlonDJ4mzppWt6xSuRH7-gzGzoHsz";
    private static String personal_leancloud_key_cn = "Wf1tUQlil0vn7FPeleN1KCyi";

    /*******************************************************************/
    // Deployment backend. DO NOT change.
    private static String company_leancloud_id_us = "FCHudlonDJ4mzppWt6xSuRH7-gzGzoHsz";
    private static String company_leancloud_key_us = "Wf1tUQlil0vn7FPeleN1KCyi";

    private static String company_leancloud_id_cn = "FCHudlonDJ4mzppWt6xSuRH7-gzGzoHsz";
    private static String company_leancloud_key_cn = "Wf1tUQlil0vn7FPeleN1KCyi";
    /*******************************************************************/

    /**
     *  Whether use US server or China server. Change this value will change to another backend app.
     *  Set to true when developing in US and false when in China.
     */
    public static Boolean USE_US_CLUSTER = false;

//    static Boolean USE_PRODUCTION_PUSH_SERVICE = false;

    /**
     *  Whether use company backend or personal backend.
     *  Use company backend **only at the final test phase of development and publish**.
     *
     *  - important: Double check this value is set to **true** before publish.
     */
    public static Boolean USE_COMPANY_BACKEND = false;



    public static String leanCloudId(){
        if (USE_COMPANY_BACKEND) {
            if (USE_US_CLUSTER) {
                return company_leancloud_id_us;
            } else {
                return company_leancloud_id_cn;
            }
        } else {
            if (USE_US_CLUSTER) {
                return personal_leancloud_id_us;
            } else {
                return personal_leancloud_id_cn;
            }
        }
    }

    public static String leanCloudKey() {
        if (USE_COMPANY_BACKEND) {
            if (USE_US_CLUSTER) {
                return company_leancloud_key_us;
            } else {
                return company_leancloud_key_cn;
            }
        } else {
            if (USE_US_CLUSTER) {
                return personal_leancloud_key_us;
            } else {
                return personal_leancloud_key_cn;
            }
        }
    }

}
