package run.halo.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class HaloQuarkusApplication implements QuarkusApplication {
    
    @Override
    public int run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("  Halo Quarkus Blog System Starting...  ");
        System.out.println("========================================");
        System.out.println("Access http://localhost:8090/system/setup to install");
        Quarkus.waitForExit();
        return 0;
    }
}
