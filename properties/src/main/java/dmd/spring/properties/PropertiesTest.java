package dmd.spring.properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertiesTest {

	@Autowired
	private PropertiesCustom propertiesCustom;

	public PropertiesTest() {
	}

	@PostConstruct
	public void init() {
		System.out.println(propertiesCustom.getName() + " " + propertiesCustom.getLastName() + "\n" + propertiesCustom.getAddress());
		
		
		propertiesCustom.setName("Juanito");
		propertiesCustom.setLastName("Valderrama");
		propertiesCustom.setAddress("Villacuenca");
		
		System.out.println(propertiesCustom.getName() + " " + propertiesCustom.getLastName() + "\n" + propertiesCustom.getAddress());

		propertiesCustom.saveParamChanges();
		
		
		
		
	}
}
