package com.example.anigo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    EditText pass_edit;
    EditText email_tb;
    TextView login_tv;

    Button exit;

    ImageView image_ava;

    View view;
    SwipeRefreshLayout swp;

    byte[] avatar;
    String email="";
    String login="";
    String password="";



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
        if(savedInstanceState != null){
            avatar = savedInstanceState.getByteArray("avatar");
            login = savedInstanceState.getString("login");
            password = savedInstanceState.getString("password");
            email = savedInstanceState.getString("email");
        }


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onCreate(outState);

        outState.putByteArray("avatar", avatar);
        outState.putString("login", login);
        outState.putString("password", password);
        outState.putString("email", email);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        this.view =view;
        presenter = new FragmentAccountPresenter(this, getContext());

        exit = (Button) view.findViewById(R.id.button_exit);

        swp = view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);

        login_tv = view.findViewById(R.id.login_tb);
        email_tb = view.findViewById(R.id.post_tb);
        pass_edit = view.findViewById(R.id.password_tb);
        image_ava = view.findViewById(R.id.user_image);

        pass_edit.setKeyListener(null);
        pass_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(getActivity(), CodeSendActivity.class);
                    Bundle email_bundle = new Bundle();
                    email_bundle.putString("email", email);
                    intent.putExtras(email_bundle);
                    startActivity(intent);
                }
                return true;
            }
        });
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                presenter.GetUser();
            }
        });
        if(!login.isEmpty()){
            login_tv.setText(login);
            email_tb.setText(email);
            pass_edit.setText(password);
            image_ava.setImageBitmap(ImageBitmapHelper.GetImageBitmap(avatar));
        }
        else {
            presenter.GetUser();
        }

        exit.setOnClickListener(new View.OnClickListener() {
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
                NavigationActivity.animes_pagination_popular.clear();
                NavigationActivity.animes_pagination.clear();
                NavigationActivity.favourites_pagination.clear();
            }
        });
    }

    @Override
    public void onSuccess(User user) {
        this.login = user.name;
        this.password = user.password;
        this.email = user.email;
        this.avatar = java.util.Base64.getDecoder().decode(user.image);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(false);
                login_tv.setText(login);
                pass_edit.setText(password);
                email_tb.setText(email);
                image_ava.setImageBitmap(ImageBitmapHelper.GetImageBitmap(avatar));
            }
        });


    }

    @Override
    public void onError(String message, String body) {

    }

    @Override
    public void onError(String message) {

    }
 }