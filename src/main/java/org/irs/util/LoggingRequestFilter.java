package org.irs.util;

import java.io.IOException;

import jakarta.ws.rs.client.ClientRequestContext;
import io.quarkus.logging.Log;
import java.util.concurrent.TimeUnit;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider; 

@Provider
public class LoggingRequestFilter implements  ContainerRequestFilter, ContainerResponseFilter {
    private static final String REQUEST_TIME_PROPERTY = "request-time";




    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(REQUEST_TIME_PROPERTY, System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if( requestContext.hasProperty(REQUEST_TIME_PROPERTY)){
          long requestStartTime = (long) requestContext.getProperty(REQUEST_TIME_PROPERTY);
            long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - requestStartTime);
            // String requestBody = (String) requestContext.getProperty(REQUEST_BODY_PROPERTY);

            Log.info(ConstantValues.responseReturned+" RESPONSE_TIME: "+durationMillis +" , " + CommonMethods.writeJson(responseContext.getEntity()) );

        }
    }
}

