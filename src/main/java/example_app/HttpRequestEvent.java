/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example_app;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author koduki
 */
@Category({"Application Profile"})
@Label("HTTP Request")
@Name("myprofile.HttpRequest")
public class HttpRequestEvent extends Event {

    @Label("Method")
    String method;

    @Label("URL")
    String url;
}