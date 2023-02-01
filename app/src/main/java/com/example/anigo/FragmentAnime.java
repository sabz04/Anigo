package com.example.anigo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAnime extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAnime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAnime.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAnime newInstance(String param1, String param2) {
        FragmentAnime fragment = new FragmentAnime();
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
        View view = inflater.inflate(R.layout.fragment_anime, container, false);


        Anime value = (Anime) getArguments().getSerializable("current_title");

        TextView name_txt = (TextView) view.findViewById(R.id.itemName);

        TextView desc_txt = (TextView) view.findViewById(R.id.itemDesc);

        TextView score_txt = (TextView) view.findViewById(R.id.itemScore);

        TextView date_txt = (TextView) view.findViewById(R.id.itemYear);

        TextView type_txt = (TextView) view.findViewById(R.id.itemType);

        desc_txt.setText(value.description);

        score_txt.setText(String.valueOf( value.scoreShiki));


        /*Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(value.Aire);*/

//        date_txt.setText(String.valueOf(cal.get(Calendar.YEAR)) + " г.");
        String type="Неизвестен";
        if(value.type != null) {

            type = value.type.name;
            if(type.equals("tv"))
                type= "Тв-сериал";
            if(type.equals("movie"))
                type= "Фильм";
            if(type.equals("ova"))
                type= "ОВА";
        }
        type_txt.setText(type);

        ImageView img = (ImageView) view.findViewById(R.id.itemPoster) ;

        img.setImageBitmap(GetImageBitmap(java.util.Base64.getDecoder().decode(value.images[0].original)));

        name_txt.setText(value.nameRus);
        return view;
    }
    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}
