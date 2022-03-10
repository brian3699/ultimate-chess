package model.util;

import java.lang.reflect.Method;

/**
 * Class to handle reflection
 *
 * @author Ryleigh Byrne, Young Jun
 */
public class ReflectionHandler {

    /**
     * Handle method given a method name and a class name
     * @param name of method to call
     * @param aClass name of class to call the method in
     * @return desired method
     */
    public Method handleMethod(String name, String aClass) {
        try {
            Class<?> thisClass = Class.forName(aClass);
            Method m = thisClass.getDeclaredMethod(name);
            return m;
        } catch (NoSuchMethodException e) {
            String error = String.format("The method: %s could not be generated. Double check method you are trying to call's name", name);
            System.out.println(error);
            return null;
        }catch(ClassNotFoundException e){
            String error = String.format("The class: %s could not be generated. Double check class you are trying to call's name", name);
            System.out.println(error);
            return null;
        }
    }
}
