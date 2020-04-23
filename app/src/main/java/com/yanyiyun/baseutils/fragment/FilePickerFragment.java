package com.yanyiyun.baseutils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.adapter.FilePickerAdapter;
import com.yanyiyun.baseutils.library.tool.file.FileUtil;
import com.yanyiyun.baseutils.library.view.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class FilePickerFragment extends Fragment {

    private EmptyRecyclerView list_erv;
    private LinearLayout directory_empty_view;

    private String CURRENT_PATH;
    private static final String FILE_PATH="current_path";
    private FilePickerAdapter adapter;
    private ArrayList<File> data=new ArrayList<>();
    private FileClickListener mFileClickListener;

    public static FilePickerFragment getInstance(String path){
        FilePickerFragment fragment=new FilePickerFragment();
        Bundle bundle=new Bundle();
        bundle.putString(FILE_PATH,path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.file_picker_fragment,null);
        list_erv=view.findViewById(R.id.list_erv);
        directory_empty_view=view.findViewById(R.id.directory_empty_view);
        CURRENT_PATH=getArguments().getString(FILE_PATH);
        initview();
        return view;
    }

    private void initview() {
        adapter=new FilePickerAdapter(getContext(), FileUtil.getInstance().getFieListByDirPath(CURRENT_PATH,null));
        adapter.setonClickListener(new FilePickerAdapter.onClickListener() {
            @Override
            public void onClick(File file) {
                mFileClickListener.onFileClicked(file);
            }
        });

        list_erv.setLayoutManager(new LinearLayoutManager(getContext()));
        list_erv.setAdapter(adapter);
        list_erv.setEmptyView(directory_empty_view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFileClickListener= (FileClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileClickListener=null;
    }

    public interface FileClickListener {
        void onFileClicked(File clickedFile);
    }
}
