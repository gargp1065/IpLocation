package com.gl.eirs.iplocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;


@SpringBootApplication
@EnableEncryptableProperties
public class IpLocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpLocationApplication.class, args);
	}

}
