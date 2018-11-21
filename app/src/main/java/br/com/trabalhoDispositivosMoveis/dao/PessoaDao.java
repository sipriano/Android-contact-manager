package br.com.trabalhoDispositivosMoveis.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.trabalhoDispositivosMoveis.bean.PessoaBean;
import br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper;

/**
 * Created by ANDRE on 10/11/2017.
 */

public class PessoaDao {

    private static DataBaseHelper helper = null;
    private static SQLiteDatabase db = null;
    private List<Map<String,Object>> contatos;

    public PessoaDao(Context context){
        helper = new DataBaseHelper(context);
    }

    private PessoaBean criaConta(Cursor cursor){
        boolean favorito =  (cursor.getInt(cursor.getColumnIndex(DataBaseHelper.Contato.FAVORITO))) == 1;
        PessoaBean pessoaBean = new PessoaBean
                (cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.VIDEO)),
                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.MUSICA)),
                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.EMAIL)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.NOME)),
                                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.CELULAR)),
                                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.ENDERECO)),
                                                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.SITE)),
                                                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Contato.FOTO)),
                                                                       favorito);
        pessoaBean.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.Contato._ID)));
        return pessoaBean;
    }

    public long inserirContato(PessoaBean pessoaBean){
        db = helper.getWritableDatabase();
        long resultado;
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Contato.VIDEO, pessoaBean.getVideoFavorito());
        values.put(DataBaseHelper.Contato.MUSICA, pessoaBean.getMusicaFavorita());
        values.put(DataBaseHelper.Contato.EMAIL, pessoaBean.getEmail());
        values.put(DataBaseHelper.Contato.ENDERECO, pessoaBean.getEndereco());
        values.put(DataBaseHelper.Contato.NOME, pessoaBean.getNome());
        values.put(DataBaseHelper.Contato.CELULAR, pessoaBean.getCelular());
        values.put(DataBaseHelper.Contato.SITE, pessoaBean.getSiteFavorito());
        values.put(DataBaseHelper.Contato.FOTO, pessoaBean.getCaminhoFoto());

        if(pessoaBean.isFavorito()){
            values.put(DataBaseHelper.Contato.FAVORITO,"1");
        }
        else {
            values.put(DataBaseHelper.Contato.FAVORITO,"0");
        }

        if(pessoaBean.getId() == 0){
            resultado = db.insert(DataBaseHelper.Contato.TABELA, null , values);
        }
        else {
            String[] whereArgs = new String[]{pessoaBean.getId()+""};
            resultado = db.update(DataBaseHelper.Contato.TABELA,values," _id = ?", whereArgs);
        }
        db.close();
        return resultado;
    }

    public List<Map<String, Object>> listarContatos(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DataBaseHelper.Contato.TABELA+" ORDER BY "+DataBaseHelper.Contato.NOME+" ASC",null);
        cursor.moveToFirst();
        contatos = new ArrayList<>();

        for(int i=0;i<cursor.getCount();i++){
            Map<String,Object> item = new HashMap<>();

            PessoaBean pessoaBean = criaConta(cursor);

            item.put(DataBaseHelper.Contato.VIDEO, pessoaBean.getVideoFavorito());
            item.put(DataBaseHelper.Contato.MUSICA, pessoaBean.getMusicaFavorita());
            item.put(DataBaseHelper.Contato._ID, pessoaBean.getId());
            item.put(DataBaseHelper.Contato.EMAIL, pessoaBean.getEmail());
            item.put(DataBaseHelper.Contato.ENDERECO, pessoaBean.getEndereco());
            item.put(DataBaseHelper.Contato.NOME, pessoaBean.getNome());
            item.put(DataBaseHelper.Contato.CELULAR, pessoaBean.getCelular());
            item.put(DataBaseHelper.Contato.SITE, pessoaBean.getSiteFavorito());
            item.put(DataBaseHelper.Contato.FOTO, pessoaBean.getCaminhoFoto());
            if(pessoaBean.isFavorito()){
                item.put(DataBaseHelper.Contato.FAVORITO,"TRUE");
            }
            else{
                item.put(DataBaseHelper.Contato.FAVORITO,"FALSE");
            }
            contatos.add(item);
            cursor.moveToNext();
        }
        return contatos;
    }

    public int delete(Integer id){
        db = helper.getWritableDatabase();
        String where[] = new String[] {id.toString()};
        int resultado = db.delete(DataBaseHelper.Contato.TABELA,"_id = ?",where);
        db.close();
        return resultado;
    }

    public List<Map<String, Object>> listaContatosFavoritos(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DataBaseHelper.Contato.TABELA+" WHERE "+DataBaseHelper.Contato.FAVORITO+" = 1 ORDER BY "+DataBaseHelper.Contato.NOME+" ASC",null);
        cursor.moveToFirst();
        contatos = new ArrayList<>();

        for(int i=0;i<cursor.getCount();i++){
            Map<String,Object> item = new HashMap<>();

            PessoaBean pessoaBean = criaConta(cursor);

            item.put(DataBaseHelper.Contato.VIDEO, pessoaBean.getVideoFavorito());
            item.put(DataBaseHelper.Contato.MUSICA, pessoaBean.getMusicaFavorita());
            item.put(DataBaseHelper.Contato._ID, pessoaBean.getId());
            item.put(DataBaseHelper.Contato.EMAIL, pessoaBean.getEmail());
            item.put(DataBaseHelper.Contato.ENDERECO, pessoaBean.getEndereco());
            item.put(DataBaseHelper.Contato.NOME, pessoaBean.getNome());
            item.put(DataBaseHelper.Contato.CELULAR, pessoaBean.getCelular());
            item.put(DataBaseHelper.Contato.SITE, pessoaBean.getSiteFavorito());
            item.put(DataBaseHelper.Contato.FOTO, pessoaBean.getCaminhoFoto());
            item.put(DataBaseHelper.Contato.FAVORITO,"TRUE");
            contatos.add(item);
            cursor.moveToNext();
        }
        return contatos;
    }

}
