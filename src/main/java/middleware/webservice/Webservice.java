package middleware.webservice;

import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.DatatypeConverter;

import middleware.MessageProcessor;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;

@WebService
public class Webservice implements WebserviceInterface {

    private int transactionTimeout = 10000;

    public Webservice(int transactionTimeout) {
	this.setTransactionTimeout(transactionTimeout);
    }

    public Webservice() {
    }

    static ISOMsg createISOMsgFromISOMessageContent(
	    ISOMessageContent messageRequest) throws ISOException {
	ISOMsg isoMessageRequest = new ISOMsg();
	/* set MTI and header */
	isoMessageRequest.setMTI(messageRequest.getMTI());
	isoMessageRequest.setHeader(messageRequest.getISOHeader().getBytes());
	/* map ISOMessageContent hashmap to ISOMsg fields */
	for (Entry<String, String> entry : messageRequest.getFields()
		.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    if (!isStringNullOrEmpty(key) && !isStringNullOrEmpty(value)) {
		isoMessageRequest.set(key, value);
	    }
	}

	return isoMessageRequest;
    }

    static ISOMessageContent createISOMessageContentFromISOMsg(
	    ISOMsg isoResponseMessage) throws ISOException {
	ISOMessageContent response = new ISOMessageContent();
	/* set MTI and header */
	response.setMTI(isoResponseMessage.getMTI());
	if (isoResponseMessage.getHeader() != null) {
	    response.setISOHeader(new String(isoResponseMessage.getHeader()));
	}
	/* map ISOMsg fields and subfields to ISOMessageContent hashmap */
	for (int i = 1; i <= isoResponseMessage.getMaxField(); i++) {
	    if (isoResponseMessage.hasField(i)) {
		response.getFields().put(i + "",
			isoResponseMessage.getString(i));
	    }
	}
	return response;
    }

    @Override
    @WebMethod
    public ISOMessageContent send(ISOMessageContent messageRequest)
	    throws Exception {

	ISOMsg isoMessageRequest = createISOMsgFromISOMessageContent(messageRequest);
	MessageProcessor messagePreprocessor = (MessageProcessor) NameRegistrar
		.getIfExists("preprocessor");
	if (messagePreprocessor != null) {
	    isoMessageRequest = messagePreprocessor.process(isoMessageRequest);
	}

	QMUX qmux = (QMUX) QMUX.getMUX("clientmux");
	ISOMsg isoResponseMessage = qmux.request(isoMessageRequest,
		this.getTransactionTimeout());

	if (isoResponseMessage == null) {
	    throw new TimeoutException("Transaction timed out!");
	}
	ISOMessageContent response = createISOMessageContentFromISOMsg(isoResponseMessage);

	return response;
    }

    private int getTransactionTimeout() {
	return transactionTimeout;
    }

    private void setTransactionTimeout(int transactionTimeout) {
	this.transactionTimeout = transactionTimeout;
    }

    static boolean isStringNullOrEmpty(String string) {
	return string == null || string.isEmpty();
    }
}
