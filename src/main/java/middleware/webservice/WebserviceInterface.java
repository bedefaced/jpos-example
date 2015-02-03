package middleware.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebserviceInterface {

    @WebMethod
    ISOMessageContent send(ISOMessageContent message) throws Exception;

}
