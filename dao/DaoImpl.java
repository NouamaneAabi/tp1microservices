package dao;

public class DaoImpl implements IDao {

    @Override
    public double getValue() {
        System.out.println("[DAO] Recuperation de la valeur depuis la source de donnees...");
        return 100.0;
    }
}