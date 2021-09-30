package mb.solo.blocnote.model;

import android.graphics.Color;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable (tableName = "categories")
public class Categorie implements Serializable {

    @DatabaseField (columnName = "id_categories", generatedId = true)
    private int id;
    @DatabaseField
    private String nom;

    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Categorie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return  nom;
    }
}
