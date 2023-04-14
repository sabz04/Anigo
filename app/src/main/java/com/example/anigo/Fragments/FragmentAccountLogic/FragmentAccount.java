package com.example.anigo.Fragments.FragmentAccountLogic;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Credentials;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivity;
import com.example.anigo.Activities.AnimeAdapter;
import com.example.anigo.Activities.CodeSendActivityLogic.CodeSendActivity;
import com.example.anigo.DialogHelper.CreateLoadingContactDialog;
import com.example.anigo.Models.Anime;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.example.anigo.UiHelper.ImageBitmapHelper;
import com.example.anigo.Activities.MainActivityLogic.MainActivity;
import com.example.anigo.Models.User;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends Fragment implements  FragmentAccountContract.View{
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private AlertDialog changePhotoDialog;
    private EditText pass_edit;
    private EditText email_tb;
    private TextView login_tv;
    private int userId = -1;
    private Button exit;
    private ImageView image_ava;
    private SwipeRefreshLayout swp;
    RecyclerView recyclerView;
    private FragmentAccountContract.Presenter presenter;
    CreateLoadingContactDialog loadingContactDialog;

    private String userAvatarUrl;
    private String email="";
    private String login="";
    private String password="";
    ActivityResultLauncher<String> launcher;

    private FragmentAccountChangePhoto fragmentAccountChangePhotoPresenter;

    public FragmentAccount() {

    }

    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            userAvatarUrl = savedInstanceState.getString("avatar");
            login = savedInstanceState.getString("login");
            password = savedInstanceState.getString("password");
            email = savedInstanceState.getString("email");
            presenter = new FragmentAccountPresenter(this, getContext());
            presenter.GetUser();
        }


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onCreate(outState);
        outState.putString("avatar", userAvatarUrl);
        outState.putString("login", login);
        outState.putString("password", password);
        outState.putString("email", email);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        presenter = new FragmentAccountPresenter(this, getContext());
        fragmentAccountChangePhotoPresenter  = new FragmentAccountChangePhoto(this, getContext());
        loadingContactDialog = new CreateLoadingContactDialog(getContext());
        exit = (Button) view.findViewById(R.id.button_exit);

        swp = view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);

        login_tv = view.findViewById(R.id.login_tb);
        email_tb = view.findViewById(R.id.post_tb);
        pass_edit = view.findViewById(R.id.password_tb);
        image_ava = view.findViewById(R.id.user_image);
        recyclerView = view.findViewById(R.id.historyRecyclerView);


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

            }});

        image_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CreateNewChooseDialog();
            }
        });

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
                userAvatarUrl = "";
                presenter.GetUser();
            }
        });
        if(!login.isEmpty()){
            login_tv.setText(login);
            email_tb.setText(email);
            pass_edit.setText(password);
            Picasso.with(getContext()).load(RequestOptions.MainHost + userAvatarUrl).into(image_ava);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Получаем URI выбранного изображения
            Uri imageUri = data.getData();

            fragmentAccountChangePhotoPresenter.ChangePhoto(imageUri);
            loadingContactDialog.ShowDialog();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Разрешение предоставлено
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);

            loadingContactDialog.ShowDialog();
            changePhotoDialog.cancel();
      } else {
            // Разрешение не предоставлено
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Доступ к галерее запрещен", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public File uriToFile(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        return file;
    }
    private void CreateNewChooseDialog(){
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(getContext());
        View changingView = getLayoutInflater().inflate(R.layout.android_choose_changing, null);

        Button acceptButton = changingView.findViewById(R.id.acceptButton);
        Button declineButton = changingView.findViewById(R.id.declineButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSIONS);
                } else {
                    // Разрешение уже предоставлено
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                    changePhotoDialog.cancel();

                }


            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changePhotoDialog != null){
                    changePhotoDialog.cancel();
                }
            }
        });
        changingView.setClipToOutline(true);
        dialog_builder.setView(changingView);
        changePhotoDialog = dialog_builder.create();
        changePhotoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changePhotoDialog.show();
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
        this.userId = user.id;
        this.login = user.name;
        this.password = user.password;
        this.email = user.email;
        this.userAvatarUrl = user.image;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_get_history,userId))
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    Anime[] animes = new Gson().fromJson(json_body, Anime[].class);
                    AnimeAdapter adapter = new AnimeAdapter(animes);
                    adapter.setOnItemClickListener(new AnimeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent to_anime = new Intent(getActivity(), AnimeActivity.class);
                            Bundle bundle = new Bundle();
                            int id = animes[position].shikiId;
                            bundle.putInt("id", id);
                            to_anime.putExtras(bundle);
                            startActivity(to_anime);
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);

                        }
                    });

                }
                else {

                }

            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(false);
                login_tv.setText(login);
                pass_edit.setText(password);
                email_tb.setText(email);
                Picasso.with(getContext()).load(RequestOptions.MainHost+userAvatarUrl).into(image_ava);
            }
        });


    }

    @Override
    public void onError(String message, String body) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingContactDialog != null){
                    loadingContactDialog.DeleteDialog();
                }
            }
        });

    }

    @Override
    public void onError(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingContactDialog != null){
                    loadingContactDialog.DeleteDialog();
                }
            }
        });
    }

    @Override
    public void OnSuccessChangePhoto(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changePhotoDialog.cancel();
                loadingContactDialog.DeleteDialog();
                Picasso.with(getContext()).load(RequestOptions.MainHost + message).into(image_ava);
                userAvatarUrl = message;
            }
        });
    }

    @Override
    public void OnErrorChangePhoto(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingContactDialog != null){
                    loadingContactDialog.DeleteDialog();
                }
            }
        });
    }
}