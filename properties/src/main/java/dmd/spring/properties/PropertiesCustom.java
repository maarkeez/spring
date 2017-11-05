package dmd.spring.properties;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

/**
 * Esta clase está configurada para recuperar ciertas propiedades de un fichero ".properties" y almacenarlas en variables de Java. Una vez almacenadas, permite editar las variables y guardarlas de nuevo en el fichero. El fichero se modificará
 * cambiando los valores de las variables, pero manteniendo el resto de propiedades que no hayan sido configuradas.
 * 
 * @author David Márquez Delgado
 *
 */
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

	/**
	 * <b>Descripción</b><br/>
	 * Permite modificar las propiedades enlazadas a las variables de esta clase.<br/>
	 * - Se modificará el fichero informado en la anotación <code>PropertySource</code><br/>
	 * - Se modificarán todas las propiedades con la anotación <code>Value</code><br/>
	 * <br/>
	 * PRECONDICIÓN: en la anotación <code>Value</code> se presupone que el valor sólo incluye el nombre de la propiedad y lo hace siguiendo el formato: <code>"${nombre.de.la.propiedad}"</code><br/>
	 * Por otra parte <code>PropertySource</code> está informado con un sólo fichero.
	 */
	public void saveParamChanges() {
		try {

			// PRE: PropertySource está informado con un sólo fichero.
			// Recuperar propiedades actuales del fichero de propiedades
			Class<?> claseDePropiedades = PropertiesCustom.class;
			PropertySource anotacionFuenteDePropiedades = claseDePropiedades.getDeclaredAnnotation(PropertySource.class);
			String[] ficherosDePropiedadesDeclarados = anotacionFuenteDePropiedades.value();
			Resource ficheroDePropiedadesDeclarado = context.getResource(ficherosDePropiedadesDeclarados[0]);
			PropertiesConfiguration propiedadesParaModificar = new PropertiesConfiguration(ficheroDePropiedadesDeclarado.getFile());

			System.out.println("Editando fichero de propiedades " + ficherosDePropiedadesDeclarados[0]);

			// Modificar la variable de propiedades, sustituyendo los valores actuales con los valores almacenados en la clase.
			Field[] camposDeLaClasePropiedades = claseDePropiedades.getDeclaredFields();
			for (Field campoActual : camposDeLaClasePropiedades) {

				Value[] anotacionConNombreDeLaPropiedad = campoActual.getDeclaredAnnotationsByType(Value.class);

				if (anotacionConNombreDeLaPropiedad != null && anotacionConNombreDeLaPropiedad.length >= 1) {

					// PRE: El valor debe seguir el formato "${nombre.de.la.propiedad}"
					String nombreDeLaPropiedadConLlaves = anotacionConNombreDeLaPropiedad[0].value();
					String nombreDeLaPropiedad = (String) nombreDeLaPropiedadConLlaves.substring(2, nombreDeLaPropiedadConLlaves.length() - 1);
					propiedadesParaModificar.setProperty(nombreDeLaPropiedad, campoActual.get(this));

					System.out.println(nombreDeLaPropiedad + " ==> " + campoActual.get(this));
				}
			}

			// Guardar los cambios en el fichero
			propiedadesParaModificar.save();

			System.out.println("Fichero de propiedades editado " + ficherosDePropiedadesDeclarados[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}