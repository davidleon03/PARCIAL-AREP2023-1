package org.escuelaing.arep;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Service {
    public ArrayList<String> getCl(String name) throws ClassNotFoundException {
        Class<?> clase = Class.forName(name);
        Method[] metodos = clase.getDeclaredMethods();
        Field[] delcarados = clase.getDeclaredFields();
        ArrayList<String> res = new ArrayList<String>();
        for (Method m: metodos){
            res.add("METODO -->  " + m.getName());
        }
        for (Field f: delcarados){
            res.add("FIELD -->" + f.getName());
        }
        return res;
    }
    public void getIV(String name, String metodo) throws ClassNotFoundException {
        Class clase = Class.forName(name);
    }
    public void getUIV(String name, String metodo, String tipo, String value) throws ClassNotFoundException {
        Class clase = Class.forName(name);
    }
    public void getBIV(String name, String metodo, String tipo, String value, String tipo2, String value2) throws ClassNotFoundException {
        Class clase = Class.forName(name);
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Service prueba = new Service();
        ArrayList<String> res = prueba.getCl("org.escuelaing.arep.HttpServer2");
        for (String r: res){
            System.out.println(r);
        }
    }
}
