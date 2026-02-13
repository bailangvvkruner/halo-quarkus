package run.halo.halo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class HaloApplication implements QuarkusApplication {
    
    @Override
    public int run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("  Halo Quarkus Blog System Starting...  ");
        System.out.println("========================================");
        Quarkus.waitForExit();
        return 0;
    }
}
