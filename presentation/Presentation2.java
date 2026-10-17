package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Presentation2 {

    public static void main(String[] args) {
        try {
            String[] classNames = loadClassNames("config.txt");
            System.out.println("[CONTAINER] Configuration chargee depuis config.txt");

            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancie : " + dao.getClass().getName());

            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Metier instancie : " + metier.getClass().getName());

            injectDependency(metier, "setDao", dao);
            System.out.println("[CONTAINER] Injection de dependance effectuee : setDao()");

            double resultat = metier.calcul();
            System.out.println("Resultats = " + resultat);

        } catch (Exception e) {
            System.err.println("[ERREUR] " + e.getClass().getSimpleName() + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (int i = 0; i < 2; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("config.txt doit contenir 2 lignes.");
                }
                classNames[i] = line.trim();
            }
        }
        return classNames;
    }

    private static Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }

    private static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        Class<?> targetClass = target.getClass();
        Class<?> dependencyInterface = dependency.getClass().getInterfaces()[0];
        Method setterMethod = targetClass.getMethod(setterName, dependencyInterface);
        setterMethod.invoke(target, dependency);
    }
}