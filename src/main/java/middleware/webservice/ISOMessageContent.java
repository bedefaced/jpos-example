package middleware.webservice;

import java.util.HashMap;

public class ISOMessageContent {

    String MTI;
    String ISOHeader;
    HashMap<String, String> fields = new HashMap<String, String>();

    public String getMTI() {
	return MTI;
    }

    public void setMTI(String mTI) {
	MTI = mTI;
    }

    public String getISOHeader() {
	return ISOHeader;
    }

    public void setISOHeader(String iSOHeader) {
	ISOHeader = iSOHeader;
    }

    public HashMap<String, String> getFields() {
	return fields;
    }

    public void setFields(HashMap<String, String> fields) {
	this.fields = fields;
    }

}
