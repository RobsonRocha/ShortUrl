package shortURL;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import org.h2.tools.DeleteDbFiles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import br.com.shorturl.utils.Utils;


@SpringBootApplication
@ComponentScan(basePackages = { "br.com.shorturl.controller" })
public class Application {	
	
	public static void main(String[] args) throws SQLException,
			PropertyVetoException {		 
		SpringApplication.run(Application.class, args);		
		DeleteDbFiles.execute("~", "test", true);
		Utils.createTable();
	}
}
