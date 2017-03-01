package com.asiainfo.o2p.wsdl.exception;


@SuppressWarnings("serial")
public class WsdlException extends RuntimeException {
	private int code;
	public WsdlException() {
		super();
	}
	public WsdlException(String description) {
		super(description);
	}
	public WsdlException(String description, Throwable cause) {
		super(description, cause);
	}

	public WsdlException(int code, String description){
		super(description);
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
}
