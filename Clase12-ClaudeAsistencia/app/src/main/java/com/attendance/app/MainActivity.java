package com.attendance.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.attendance.app.databinding.ActivityMainBinding;
import com.attendance.app.navigation.NavigationHost;
import com.attendance.app.ui.fragments.AlumnosFragment;
import com.attendance.app.ui.fragments.AsistenciasFragment;
import com.attendance.app.ui.fragments.CursosFragment;

/**
 * Única Activity de la aplicación (Single Activity Architecture).
 *
 * <p>Implementa {@link NavigationHost} para que los fragmentos puedan
 * solicitar navegación sin acoplarse a esta clase concreta.</p>
 *
 * <p>Usa el FragmentManager manualmente (sin NavGraph XML) para mantener
 * el control explícito del back stack y las transiciones.</p>
 */
public class MainActivity extends AppCompatActivity implements NavigationHost {

    private ActivityMainBinding binding;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            // Carga el fragmento inicial sólo en la primera creación
            cargarFragmento(new CursosFragment(), CursosFragment.TAG, false);
        }
    }

    // ── NavigationHost ────────────────────────────────────────────────────────

    @Override
    public void navegarAAlumnos(long cursoId, String cursoNombre) {
        cargarFragmento(
                AlumnosFragment.newInstance(cursoId, cursoNombre),
                AlumnosFragment.TAG,
                true   // agrega al back stack
        );
    }

    @Override
    public void navegarAAsistencias(long alumnoId, long cursoId, String alumnoNombre) {
        cargarFragmento(
                AsistenciasFragment.newInstance(alumnoId, cursoId, alumnoNombre),
                AsistenciasFragment.TAG,
                true
        );
    }

    @Override
    public void navegarAtras() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    // ── Back press ────────────────────────────────────────────────────────────

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void cargarFragmento(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction tx = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, fragment, tag);

        if (addToBackStack) tx.addToBackStack(tag);
        tx.commit();
    }
}
