package br.com.trabalhoDispositivosMoveis.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import br.com.trabalhoDispositivosMoveis.R;
import br.com.trabalhoDispositivosMoveis.fragments.PessoasFavoritasFragment;
import br.com.trabalhoDispositivosMoveis.fragments.MostrarPessoasFragment;

/**
 * Created by RODRIGO on 19/11/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nome;
    private TextView email;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarMenu();
        criaCabecalho();
        criarLeitorMenu(savedInstanceState);
    }

    private void criarMenu(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void criaCabecalho(){
        View header = navigationView.getHeaderView(0);
        nome = (TextView) header.findViewById(R.id.meunome);
        email = (TextView) header.findViewById(R.id.email);
        mostraInfoDev();
    }

    public void mostraInfoDev(){
        nome.setText("Rodrigo Sipriano Pereira");
        email.setText("rodrigo-s-pereira@hotmail.com");
    }

    private void criarLeitorMenu(Bundle bundle){
        if(bundle == null){
            MenuItem item = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(isNavigationDrawerOpen()){
            closeNavitagionDrawer();
        }
        else {
            super.onBackPressed();
        }

    }

    protected boolean isNavigationDrawerOpen(){

        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);

    }

    protected void closeNavitagionDrawer(){
        if(drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        selectDrawerItem(item);
        return true;
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        ListFragment listFragment = null;

        switch (menuItem.getItemId())
        {
            case R.id.fragmento_contatos:
                listFragment = new MostrarPessoasFragment();
                break;
            case R.id.fragmento_favoritos:
                listFragment = new PessoasFavoritasFragment();
                break;
            default:
                break;
        }
        if(listFragment != null)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.drawer_content, listFragment).commit();
            setTitle(menuItem.getTitle());
        }
    }

}
