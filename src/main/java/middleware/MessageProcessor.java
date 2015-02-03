package middleware;

import org.jpos.iso.ISOMsg;

public interface MessageProcessor {
    public ISOMsg process(ISOMsg message);
}
