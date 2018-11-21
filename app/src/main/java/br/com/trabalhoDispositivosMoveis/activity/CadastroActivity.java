package br.com.trabalhoDispositivosMoveis.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.io.File;

import br.com.trabalhoDispositivosMoveis.R;
import br.com.trabalhoDispositivosMoveis.bean.PessoaBean;
import br.com.trabalhoDispositivosMoveis.dao.PessoaDao;
import br.com.trabalhoDispositivosMoveis.helper.SessionManager;

/**
 * Created by RODRIGO on 19/11/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    private PessoaBean contato;
    private ImageView imagem;
    private Button foto;
    private Button galeria;
    private EditText nome;
    private MaskedEditText celular;
    private EditText email;
    private EditText site;
    private EditText musica;
    private EditText video;
    private Button endereco;
    private CheckBox favorito;
    private Button salva;
    private SessionManager sessionManager;
    private int REQUEST_CODE = 200;
    private int CAMERA = 10;
    private int GALERIA = 11;
    private String pathFoto;
    private String MSG_ERRO;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_contato);
        contato = (getIntent().hasExtra("contato")) ? (PessoaBean) getIntent().getSerializableExtra("contato") : null;
        sessionManager = new SessionManager(getBaseContext());
        iniciaComponentes();
        pegarDadosExistentes();
        setupToolbar();
        endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Endereco();
            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takephoto();
            }
        });
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarGaleria();
            }
        });
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarContato();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place endereco = PlaceAutocomplete.getPlace(getBaseContext(),data);
                String enderecoContato = endereco.getAddress().toString();
                this.endereco.setText(enderecoContato);
                sessionManager.setEndereco(enderecoContato);
            }
            else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(getBaseContext(), data);
                Log.i("Erro", status.getStatusMessage());
            }
            else if(resultCode == RESULT_CANCELED){
                Log.i("Operação cancelada", "Usuário cancelou a consulta");
            }
        }
        if(requestCode == CAMERA && resultCode == RESULT_OK){
            pegarFoto(imagem, pathFoto);
        }
        if (resultCode == RESULT_OK && requestCode == GALERIA) {
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            pathFoto = c.getString(columnIndex);
            c.close();
            pegarFoto(imagem, pathFoto);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setCadastro(false);
        sessionManager.limpaVariaveis();
        startActivity(new Intent(CadastroActivity.this,MainActivity.class));
    }

    public void mostrarGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALERIA);
    }

    public void iniciaComponentes(){
        favorito = (CheckBox) findViewById(R.id.contato_favorito);
        imagem = (ImageView) findViewById(R.id.foto_contato);
        foto = (Button) findViewById(R.id.tira_foto);
        galeria = (Button) findViewById(R.id.acessa_galeria);
        nome = (EditText) findViewById(R.id.nome_contato_cadastro);
        celular = (MaskedEditText) findViewById(R.id.telefone_usuario_cadastro);
        email = (EditText) findViewById(R.id.email_contato_cadastro);
        site = (EditText) findViewById(R.id.site_contato_cadastro);
        musica = (EditText) findViewById(R.id.musica_contato_cadastro);
        video = (EditText) findViewById(R.id.video_contato_cadastro);
        endereco = (Button) findViewById(R.id.endereco_usuario);
        salva = (Button) findViewById(R.id.cadastrar);
        if(sessionManager.getFoto() != null){
            pathFoto = sessionManager.getFoto();
            pegarFoto(imagem, pathFoto);
        }
        if(sessionManager.getEndereco() != null){
            endereco.setText(sessionManager.getEndereco());
        }
        else{
            endereco.setText("Mudar Endereço");
        }
    }

    private void pegarFoto(ImageView imagem, String photoPath){
        int widthTarget = imagem.getWidth();
        int heightTarget = 200;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath,options);
        int height = options.outHeight;
        int width = options.outWidth;
        int constante = 1;
        if(height > heightTarget || width > widthTarget){
            final int halfedHeight = height/2;
            final int helfedWidth = width/2;
            while((halfedHeight/constante)> heightTarget && (helfedWidth/constante) > widthTarget){
                constante *= 2;
            }
        }
        if(constante > 8){
            constante = 8;
        }
        options.inSampleSize = constante;
        options.inJustDecodeBounds = false;
        imagem.setImageBitmap(BitmapFactory.decodeFile(photoPath,options));
        imagem.setTag(photoPath);
        sessionManager.setFoto(photoPath);
    }

    public void takephoto(){
        Intent tiraFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(tiraFoto.resolveActivity(getPackageManager()) != null){
            pathFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis()+".jpg";
            Uri localFoto = Uri.fromFile(new File(pathFoto));
            tiraFoto.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
            startActivityForResult(tiraFoto, CAMERA);
        }
    }


    public void pegarDadosExistentes(){
        endereco.setText("ALTERAR ENDEREÇO");
        salva.setText("CADASTRAR");
        if(contato != null){
            salva.setText("ALTERAR");
            video.setText(contato.getVideoFavorito());
            musica.setText(contato.getMusicaFavorita());
            site.setText(contato.getSiteFavorito());
            email.setText(contato.getEmail());
            celular.setText(contato.getCelular());
            nome.setText(contato.getNome());
            if(contato.isFavorito()){
                favorito.setChecked(true);
            }
            if(contato.getEndereco() != null){
                endereco.setText(contato.getEndereco());
            }
            if(contato.getCaminhoFoto() != null){
                pegarFoto(imagem, contato.getCaminhoFoto());
            }
        }
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setShowHideAnimationEnabled(true);
            bar.setTitle("Cadastrar Contatos");
        }
    }


    public void guardarContato(){

        String savedName = nome.getText().toString().trim();
        String savedPhone = celular.getText().toString().trim();
        String savedEMail = email.getText().toString().trim();
        String savedSite = site.getText().toString().trim();
        String savedMusic = musica.getText().toString().trim();
        String address = this.endereco.getText().toString().trim();
        String savedAddress = !address.equals("ALTERAR ENDEREÇO")? address: "";
        String savedVideo = video.getText().toString().trim();
        String photo = null;
        if(imagem.getTag() != null){
            photo = imagem.getTag().toString();
        }
        boolean savedFavoriteChack;
        if (favorito.isChecked())
            savedFavoriteChack = true;
        else savedFavoriteChack = false;

        if(nome.getText().toString().isEmpty() || celular.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext()," VERIFIQUE SE HÁ UM NOME OU TELEFONE",Toast.LENGTH_LONG).show();
        }
        else{
            PessoaBean pessoaBeanCadastro = new PessoaBean(savedVideo,savedMusic,savedEMail,savedName,savedPhone,savedAddress,savedSite,photo,savedFavoriteChack);
            int id = 0;
            if(getIntent().hasExtra("contato")){
                id = contato.getId();
            }
            pessoaBeanCadastro.setId(id);
            PessoaDao pessoaDao = new PessoaDao(getBaseContext());
            long idContatoSalvo = pessoaDao.inserirContato(pessoaBeanCadastro);
            if(idContatoSalvo != -1){
                sessionManager.setFoto(null);
                sessionManager.setEndereco(null);
                if(id == 0){
                    Toast.makeText(getBaseContext(),"Contato criado com sucesso!",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getBaseContext(),"Mudança efetuada",Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
            else{
                Toast.makeText(getBaseContext(),"Erro ao cadastrar usuário",Toast.LENGTH_LONG);
            }
        }
    }

    public void Endereco(){
        try {
            startActivityForResult(new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this), REQUEST_CODE);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getBaseContext(),"Erro no apliactivo GooglePlayServices",Toast.LENGTH_LONG);
            Log.e("GooglePlayServiceError",e.getMessage());
        }
    }

}
