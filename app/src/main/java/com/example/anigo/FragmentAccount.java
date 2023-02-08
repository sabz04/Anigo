package com.example.anigo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends Fragment implements  FragmentAccountContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;

    private FragmentAccountContract.Presenter presenter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        this.view =view;
        presenter = new FragmentAccountPresenter(this, getContext());

        Button btn = (Button) view.findViewById(R.id.button_exit);

        presenter.GetUser();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.Exit(view.getContext());
            }
        });

        return view;
    }

    @Override
    public void onSuccess(String message) {

        Handler dn = new Handler();
        dn.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onSuccess(User user) {
        String name = user.name;
        String pass = user.password;
        String email = user.email;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView image_ava = (ImageView) view.findViewById(R.id.user_image);

                EditText login_edit = (EditText) view.findViewById(R.id.login_tb);
                EditText pass_edit = (EditText) view.findViewById(R.id.password_tb);
                EditText email_edit = (EditText) view.findViewById(R.id.post_tb);

                login_edit.setText(name);
                pass_edit.setText(pass);
                email_edit.setText(email);
                image_ava.setImageBitmap(GetImageBitmap(java.util.Base64.getDecoder().decode(user.image)));
            }
        });


    }

    @Override
    public void onError(String message, String body) {

    }

    @Override
    public void onError(String message) {

    }
    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}