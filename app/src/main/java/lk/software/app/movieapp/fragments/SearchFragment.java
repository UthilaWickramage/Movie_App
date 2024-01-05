package lk.software.app.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import lk.software.app.movieapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private static SearchFragmentListener searchFragmentListener;
    EditText editText;
    public interface SearchFragmentListener{
        void passSearchText(String searchText);
    }


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.autoCompleteTextView);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(editText.getText().toString().isEmpty()){
                    editText.setError("Search term cannot be empty");
                    return false;
                }else{
                    String searchText = editText.getText().toString();
                    System.out.println(searchText);
                    searchFragmentListener.passSearchText(searchText);
                    return true;
                }

            }
        });
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragmentListener){
            searchFragmentListener = (SearchFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}