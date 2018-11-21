package mobi.hifun.seeu.medal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan(basePackages = "mobi.hifun.seeu.medal.dao")
@EnableTransactionManagement
public class MedalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedalServiceApplication.class, args);
	}

}
