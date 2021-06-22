package com.ap.datagrid.spring.rest.model;


import java.util.Collection;

import com.ap.datagrid.spring.core.client.Client;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
/**
 * Model for REST interface.
 * 
 */
public class Clients {
    
	@ProtoField(number = 1)
    private Collection<Client> clients;
	@ProtoField(number = 2)
    private Client client;

    public Collection<Client> getClients() {
        return clients;
    }

    public void setClients(Collection<Client> clients) {
        this.clients = clients;
    }

	@ProtoFactory 
    public Clients(Collection<Client> clients) {
        this.clients = clients;
    }
	
	@ProtoFactory 
	public Clients(Client clients2) {
		this.setClient(clients2);
		// TODO Auto-generated constructor stub
	}
    
	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}
}
