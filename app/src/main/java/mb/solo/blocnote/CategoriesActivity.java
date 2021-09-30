package mb.solo.blocnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mb.solo.blocnote.model.Categorie;
import mb.solo.blocnote.orm.CategorieDao;

public class CategoriesActivity extends AppCompatActivity{

    List<Categorie> data;
    CategorieDao dao = new CategorieDao(CategoriesActivity.this);
    RecyclerView list;
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        data = dao.list();
        makeList();

        Button btnAddCategorie = findViewById(R.id.bt_add_categorie);

        btnAddCategorie.setOnClickListener(v ->{
            createCategorie();
        });
    }

    /**
     * Permet de rafraîchir l'activiter déjà crée!
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        data = dao.list();
    }

    private void createCategorie(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_label)
                .setMessage(R.string.message_new_label);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        builder.setView(customLayout)
                .setPositiveButton(getResources().getString(R.string.btn_ok_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edtName = customLayout.findViewById(R.id.edt_title_note);
                        String nom = edtName.getText().toString();
                        if(!nom.equals("")){
                            dao.create(new Categorie(0, nom));
                            data = dao.list();
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(CategoriesActivity.this, getResources().getString(R.string.toast_error_name_txt), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel_txt), null).show();
    }

    private void makeList(){
        list = findViewById(R.id.rv_label);

        list.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyListAdapter();
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends RecyclerView.Adapter<MyListViewHolder>{

        @NonNull
        @Override
        public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_categorie_layout, parent, false);
            return new MyListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {
            holder.nomCategorie.setText(data.get(position).getNom());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class MyListViewHolder extends RecyclerView.ViewHolder {

        TextView nomCategorie;
        ImageButton editCategorie;
        ImageButton eraseCategorie;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCategorie = itemView.findViewById(R.id.tv_name_categorie);
            editCategorie = itemView.findViewById(R.id.ib_edit_categorie);
            eraseCategorie = itemView.findViewById(R.id.ib_erase_categorie);

            editCategorie.setOnClickListener(v ->{
                int index = getAdapterPosition();
                Categorie element = data.get(index);
                updateItem(element, index);
            });

            eraseCategorie.setOnClickListener(v ->{
                int index = getAdapterPosition();
                Categorie element = data.get(index);
                deleteItem(element, index);
            });
        }
    }

    private void updateItem(Categorie element, int index){
        String titre = element.getNom();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_categorie)
                .setMessage(R.string.message_update_categorie);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        builder.setView(customLayout);
        EditText edtName = customLayout.findViewById(R.id.edt_title_note);
        edtName.setText(titre);
        builder.setPositiveButton(getResources().getString(R.string.btn_rename_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitre = edtName.getText().toString();
                if(!newTitre.equals("")){
                    element.setNom(edtName.getText().toString());
                    dao.update(element);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(CategoriesActivity.this, getResources().getString(R.string.toast_error_name_txt), Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton(getResources().getString(R.string.btn_cancel_txt), null).show();
    }

    private void deleteItem(Categorie element, int index){
        Resources res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.erase_categorie_title)
                .setMessage(res.getString(R.string.message_erase_categorie) + " " + element.getNom() + "?")
                .setPositiveButton(getResources().getString(R.string.btn_yes_txt),  new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dao.delete(element);
                            data.remove(index);
                            list.removeViewAt(index);
                            adapter.notifyItemRemoved(index);
                            adapter.notifyItemRangeChanged(index, data.size());
                        } catch (Exception e) {
                            Toast.makeText(CategoriesActivity.this, getResources().getString(R.string.toast_error_erase_label), Toast.LENGTH_SHORT).show();
                        }
                    }
        })
        .setNegativeButton(getResources().getString(R.string.btn_no_txt), null).show();
    }

}