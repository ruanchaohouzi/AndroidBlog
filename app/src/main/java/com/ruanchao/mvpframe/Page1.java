package com.ruanchao.mvpframe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Page1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View inflate = inflater.inflate(R.layout.layout, container, false);
        Bundle arguments = getArguments();
        String page = arguments.getString("page");
        TextView textView = inflate.findViewById(R.id.tv_text);
        textView.setText(page);

        return inflate;
    }
}
