package dmd.spring.properties;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

@Configuration
@PropertySource("classpath:custom.properties")
public class PropertiesCustom {

	@Autowired
	private ApplicationContext context;

	@Value("${custom.name}")
	private String name;
	@Value("${custom.lastname}")
	private String lastName;
	@Value("${custom.address}")
	private String address;
	@Value("${custom.stringarray}")
	private String[] stringArray;

	public PropertiesCustom() {
	}

	@PostConstruct
	private void init() {
		System.out.println("Listando propiedades almacenadas");
		System.out.println("name ==> " + name);
		System.out.println("lastName ==> " + lastName);
		System.out.println("address ==> " + address);
		System.out.print("stringArray ==> [");
		for (String s : stringArray) {
			System.out.print(" " + s);
		}
		System.out.println("]");

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

	public String[] getStringArray() {
		return stringArray;
	}

	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}

	public void saveParamChanges() {
		try {

			Class<?> pc = PropertiesCustom.class;
			PropertySource ps = pc.getDeclaredAnnotation(PropertySource.class);
			String[] propertyFiles = ps.value();

			Resource propertyResource = context.getResource(propertyFiles[0]);
			System.out.println("Editando fichero de propiedades " + propertyFiles[0]);
			
			PropertiesConfiguration props = new PropertiesConfiguration(propertyResource.getFile());
			Field[] flds = pc.getDeclaredFields();
			for (Field f : flds) {
				Value[] valueAnnotations = f.getDeclaredAnnotationsByType(Value.class);
				if (valueAnnotations != null && valueAnnotations.length >= 1) {
					String valueAnnotation = valueAnnotations[0].value();
					//PRE: El valor debe seguir el formato "${nombre.de.la.propiedad}"
					String propertyName = (String) valueAnnotation.substring(2, valueAnnotation.length() - 1);
					System.out.println(propertyName + " ==> " + f.get(this));
					props.setProperty(propertyName, f.get(this));
				}
			}
			props.save();

			System.out.println("Fichero de propiedades editado " + propertyFiles[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}