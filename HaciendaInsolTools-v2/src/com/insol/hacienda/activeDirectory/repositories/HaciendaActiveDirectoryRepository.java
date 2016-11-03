package com.insol.hacienda.activeDirectory.repositories;

public class HaciendaActiveDirectoryRepository extends ActiveDirectoryRepository{

	@Override
	public String configLocation() {
		return "C:\\Insol\\Config\\NPP\\filenetApiConfig.xml";
	}

}
