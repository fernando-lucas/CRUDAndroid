package com.fernando.crudandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText_nome, editText_telefone, editText_email, editText_id;
    Button button_novo, button_salvar, button_excluir;
    ListView listaView_contatos;

    private String HOST = "http://seu.IP.aqui/webservice3";

    private int itemClicado;

    // AULA 09 (INICIO)-------------------------------------------------
    ContatosAdapter contatosAdapter;
    List<Contato> lista;
    // AULA 09 (FIM)-------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_nome = (EditText)findViewById(R.id.editText_nome);
        editText_telefone = (EditText)findViewById(R.id.editText_telefone);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_id = (EditText)findViewById(R.id.editText_id);

        button_novo = (Button)findViewById(R.id.button_novo);
        button_salvar = (Button)findViewById(R.id.button_salvar);
        button_excluir = (Button)findViewById(R.id.button_excluir);

        listaView_contatos = (ListView)findViewById(R.id.listView_contatos);

        // AULA 09 (INICIO)-------------------------------------------------
        lista = new ArrayList<Contato>();
        contatosAdapter = new ContatosAdapter(MainActivity.this, lista);

        listaView_contatos.setAdapter(contatosAdapter);

        listaContatos();
        // AULA 09 (FIM)-------------------------------------------------

        // AULA 10 (INICIO)-------------------------------------------------
        button_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
        // AULA 10 (FIM)-------------------------------------------------

        button_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = editText_id.getText().toString(); // aula 12 fez alteração;
                final String nome = editText_nome.getText().toString();
                final String telefone = editText_telefone.getText().toString();
                final String email = editText_email.getText().toString();

                if(nome.isEmpty()) {
                    editText_nome.setError("O Nome é obrigatório!");
                } else if(id.isEmpty()) {

                    // CREATE
                    String url = HOST + "/create.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if(result.get("CREATE").getAsString().equals("OK")) {
                                        int idRetornado = Integer.parseInt(result.get("ID").getAsString());

                                        // AULA 12 (INICIO)-------------------------------------------------
                                        Contato c = new Contato();

                                        c.setId(idRetornado);
                                        c.setNome(nome);
                                        c.setTelefone(telefone);
                                        c.setEmail(email);

                                        lista.add(c);

                                        contatosAdapter.notifyDataSetChanged();
                                        // AULA 12 (FIM)-------------------------------------------------

                                        limpaCampos();

                                        // AULA 12 (INICIO)-------------------------------------------------
                                        /*Toast.makeText(MainActivity.this,
                                                "Salvo com sucesso, id " + idRetornado, Toast.LENGTH_LONG).show();*/

                                        Toast.makeText(MainActivity.this,
                                                "Salvo com sucesso",
                                                Toast.LENGTH_LONG).show();

                                        // AULA 12 (FIM)-------------------------------------------------
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Ocorreu um erro ao salvar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    // AULA 12 (INICIO)-------------------------------------------------
                    // UPDATE
                    String url = HOST + "/update.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("id", id)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if(result.get("UPDATE").getAsString().equals("OK")) {

                                        Contato c = new Contato();

                                        c.setId(Integer.parseInt(id));
                                        c.setNome(nome);
                                        c.setTelefone(telefone);
                                        c.setEmail(email);

                                        lista.set(itemClicado, c);

                                        contatosAdapter.notifyDataSetChanged();

                                        limpaCampos();

                                        Toast.makeText(MainActivity.this,
                                                "Atualizado com sucesso",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Ocorreu um erro ao atualizar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    // AULA 12 (FIM)-------------------------------------------------
                }
            }


        });

        // AULA 10 (INICIO)-------------------------------------------------
        listaView_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Contato c = (Contato) adapterView.getAdapter().getItem(position);

                editText_id.setText(String.valueOf(c.getId()));
                editText_nome.setText(c.getNome());
                editText_telefone.setText(c.getTelefone());
                editText_email.setText(c.getEmail());

                itemClicado = position;
            }
        });
        // AULA 10 (FIM)-------------------------------------------------
        // AULA 13 (INICIO)-------------------------------------------------
        button_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editText_id.getText().toString();

                if(id.isEmpty()) {

                    Toast.makeText(MainActivity.this,
                            "Nenhum contato está selecionado",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Apagar o contato;
                    String url = HOST + "/delete.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("id", id)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if(result.get("DELETE").getAsString().equals("OK")) {

                                        lista.remove(itemClicado);

                                        contatosAdapter.notifyDataSetChanged();

                                        limpaCampos();

                                        Toast.makeText(MainActivity.this,
                                                "Excluído com sucesso",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Ocorreu um erro ao excluir", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        // AULA 13 (FIM)-------------------------------------------------
    }
    // AULA 10 (INICIO)-------------------------------------------------
    // Movido para cá na aula 10
    public void limpaCampos() {
        editText_id.setText("");
        editText_nome.setText("");
        editText_telefone.setText("");
        editText_email.setText("");

        editText_nome.requestFocus();
    }
    // AULA 10 (FIM)-------------------------------------------------
    // AULA 09 (INICIO)-------------------------------------------------
    private void listaContatos(){
        String url = HOST + "/read.php";

        Ion.with(getBaseContext())
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        for(int i = 0; i < result.size(); i++){

                            JsonObject obj = result.get(i).getAsJsonObject();

                            Contato c = new Contato();

                            c.setId(obj.get("id").getAsInt());
                            c.setNome(obj.get("nome").getAsString());
                            c.setTelefone(obj.get("telefone").getAsString());
                            c.setEmail(obj.get("email").getAsString());

                            lista.add(c);
                        }

                        contatosAdapter.notifyDataSetChanged();
                    }
                });
    }
    // AULA 09 (FIM)-------------------------------------------------
}
