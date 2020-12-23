/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example_app;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author koduki
 */
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@WebTrace
public class WebTraceIntersepter {

    @Inject
    HttpServletRequest req;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        var event = new HttpRequestEvent();

        event.begin();
        var result = ic.proceed();
        event.end();

        event.url = req.getRequestURI();
        event.method = req.getMethod();
        event.commit();

        return result;
    }
}
