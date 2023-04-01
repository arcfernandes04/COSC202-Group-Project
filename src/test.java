package cosc202.andie;

import java.util.*;
import java.util.prefs.*;

public class test {

    public test()
    {
        work();
    }

    public static void work()
    {
        Preferences prefs = Preferences.userNodeForPackage(test.class);

        Locale.setDefault(new Locale(prefs.get("language", "en"), 
                prefs.get("country", "NZ")));

        ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle");

        System.out.println(bundle.getString("greeting"));

        prefs.put("language", "en");
        prefs.put("country", "NZ");
    }
}
