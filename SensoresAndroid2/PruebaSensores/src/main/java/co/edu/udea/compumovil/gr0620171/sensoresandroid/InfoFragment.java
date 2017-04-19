package co.edu.udea.compumovil.gr0620171.sensoresandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends DialogFragment {

    TextView descripcion,datos, uso, title;
    String Sdescripcion,Sdatos, Suso, Stitle;
    public InfoFragment() {
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Sdescripcion = getArguments().getString("descripcion");
        Sdatos = getArguments().getString("datos");
        Suso = getArguments().getString("uso");
        Stitle = getArguments().getString("sensor");

        descripcion = (TextView) view.findViewById(R.id.descripcion);
        datos = (TextView) view.findViewById(R.id.datos);
        uso = (TextView) view.findViewById(R.id.Uso);
        title = (TextView) view.findViewById(R.id.title);

        title.setText(Stitle);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        descripcion.setText("Descripcion: "+ Sdescripcion);
        uso.setText("Uso: "+ Suso);
        datos.setText("Datos: "+ Sdatos);

        return view;
    }

}
