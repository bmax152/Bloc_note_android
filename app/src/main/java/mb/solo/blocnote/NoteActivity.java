package mb.solo.blocnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mb.solo.blocnote.model.Categorie;
import mb.solo.blocnote.model.Note;
import mb.solo.blocnote.orm.CategorieDao;
import mb.solo.blocnote.orm.NoteDao;

public class NoteActivity extends AppCompatActivity {

    Note maNote;
    Spinner spCategorie;
    List<Categorie> list = new ArrayList<>();
    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Button btnSave = findViewById(R.id.btn_save);
        EditText edtNote = findViewById(R.id.tb_note);
        makeSpinner();

        Intent intentStart = getIntent();
        Note item = (Note) intentStart.getSerializableExtra("item");
        String titre;
        if(item != null){
            titre = item.getNom();
            edtNote.setText(item.getTexte());
            maNote = item;
            Categorie categorieItem = maNote.getCategorie();
            if(categorieItem != null){
                //On recherche à quelle position est la catégorie de l'item déjà sauvegarder dans la liste
                int positionCategorie = findCategorie(categorieItem);
                spCategorie.setSelection(positionCategorie);
                //Log.i("orm", "catégorie OK // " +positionCategorie+" // " + categorieItem.getNom());
            }
        }else{
            titre = intentStart.getStringExtra("titre");
            maNote = new Note(0, titre, "");
        }
        getSupportActionBar().setTitle(titre);

        btnSave.setOnClickListener(v -> {
            try {
                String note = edtNote.getText().toString();
                maNote.setTexte(note);
                Categorie categorie = (Categorie) spCategorie.getSelectedItem();
                if(categorie != list.get(0)){
                    maNote.setCategorie(categorie);
                }else{
                    maNote.setCategorie(null);
                }
                Date dateNow = Calendar.getInstance().getTime();
                maNote.setDateNote(myFormat.format(dateNow));
                NoteDao dao = new NoteDao(NoteActivity.this);
                dao.createOrUpdate(maNote);
                Toast.makeText(NoteActivity.this, getResources().getString(R.string.toast_note_save), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(NoteActivity.this, getResources().getString(R.string.toast_error_save), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void makeSpinner(){
        spCategorie = findViewById(R.id.sp_categorie);
        CategorieDao dao = new CategorieDao(NoteActivity.this);
        list.add(new Categorie(0, getResources().getString(R.string.list_categorie_aucune)));
        list.addAll(dao.list());
        spCategorie.setAdapter(new ArrayAdapter<>(NoteActivity.this, android.R.layout.simple_list_item_1, list));
    }

    private int findCategorie(Categorie categorieItem){
        for (Categorie categorieList: list) {
            if(categorieItem.getId() == categorieList.getId()){
                return list.indexOf(categorieList);
            }
        }
        return 0;
    }
}