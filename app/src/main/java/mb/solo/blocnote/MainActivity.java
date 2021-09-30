package mb.solo.blocnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mb.solo.blocnote.model.Categorie;
import mb.solo.blocnote.model.Note;
import mb.solo.blocnote.orm.CategorieDao;
import mb.solo.blocnote.orm.NoteDao;

public class MainActivity extends BaseActivity {

    String TAG = "orm";
    List<Note> data;
    List<Categorie> listCategorie = new ArrayList<>();
    NoteDao dao = new NoteDao(MainActivity.this);
    RecyclerView list;
    MyListeAdapter adapter;
    Spinner spCategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = dao.list();

        //Pour tests
        /*for (Note n:data) {
            Log.i(TAG, n.toString());
        }*/
        //
        makeList();
        makeSpinner();

        Button btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v ->{
            createListWithName();
        });

        EditText search = findViewById(R.id.edt_search);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(spCategorie.getSelectedItemPosition() != 0){
                    spCategorie.setSelection(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.i(TAG, "afterTextChanged: "+s.toString());
                String mySearch = s.toString();
                if(mySearch.equals("")){
                    data = dao.list();
                }else {
                    data = dao.findText(mySearch);
                }
                adapter.notifyDataSetChanged();
            }
        });

        spCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(search.getText().toString().equals("")){
                    makeListWitchSpinner(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void makeListWitchSpinner(int position){
        if(position != 0){
            Categorie item = listCategorie.get(position);

            if(item.getNom().equals(getResources().getString(R.string.list_categorie_aucune))){
                data = dao.findCategorieNull();
            }else{
                Map<String, Object> params = new HashMap<>();
                params.put("categorie_id_categories", item);
                data = dao.find(params);
            }
        }else{
            data = dao.list();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Permet de rafraîchir l'activiter déjà crée!
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        data = dao.list();
    }

    private void createListWithName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_note_txt)
                .setMessage(R.string.message_new_note);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        builder.setView(customLayout)
                .setPositiveButton(getResources().getString(R.string.btn_ok_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edtName = customLayout.findViewById(R.id.edt_title_note);
                        String titre = edtName.getText().toString();
                        if(!titre.equals("")){
                            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                            intent.putExtra("titre", titre);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_error_name_txt), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel_txt), null).show();
    }

    private void makeList() {
        list = findViewById(R.id.rv_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyListeAdapter();
        list.setAdapter(adapter);
    }

    private void makeSpinner(){
        spCategorie = findViewById(R.id.sp_main_categorie);
        CategorieDao daoCategorie = new CategorieDao(MainActivity.this);
        listCategorie.add(new Categorie(0, getResources().getString(R.string.list_main_categorie)));
        listCategorie.add(new Categorie(0, getResources().getString(R.string.list_categorie_aucune)));
        listCategorie.addAll(daoCategorie.list());
        spCategorie.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listCategorie));
    }


    private class MyListeAdapter extends RecyclerView.Adapter<MyListViewHolder> {

        public MyListeAdapter(){
        }
        @NonNull
        @Override
        public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            return new MyListViewHolder((itemView));
        }

        @Override
        public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {

            Note item = data.get(position);
            holder.itemNom.setText(item.getNom());
            Categorie categorie = item.getCategorie();
            String detail = item.getDateNote()+" | ";
            if(categorie != null){
                detail = detail + categorie.getNom();
            }else{
                detail = detail + getResources().getString(R.string.detail_no_categorie_txt);
            }
            holder.labelCategorie.setText(detail);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class MyListViewHolder extends RecyclerView.ViewHolder {

        TextView itemNom;
        ImageButton editTitle;
        ImageButton eraseNote;
        TextView labelCategorie;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNom =  itemView.findViewById(R.id.tv_nom);
            editTitle = itemView.findViewById(R.id.ib_edit_title);
            eraseNote = itemView.findViewById(R.id.ib_erase_note);
            labelCategorie = itemView.findViewById(R.id.tv_label);

            itemView.setOnClickListener(v ->  {
                int index = getAdapterPosition();
                Note element = data.get(index);
                //Toast.makeText(MainActivity.this, "Click on: " + element.getNom() , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("item", element);
                startActivity(intent);
            });

            editTitle.setOnClickListener(v ->{
                int index = getAdapterPosition();
                Note element = data.get(index);
                updateItem(element, index);
            });

            eraseNote.setOnClickListener(v ->{
                int index = getAdapterPosition();
                Note element = data.get(index);
                deleteItem(element, index);
            });

        }
    }

    private void updateItem(Note element, int index){
        String titre = element.getNom();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_title)
                .setMessage(R.string.message_update_note);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        builder.setView(customLayout);
        EditText edtName = customLayout.findViewById(R.id.edt_title_note);
        edtName.setText(titre);
        builder.setPositiveButton(getResources().getString(R.string.btn_rename_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitre = edtName.getText().toString();
                if(!newTitre.equals("")) {
                    element.setNom(edtName.getText().toString());
                    dao.update(element);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_error_name_txt), Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton(getResources().getString(R.string.btn_cancel_txt), null).show();

    }

    private void deleteItem(Note element, int index){
        Resources res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.erase_note_title)
                .setMessage(res.getString(R.string.message_erase_note) + " " + element.getNom() + "?")
                .setPositiveButton(getResources().getString(R.string.btn_yes_txt),  new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(element);
                        /*https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data*/
                        data.remove(index);
                        list.removeViewAt(index);
                        adapter.notifyItemRemoved(index);
                        adapter.notifyItemRangeChanged(index, data.size());
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_no_txt), null).show();
    }
}