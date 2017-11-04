package dmd.spring.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:custom.properties")
public class PropertiesCustom {

	@Value("${custom.name}")
	private String name;
	@Value("${custom.lastname}")
	private String lastName;
	@Value("${custom.address}")
	private String address;

	public PropertiesCustom() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void saveParamChanges() {
		try {
			System.out.println("Editando fichero de propiedades");
			Properties props = new Properties();
			props.setProperty("custom.name", name);
			props.setProperty("custom.lastname", lastName);
			props.setProperty("custom.address", address);

			URL url = Thread.currentThread().getContextClassLoader().getResource("custom.properties");

			File f = new File(url.toURI().getPath());

			OutputStream out = new FileOutputStream(f);
			props.store(out, "Edited");
			System.out.println("Fichero de propiedades editado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}