package mb.solo.blocnote.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

@DatabaseTable(tableName = "notes")
public class Note implements Serializable, Comparable<Note> {

    @DatabaseField(columnName = "id_note", generatedId = true)
    private int id;
    @DatabaseField
    private String nom;
    @DatabaseField
    private String texte;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "id_categories")
    private Categorie categorie;
    @DatabaseField
    private String dateNote;

    public Note() {
    }

    public Note(int id, String nom, String texte) {
        this.id = id;
        this.nom = nom;
        this.texte = texte;
    }

    public Note(int id, String nom, String texte, Categorie categorie) {
        this.id = id;
        this.nom = nom;
        this.texte = texte;
        this.categorie = categorie;
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

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getDateNote() {
        return dateNote;
    }

    public void setDateNote(String dateNote) {
        this.dateNote = dateNote;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie=" + categorie +
                '}';
    }

    @Override
    public int compareTo(Note n) {
        return this.getDateNote().compareTo(n.getDateNote());
    }
}
