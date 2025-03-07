import com.sanshugpt.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(scanBasePackages = "com.sanshugpt")
@MapperScan("com.sanshugpt.**.dao")
@RequiredArgsConstructor
public class SanshuGPTApp implements CommandLineRunner {

  final AppUserService userService;

  public static void main(String[] args) {
    SpringApplication.run(SanshuGPTApp.class, args);
  }


  @Override
  public void run(String... params) throws Exception {
    /*AppUser admin = new AppUser();
    admin.setUsername("admin");
    admin.setPassword("admin");
    admin.setEmail("admin@email.com");
    admin.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_ADMIN)));

    userService.signup(admin);

    AppUser client = new AppUser();
    client.setUsername("client");
    client.setPassword("client");
    client.setEmail("client@email.com");
    client.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_CLIENT)));

    userService.signup(client);*/
  }

}
