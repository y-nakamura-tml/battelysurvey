package com.technomedialab.battelysurvey.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.technomedialab.battelysurvey.ExplorerItem;
import com.technomedialab.battelysurvey.ExplorerListArrayAdapter;
import com.technomedialab.battelysurvey.R;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private File currentDir;
    private ViewAnimator dirViewStacks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        getActivity().setContentView(R.layout.fragment_dashboard);

        dirViewStacks = (ViewAnimator) getActivity().findViewById(R.id.dirViewAnimator);

        currentDir = new File("/");
        updateDir(currentDir);

        return root;
    }

    /** フォルダに移動して画面を更新する。 */
    private void updateDir(File file)
    {
        File[] files = file.listFiles();

        List<ExplorerItem> items = new ArrayList<ExplorerItem>();
        if(files != null){
            for(File f : files){
                Date lastModDate = new Date(f.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String dateModify = formater.format(lastModDate);
                items.add(new ExplorerItem(
                        f.getName(), f.getAbsolutePath(), dateModify));
            }
        }

        /**リストビューとアダプターの作成*/
        ExplorerListArrayAdapter adapter = new ExplorerListArrayAdapter(
                getActivity(), R.layout.explorer_lite_item, items);
        ListView listView = new ListView(getActivity());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ExplorerItem item
                        = (ExplorerItem) parent.getAdapter().getItem(position);
                File file = new File(item.path);
                if(file.isDirectory()){
                    currentDir = file;
                    updateDir(file);
                    dirViewStacks.showNext();
                }
            }
        });
        listView.setAdapter(adapter);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dirViewStacks.addView(listView);
    }

    /**ディレクトを1つ戻る。戻れなかったら終了。*/
    public void onBackPressed()
    {
        if(currentDir.getParent() != null
                || dirViewStacks.getChildCount() != 0){
            currentDir = currentDir.getParentFile();
            dirViewStacks.removeViewAt(dirViewStacks.getChildCount() - 1);
        }else{
            super.getActivity().onBackPressed();
        }

        if(dirViewStacks.getChildCount() == 0)
            super.getActivity().onBackPressed();
    }
}
