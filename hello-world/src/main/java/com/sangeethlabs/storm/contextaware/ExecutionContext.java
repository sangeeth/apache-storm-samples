package com.sangeethlabs.storm.contextaware;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ExecutionContext implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, Serializable> attributes;
    
    public ExecutionContext() {
        super();
        this.attributes = new TreeMap<>();
    }
    
    public Serializable getAttribute(String name) {
        return this.attributes.get(name);
    }
    
    public <A extends Serializable> A getAttribute(Class<A> type, String name) {
        return (A)this.attributes.get(name);
    }
    
    public void setAttribute(String name, Serializable value) {
        this.attributes.put(name, value);
    }
    
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }
    
    public Map<String, Serializable> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }
    
    public void clear() {
        this.attributes.clear();
    }
}