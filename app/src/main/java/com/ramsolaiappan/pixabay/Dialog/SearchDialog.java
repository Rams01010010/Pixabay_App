package com.ramsolaiappan.pixabay.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ramsolaiappan.pixabay.Activities.MainActivity;
import com.ramsolaiappan.pixabay.Adapters.SpinnerAdapter;
import com.ramsolaiappan.pixabay.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchDialog extends DialogFragment {

    private Context context;
    private EditText searchET,minWidth,minHeight;
    private Button cancelBtn,searchBtn;
    private MaterialButton optionsBtn;
    private LinearLayout optionsLayout;
    private Spinner typeSp,orientationSp,categorySp,colorSp;
    private ArrayList<String> types = new ArrayList<>(Arrays.asList(new String[]{"all","photo","illustration","vector"}));
    private ArrayList<String> orientations = new ArrayList<>(Arrays.asList(new String[]{"all","horizontal","vertical"}));
    private ArrayList<String> categories = new ArrayList<>(Arrays.asList(new String[]{"Select","backgrounds","fashion","nature","science","education","feelings","health","people","religion","places","animals","industry","computer","food","sports","transportation","travel","buildings","business","music"}));
    private ArrayList<String> colors = new ArrayList<>(Arrays.asList(new String[]{"Select","grayscale","transparent","red","orange","yellow","green","turquoise","blue","lilac","pink","white","gray","black","brown"}));

    private OnClickListener onClickListener;
    private ArrayList<String> args = new ArrayList<>(Arrays.asList(new String[]{"","all","all","","0","0","","false"}));

    public SearchDialog(Context context) {
        this.context = context;
    }

    public interface OnClickListener
    {
        void OnSearch(ArrayList<String> args);
        void OnCancel();
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_search,null);
        searchET = view.findViewById(R.id.searchET);
        cancelBtn = view.findViewById(R.id.cancelSearchBtn);
        searchBtn = view.findViewById(R.id.searchBtn);
        optionsBtn = view.findViewById(R.id.optionsBtn);
        optionsLayout = view.findViewById(R.id.optionsLayout);
        typeSp = view.findViewById(R.id.typeSpinner);
        orientationSp = view.findViewById(R.id.orientationSpinner);
        categorySp = view.findViewById(R.id.categorySpinner);
        colorSp = view.findViewById(R.id.colorSpinner);
        minHeight = view.findViewById(R.id.minheightET);
        minWidth = view.findViewById(R.id.minwidthET);

        setSpinners();
        optionsLayout.setVisibility(View.GONE);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null)
                {
                    onClickListener.OnCancel();
                }
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchET.getText().toString().equals(""))
                {
                    if(onClickListener != null)
                    {
                        setArgs();
                        onClickListener.OnSearch(args);
                    }
                }
                else
                    searchET.setError("Fill Text");
            }
        });
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionsBtn.getTag().toString().equals("false"))
                {
                    optionsBtn.setIconResource(R.drawable.ic_arrow_up);
                    optionsBtn.setTag("true");
                    optionsLayout.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(searchET.getApplicationWindowToken(), 0);
                    Snackbar.make(searchBtn,"Scroll for more options", BaseTransientBottomBar.LENGTH_SHORT).setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
                else
                {
                    optionsBtn.setIconResource(R.drawable.ic_arrow_down);
                    optionsBtn.setTag("false");
                    optionsLayout.setVisibility(View.GONE);
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public void setArgs()
    {
        args.set(0,searchET.getText().toString());
        args.set(1,typeSp.getSelectedItem().toString());
        args.set(2,orientationSp.getSelectedItem().toString());
        args.set(3,categorySp.getSelectedItem().toString());
        args.set(4,minWidth.getText().toString());
        args.set(5,minHeight.getText().toString());
        args.set(6,colorSp.getSelectedItem().toString());
    }

    public void setSpinners()
    {
        SpinnerAdapter typeAdapter = new SpinnerAdapter(getContext(),R.layout.layout_spinner_item,types);
        typeAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        typeSp.setAdapter(typeAdapter);
        typeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setArgs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerAdapter orientationAdapter = new SpinnerAdapter(getContext(),R.layout.layout_spinner_item,orientations);
        orientationAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        orientationSp.setAdapter(orientationAdapter);
        orientationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setArgs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerAdapter categoryAdaper = new SpinnerAdapter(getContext(),R.layout.layout_spinner_item,categories);
        categoryAdaper.setDropDownViewResource(R.layout.layout_spinner_item);
        categorySp.setAdapter(categoryAdaper);
        categorySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setArgs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerAdapter colorAdaper = new SpinnerAdapter(getContext(),R.layout.layout_spinner_item,colors);
        colorAdaper.setDropDownViewResource(R.layout.layout_spinner_item);
        colorSp.setAdapter(colorAdaper);
        colorSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setArgs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
