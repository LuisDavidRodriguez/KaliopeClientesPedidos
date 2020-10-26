package com.example.kaliopeclientespedidos.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kaliopeclientespedidos.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SegundoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SegundoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SegundoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SegundoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SegundoFragment newInstance(String param1, String param2) {
        SegundoFragment fragment = new SegundoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_segundo, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //es mejor referenciar componentes aqui, el on crate view podria tener problemas inflando las vistas y depronto
        //no cargarce correctamente, entonces este metodo se llama inmediatamente despues del onCreateView cuando la vista ya fue creada completamente

        Button boton1 = (Button) view.findViewById(R.id.segundoFragment_btnregresar);
        Button boton2 = (Button) view.findViewById(R.id.segundoFragment_btnsiguiente);

        final NavController navController = Navigation.findNavController(view);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //y aqui vamos a llamar al fragmento que corresponde a cada boton
                //Navigation.findNavController(v).navigate(R.id.FirstFragment);
                navController.navigate(R.id.inicioFragment);
            }
        });


        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //y aqui vamos a llamar al fragmento que corresponde a cada boton
                navController.navigate(R.id.tercerFragment);
            }
        });

    }
}