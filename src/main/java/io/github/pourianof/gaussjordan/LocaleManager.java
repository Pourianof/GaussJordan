package io.github.pourianof.gaussjordan;


import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

public class LocaleManager extends Observable {
    static private LocaleManager instance;
    static public LocaleManager getManager(){
        if(instance == null){
            instance = new LocaleManager(new Locale("en"));
        }
        return instance;
    }

    private ResourceBundle bundle;

    private LocaleManager(Locale locale){
        this.setBundle(locale);
    }

    private void setBundle(Locale locale){
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    void changeLanguage(Locale newLocale){
        if(bundle != null && newLocale.equals(bundle.getLocale())){
            return;
        }
        this.setBundle(newLocale);
        setChanged();
        notifyObservers();
    }

    public String get(String key) {
        return bundle.getString(key);
    }
}
