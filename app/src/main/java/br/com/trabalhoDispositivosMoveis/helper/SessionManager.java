package br.com.trabalhoDispositivosMoveis.helper;


import android.content.Context;
import android.content.SharedPreferences;

import br.com.trabalhoDispositivosMoveis.bean.PessoaBean;

import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.CELULAR;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.EMAIL;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.ENDERECO;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.FAVORITO;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.FOTO;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.MUSICA;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.NOME;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.SITE;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato.VIDEO;
import static br.com.trabalhoDispositivosMoveis.helper.DataBaseHelper.Contato._ID;

/**
 * Created by ANDRE on 13/11/2017.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    String CADASTRO = "cadastro";

    int PRIVATE_MODE = 0;


    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences("Contato",PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean cadastro(){
        return pref.getBoolean(CADASTRO,false);
    }

    public void setCadastro(boolean cadastro){
        editor.putBoolean(CADASTRO,cadastro);
    }

    public void insereContato(PessoaBean pessoaBean){
        limpaVariaveis();
        if(pessoaBean.getId() != 0){
            editor.putLong(_ID, pessoaBean.getId());
        }
        if(pessoaBean.getNome() != null){
            editor.putString(NOME, pessoaBean.getNome());
        }
        if(pessoaBean.getCelular() != null){
            editor.putString(CELULAR, pessoaBean.getCelular());
        }
        if(pessoaBean.getEmail() != null){
            editor.putString(EMAIL, pessoaBean.getEmail());
        }
        if(pessoaBean.getSiteFavorito() != null){
            editor.putString(SITE, pessoaBean.getSiteFavorito());
        }
        if(pessoaBean.getMusicaFavorita() != null){
            editor.putString(MUSICA, pessoaBean.getMusicaFavorita());
        }
        if(pessoaBean.getVideoFavorito() != null){
            editor.putString(VIDEO, pessoaBean.getVideoFavorito());
        }
        if(pessoaBean.getCaminhoFoto() != null){
            editor.putString(FOTO, pessoaBean.getCaminhoFoto());
        }
        if(pessoaBean.getEndereco() != null){
            editor.putString(ENDERECO, pessoaBean.getEndereco());
        }
        editor.putBoolean(FAVORITO, pessoaBean.isFavorito());
        editor.commit();
    }

    public PessoaBean retornaUsuario(){
        PessoaBean contato = new PessoaBean();
        contato.setId(pref.getInt(_ID,0));
        contato.setNome(pref.getString(NOME,""));
        contato.setCelular(pref.getString(CELULAR,""));
        contato.setEmail(pref.getString(EMAIL,""));
        contato.setSiteFavorito(pref.getString(SITE,""));
        contato.setMusicaFavorita(pref.getString(MUSICA,""));
        contato.setEndereco(pref.getString(ENDERECO,null));
        contato.setVideoFavorito(pref.getString(VIDEO,""));
        contato.setFavorito(pref.getBoolean(FAVORITO,false));
        contato.setCaminhoFoto(pref.getString(FOTO,null));
        return contato;
    }

    public void setFoto(String caminhofoto){
        editor.putString(FOTO,caminhofoto);
        editor.commit();
    }

    public String getFoto(){
        return pref.getString(FOTO,null);
    }

    public void setEndereco(String endereco){
        editor.putString(ENDERECO,endereco);
        editor.commit();
    }

    public String getEndereco(){
        return pref.getString(ENDERECO,null);
    }

    public void limpaVariaveis(){
        editor.remove(_ID).commit();
        editor.remove(NOME).commit();
        editor.remove(CELULAR).commit();
        editor.remove(EMAIL).commit();
        editor.remove(SITE).commit();
        editor.remove(MUSICA).commit();
        editor.remove(ENDERECO).commit();
        editor.remove(VIDEO).commit();
        editor.remove(FAVORITO).commit();
        editor.remove(FOTO).commit();
        editor.clear().commit();;
    }

}
